package app.modules.security.allEndpoint;

import app.common.util.CommonUtil;
import app.modules.moduleInfo.entity.ApiAgainstModule;
import app.modules.moduleInfo.entity.ModuleInfo;
import app.modules.moduleInfo.repo.ApiAgainstModuleRepo;
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
    private final ApiAgainstModuleRepo apiAgainstModuleRepo;

    public ApiEndpointRetriever(RequestMappingHandlerMapping mapping ,
                                ModuleInfoRepo moduleInfoRepo ,
                                ApiAgainstModuleRepo apiAgainstModuleRepo) {
        this.requestMappingHandlerMapping = mapping;
        this.moduleInfoRepo = moduleInfoRepo;
        this.apiAgainstModuleRepo = apiAgainstModuleRepo;
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


        for(String module : apiListUnderModule.keySet()){
            List<String> apiList = apiListUnderModule.get(module);
            ModuleInfo moduleInfo = moduleInfoRepo.findByName(module);
            if(moduleInfo==null){
                moduleInfo = new ModuleInfo();
                moduleInfo.setName(module);
            }

            for(String api : apiList ){

                String arr[] = api.split(">");
                if(arr.length<2){
                    continue;
                }

                ApiAgainstModule apiUnderModule = new ApiAgainstModule();
                apiUnderModule.setApiPattern(arr[0]);
                apiUnderModule.setMethodName(arr[1]);
                boolean alReadyExist=false;
                for(ApiAgainstModule dtl : moduleInfo.getDetails()){
                    if(dtl.getApiPattern().equals(arr[0])){
                        alReadyExist=true;
                        break;
                    }
                }

                if(!alReadyExist){
                    moduleInfo.getDetails().add(apiUnderModule);
                    apiUnderModule.setModuleInfo(moduleInfo);
                    moduleInfoRepo.save(moduleInfo);
                }
            }


        }



    }
}