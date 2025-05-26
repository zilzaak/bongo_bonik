package app.modules.organization.service;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.modules.organization.entity.Branch;
import app.modules.organization.entity.Organization;
import app.modules.organization.repo.BranchRepo;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class BranchService {


    @Autowired
    private BranchRepo branchRepo;


    Map<String,Object> validate(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getOrgId()==null || dto.getAddress()==null){
            mp.put("hasError",true);
            mp.put("message","name , organization , address are required field");
            return mp;
        }

        Organization org = new Organization();
        org.setId(dto.getOrgId());

        if(dto.getId()==null){
            if(branchRepo.existsByNameAndOrg(dto.getName(),org)){
                mp.put("hasError",true);
                mp.put("message","This branch already exist against selected Organization");
                return mp;
            }


        }else{
            if(branchRepo.existsByNameAndOrgAndIdNotIn(dto.getName(), org , Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","This branch already exist against selected Organization");
                return mp;
            }

        }
        mp.put("org",org);
        return mp;

    }

    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = validate(dto);
       if((boolean)mp.get("hasError")){
           return new MsgResponse((String)validate(dto).get("message"),false);
       }
       Branch branch = new Branch();
       branch.setOrg((Organization) mp.get("org"));
       branch.setName(dto.getName());
       branch.setPhone(dto.getPhone());
       branch.setAddress(dto.getAddress());
       branch.setLocation(dto.getLocation());
       branchRepo.save(branch);
       return  new MsgResponse(dto.getId()==null?"Successfully created":"Successfully updated",false);
    }

    public MsgResponse edit(CommonDTO dto) {
        return  create(dto);
    }

    public MsgResponse getList(Map<String, String> params) {
        return null;
    }
}
