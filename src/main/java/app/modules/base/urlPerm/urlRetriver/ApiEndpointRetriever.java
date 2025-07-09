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
    public List<String> urlContains = Arrays.asList("get","update","edit","delete","list");

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
            if(subModules.size()>1){
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

        for(MenuHierarchy parent : parentModules){
            List<MenuHierarchy> subModules = parent.getDetails();
             if(subModules.size()>0){
                         int checkApiIndex=2;

                Map<String,List<MenuHierarchy>> newParentHierarchy = new HashMap<>();

                  List<MenuHierarchy> childGoneUnderNewParent=new ArrayList<>();

                 for(MenuHierarchy obj : subModules){
                         if(obj.getType().equals(HeirarchyType.MENU.name())){
                             continue;
                         }

                        String[] apiArr=obj.getApiPattern().split("/");

                        String newModulePart=null; String nextPart=null;

                        String apiPattern=obj.getApiPattern();

                        String methodName=obj.getMethodName();

                        try{newModulePart=apiArr[checkApiIndex];}catch (ArrayIndexOutOfBoundsException e){newModulePart=null;
                        }
                        try{nextPart=apiArr[checkApiIndex+1];}catch (ArrayIndexOutOfBoundsException e){nextPart=null;}

                        if(newModulePart!=null && !urlContains.contains(newModulePart)){
                                String type=null;
                              if(nextPart!=null && !urlContains.contains(nextPart)){
                                  type=HeirarchyType.SUB_MODULE.name();
                              }
                              else if(nextPart!=null && urlContains.contains(nextPart)){
                                  type=HeirarchyType.MENU.name();
                              }
                              else if(nextPart==null){
                                  type=HeirarchyType.SUB_MODULE.name();
                              }

                              if(!type.equals(obj.getType())){
                                  List<MenuHierarchy> childs = new ArrayList<>();
                                  MenuHierarchy hr = new MenuHierarchy();
                                  hr.setMethodName(methodName);
                                  hr.setApiPattern(apiPattern);
                                  hr.setType(type);
                                  childs.add(hr);
                                  childGoneUnderNewParent.add(obj);
                                  if(!newParentHierarchy.containsKey(newModulePart)){
                                      newParentHierarchy.put(newModulePart,childs);
                                  }else{
                                      newParentHierarchy.get(newModulePart).add(hr);
                                  }
                              }

                        }
                 }

                 parent.getDetails().removeAll(childGoneUnderNewParent);
                 parent.getDetails().addAll(convertToList(newParentHierarchy));

             }
        }

        apiAgainstModuleRepo.saveAll(parentModules);

    }



    private List<MenuHierarchy> convertToList(Map<String, List<MenuHierarchy>> newParentHierarchy) {
        List<MenuHierarchy> newParentList = new ArrayList<>();
        if(newParentHierarchy.isEmpty()){
            return newParentList;
        }
        for(String newModule : newParentHierarchy.keySet()){
            MenuHierarchy parent = new MenuHierarchy();
            List<MenuHierarchy> childs =  newParentHierarchy.get(newModule);
            if(childs.get(0).getType().equals(HeirarchyType.MENU.name())){
                parent.setType(HeirarchyType.SUB_MODULE.name());
            }
            if(childs.get(0).getType().equals(HeirarchyType.SUB_MODULE.name())){
                parent.setType(HeirarchyType.MODULE.name());
            }
            parent.setName(newModule);
            parent.setDetails(childs);
            newParentList.add(parent);
        }
           return newParentList;
    }

}