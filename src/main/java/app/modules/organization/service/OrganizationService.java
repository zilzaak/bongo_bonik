package app.modules.organization.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.organization.entity.Organization;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrganizationService {

    @Autowired
    private OrgRepo orgRepo;

    Map<String,Object> validate(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getName().isEmpty() ||
                dto.getPhone()==null ||
                dto.getPhone().isEmpty() ||
                dto.getAddress()==null || dto.getAddress().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Phone no , org name , address are required field");
            return mp;
        }

        if(dto.getId()==null){
           if(orgRepo.existsByName(dto.getName())){
               mp.put("hasError",true);
               mp.put("message","Org name must be unique");
               return mp;
           }


        }else{

            Organization org = orgRepo.findById(dto.getId()).orElse(null);
            if(org==null){
                mp.put("hasError",true);
                mp.put("message","Organization not found");
                return mp;
            }
            if(orgRepo.existsByNameAndIdNotIn(dto.getName(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","Org name must be unique");
                return mp;
            }

            mp.put("org",org);

        }

        return mp;

    }

    public MsgResponse create(CommonDTO dto) {

        Map<String, Object> mp = validate(dto);
         String sms = null;
         if((boolean)mp.get("hasError")){
             return new MsgResponse((String) mp.get("message"), false);
         }
         Organization org = new Organization();
         if(dto.getId()==null){
             org.setName(dto.getName());
             org.setPhone(dto.getPhone());
             org.setAddress(dto.getAddress());
             org.setLocation(dto.getLocation());
             sms="Successfully created";
         }else{
             org = (Organization) mp.get("org");
             BeanUtils.copyProperties(dto,org,"created","updated");
             sms="Successfully updated";
         }
         orgRepo.save(org);
         return new MsgResponse(sms,true);
    }


    public MsgResponse edit(CommonDTO dto) {
        return create(dto);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = orgRepo.getList(dto.orgId,dto.commonField,pageable);
        return CommonUtil.responseFromPage(page);

    }
}
