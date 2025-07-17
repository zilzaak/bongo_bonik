package app.modules.base.urlPerm.service;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.base.moduleInfo.dto.MenuHierarchyDTO;
import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.moduleInfo.repo.MenuHierarchyRepo;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.urlPerm.entity.PermittedApi;
import app.modules.base.user.entity.User;
import app.modules.base.user.repo.UserRepository;
import app.modules.base.urlPerm.dto.PrmttedApiDTO;
import app.modules.base.urlPerm.repo.PermittedApiRepository;
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
        if(dto.getFrontendUrl()!=null && !apiAgainstModuleRepo.existsByFrontUrl(dto.getFrontendUrl()) ){
            mp.put("hasError",true);
            mp.put("message","FrontendUrl is not exist");
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


        if(dto.getId()==null && permittedApiRepository.existsByRoleIdAndUserIdAndBackendUrl(dto.getRole(),dto.getUser(),dto.getBackendUrl())){
            mp.put("hasError",true);
            mp.put("message","This Api permission already given for this role/user");
            return mp;
        }
        else if(dto.getId()!=null && permittedApiRepository.existsByRoleIdAndUserIdAndBackendUrlAndIdNotIn(dto.getRole(),dto.getUser(),
                dto.getBackendUrl(),Arrays.asList(dto.getId()))){
            mp.put("hasError",true);
            mp.put("message","This Api permission already given for this role/user");
            return mp;
        }

        MenuHierarchy menu = null;
        if(dto.getBackendUrl()!=null){
            menu =  apiAgainstModuleRepo.findByApiPattern(dto.getBackendUrl());
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
        Page<Map<String,Object>> page = permittedApiRepository.getList(dto.getMenuId(),dto.getUserId(),dto.getRoleId(),pageable);
        return CommonUtil.responseFromPage(page);
    }

    public MsgResponse delete(Long id) {
        permittedApiRepository.deleteById(id);
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
            if(x.getId()!=null){
                edit=true;
            }
            PermittedApi obj = new PermittedApi();
            BeanUtils.copyProperties(obj,dto);
            permissions.add(obj);
        }
        permittedApiRepository.saveAll(permissions);

        return new MsgResponse(edit?"Successfully edited ":"Successfully created",true);
    }


        public MsgResponse getMenu() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // comes from the token subject
        User user = userRepository.findByUsername(username);
        List<PermittedApi> permittedApis = permittedApiRepository.getPermittedApis(user.getId(),user.getRoles());
        List<MenuHierarchyDTO> menus=new ArrayList<>();
        for(PermittedApi obj : permittedApis){
            List<Long> menuIds = CommonUtil.strListToLong(CommonUtil.bulkStrToList(obj.getMenuIdsHierarchy()));
            List<Map<String,Object>> sortedMenus = new ArrayList<>();
            List<Map<String,Object>> dbMenus = apiAgainstModuleRepo.getMenuNames(menuIds);
            for(Long id : menuIds){
               for(Map<String,Object> k : dbMenus){
                  if(k.get("id").equals(id)){
                      sortedMenus.add(k);
                  }
               }
            }
            this.makeHierarchy(sortedMenus,menuIds,menus,obj);

        }
        return new MsgResponse("Menu permission list retrieved ",menus,true);

    }

    public MenuHierarchyDTO makeObj(List<Map<String,Object>> names,
                        PermittedApi obj,int i,boolean child){
        MenuHierarchyDTO hr = new MenuHierarchyDTO();
        hr.setId((Long) names.get(i).get("id"));
        hr.setMenu((String) names.get(i).get("menu"));
        hr.setParentMenu(Optional.ofNullable(names.get(i+1).get("menu")).map(Object::toString).orElse(null));
        hr.setMethodName((String) Optional.ofNullable(names.get(i).get("methodName")).orElse(null));
        if(child){
            hr.setApiPattern(obj.getBackendUrl());
            hr.setFrontUrl(obj.getFrontendUrl());
        }
        return hr;
    }


    public void findChildAndSet(String parentMenu,String nextParent ,
                                Long parentId,List<MenuHierarchyDTO> menus,
                                Boolean childFound){
        for(MenuHierarchyDTO child : menus){
           if(child.getParentMenu()!=null && child.getParentMenu().equals(parentMenu)){
               MenuHierarchyDTO parent = new MenuHierarchyDTO();
               parent.setId(parentId);
               parent.setMenu(parentMenu);
               parent.setParentMenu(nextParent);
               parent.getDetails().add(child);
               childFound=Boolean.TRUE;
               break;
           }else{
               if(child.getDetails().size()>0){
                   this.findChildAndSet(parentMenu,nextParent,parentId,child.getDetails(),childFound);
               }
           }
        }
    }

    private void makeHierarchy(List<Map<String,Object>> names, List<Long> menuIds, List<MenuHierarchyDTO> menus, PermittedApi obj){
        if(menus.size()<1){
            for(int i=0;i<names.size();i++){
               if(i==0){
                   menus.add(this.makeObj(names,obj,i,true));
               }else{
                   Boolean childFound=Boolean.FALSE;
                   this.findChildAndSet((String)names.get(i).get("menu"),
                           Optional.ofNullable(names.get(i+1).get("menu")).map(Object::toString).orElse(null),
                           (Long)names.get(i).get("id"),menus,childFound);
                   if(!childFound){
                       menus.add(this.makeObj(names,obj,i,false));
                   }
               }
            }
        }
    }
}
