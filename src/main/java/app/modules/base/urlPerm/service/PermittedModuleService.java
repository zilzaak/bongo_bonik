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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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


        if(dto.getId()==null && permittedApiRepository.existsByRoleIdAndUserIdAndMenuId(dto.getRole(),dto.getUser(),dto.getMenuId())){
            mp.put("hasError",true);
            mp.put("message","This Api permission already given for this role/user");
            return mp;
        }
        else if(dto.getId()!=null && permittedApiRepository.existsByRoleIdAndUserIdAndAndMenuIdAndIdNotIn(dto.getRole(),dto.getUser()
                ,dto.getMenuId(),Arrays.asList(dto.getId()))){
            mp.put("hasError",true);
            mp.put("message","This Api permission already given for this role/user");
            return mp;
        }

        MenuHierarchy menu = apiAgainstModuleRepo.findById(dto.getMenuId()).orElse(null);
            dto.setMenuId(menu.getId());
            dto.setMenuIdsHierarchy(this.getMenuHierarchy(menu,new HashMap<>()));
            return mp;
    }

    public String getMenuHierarchy(MenuHierarchy menu,Map<Long,String> menuIdVsHierarchy){
        String menuIdsHierarchy=null;
        Long menuId = Long.valueOf(menu.getId().toString());
        while(menu!=null){
            if(menuIdsHierarchy==null){
                menuIdsHierarchy=menu.getId().toString();
            }else{
                menuIdsHierarchy=menuIdsHierarchy+","+menu.getId();
            }

            Long parentId = apiAgainstModuleRepo.findParentIdById(menu.getId());

            if(parentId!=null){
                if(menuIdVsHierarchy.containsKey(parentId)){
                    menuIdsHierarchy=menuIdsHierarchy+","+ menuIdVsHierarchy.get(parentId);
                    break;
                }else{
                    menu = apiAgainstModuleRepo.findById(parentId).orElse(null);
                }
            }else{
                menu=null;
            }
        }
        menuIdVsHierarchy.put(menuId,menuIdsHierarchy);
        return menuIdsHierarchy;
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
            String key=obj.get("menuId").toString()+obj.get("backendUrl");
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
        List<String> permittedApis = permittedApiRepository.getPermittedApis(user,user.getRoles());
        List<MenuHierarchyDTO> menuResponse=new ArrayList<>();
        Map<Long,Long> menuProcessed=new HashMap<>();

        for(String s : permittedApis){

            List<Long> menuIdHierarchyOfTheApi = CommonUtil.reverseOrderList(CommonUtil.strListToLong(CommonUtil.bulkStrToList(s)));

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
                    element.setSidebarElement(hr.getSidebarElement());
                    element.setFrontUrl(hr.getFrontUrl());
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

           public PermittedApi getById(Long id){
             return  permittedApiRepository.findById(id).orElse(null);
           }

          public PermittedApi save(PermittedApi permittedApi){
            return  permittedApiRepository.saveAndFlush(permittedApi);
          }

        @Transactional(propagation = Propagation.REQUIRED)
        public void updateMenuHeirarchyForUpdatedMenuAndHisChilds(Map<Long,String> menuIdVsHierarchy,MenuHierarchy updatedMenu) {
        //check this menu is backend api containing menu or only for ui/frontend menu
           if(updatedMenu.getApiPattern()!=null && !updatedMenu.getApiPattern().isBlank()){
               PermittedApi permittedApi = this.getByMenuId(updatedMenu.getId());
               permittedApi.setMenuIdsHierarchy(this.getMenuHierarchy(updatedMenu,menuIdVsHierarchy));
               //permittedApi.setBackendUrl(updatedMenu.getApiPattern());
               this.save(permittedApi);
           }
           //update all child menus positional hierarchy if permission exist and if child menu is related to backend url
           if(updatedMenu.getDetails()!=null && updatedMenu.getDetails().size()>0){
               for(MenuHierarchy childElement : updatedMenu.getDetails()){
                   this.updateMenuHeirarchyForUpdatedMenuAndHisChilds(menuIdVsHierarchy,childElement);
               }
           }
          }

    public PermittedApi getByMenuId(Long id) {
        return permittedApiRepository.findByMenuId(id);
    }
}
