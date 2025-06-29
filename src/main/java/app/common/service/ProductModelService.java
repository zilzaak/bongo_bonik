package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.Brand;
import app.common.entity.Product;
import app.common.entity.ProductModel;
import app.common.repo.BrandRepo;
import app.common.repo.ProductModelRepo;
import app.common.repo.ProductRepo;
import app.common.util.CommonUtil;
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
public class ProductModelService {

    @Autowired
    private ProductModelRepo modelRepo;

    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private OrgRepo orgRepo;

    @Autowired
    private ProductRepo productRepo;

    Map<String,Object> formValidation(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null ||  dto.getBrandId()==null){
            mp.put("hasError",true);
            mp.put("message","Name , brand is required");
            return mp;
        }

        Brand brand = brandRepo.findById(dto.getBrandId()).orElse(null);
        if(brand==null){
            mp.put("hasError",true);
            mp.put("message","No Brand exist with id="+dto.getBrandId());
            return mp;
        }

        dto.setOrgName(brand.getOrgName());
        dto.setOrgId(brand.getOrgId());
        dto.setBrandName(brand.getName());

        if(dto.getId()==null){
            if(modelRepo.existsByNameAndBrandId(dto.getName(),dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
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
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }

            mp.put("model",model);

        }
        return mp;
    }


    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse((String) mp.get("message"),false);
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
        if(productRepo.existsByModelId(dto.getId())){
            Product pd = productRepo.findTopByModelId(dto.getId());
            return  new MsgResponse("This model can not be delete , it is used in Product "+pd.getId()+"-"+pd.getName(),false);
        }else{
            modelRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = modelRepo.getList(dto.brandId,dto.orgId,dto.modelId ,pageable);
        return CommonUtil.responseFromPage(page);
    }
}
