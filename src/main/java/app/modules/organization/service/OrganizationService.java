package app.modules.organization.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.modules.organization.entity.Organization;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

        if(dto.getName()==null || dto.getPhone()==null || dto.getAddress()==null){
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

         if((boolean)mp.get("hasError")){
             return new MsgResponse("Invalid form ", false);
         }
         Organization org = new Organization();
         if(dto.getId()==null){
             org.setName(dto.getName());
             org.setPhone(dto.getPhone());
             org.setAddress(dto.getAddress());
             org.setLocation(dto.getLocation());
         }else{
             org = (Organization) mp.get("org");
             BeanUtils.copyProperties(dto,org,"created","updated");
         }
         orgRepo.save(org);
         return new MsgResponse("Successfully created",true);
    }


    public MsgResponse edit(CommonDTO dto) {
        return create(dto);
    }

    public MsgResponse getList(Map<String, String> params) {

        return null;
    }
}
