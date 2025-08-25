package app.modules.base.user.service;


import app.common.counter.service.CounterService;
import app.common.dto.CustomException;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.common.util.CounterEnum;
import app.modules.base.org.entity.Organization;
import app.modules.base.org.repo.OrgRepo;
import app.modules.base.role.entity.Role;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.user.dto.UserDTO;
import app.modules.base.user.dto.UserOrgDTO;
import app.modules.base.user.entity.User;
import app.modules.base.user.entity.UserOrg;
import app.modules.base.user.repo.UserOrgRepository;
import app.modules.base.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CounterService counterService;
    @Autowired
    private UserOrgRepository userOrgRepository;


public Map<String, Object> checkValidData(UserDTO dto){
    Map<String, Object> mp = new HashMap<>();
    mp.put("hasError",false);
    mp.put("message","no error exist");

    if(dto.getPhone()==null || dto.getPhone().isBlank()){
        mp.put("hasError",true);
        mp.put("message","Phone is required");
        return mp;
    }
    if(dto.getAddress()==null || dto.getAddress().isBlank()){
        mp.put("hasError",true);
        mp.put("message","Address is required");
        return mp;
    }
    if(dto.getDisplayName()==null || dto.getDisplayName().isBlank()){
        mp.put("hasError",true);
        mp.put("message","Name is required");
        return mp;
    }

    if(dto.getId()==null){
    if(userRepository.existsByPhone(dto.getPhone())){
    mp.put("hasError",true);
    mp.put("message","Phone must be unique");
    return mp;
    }
    }else{
        if(userRepository.existsByPhoneAndIdNotIn(dto.getPhone(),Arrays.asList(dto.getId()))){
            mp.put("hasError",true);
            mp.put("message","Phone must be unique");
            return mp;
        }
    }

    for(String role : dto.getRoles()){
        if(!roleRepository.existsByAuthority(role)){
            mp.put("hasError",true);
            mp.put("message","This assigned role "+role+" is not exist in system");
            return mp;
        }
    }


    for(UserOrgDTO org : dto.getUserOrgs()){
        if(!orgRepo.existsById(org.getOrg())){
            mp.put("hasError",true);
            mp.put("message","No organization exist with id="+org.getOrg());
            return mp;
        }
    }


    return mp;
}


    @Transactional
    public MsgResponse create(UserDTO userDTO){
        Map<String,Object> resp = checkValidData(userDTO);
        if((boolean)resp.get("hasError")){
            return new MsgResponse((String)resp.get("message"),false);
        }
        Set<Role> roleList = new HashSet<>();
        for(String Authority : userDTO.getRoles()){
            Role role = roleRepository.findByAuthority(Authority);
            roleList.add(role);
             }
        User user = new User();
        if(userDTO.getId()!=null){
            user=userRepository.findById(userDTO.getId()).get();
        }
        user.setEmail(userDTO.getEmail());
        user.setDisplayName(userDTO.getDisplayName());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setRoles(roleList);
        user.setEnabled(userDTO.getEnabled());
        String operation=null;
        if(userDTO.getId()==null){
            String prefix="";
            operation="create";
            user.setUsername(counterService.getCounterCode(null,null,CounterEnum.SYS_USER.name(),prefix.trim()));
            if(userRepository.existsByUsername(user.getUsername())){
                return new MsgResponse("Username must be unique",false);
            }
            user.setPassword(passwordEncoder.encode("123456"));
            user.setCreateBy(CommonUtil.currentUser());
        }else{
            operation="update";
            if(userDTO.getPassword().length()<8 && !user.getPassword().equals(userDTO.getPassword())){
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            Set<Role> remove=new HashSet<>();
            for(Role er : user.getRoles()){
                boolean exist=false;
                for(String str : userDTO.getRoles()){
                    if(er.getAuthority().equals(str)){
                        exist=true;
                        break;
                    }
                }
                if(!exist){
                    remove.add(er);
                }
            }
            user.getRoles().removeAll(remove);
            Set<Role> latest=new HashSet<>();
            for(String str : userDTO.getRoles()){
                Role rn = roleRepository.findByAuthority(str);
                latest.add(rn);
            }
            user.getRoles().addAll(latest);
            user.setUpdateBy(CommonUtil.currentUser());
        }

        try{
            userRepository.save(user);
            if(operation.equals("create")){
                List<UserOrg> orgList=new ArrayList<>();
                for(UserOrgDTO o : userDTO.getUserOrgs()){
                    UserOrg obj =new UserOrg();
                    Organization org = new Organization();
                    org.setId(o.getOrg());
                    obj.setUser(user);
                    orgList.add(obj);
                }
                userOrgRepository.saveAll(orgList);
            }else{
                List<UserOrg> remove=new ArrayList<>();
                List<UserOrg> latestList=new ArrayList<UserOrg>();
                List<UserOrg> existList=userOrgRepository.findByUser(user);
                for(UserOrg obj : existList){
                    boolean exist=false;
                    for(UserOrgDTO o : userDTO.getUserOrgs()){
                        if(o.getOrg().equals(obj.getOrg().getId())){
                            exist=true;
                            break;
                        }}
                    if(!exist){
                        remove.add(obj);
                    }
                }

                for(UserOrgDTO o : userDTO.getUserOrgs()){
                    UserOrg k = new UserOrg();
                    if(o.getId()!=null){
                       k=userOrgRepository.findById(o.getId()).orElse(null);
                       if(!k.getUser().getId().equals(user.getId())){
                           throw new Exception("You are editing another persons data");
                       }
                        k.setUpdateBy(CommonUtil.currentUser());
                    }else{
                        k.setCreateBy(CommonUtil.currentUser());
                    }
                    k.setUser(user);
                    Organization org = new Organization();
                    org.setId(o.getOrg());
                    k.setOrg(org);
                    latestList.add(k);
                }
                userOrgRepository.deleteAll(remove);
                userOrgRepository.saveAll(latestList);
            }

        }catch (Exception e){
            return new MsgResponse(e.getMessage(),false);
        }
        return new MsgResponse("Successfully "+operation+"ed user",true);
    }

    @Transactional
    public MsgResponse edit(UserDTO userDTO) {
      return this.create(userDTO);

    }

    public MsgResponse getByUser(Map<String, String> param) throws CustomException {
        try{
            User user = userRepository.findById(Long.parseLong(param.get("id"))).get();
            MsgResponse resp = new MsgResponse();
            Map<String,Object> mp = new HashMap<>();
            mp.put("user",user);
            mp.put("userOrg",userOrgRepository.getList(user.getId()));
            resp.setData(mp);
            resp.setSuccess(true);
            resp.setMessage("-----------");
            return resp;
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    public void delete(Map<String, String> param) throws CustomException {
        try{
        userRepository.deleteById(Long.parseLong(param.get("id")));
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }

    }


    // when first time the application run then the system by default create a user named as admin
    // and create a role as super admin
    // and admin will be assigned super_admin role who can change anything in  whole system
    // or edit anything,the admin user only created first time only.
    public void createDefaultUser(){
        Set<Role> defaultRoleList = new HashSet<>();
        Role superAdmin= roleRepository.findByAuthority("SUPER_ADMIN");
        if(superAdmin==null){
            superAdmin = new Role();
            superAdmin.setAuthority("SUPER_ADMIN");
            roleRepository.save(superAdmin);
        }
        defaultRoleList.add(superAdmin);

        if(userRepository.count()<1 && superAdmin!=null){
            User super_admin=new User();
            super_admin.setEnabled(Boolean.TRUE);
            super_admin.setUsername("admin");
            super_admin.setPassword(passwordEncoder.encode("123456"));
            super_admin.setAddress("admin address");
            super_admin.setPhone("01753181186");
            super_admin.setDisplayName("parvez");
            super_admin.setRoles(defaultRoleList);
            userRepository.save(super_admin);
        }

        Role permAllRole = roleRepository.findByAuthority("PERMIT_ALL");
        if(permAllRole==null){
            permAllRole=new Role();
            permAllRole.setAuthority("PERMIT_ALL");
            roleRepository.save(permAllRole);
        }
        // by default create PERMIT_ALL role so that every body can login via /auth/getToken api
    }

    public MsgResponse list(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = userRepository.list(dto.getUsername(),dto.getCommonField(),pageable);
        MsgResponse response = CommonUtil.responseFromPage(page);
        if(dto.getDropDown()!=null && !dto.getDropDown().isBlank()){
            return response;
        }
        List<Map<String ,Object>> listData = new ArrayList<>();
        for(Map<String ,Object> m : page.getContent()){
            Map<String ,Object> cpy = new HashMap<>();
            cpy.putAll(m);
            //concat role details as bulk single string
            User user = userRepository.findByUsername((String) m.get("username"));
            String roles = null;
            for(Role rl : user.getRoles()){
                if(roles==null){
                    roles=rl.getAuthority();
                }else{
                    roles=roles+","+rl.getAuthority();
                }
            }
            cpy.put("roles",roles);
            //concat organization details as bulk single string
            String orgs = null;
            List<Object[]> orgnames=userOrgRepository.orgnames(user.getId());
            for(Object[] arr : orgnames){
                if(orgs==null){
                    orgs= (String) arr[0];
                }else{
                    orgs=orgs+","+arr[0];
                }
            }
            cpy.put("orgNames",orgs);
            listData.add(cpy);
        }
        ((Map<String ,Object>)response.getData()).put("listData",listData);
        return response;
    }
}
