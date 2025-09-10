package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.Brand;
import app.common.entity.Product;
import app.common.entity.ProductModel;
import app.common.repo.*;
import app.common.util.CommonUtil;
import app.modules.base.org.entity.Organization;
import app.modules.base.org.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        dto.setOrgId(brand.getOrg().getId());

        if(dto.getId()==null){
            if(modelRepo.existsByNameAndBrandId(dto.getName(),dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }
            List<Map<String,Object>> existBrand=modelRepo.existData(dto.getOrgId(),dto.getName(),dto.getId());
            if(!dto.confirmSimilarity && existBrand.size()>0){
                mp.put("hasError",true);
                mp.put("message","Similar criteria value exist , please recheck before confirm");
                mp.put("productCrit",existBrand);
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
            List<Map<String,Object>> existBrand=modelRepo.existData(dto.getOrgId(),dto.getName(),dto.getId());
            if(!dto.confirmSimilarity && existBrand.size()>0){
                mp.put("hasError",true);
                mp.put("message","Similar criteria value exist , please recheck before confirm");
                mp.put("productCrit",existBrand);
                return mp;
            }
            if(!brand.getOrg().getId().equals(model.getOrg().getId())){
                mp.put("hasError",true);
                mp.put("message","you can not edit the organization because its usual is sensitive");
                return mp;
            }

            mp.put("model",model);

        }
        return mp;
    }


    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse((String) mp.get("message"),mp,false);
        }
        ProductModel model = new ProductModel();
        if(dto.getId()==null){
            model.setName(dto.getName());
            Brand b=new Brand();
            b.setId(dto.getBrandId());
            model.setBrand(b);
            Organization o=new Organization();
            o.setId(dto.getOrgId());
            model.setOrg(o);
            model.setCreateBy(CommonUtil.currentUser());
            model.setDescription(dto.getDescription());
        }else{
            model = (ProductModel) mp.get("model");
            BeanUtils.copyProperties(dto,model,"created","createBy");
            Brand b=new Brand();
            b.setId(dto.getBrandId());
            model.setBrand(b);
            Organization o=new Organization();
            o.setId(dto.getOrgId());
            model.setOrg(o);
            model.setUpdateBy(CommonUtil.currentUser());
            model.setDescription(dto.getDescription());
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
        Page<Map<String,Object>> page = modelRepo.getList(dto.brandId,dto.orgId,dto.id ,dto.getName(),pageable);
        return CommonUtil.responseFromPage(page);
    }
}
