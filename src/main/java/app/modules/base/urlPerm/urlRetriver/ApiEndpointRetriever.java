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
    public List<String> urlContains = Arrays.asList("create", "get", "update", "edit", "delete", "list");

    public ApiEndpointRetriever(RequestMappingHandlerMapping mapping,
                                MenuHierarchyRepo apiAgainstModuleRepo) {
        this.requestMappingHandlerMapping = mapping;
        this.apiAgainstModuleRepo = apiAgainstModuleRepo;
    }

    private String getMethod(String method) {
        method = CommonUtil.removeCharFromString(method, '/');
        method = CommonUtil.removeCharFromString(method, '[');
        method = CommonUtil.removeCharFromString(method, ']');

        return method;
    }


    public List<String[]> allUrlWithMethod() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        List<String> api = new ArrayList<>();
        List<String> methods = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            Set<String> apiInfo = info.getDirectPaths();
            if (apiInfo.size() > 0) {
                String method = "/" + info.getMethodsCondition().getMethods();
                method = getMethod(method);
                String apiUrl = new ArrayList<>(apiInfo).get(0);
                apiUrl = CommonUtil.removeAllSpace(apiUrl);
                if (!apiUrl.contains("error")) {
                    api.add(apiUrl);
                    methods.add(method);
                }
            }
        }
        String[] apiArray = api.toArray(new String[0]);
        String[] methodArray = methods.toArray(new String[0]);
        List<String[]> list = new ArrayList<>();
        list.add(apiArray);
        list.add(methodArray);
        return  list;

    }


    @PostConstruct
    public void categoryIntoSubModuleOrMenu() {
        List<String[]> allApi = this.allUrlWithMethod();
        String[] apiUrls = allApi.get(0);
        String[] methods = allApi.get(1);

        List<MenuHierarchy> menuTree = new ArrayList<MenuHierarchy>();
        Map<String,String> menuTracker = new HashMap<>();

        for(int i=0 ; i<apiUrls.length ;i++){
            String api=apiUrls[i];
            String[] apiParts = api.split("/");
            String method=methods[i];

            for(int j=0;j<apiParts.length;j++){
                if(j==0)continue;
                String childMenu = apiParts[j];
                String parentMenu=(j>=2)?apiParts[j-1]:null;
                TreePartTrack track = new TreePartTrack();
                track.apiUrl=api;
                track.childMenu=childMenu;
                track.parentMenu=parentMenu;
                track.isLastPart=(apiParts.length==(j+1));
                track.methodName=method;
                this.setUnderParent(menuTree,track,menuTracker);

            }

        }

    
    }


    public void setUnderParent(List<MenuHierarchy> menuTree,TreePartTrack track,Map<String,String> menuTracker){
        if(menuTree.size()<1 && !menuTracker.containsKey(track.childMenu)){
            MenuHierarchy menu = new MenuHierarchy();
            menu.setMenu(track.childMenu);
            menu.setParentMenu(track.parentMenu);
            menuTree.add(menu);
            menuTracker.put(track.childMenu,track.childMenu);
        }else{
            for(MenuHierarchy menu : menuTree){
                if(menu.getMenu().equals(track.parentMenu)){
                    MenuHierarchy m = new MenuHierarchy();
                    m.setMenu(track.childMenu);
                    m.setParentMenu(track.parentMenu);
                    if(track.isLastPart){
                     m.setMethodName(track.methodName);
                     m.setApiPattern(track.apiUrl);
                    }
                    menu.getDetails().add(m);
                    menuTracker.put(track.childMenu,track.childMenu);
                    break;
                }else{
                  this.setUnderParent(menu.getDetails(),track,menuTracker);
                }
            }

        }
    }


//    public void saveMenuTree(List<MenuHierarchy> menuTree){
//
//            for(MenuHierarchy menu : menuTree){
//                if(menu.getDetails().size()>0){
//                   this.saveMenuTree(menu.getDetails());
//                }else{
//                  if(!apiAgainstModuleRepo.existsByMenuAndApiPatternInAndParentMenuIn(menu.getMenu(),Arrays.asList(menu.getApiPattern()),Arrays.asList(menu.getParentMenu()))){
//                      if(menu.getParentMenu()!=null){
//                          MenuHierarchy dbItem = apiAgainstModuleRepo.findByMenuAndApiPatternAndParentMenu();
//                      }else{
//
//                      }
//                  }
//                }
//            }
//    }
}