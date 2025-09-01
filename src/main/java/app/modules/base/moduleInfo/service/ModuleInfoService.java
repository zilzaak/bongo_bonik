package app.modules.base.moduleInfo.service;


import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.base.moduleInfo.dto.MenuDTO;
import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.moduleInfo.repo.MenuHierarchyRepo;
import app.modules.base.urlPerm.entity.PermittedApi;
import app.modules.base.urlPerm.repo.PermittedApiRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Service
public class ModuleInfoService {


    @Autowired
    private MenuHierarchyRepo hierarchyRepo;

    @Autowired
    private PermittedApiRepository permittedApiRepository;

    private final RequestMappingHandlerMapping handlerMapping;
    public ModuleInfoService(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }
    public List<String> methods = Arrays.asList("GET","DELETE","POST","PUT","PATCH");

    Map<String,Object> validate(List<MenuDTO> list){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);
        if(list.isEmpty()){
            mp.put("hasError",true);
            mp.put("message","No menu selected");
            return mp;
        }


        for(MenuDTO menu : list){
            if(menu.menu==null || menu.getMenu().isBlank()){
                mp.put("hasError",true);
                mp.put("message","Menu is required");
                return mp;
            }
            if(menu.apiSeq==null || menu.getApiSeq().isBlank()){
                mp.put("hasError",true);
                mp.put("message","Api sequence is required");
                return mp;
            }
            if((menu.methodName!=null && !this.methods.contains(menu.methodName))){
                mp.put("hasError",true);
                mp.put("message","Method name is required");
                return mp;
            }

            if(menu.methodName!=null && (menu.apiPattern==null || menu.apiPattern.isBlank())){
                mp.put("hasError",true);
                mp.put("message","Backend Url is required");
                return mp;
            }

            if(menu.apiPattern!=null && (menu.methodName==null || !this.methods.contains(menu.methodName))){
                mp.put("hasError",true);
                mp.put("message","Method of Url is required");
                return mp;
            }

            if(menu.apiSeq.startsWith("/")){
                menu.setApiSeq(CommonUtil.removeFirstChar(menu.apiSeq));
            }

            if(menu.getId()==null){
                if(menu.getParentId()!=null && !hierarchyRepo.existsById(menu.getParentId())){
                    mp.put("hasError",true);
                    mp.put("message","The parent menu selected but not created yet");
                    return mp;
                }
                if(menu.getParentMenu()!=null && hierarchyRepo.existsByMenuAndParentMenu(menu.getMenu(),menu.getParentMenu())){
                    mp.put("hasError",true);
                    mp.put("message","Duplicate menu creation");
                    return mp;
                }
               if(hierarchyRepo.existsByApiSeqAndMenu(menu.getApiSeq(),menu.getMenu())){
                   mp.put("hasError",true);
                   mp.put("message","Menu already exist");
                   return mp;
               }
           }else{
                if(menu.getParentMenu()!=null && hierarchyRepo.existsByMenuAndParentMenuAndIdNotIn(menu.getMenu(),menu.getParentMenu(),Arrays.asList(menu.id))){
                    mp.put("hasError",true);
                    mp.put("message","Duplicate menu creation");
                    return mp;
                }
               if(hierarchyRepo.existsByApiSeqAndMenuAndIdNotIn(menu.apiSeq,menu.getMenu(),Arrays.asList(menu.id))){
                   mp.put("hasError",true);
                   mp.put("message","Menu already exist");
                   return mp;
               }
              }
        }

