package app.modules.base.user.service;


import app.common.counter.service.CounterService;
import app.common.dto.CustomException;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.common.util.CounterEnum;
import app.modules.base.security.auth.entity.*;
import app.modules.base.security.auth.repo.AuthorityPermissionRepository;
import app.modules.base.role.entity.Role;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.user.dto.UserDTO;
import app.modules.base.user.entity.User;
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
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityPermissionRepository authorityPermissionRepository;
    @Autowired
    private CounterService counterService;



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
        user.setEmail(userDTO.getEmail());
        user.setDisplayName(userDTO.getDisplayName());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setRoles(roleList);
        user.setEnabled(userDTO.getEnabled());
        String prefix="";
        user.setUsername(counterService.getCounterCode(null,null,CounterEnum.SYS_USER.name(),prefix.trim()));
        user.setPassword(passwordEncoder.encode("123456"));
        if(userRepository.existsByUsername(user.getUsername())){
            return new MsgResponse("Username must be unique",false);
        }
        try{
            userRepository.saveAndFlush(user);
        }catch (Exception e){
            return new MsgResponse(e.getMessage(),false);
        }
        return new MsgResponse("Successfully created user",false);
    }

    @Transactional
    public MsgResponse edit(UserDTO userDTO) {
        Map<String,Object> resp = checkValidData(userDTO);
        if((boolean)resp.get("hasError")){
            return new MsgResponse((String)resp.get("message"),false);
        }

        Set<Role> roleList = new HashSet<>();
        for(String Authority : userDTO.getRoles()){
            Role role = roleRepository.findByAuthority(Authority);
            roleList.add(role);
        }

        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if(user==null){
            return new MsgResponse("No user found with id "+ userDTO.getId(),false);
        }
        user.setEnabled(userDTO.getEnabled());
        user.setRoles(roleList);
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setDisplayName(userDTO.getDisplayName());
        if(userDTO.getPassword()!=null || !userDTO.getPassword().isBlank() && userDTO.getPassword().length()>5){
           user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if(userRepository.existsByUsernameAndIdNotIn(user.getUsername(),Arrays.asList(userDTO.getId()))){
            return new MsgResponse("Username must be unique",false);
        }
        try{
            userRepository.saveAndFlush(user);
        }catch (Exception e){
            return new MsgResponse(e.getMessage(),false);
        }
        return new MsgResponse("Successfully edited user",false);

    }

    public User getByUser(Map<String, String> param) throws CustomException {
        try{
            User user = userRepository.findById(Long.parseLong(param.get("id"))).get();
            return user;
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


    public void createDefaultUser(){

        Set<Role> defaultRoleList = new HashSet<>();
        if(!roleRepository.existsByAuthority("SUPER_ADMIN")){
            Role superAdmin = new Role();
            superAdmin.setAuthority("SUPER_ADMIN");
            roleRepository.saveAndFlush(superAdmin);
            defaultRoleList.add(superAdmin);
        }

        if(userRepository.count()<1){
            User super_admin=new User();
            super_admin.setEnabled(Boolean.TRUE);
            super_admin.setUsername("admin");
            super_admin.setPassword(passwordEncoder.encode("123456"));
            super_admin.setRoles(defaultRoleList);
            userRepository.saveAndFlush(super_admin);
        }

        if(!roleRepository.existsByAuthority("PERMIT_ALL")){
            Role permAllRole = new Role();
            permAllRole.setAuthority("PERMIT_ALL");
            roleRepository.saveAndFlush(permAllRole);
        }
        // by default create PERMIT_ALL role so that every body can login via /auth/getToken api
    }

    public MsgResponse list(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = userRepository.list(dto.getUsername(),pageable);
        MsgResponse response = CommonUtil.responseFromPage(page);
        List<Map<String ,Object>> listData = new ArrayList<>();
        for(Map<String ,Object> m : page.getContent()){
            Map<String ,Object> cpy = new HashMap<>();
            cpy.putAll(m);
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
            listData.add(cpy);
        }
        ((Map<String ,Object>)response.getData()).put("listData",listData);
        return response;
    }
}
