package app.modules.base.urlPerm.urlRetriver;

import app.common.util.CommonUtil;
import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.moduleInfo.repo.MenuHierarchyRepo;
import app.modules.base.role.entity.Role;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.urlPerm.entity.PermittedApi;
import app.modules.base.urlPerm.repo.PermittedApiRepository;
import app.modules.base.user.repo.UserRepository;
import app.modules.base.user.service.UserService;
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
    private final PermittedApiRepository permittedApiRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public ApiEndpointRetriever(RequestMappingHandlerMapping mapping,
                                MenuHierarchyRepo apiAgainstModuleRepo,
                                PermittedApiRepository permittedApiRepository,
                                UserRepository userRepository,
                                RoleRepository roleRepository,
                                UserService userService) {
        this.requestMappingHandlerMapping = mapping;
        this.apiAgainstModuleRepo = apiAgainstModuleRepo;
        this.permittedApiRepository=permittedApiRepository;
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.userService=userService;
    }

    private String getMethod(String method) {
        method = CommonUtil.removeCharFromString(method, '/');
        method = CommonUtil.removeCharFromString(method, '[');
        method = CommonUtil.removeCharFromString(method, ']');
        return method;
    }

    public List<String> crudMenu = Arrays.asList("create","edit","update","delete","list","get");
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


    public TreePartTrack makeTracker(int j ,
                                     String childMenu,
                                     String parentMenu,
                                     String api,
                                     int length,
                                     String method,String apiSeq){
        TreePartTrack track = new TreePartTrack();
        track.apiUrl=api;
        track.childMenu=childMenu;
        track.parentMenu=parentMenu;
        track.isLastPart=(length==(j+1));
        track.methodName=method;
        track.apiSeq=apiSeq;
        return track;
    }


    public MenuHierarchy makeMenuHierarchy(TreePartTrack track,Map<String,String> partTracker){
        MenuHierarchy m = new MenuHierarchy();
        m.setMenu(track.childMenu);
        m.setParentMenu(track.parentMenu);
        m.setApiSeq(track.apiSeq);
        if(track.isLastPart){
            m.setMethodName(track.methodName);
            m.setApiPattern(track.apiUrl);
            if(m.getApiPattern().equals("/base/user/list")){
              m.setFrontUrl("base/user/list");
            }
            else if(m.getApiPattern().equals("/base/role/list")){
                m.setFrontUrl("base/role/list");
            }
            else if(m.getApiPattern().equals("/base/module/list")){
                m.setFrontUrl("base/menu/list");
            }
            else if(m.getApiPattern().equals("/base/permittedModule/list")){
                m.setFrontUrl("base/menuPerm/list");
            }
        }
        partTracker.put(track.apiSeq,track.apiSeq);
        return m;
    }


    @PostConstruct
    public void categoryIntoSubModuleOrMenu() {
        userService.createDefaultUser();
        List<String[]> allApi = this.allUrlWithMethod();
        String[] apiUrls = allApi.get(0);
        String[] methods = allApi.get(1);

        List<MenuHierarchy> menuTree = new ArrayList<MenuHierarchy>();
        Map<String,String> partTracker = new HashMap<>();
        for(int i=0 ; i<apiUrls.length ;i++){
            String api=apiUrls[i];
            String[] apiParts = api.split("/");
            String method=methods[i];

            String apiSeq=null;
            for(int j=0;j<apiParts.length;j++){
                if(j==0)continue;

                String childMenu = apiParts[j];
                String parentMenu=(j>=2)?apiParts[j-1]:null;

                if(apiSeq==null){apiSeq=childMenu;}else{apiSeq=apiSeq+"/"+childMenu;}

                TreePartTrack track = this.makeTracker(j,childMenu,parentMenu,api,apiParts.length,method,apiSeq);

                if(menuTree.size()<1){
                    menuTree.add(this.makeMenuHierarchy(track,partTracker));
                }
                else{
                    if(!partTracker.containsKey(track.apiSeq)){
                        this.setUnderParent(menuTree,track,partTracker);
                    }
                }
            }
        }
        Role role = roleRepository.findByAuthority("SUPER_ADMIN");
        if(this.apiAgainstModuleRepo.count()<1){
            this.apiAgainstModuleRepo.saveAll(menuTree);
        }
        if(permittedApiRepository.countByRole(role)<1){
            List<MenuHierarchy> menuList = apiAgainstModuleRepo.getAllUrl();
            this.createApiPermission(menuList,role);
        }
    }


    private void createApiPermission(List<MenuHierarchy> menuTree, Role role) {
        Role permitAll = roleRepository.findByAuthority("PERMIT_ALL");
        List<PermittedApi>  list = new ArrayList<>();
        for(MenuHierarchy m : menuTree){
            PermittedApi x = new PermittedApi();
            if(!m.getApiPattern().contains("/getToken")){
                x.setRole(role);
            }else{
                x.setRole(permitAll);
            }
            x.setBackendUrl(m.getApiPattern());
            x.setMenuId(m.getId());
            String  ids=m.getId().toString();

            Long parentId = apiAgainstModuleRepo.findParentIdById(m.getId());
            MenuHierarchy k=null;
            if(parentId!=null){
                k = apiAgainstModuleRepo.findById(parentId).orElse(null);
            }

            while(k!=null){
                ids=ids+","+k.getId();
                parentId = apiAgainstModuleRepo.findParentIdById(k.getId());
                if(parentId!=null){
                    k = apiAgainstModuleRepo.findById(parentId).orElse(null);
                }else{
                    k=null;
                }
            }
            x.setMenuIdsHierarchy(ids);
            list.add(x);
        }

        this.permittedApiRepository.saveAll(list);
    }


    public void setUnderParent(List<MenuHierarchy> menuTree,TreePartTrack track,Map<String,String> partTracker){
        Boolean parentFound=false;
        for(MenuHierarchy menu : menuTree){
            String parentSeq = null ;
            if(track.apiSeq.contains("/")){
                int lastSlashIndex = track.apiSeq.lastIndexOf('/'); // Finds
                parentSeq = track.apiSeq.substring(0, lastSlashIndex);
            }
            if(parentSeq==null){
                parentFound=true;
                break;
            }
            else if(menu.getApiSeq().equals(parentSeq)){
                menu.getDetails().add(this.makeMenuHierarchy(track,partTracker));
                break;
            }else{
                this.setUnderParent(menu.getDetails(),track,partTracker);
            }
        }

        if(parentFound){
            menuTree.add(this.makeMenuHierarchy(track,partTracker));
        }
    }

}