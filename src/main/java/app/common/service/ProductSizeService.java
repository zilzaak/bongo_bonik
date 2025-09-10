package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.Product;
import app.common.entity.ProductSize;
import app.modules.base.org.repo.OrgRepo;
import app.common.repo.ProductRepo;
import app.common.repo.ProductSizeRepo;
import app.common.util.CommonUtil;
import app.modules.base.org.entity.Organization;
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
public class ProductSizeService {

    @Autowired
    private ProductSizeRepo sizeRepo;

    @Autowired
    private OrgRepo orgRepo;

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

        if(!orgRepo.existsById(dto.getOrgId())){
            mp.put("hasError",true);
            mp.put("message","No Organization exist with id = "+dto.getOrgId());
            return mp;
        }

        if(dto.getId()==null){
            if(sizeRepo.existsByName(dto.getName())){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }
            List<Map<String,Object>> existBrand=sizeRepo.existData(dto.getOrgId(),dto.getName(),dto.getId());
            if(!dto.confirmSimilarity && existBrand.size()>0){
                mp.put("hasError",true);
                mp.put("message","Similar criteria value exist , please recheck before confirm");
                mp.put("productCrit",existBrand);
                return mp;
            }

        }else{
            ProductSize size = sizeRepo.findById(dto.getId()).orElse(null);
            if(size==null){
                mp.put("hasError",true);
                mp.put("message","Db data not found for edit");
                return mp;
            }
            if(sizeRepo.existsByNameAndIdNotIn(dto.getName(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }
            List<Map<String,Object>> existBrand=sizeRepo.existData(dto.getOrgId(),dto.getName(),dto.getId());
            if(!dto.confirmSimilarity && existBrand.size()>0){
                mp.put("hasError",true);
                mp.put("message","Similar criteria value exist , please recheck before confirm");
                mp.put("productCrit",existBrand);
                return mp;
            }
            if(!size.getOrgId().equals(dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","you can not edit the organization because its usual is sensitive");
                return mp;
            }
            mp.put("size",size);

        }
        return mp;
    }


    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse((String) mp.get("message"),mp,false);
        }
        ProductSize size = new ProductSize();
        if(dto.getId()==null){
            Organization organization = orgRepo.findById(dto.getOrgId()).get();
            size.setName(dto.getName());
            size.setOrgId(dto.getOrgId());
            size.setOrgName(organization.getName());
            size.setCreateBy(CommonUtil.currentUser());
            size.setDescription(dto.getDescription());
        }else{
            size = (ProductSize) mp.get("size");
            BeanUtils.copyProperties(dto,size,"created","createBy");
            size.setUpdateBy(CommonUtil.currentUser());
        }
        sizeRepo.save(size);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Edited successfully",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        if(productRepo.existsBySizeId(dto.getId())){
            Product pd = productRepo.findTopBySizeId(dto.getId());
            return  new MsgResponse("This size can not be delete , it is used in Product "+pd.getId()+"-"+pd.getName(),false);
        }else{
            sizeRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = sizeRepo.getList(dto.id,dto.orgId,dto.getName(),pageable);
        return CommonUtil.responseFromPage(page);
    }
}
