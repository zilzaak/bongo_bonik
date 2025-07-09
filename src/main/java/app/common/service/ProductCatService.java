package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.Product;
import app.common.entity.ProductCat;
import app.common.repo.BranchRepo;
import app.common.repo.ProductCatRepo;
import app.common.repo.ProductRepo;
import app.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductCatService {

    @Autowired
    private ProductCatRepo catRepo;

    @Autowired
    private BranchRepo.OrgRepo orgRepo;

    @Autowired
    private ProductRepo productRepo;


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
        if(orgName==null){
            mp.put("hasError",true);
            mp.put("message","No Organization exist with id="+dto.getOrgId());
            return mp;
        }


        if(dto.getId()==null){
            if(catRepo.existsByNameAndOrgId(dto.getName(),dto.getOrgId())>0){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }

        }else{
            ProductCat cat = catRepo.findById(dto.getId()).orElse(null);
            if(cat==null){
                mp.put("hasError",true);
                mp.put("message","Data not found");
                return mp;
            }

            if(catRepo.existsByNameAndOrgIdAndIdNotIn(dto.getName(),dto.getOrgId(), Arrays.asList(dto.getId()))>0){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }

            if(!cat.getOrgId().equals(dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","you can not edit the organization because its usual is sensitive");
                return mp;
            }
                mp.put("cat",cat);
        }
        return mp;
    }

    public MsgResponse create(CommonDTO dto) {
         Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse((String)mp.get("message"),false);
        }
        ProductCat cat = new ProductCat();
        if(dto.getId()==null){
            cat.setName(dto.getName());
            cat.setOrgName(dto.getOrgName());
            cat.setOrgId(dto.getOrgId());
        }else{
            cat = (ProductCat) mp.get("cat");
            cat.setName(dto.getName());
            cat.setOrgName(dto.getOrgName());
            cat.setOrgId(dto.getOrgId());
        }
        catRepo.save(cat);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Edited successfully",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        if(productRepo.existsByCatId(dto.getId())){
            Product pd = productRepo.findTopByCatId(dto.getId());
            return  new MsgResponse("This category can not be delete , it is used in Product "+pd.getId()+"-"+pd.getName(),false);
        }else{
            catRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = catRepo.getList(dto.catId,dto.orgId,pageable);
        return CommonUtil.responseFromPage(page);
    }
}
