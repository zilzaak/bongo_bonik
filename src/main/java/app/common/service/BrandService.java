package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.entity.Brand;
import app.common.repo.BrandRepo;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class BrandService {
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private OrgRepo orgRepo;

    Map<String,Object> formValidation(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getOrgId()==null){
            mp.put("hasError",true);
            mp.put("message","Name , Organization are required");
            return mp;
        }

        String orgName = orgRepo.getName(dto.getOrgId());
        dto.setOrgName(orgName);

        if(dto.getId()==null){
           if(brandRepo.existsByNameAndOrgId(dto.getName(),dto.getOrgId())){
               mp.put("hasError",true);
               mp.put("message","Name against"+dto.getOrgName()+" already exist , give unique name");
               return mp;
           }

        }else{
            if(brandRepo.existsByNameAndOrgIdAndIdNotIn(dto.getName(),dto.getOrgId(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","Name against"+dto.getOrgName()+" already exist , give unique name");
                return mp;
            }

        }
         return mp;
    }


    public MsgResponse create(CommonDTO dto) {

        if((boolean)formValidation(dto).get("hasError")){
            return new MsgResponse("fail",false);
        }
        Brand brand = new Brand();
        brand.setName(dto.getName());
        brand.setOrgName(dto.getOrgName());
        brand.setOrgId(dto.getOrgId());
        brandRepo.save(brand);
        return new MsgResponse("Successfully created",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        return null;
    }
}