        return mp;

    }

    public MsgResponse create(List<MenuDTO> list) {
        Map<String,Object> mp = validate(list);
        if((boolean)mp.get("hasError")){
            return new MsgResponse(mp.get("message"),false);
        }

        for(MenuDTO obj : list){
            if(obj.getId()!=null){
                MenuHierarchy menu = hierarchyRepo.findById(obj.id).get();
                Long oldParentId=Optional.ofNullable(menu.getParent()).map(MenuHierarchy::getId).orElse(null);

                 BeanUtils.copyProperties(obj,menu,"details");
                if((oldParentId==null &&  obj.parentId==null) ||
                  (oldParentId!=null && obj.parentId!=null && 
                   oldParentId.equals(obj.parentId))){
                    hierarchyRepo.save(menu);
                }
                else if((obj.parentId!=null && oldParentId!=null
                        && !obj.parentId.equals(oldParentId)) ||
                        (obj.parentMenu!=null && oldParentId==null) ||
                        (oldParentId!=null && obj.parentId==null)){

                    if(menu.getParent()!=null && menu.getParent().getId().equals(menu.getId())){
                        return new MsgResponse("A menu can not be parent itself",false);
                    }


                    if(menu.getParent()!=null){
                        MenuHierarchy toBeRemoveItem=null;
                        for(MenuHierarchy x : menu.getParent().getDetails()){
                            if(x.getId().equals(menu.getId())){
                                toBeRemoveItem=x;
                                break;
                            }
                        }

                        if(toBeRemoveItem!=null) {
                            menu.getParent().getDetails().remove(toBeRemoveItem);
                            hierarchyRepo.save(menu.getParent());
                        }
                    }


                    MenuHierarchy updatedParent = null;
                    if(obj.parentId!=null){
                        updatedParent = hierarchyRepo.findById(obj.parentId).get();
                        menu.setParent(updatedParent);
                        updatedParent.getDetails().add(menu);
                        hierarchyRepo.save(updatedParent);
                    }
                }

                PermittedApi permission = permittedApiRepository.findByMenuId(menu.getId());
                if(permission!=null){
                    permission.setBackendUrl(menu.getApiPattern());
                    permittedApiRepository.save(permission);
                }

            }else{
                    Long parentId=obj.parentId;
                    MenuHierarchy parentMenu = null;
                    if(parentId!=null){
                        parentMenu=hierarchyRepo.findById(parentId).orElse(null);
                    }

                    MenuHierarchy child = new MenuHierarchy();
                    BeanUtils.copyProperties(obj,child);
                    if(parentMenu!=null){
                        child.setParent(parentMenu);
                        parentMenu.getDetails().add(child);
                        hierarchyRepo.save(parentMenu);
                    }else{
                        hierarchyRepo.save(child);
                    }
            }
        }

        return new MsgResponse("Successfully created",true);
    }

    public MsgResponse edit(List<MenuDTO> list) {
        return create(list);
    }

    public MsgResponse delete(Long id) {

        return null;
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        if(dto.getParentMenuId()!=null || dto.getFrontendUrl()!=null || dto.getBackendUrlId()!=null){
            MsgResponse resp =CommonUtil.responseFromPage(hierarchyRepo.getListParent(dto.getParentMenuId(),dto.getBackendUrlId(),dto.getFrontendUrl(),pageable)) ;
            return resp;
        }

        String url=null;
        String menu=null;
        String loadMenu=null;
        String loadMethod=null;
        if(dto.getLoadMethod()!=null){
            loadMethod="loadMethod";
            loadMenu=null;
            url=dto.getMenu();
            menu=null;
        }

         if(dto.getMenuSearch()!=null){
            loadMenu="loadMenu";
            loadMethod=null;
            menu=dto.getMenu();
            url=null;
        }

        MsgResponse resp =CommonUtil.responseFromPage(hierarchyRepo.getList(dto.getModuleId(),menu,url,loadMethod,loadMenu,pageable)) ;

        if(dto.getMenuDetails()!=null && dto.getModuleId()!=null){
          Map<String,Object> data = (Map<String, Object>) resp.getData();
          List<MenuHierarchy> childDetails=new ArrayList<>();
          if(dto.getModuleId()!=null){
              MenuHierarchy obj =hierarchyRepo.findById(dto.getModuleId()).orElse(null);
              if(obj!=null){
                 for(MenuHierarchy x : obj.getDetails()){
                     MenuHierarchy temp = new MenuHierarchy();
                     BeanUtils.copyProperties(x,temp,"details","parent");
                     childDetails.add(temp);
                 }
              }
          }
            data.put("childMenus",childDetails);
          MenuHierarchy parent=null;
          Long parentId=hierarchyRepo.findParentIdById(dto.getModuleId());
          if(parentId!=null){
              parent=new MenuHierarchy();
              MenuHierarchy mx= hierarchyRepo.findById(parentId).orElse(null);
              BeanUtils.copyProperties(mx,parent,"details","parent");
          }
            data.put("parent",parent);
            resp.setData(data);
        }

        return resp;
    }


}
