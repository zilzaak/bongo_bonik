package app.modules.base.urlPerm.urlRetriver;

import app.common.util.CommonUtil;
import app.modules.base.moduleInfo.entity.HeirarchyType;
import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.moduleInfo.repo.MenuHierarchyRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Component
public class ApiEndpointRetriever {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final MenuHierarchyRepo apiAgainstModuleRepo;
    public List<String> urlContains = Arrays.asList("create","get","update","edit","delete","list");

    public ApiEndpointRetriever(RequestMappingHandlerMapping mapping ,
                                MenuHierarchyRepo apiAgainstModuleRepo) {
        this.requestMappingHandlerMapping = mapping;
        this.apiAgainstModuleRepo = apiAgainstModuleRepo;
    }

    private String getMethod(String method){
            method=CommonUtil.removeCharFromString(method,'/');
            method=CommonUtil.removeCharFromString(method,'[');
            method=CommonUtil.removeCharFromString(method,']');

        return  method;
    }


    public  List<MenuHierarchy> allParentModule() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Map<String,List<String>> apiListUnderModule = new HashMap<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            Set<String> apiInfo  = info.getDirectPaths();
            if(apiInfo.size()>0){
                String method = "/"+info.getMethodsCondition().getMethods();
                method=getMethod(method);
                String apiUrl = new ArrayList<>(apiInfo).get(0);
                apiUrl=CommonUtil.removeAllSpace(apiUrl);
                if(!apiUrl.contains("error")){
                    String apiParts[] = apiUrl.split("/");
                    String module = apiParts[1];
                    apiUrl=apiUrl+">"+method;
                    if(!apiListUnderModule.containsKey(module)){
                        List<String> apiList = new ArrayList<>();
                        apiList.add(apiUrl);
                        apiListUnderModule.put(module,apiList);
                    }else{
                        apiListUnderModule.get(module).add(apiUrl);
                    }
                }
            }
        }

        List<MenuHierarchy> parents = new ArrayList<>();
        for(String module : apiListUnderModule.keySet()){
            MenuHierarchy parentModule = new MenuHierarchy();
            parentModule.setType(HeirarchyType.MODULE.name());
            parentModule.setName(module);
            List<String> subModules = apiListUnderModule.get(module);
            List<MenuHierarchy> subLists = new ArrayList<>();
            if(subModules.size()>0){
                for(String subModule : subModules){
                    MenuHierarchy sub = new MenuHierarchy();
                    String arr[]= subModule.split("/");
                    String[] method = subModule.split(">");
                    String type=null;
                    String nextPart = null;
                    try{nextPart=arr[2];}catch (ArrayIndexOutOfBoundsException e){}

                    if(nextPart==null){
                        type=HeirarchyType.MENU.name();
                        nextPart=module;
                    }else{
                       if(!urlContains.contains(nextPart)){
                           String next = null;
                           try{next=arr[3];}catch (ArrayIndexOutOfBoundsException e){}
                           if(next==null){
                               type=HeirarchyType.MENU.name();
                           }else{
                               if(!urlContains.contains(next)){
                                   type=HeirarchyType.SUB_MODULE.name();
                               }else {
                                   type=HeirarchyType.MENU.name();
                               }
                           }
                       }
                    }

                    sub.setType(type);
                    sub.setName(nextPart);
                    sub.setMethodName(method[1]);
                    sub.setApiPattern(subModule);
                    subLists.add(sub);
                }
            }
            parentModule.setDetails(subLists);
            parents.add(parentModule);
        }
        return  parents;
    }


    @PostConstruct
    public  void categoryIntoSubModuleOrMenu(){
        List<MenuHierarchy>  parentModules = this.allParentModule();
        this.convertToHierarchicalStructure(parentModules);
        apiAgainstModuleRepo.saveAll(parentModules);
    }


    private void convertToHierarchicalStructure(List<MenuHierarchy>  parentModules){

        //arrange sub modules
        for(MenuHierarchy parent : parentModules){
            if(parent.getDetails().size()<1) continue;

            int nextHierarchyIndex=2;

            Map<String,List<MenuHierarchy>> newParentHierarchy = new HashMap<>();
            List<MenuHierarchy> childGoneUnderNewParent=new ArrayList<>();

            for(MenuHierarchy obj : parent.getDetails()){
                String[] apiArr=obj.getApiPattern().split("/");
                String newModulePart=null;
                String nextModulePart=null;
                String apiPattern=obj.getApiPattern();
                String methodName=obj.getMethodName();

                try{newModulePart=apiArr[nextHierarchyIndex];}catch (ArrayIndexOutOfBoundsException e){newModulePart=null;
                }
                try{nextModulePart=apiArr[nextHierarchyIndex+1];}catch (ArrayIndexOutOfBoundsException e){nextModulePart=null;}

                if(newModulePart!=null && !urlContains.contains(newModulePart)){
                    String type=null;
                    if(nextModulePart!=null && !urlContains.contains(nextModulePart)){
                        type=HeirarchyType.SUB_MODULE.name();
                    }
                    else if(nextModulePart!=null && urlContains.contains(nextModulePart)){
                        type=HeirarchyType.MENU.name();
                    }
                    else if(nextModulePart==null){
                        type=HeirarchyType.MENU.name();
                    }

                        List<MenuHierarchy> childs = new ArrayList<>();
                        MenuHierarchy hr = new MenuHierarchy();
                        hr.setMethodName(methodName);
                        hr.setApiPattern(apiPattern);
                        hr.setType(type);
                        hr.setName(newModulePart);
                        childs.add(hr);
                        childGoneUnderNewParent.add(obj);
                        if(!newParentHierarchy.containsKey(newModulePart)){
                            newParentHierarchy.put(newModulePart,childs);
                        }else{
                            newParentHierarchy.get(newModulePart).add(hr);
                        }
                }
            }

            parent.getDetails().removeAll(childGoneUnderNewParent);
            parent.getDetails().addAll(convertToList(newParentHierarchy));

             if(parent.getType().equals(HeirarchyType.MODULE.name())){
                 parent.setDetails(this.arrangeSubModule(parent.getDetails()));
             }

        }

    }

    private List<MenuHierarchy> arrangeSubModule(List<MenuHierarchy> list){
        Map<String,List<MenuHierarchy>> newParentHierarchy = new HashMap<>();
        for(MenuHierarchy x : list){
            MenuHierarchy child = new MenuHierarchy();
            child.setApiPattern(x.getApiPattern());
            child.setType(HeirarchyType.MENU.name());
            String arr[]= x.getApiPattern().split("/");
            String name = null;
            try{
               name=arr[3];
            }catch (Exception e){
                name=arr[2];
            }
            child.setName(name);
            child.setMethodName(x.getMethodName());
            if(!newParentHierarchy.containsKey(x.getName())){
                List<MenuHierarchy> m = new ArrayList<>();
                m.add(child);
                newParentHierarchy.put(x.getName(),m);
            }else{
                newParentHierarchy.get(x.getName()).add(child);
            }
        }
        list.clear();

        for(String subMenu : newParentHierarchy.keySet()){
            List<MenuHierarchy> menus = newParentHierarchy.get(subMenu);
            MenuHierarchy parent = new MenuHierarchy();
            parent.setType(HeirarchyType.SUB_MODULE.name());
            parent.setName(subMenu);
            parent.setDetails(menus);
            list.add(parent);
        }
return list;
    }


    private List<MenuHierarchy> convertToList(Map<String, List<MenuHierarchy>> newParentHierarchy) {
        List<MenuHierarchy> newParentList = new ArrayList<>();
        if(newParentHierarchy.isEmpty()){
            return newParentList;
        }
        for(String newModule : newParentHierarchy.keySet()){
            MenuHierarchy parent = new MenuHierarchy();
            List<MenuHierarchy> childs =  newParentHierarchy.get(newModule);
            parent.setType(HeirarchyType.SUB_MODULE.name());
            parent.setName(newModule);
            parent.setDetails(childs);
            parent.setApiPattern(childs.get(0).getApiPattern());
            newParentList.add(parent);
        }
           return newParentList;
    }

}