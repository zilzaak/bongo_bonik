package app.common.service;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.MadeWith;
import app.common.entity.Product;
import app.common.repo.MadeWithRepo;
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
public class MadeWithService {
    @Autowired
    private MadeWithRepo madeWithRepo;
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

        String orgName = orgRepo.getName(dto.getOrgId());
        if(orgName==null){
            mp.put("hasError",true);
            mp.put("message","No Organization exist with id = "+dto.getOrgId());
            return mp;
        }
        dto.setOrgName(orgName);

        if(dto.getId()==null){
            if(madeWithRepo.existsByNameAndOrgId(dto.getName(),dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }

        }else{
            MadeWith mdwth = madeWithRepo.findById(dto.getId()).orElse(null);
            if(mdwth==null){
                mp.put("hasError",true);
                mp.put("message","Db data not found for edit");
                return mp;
            }
            if(madeWithRepo.existsByNameAndOrgIdAndIdNotIn(dto.getName(),dto.getOrgId(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message",dto.getName()+" is exist under organization "+dto.getOrgName()+" give unique name");
                return mp;
            }

            if(!mdwth.getOrgId().equals(dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","you can not edit the organization because its usual is sensitive");
                return mp;
            }
            mp.put("mdwth",mdwth);

        }
        return mp;
    }


    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = formValidation(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse("fail",false);
        }
        MadeWith mdwth = new MadeWith();
        if(dto.getId()==null){
            mdwth.setName(dto.getName());
            mdwth.setOrgName(dto.getOrgName());
            mdwth.setOrgId(dto.getOrgId());
        }else{
            mdwth = (MadeWith) mp.get("mdwth");
            BeanUtils.copyProperties(dto,mdwth,"created");
        }
        madeWithRepo.save(mdwth);
        return new MsgResponse(dto.getId()==null?"Successfully created":"Edited successfully",true);
    }

    public MsgResponse delete(CommonDTO dto) {
        if(productRepo.existsByMadeWithId(dto.getId())){
            Product pd = productRepo.findTopByMadeWithId(dto.getId());
            return  new MsgResponse("This size can not be delete , it is used in Product "+pd.getId()+"-"+pd.getName(),false);
        }else{
            madeWithRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = madeWithRepo.getList(dto.madeWithId,dto.orgId,pageable);
        return CommonUtil.responseFromPage(page);
    }

}
