package app.modules.security.allEndpoint;

import app.common.util.CommonUtil;
import app.modules.moduleInfo.repo.ModuleInfoRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Component
public class ApiEndpointRetriever {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final ModuleInfoRepo moduleInfoRepo;

    public ApiEndpointRetriever(RequestMappingHandlerMapping mapping , ModuleInfoRepo moduleInfoRepo) {
        this.requestMappingHandlerMapping = mapping;
        this.moduleInfoRepo = moduleInfoRepo;
    }

    private String getMethod(String method){
            method=CommonUtil.removeCharFromString(method,'/');
            method=CommonUtil.removeCharFromString(method,'[');
            method=CommonUtil.removeCharFromString(method,']');

        return  method;
    }

    @PostConstruct
    public void logRegisteredEndpoints() {

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        Map<String,List<String>> moduleApiList = new HashMap<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            Set<String> x  = info.getDirectPaths();
            String method = "/"+info.getMethodsCondition().getMethods();
            if(x.size()>0){
                String apiUrl = new ArrayList<>(x).get(0);
                String[] arr = apiUrl.split("/");
                String module = arr[0];
                method=getMethod(method);
                apiUrl=apiUrl+">"+method;
                if(!moduleApiList.containsKey(module)){
                    List<String> apiList = new ArrayList<>();
                    apiList.add(apiUrl);
                    moduleApiList.put(module,apiList);
                }else{
                    moduleApiList.get(module).add(apiUrl);
                }
            }
        }


        System.out.println(moduleApiList.keySet());



    }
}