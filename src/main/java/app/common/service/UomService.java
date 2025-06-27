package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.ProductCat;
import app.common.entity.ProductSize;
import app.common.entity.UnitOfMeasure;
import app.common.repo.ProductCatRepo;
import app.common.repo.ProductSizeRepo;
import app.common.repo.UnitOfMeasureRepo;
import app.common.util.CommonUtil;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UomService {

    @Autowired
    private UnitOfMeasureRepo uomRepo;

    @Autowired
    private OrgRepo orgRepo;

    @Autowired
    private ProductCatRepo catRepo;

    Map<String,Object> formValidation(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getProductCatIds()==null){
            mp.put("hasError",true);
            mp.put("message","Name , Product category are required");
            return mp;
        }

        List<String> cats = CommonUtil.bulkStrToList(dto.getProductCatIds());
        List<Long> catIdLst = CommonUtil.strListToLong(cats);

        Long orgId=null;
        for(Long id : catIdLst){

            ProductCat pcat = catRepo.findById(id).orElse(null);
            if(pcat==null){
                mp.put("hasError",true);
                mp.put("message","product category not exist with id"+id);
                return mp;
            }
            if(orgId==null){
                orgId=pcat.getOrgId();
            }else{
                if(!orgId.equals(pcat.getOrgId())){
                    mp.put("hasError",true);
                    mp.put("message","product "+pcat.getName()+" is not under same organization");
                    return mp;
                }
            }

        }

        String orgName = orgRepo.getName(orgId);
        dto.setOrgName(orgName);
        dto.setOrgId(orgId);

        List<String> existCats = uomRepo.getExistCat(dto.getName(),dto.getOrgId());

        if(dto.getId()==null){
            for(String m : existCats){
                   for(String k : cats){
                       if(m.contains(k)){
                           mp.put("hasError",true);
                           mp.put("message","product cat already assigned for selected UOM");
                           return mp;
                       }
                   }
               }


        }else{
            UnitOfMeasure uom = uomRepo.findById(dto.getId()).orElse(null);
            if(uom==null){
                mp.put("hasError",true);
                mp.put("message","Db data not found for edit");
                return mp;
            }
            existCats = uomRepo.getExistCatExceptId(dto.getName(),dto.getOrgId(),dto.getId());
            for(String m : existCats){
                for(String k : cats){
                    if(m.contains(k)){
                        mp.put("hasError",true);
                        mp.put("message","product cat already assigned for selected UOM");
                        return mp;
                    }
                }
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
            oum.setProductCatIds(dto.getProductCatIds());
        }else{
            oum = (UnitOfMeasure) mp.get("oum");
            BeanUtils.copyProperties(dto,oum,"created","updated");
        }
        uomRepo.save(oum);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Edited successfully",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        uomRepo.deleteById(dto.getId());
        return null;
    }

    public MsgResponse getList(SearchParamDTO dto) {

        return null;
    }
}
