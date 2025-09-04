package app.modules.base.urlPerm.service;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.base.moduleInfo.dto.MenuHierarchyDTO;
import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.moduleInfo.repo.MenuHierarchyRepo;
import app.modules.base.role.entity.Role;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.urlPerm.dto.MenuData;
import app.modules.base.urlPerm.dto.SubMenuTrack;
import app.modules.base.urlPerm.entity.PermittedApi;
import app.modules.base.user.entity.User;
import app.modules.base.user.repo.UserOrgRepository;
import app.modules.base.user.repo.UserRepository;
import app.modules.base.urlPerm.dto.PrmttedApiDTO;
import app.modules.base.urlPerm.repo.PermittedApiRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermittedModuleService {

    @Autowired
    private PermittedApiRepository permittedApiRepository;

    @Autowired
    private MenuHierarchyRepo apiAgainstModuleRepo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserOrgRepository orgRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Map<String, Object> validate(PrmttedApiDTO dto) {

        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);
        if(dto.getBackendUrl()==null){
            mp.put("hasError",true);
            mp.put("message","BackendUrl is required field");
            return mp;
        }

        if(!apiAgainstModuleRepo.existsByApiPattern(dto.getBackendUrl())){
            mp.put("hasError",true);
            mp.put("message","BackendUrl is not exist");
            return mp;
        }

        if(dto.getRole()==null && dto.getUser()==null){
            mp.put("hasError",true);
            mp.put("message","User or role any one is required");
            return mp;
        }
        if(dto.getRole()!=null && dto.getUser()!=null){
            mp.put("hasError",true);
            mp.put("message","User or role any one is required");
            return mp;
        }


        if(dto.getId()==null && permittedApiRepository.existsByRoleIdAndUserIdAndBackendUrlAndMenuId(dto.getRole(),dto.getUser(),dto.getBackendUrl(),dto.getMenuId())){
            mp.put("hasError",true);
            mp.put("message","This Api permission already given for this role/user");
            return mp;
        }
        else if(dto.getId()!=null && permittedApiRepository.existsByRoleIdAndUserIdAndBackendUrlAndAndMenuIdAndIdNotIn(dto.getRole(),dto.getUser(),
                dto.getBackendUrl(),dto.getMenuId(),Arrays.asList(dto.getId()))){
            mp.put("hasError",true);
            mp.put("message","This Api permission already given for this role/user");
            return mp;
        }

        MenuHierarchy menu = null;
        if(dto.getBackendUrl()!=null){
            menu =  apiAgainstModuleRepo.findById(dto.getMenuId()).orElse(null);
        }
        String menuIdsHierarchy=null;
        while(menu!=null){
          if(menuIdsHierarchy==null){
              menuIdsHierarchy=menu.getId().toString();
              dto.setMenuId(menu.getId());
          }else{
              menuIdsHierarchy=menuIdsHierarchy+","+menu.getId();
          }
              Long parentId = apiAgainstModuleRepo.findParentIdById(menu.getId());
          if(parentId!=null){
              menu = apiAgainstModuleRepo.findById(parentId).orElse(null);
          }else{
              menu=null;
          }
        }
        dto.setMenuIdsHierarchy(menuIdsHierarchy);
        return mp;
    }


    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        String parentMenuId=null;
        if(dto.getParentMenuId()!=null){
            parentMenuId= dto.getParentMenuId().toString();
        }
        Page<Map<String,Object>> page = permittedApiRepository.getList(dto.getId(),parentMenuId,dto.getMenuId(),dto.getUserId(),dto.getRoleId(),pageable);
        MsgResponse resp=CommonUtil.responseFromPage(page);
        List<Map<String,Object>> listData=new ArrayList<>();
        List<Map<String,Object>> orgList= (List<Map<String, Object>>) ((Map<String,Object>)resp.getData()).get("listData");
        Map<String,Object> apiCache=new HashMap<>();

        Integer index=-1;
        for(Map<String,Object> dbObj : orgList){
            Map<String,Object> obj=new HashMap<>();
            obj.putAll(dbObj);
            index++;
            String key=(String) obj.get("menuId")+obj.get("backendUrl");
            if(apiCache.containsKey(key)){
                     String[] existInfoInCache= ((String)apiCache.get(key)).split(">");
                     String users=existInfoInCache[0];
                     String authority=existInfoInCache[1];
                     Integer existDataIndex=Integer.parseInt(existInfoInCache[2]);
                   if(obj.get("authority")!=null){
                       if(!authority.isEmpty()){
                           authority=authority+","+ obj.get("authority");
                       }else{
                           authority=(String)obj.get("authority");
                       }

                   }
                   if(obj.get("username")!=null){
                       if(!users.isEmpty()){
                           users=users+","+obj.get("username");
                       }else{
                           users=(String)obj.get("username");
                       }
                   }
                   apiCache.put(key,users+">"+authority+">"+existDataIndex);
                   listData.get(existDataIndex).put("authority",authority);
                   listData.get(existDataIndex).put("username",users);
               }else{
                   String users="";
                   String authority="";
                   if(obj.get("authority")!=null){
                       authority= (String) obj.get("authority");
                   }
                   if(obj.get("username")!=null){
                       users= (String) obj.get("username");
                   }
                   obj.put("authority",authority);
                   obj.put("username",users);
                   listData.add(obj);
                   apiCache.put(key,users+">"+authority+">"+index);
               }

        }
         ((Map<String,Object>)resp.getData()).put("listData",listData);
        return  resp;
    }

    public MsgResponse delete(Long id) {
        try{
            permittedApiRepository.deleteById(id);
        }catch (Exception e){
            return new MsgResponse(e.getMessage(),false);
        }
        return new MsgResponse("Successfully deleted",true);
    }

    public MsgResponse edit( List<PrmttedApiDTO> dto) {
        return create(dto);
    }

    public MsgResponse create(List<PrmttedApiDTO> dto) {
        List<PermittedApi> permissions = new ArrayList<>();
        boolean edit=false;
        for(PrmttedApiDTO x : dto){
            Map<String,Object> mp = validate(x);
            if((boolean)mp.get("hasError")){
                return new MsgResponse((String)mp.get("message"),false);
            }
            PermittedApi obj = new PermittedApi();
            if(x.getId()!=null){
                edit=true;
                obj=permittedApiRepository.findById(x.getId()).orElse(new PermittedApi());
            }
            BeanUtils.copyProperties(x,obj);

            if(x.getUser()!=null){
                User u = new User();
                u.setId(x.getUser());
                obj.setUser(u);
            }
            if(x.getRole()!=null){
                Role r =new Role();
                r.setId(x.getRole());
                obj.setRole(r);
            }
            permissions.add(obj);
        }
        permittedApiRepository.saveAll(permissions);

        return new MsgResponse(edit?"Successfully edited ":"Successfully created",true);
    }


        public MsgResponse getMenu() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // comes from the token subject
        User user = userRepository.findByUsername(username);
        List<PermittedApi> permittedApis = permittedApiRepository.getPermittedApis(user,user.getRoles());
        List<MenuHierarchyDTO> menuResponse=new ArrayList<>();
        Map<Long,Long> menuProcessed=new HashMap<>();
        for(PermittedApi api : permittedApis){
            List<Long> menuIdHierarchyOfTheApi = CommonUtil.reverseOrderList(CommonUtil.strListToLong(CommonUtil.bulkStrToList(api.getMenuIdsHierarchy())));
            for(int i=0;i<menuIdHierarchyOfTheApi.size();i++){
                MenuHierarchyDTO element = new MenuHierarchyDTO();
                element.setId(menuIdHierarchyOfTheApi.get(i));
                if(i>0){
                    element.setParentId(menuIdHierarchyOfTheApi.get(i-1));
                }
                MenuData hr = apiAgainstModuleRepo.getData(element.getId());
                if(hr!=null){
                    element.setMenu(hr.getMenu());
                    element.setParentMenu(hr.getParentMenu());
                    element.setMethodName(hr.getMethodName());
                    element.setApiPattern(hr.getApiPattern());
                    element.setFrontUrl(hr.getFrontUrl());
                    element.setApiSeq(hr.getApiSeq());
                }
                if(menuResponse.size()<1){
                    menuResponse.add(element);
                    menuProcessed.put(element.getId(), element.getId());
                    continue;
                }

                if(!menuProcessed.containsKey(element.getId())){
                    SubMenuTrack track = new SubMenuTrack();
                    this.makeHierarchy(menuResponse,element,track);
                    if(!track.parentFound){
                        menuResponse.add(element);
                    }
                    menuProcessed.put(element.getId(), element.getId());
                }


            }

        }
        Map<String,Object> resp=new HashMap<>();
        resp.put("menus",menuResponse);
        resp.put("orgs",orgRepository.getPermittedOrg(user));
        return new MsgResponse("Menu permission list retrieved ",resp,true);
    }

    public void makeHierarchy(List<MenuHierarchyDTO> menuResponse,MenuHierarchyDTO element,SubMenuTrack track){
        for(MenuHierarchyDTO menu : menuResponse){
            if(element.getParentId()!=null && menu.getId().equals(element.getParentId())){
                    menu.getDetails().add(element);
                    track.parentFound=Boolean.TRUE;
                    break;
                }else{
                    if(menu.getDetails().size()>0){
                        this.makeHierarchy(menu.getDetails(),element,track);
                    }
                }
            }
           }


}
