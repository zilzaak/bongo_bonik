package app.modules.moduleInfo.service;


import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.moduleInfo.dto.AgainstModuleDTO;
import app.modules.moduleInfo.dto.ModuleInfoDTO;
import app.modules.moduleInfo.entity.ApiAgainstModule;
import app.modules.moduleInfo.entity.ModuleInfo;
import app.modules.moduleInfo.repo.ApiAgainstModuleRepo;
import app.modules.moduleInfo.repo.ModuleInfoRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Service
public class ModuleInfoService {

    @Autowired
    private ModuleInfoRepo moduleInfoRepo;

    @Autowired
    private ApiAgainstModuleRepo detailsRepo;

    private final RequestMappingHandlerMapping handlerMapping;
    public ModuleInfoService(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }


    Map<String,Object> validate(ModuleInfoDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);
        if(dto.getName()==null && dto.getDetails().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Module name and menu under module is required field");
            return mp;
        }

        if(dto.getId()==null){
           if(moduleInfoRepo.existsByName(dto.getName())){
               mp.put("hasError",true);
               mp.put("message","Module name already exist");
               return mp;}
        }else{
            if(moduleInfoRepo.existsByNameAndIdNotIn(dto.getName(),Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","Module name already exist");
                return mp;
            }
        }


         for(AgainstModuleDTO dtl : dto.getDetails()){
            if(dtl.getApiPattern()==null || dtl.getMethodName()==null){
                mp.put("hasError",true);
                mp.put("message","Api pattern and Method name is required");
                return mp;
            }
            if(detailsRepo.existPatternOrName(dtl.getApiPattern() , Arrays.asList(dtl.getId()))>0){
                mp.put("hasError",true);
                mp.put("message","Api pattern and Method name is already exist");
                return mp;
            }
             int duplicity=0;
            for(AgainstModuleDTO x : dto.getDetails()){
                if(dtl.getApiPattern().equals(x.getApiPattern())){duplicity++;}}
            if(duplicity>1){
                mp.put("hasError",true);
                mp.put("message","duplicate Api pattern found ");
                return mp;
            }
          }

            return mp;

    }

    public MsgResponse create(ModuleInfoDTO dto) {
        Map<String,Object> mp = validate(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse(mp.get("message"),false);
        }
        ModuleInfo moduleInfo = new ModuleInfo();
        BeanUtils.copyProperties(dto,moduleInfo);
        if(moduleInfo.getId()==null){
            moduleInfoRepo.save(moduleInfo);
        }

        for(AgainstModuleDTO x : dto.getDetails()){
            ApiAgainstModule dtl = new ApiAgainstModule();
            BeanUtils.copyProperties(x,dtl);
            dtl.setModuleInfo(moduleInfo);
            detailsRepo.save(dtl);
            }
        return new MsgResponse("Successfully created",true);
    }

    public MsgResponse edit(ModuleInfoDTO dto) {

        return create(dto);
    }

    public MsgResponse delete(Long id) {

        return null;
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        return CommonUtil.responseFromPage(detailsRepo.getList(dto.getModuleId(),pageable));
    }


}
