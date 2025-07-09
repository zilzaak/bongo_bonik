package app.common.service;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.Product;
import app.common.entity.ProductColor;
import app.common.repo.BranchRepo;
import app.common.repo.ProductColorRepo;
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
public class ProductColorService {

    @Autowired
    private ProductColorRepo colorRepo;

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
        if(orgName==null){
            mp.put("hasError",true);
            mp.put("message","No Organization exist with id = "+dto.getOrgId());
            return mp;
        }
        dto.setOrgName(orgName);

        if(dto.getId()==null){
            if(colorRepo.existsByNameAndOrgId(dto.getName(),dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }

        }else{
            ProductColor color = colorRepo.findById(dto.getId()).orElse(null);
            if(color==null){
                mp.put("hasError",true);
                mp.put("message","Db data not found for edit");
                return mp;
            }
            if(colorRepo.existsByNameAndOrgIdAndIdNotIn(dto.getName(),dto.getOrgId(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }

            if(!color.getOrgId().equals(dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","you can not edit the organization because its usual is sensitive");
                return mp;
            }

            mp.put("color",color);

        }
        return mp;
    }


    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse("fail",false);
        }
        ProductColor color = new ProductColor();
        if(dto.getId()==null){
            color.setName(dto.getName());
            color.setOrgName(dto.getOrgName());
            color.setOrgId(dto.getOrgId());
        }else{
            color = (ProductColor) mp.get("color");
            BeanUtils.copyProperties(dto,color,"created","updated");
        }
        colorRepo.save(color);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Edited successfully",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        if(productRepo.existsByColorId(dto.getId())){
            Product pd = productRepo.findTopByColorId(dto.getId());
            return  new MsgResponse("This color can not be delete , it is used in Product "+pd.getId()+"-"+pd.getName(),false);
        }else{
            colorRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = colorRepo.getList(dto.colorId,dto.orgId,pageable);
        return CommonUtil.responseFromPage(page);
    }
}
