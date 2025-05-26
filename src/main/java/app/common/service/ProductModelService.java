package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.entity.Brand;
import app.common.entity.ProductModel;
import app.common.repo.BrandRepo;
import app.common.repo.ProductModelRepo;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductModelService {

    @Autowired
    private ProductModelRepo modelRepo;

    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private OrgRepo orgRepo;

    Map<String,Object> formValidation(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null ||  dto.getBrandId()==null){
            mp.put("hasError",true);
            mp.put("message","Name , brand is required are required");
            return mp;
        }

        Brand brand = brandRepo.findById(dto.getBrandId()).orElse(null);
        dto.setOrgName(brand.getOrgName());
        dto.setOrgId(brand.getOrgId());
        dto.setBrandName(brand.getName());

        if(dto.getId()==null){
            if(modelRepo.existsByNameAndBrandId(dto.getName(),dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","Name against"+dto.getOrgName()+" already exist , give unique name");
                return mp;
            }

        }else{
            ProductModel model = modelRepo.findById(dto.getId()).orElse(null);
            if(model==null){
                mp.put("hasError",true);
                mp.put("message","Db data not found for edit");
                return mp;
            }
            if(modelRepo.existsByNameAndBrandIdAndIdNotIn(dto.getName(),dto.getOrgId(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","Name against"+dto.getOrgName()+" already exist , give unique name");
                return mp;
            }

            mp.put("model",model);

        }
        return mp;
    }


    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse("fail",false);
        }
        ProductModel model = new ProductModel();
        if(dto.getId()==null){
            model.setName(dto.getName());
            model.setBrandId(dto.getBrandId());
            model.setBrandName(dto.getBrandName());
            model.setOrgName(dto.getOrgName());
            model.setOrgId(dto.getOrgId());
        }else{
            model = (ProductModel) mp.get("model");
            BeanUtils.copyProperties(dto,model,"created","updated");
        }
        modelRepo.save(model);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Edited successfully",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        return null;
    }

    public MsgResponse getList(Map<String, String> params) {

        return null;
    }
}
