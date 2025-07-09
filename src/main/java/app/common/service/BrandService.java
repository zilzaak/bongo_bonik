package app.common.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.Brand;
import app.common.entity.Product;
import app.common.repo.BranchRepo;
import app.common.repo.BrandRepo;
import app.common.repo.OrgRepo;
import app.common.repo.ProductRepo;
import app.common.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class BrandService {
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private OrgRepo orgRepo;

    @Autowired
    private ProductRepo productRepo;

    Map<String,Object> formValidation(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getOrgId()==null){
            mp.put("hasError",true);
            mp.put("message","Brand Name and Organization is required");
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
           if(brandRepo.existsByNameAndOrgId(dto.getName(),dto.getOrgId())>0){
               mp.put("hasError",true);
               mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
               return mp;
           }

        }else{
            Brand brand = brandRepo.findById(dto.getId()).orElse(null);
            if(brand==null){
                mp.put("hasError",true);
                mp.put("message","Db data not found for edit");
                return mp;
            }
            if(brandRepo.existsByNameAndOrgIdAndIdNotIn(dto.getName(),dto.getOrgId(), Arrays.asList(dto.getId()))>0){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }
            if(!brand.getOrgId().equals(dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","you can not edit the organization because its usual is sensitive");
                return mp;
            }

            mp.put("brand",brand);

        }
         return mp;
    }


    public MsgResponse create(CommonDTO dto) {
          Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse((String) mp.get("message"),false);
        }
        Brand brand = new Brand();
        if(dto.getId()==null){
            brand.setName(dto.getName());
            brand.setOrgName(dto.getOrgName());
            brand.setOrgId(dto.getOrgId());
        }else{
            brand = (Brand) mp.get("brand");
            BeanUtils.copyProperties(dto,brand,"created");
        }
        brandRepo.save(brand);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Successfully edited",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        if(productRepo.existsByBrandId(dto.getId())){
            Product pd = productRepo.findTopByBrandId(dto.getId());
            return  new MsgResponse("This brand can not be delete , it is used in Product "+pd.getId()+"-"+pd.getName(),false);
        }else{
            brandRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = brandRepo.getList(dto.brandId,dto.orgId,pageable);
        return CommonUtil.responseFromPage(page);
    }
}
