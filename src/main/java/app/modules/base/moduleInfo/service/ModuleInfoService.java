package app.modules.base.moduleInfo.service;


import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.base.moduleInfo.dto.MenuDTO;
import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.moduleInfo.repo.MenuHierarchyRepo;
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

            menu.setApiSeq(CommonUtil.removeFirstChar(menu.apiPattern));
            String parentApiSeq=CommonUtil.removeWordFromString(menu.apiSeq,menu.menu);

            if((menu.methodName!=null || !menu.methodName.isEmpty())&& !this.methods.contains(menu.methodName)){
                mp.put("hasError",true);
                mp.put("message","Method name is required");
                return mp;
            }

            if(menu.getId()==null){
                if(!hierarchyRepo.existsByParentMenu(menu.getParentMenu())){
                    mp.put("hasError",true);
                    mp.put("message","The parent menu does not exist");
                    return mp;
                }
               if(hierarchyRepo.existsByApiSeqAndParentMenu(parentApiSeq,menu.getParentMenu())){
                   mp.put("hasError",true);
                   mp.put("message","Menu already exist");
                   return mp;
               }
           }else{
                if(!hierarchyRepo.existsByParentMenuAndIdNotIn(menu.getParentMenu(),Arrays.asList(menu.id))){
                    mp.put("hasError",true);
                    mp.put("message","The parent menu does not exist");
                    return mp;
                }
               if(hierarchyRepo.existsByApiSeqAndParentMenuAndIdNotIn(parentApiSeq,menu.getParentMenu(),Arrays.asList(menu.id))){
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
                BeanUtils.copyProperties(obj,menu);
                hierarchyRepo.save(menu);
            }else{
                    String[] arr = obj.apiSeq.split("/");
                    String parentApiSeq = CommonUtil.removeWordFromString(obj.apiSeq,arr[arr.length-1]);
                    MenuHierarchy menu = hierarchyRepo.findByMenuAndApiSeq(obj.getParentMenu(),parentApiSeq);
                    MenuHierarchy child = new MenuHierarchy();
                    BeanUtils.copyProperties(obj,child);
                    if(menu!=null){
                        menu.getDetails().add(child);
                        hierarchyRepo.save(menu);
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
        return CommonUtil.responseFromPage(hierarchyRepo.getList(dto.getModuleId(),pageable));
    }


}
