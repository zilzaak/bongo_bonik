package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.entity.ProductSize;
import app.common.entity.UnitOfMeasure;
import app.common.repo.ProductSizeRepo;
import app.common.repo.UnitOfMeasureRepo;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class UomService {

    @Autowired
    private UnitOfMeasureRepo uomRepo;

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
            if(uomRepo.existsByNameAndOrgId(dto.getName(),dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","Name against"+dto.getOrgName()+" already exist , give unique name");
                return mp;
            }

        }else{
            UnitOfMeasure uom = uomRepo.findById(dto.getId()).orElse(null);
            if(uom==null){
                mp.put("hasError",true);
                mp.put("message","Db data not found for edit");
                return mp;
            }
            if(uomRepo.existsByNameAndOrgIdAndIdNotIn(dto.getName(),dto.getOrgId(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","Name against"+dto.getOrgName()+" already exist , give unique name");
                return mp;
            }

            mp.put("uom",uom);

        }
        return mp;
    }


    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse("fail",false);
        }
        UnitOfMeasure oum = new UnitOfMeasure();
        if(dto.getId()==null){
            oum.setName(dto.getName());
            oum.setOrgName(dto.getOrgName());
            oum.setOrgId(dto.getOrgId());
        }else{
            oum = (UnitOfMeasure) mp.get("oum");
            BeanUtils.copyProperties(dto,oum,"created","updated");
        }
        uomRepo.save(oum);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Edited successfully",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        return null;
    }

    public MsgResponse getList(Map<String, String> params) {

        return null;
    }
}
