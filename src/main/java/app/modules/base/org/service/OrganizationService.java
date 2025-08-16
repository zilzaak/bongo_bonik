package app.modules.base.org.service;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.*;
import app.common.repo.*;
import app.common.util.CommonUtil;
import app.common.entity.Branch;
import app.modules.base.org.entity.Organization;
import app.common.repo.BranchRepo;
import app.modules.base.org.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrganizationService {

    @Autowired
    private OrgRepo orgRepo;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private BrandRepo brandRepo;

    @Autowired
    private ProductCatRepo catRepo;

    @Autowired
    private ProductModelRepo modelRepo;

    @Autowired
    private ProductSizeRepo sizeRepo;

    @Autowired
    private ProductColorRepo colorRepo;

    @Autowired
    private UnitOfMeasureRepo unitOfMeasureRepo;

    @Autowired
    private MadeWithRepo madeWithRepo;


    Map<String,Object> validate(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getName().isEmpty() ||
                dto.getPhone()==null ||
                dto.getPhone().isEmpty() ||
                dto.getAddress()==null || dto.getAddress().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Phone no , org name , address are required field");
            return mp;
        }

        if(dto.getId()==null){
           if(orgRepo.checkExistName(dto.getName())>0){
               mp.put("hasError",true);
               mp.put("message","Org name must be unique");
               return mp;
           }


        }else{

            Organization org = orgRepo.findById(dto.getId()).orElse(null);
            if(org==null){
                mp.put("hasError",true);
                mp.put("message","Organization not found");
                return mp;
            }
            if(orgRepo.checkExistNameEdit(dto.getName(), Arrays.asList(dto.getId()))>0){
                mp.put("hasError",true);
                mp.put("message","Org name must be unique");
                return mp;
            }

            mp.put("org",org);

        }

        return mp;

    }

    public MsgResponse create(CommonDTO dto) {

        Map<String, Object> mp = validate(dto);
         String sms = null;
         if((boolean)mp.get("hasError")){
             return new MsgResponse((String) mp.get("message"), false);
         }
         Organization org = new Organization();
         if(dto.getId()==null){
             org.setName(dto.getName());
             org.setPhone(dto.getPhone());
             org.setAddress(dto.getAddress());
             org.setLocation(dto.getLocation());
             sms="Successfully created";
         }else{
             org = (Organization) mp.get("org");
             BeanUtils.copyProperties(dto,org,"created","updated");
             sms="Successfully updated";
         }
         orgRepo.save(org);
         return new MsgResponse(sms,true);
    }


    public MsgResponse edit(CommonDTO dto) {
        return create(dto);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = orgRepo.getList(dto.orgId,dto.commonField,pageable);
        return CommonUtil.responseFromPage(page);

    }

    public MsgResponse delete(CommonDTO dto) {
        if(!orgRepo.existsById(dto.getId())){
            return  new MsgResponse("The organization with id"+dto.getId()+" dont exist in DB",false);
        }

        if(productRepo.existsByOrgId(dto.getId())){
            Product pdct = productRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in product "+pdct.getId()+"-"+pdct.getName(),false);
        }
        else if(branchRepo.existsByOrgId(dto.getId())){
            Branch pdct = branchRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in Branch "+pdct.getId()+"-"+pdct.getName(),false);
        }
        else if(brandRepo.existsByOrgId(dto.getId())){
            Brand pdct = brandRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in Brand "+pdct.getId()+"-"+pdct.getName(),false);
        }
        else if(modelRepo.existsByOrgId(dto.getId())){
            ProductModel pdct = modelRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in ProductModel "+pdct.getId()+"-"+pdct.getName(),false);
        }
        else if(catRepo.existsByOrgId(dto.getId())){
            ProductCat pdct = catRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in ProductCat "+pdct.getId()+"-"+pdct.getName(),false);
        }
        else if(sizeRepo.existsByOrgId(dto.getId())){
            ProductSize pdct = sizeRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in ProductSize "+pdct.getId()+"-"+pdct.getName(),false);
        }
        else if(madeWithRepo.existsByOrgId(dto.getId())){
            MadeWith pdct = madeWithRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in MadeWith "+pdct.getId()+"-"+pdct.getName(),false);
        }

        else if(colorRepo.existsByOrgId(dto.getId())){
            ProductColor pdct = colorRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in ProductColor "+pdct.getId()+"-"+pdct.getName(),false);
        }

        else if(unitOfMeasureRepo.existsByOrgId(dto.getId())){
            UnitOfMeasure pdct = unitOfMeasureRepo.findTopByOrgId(dto.getId());
            return  new MsgResponse("This organization can not be delete , it is used in UnitOfMeasure "+pdct.getId()+"-"+pdct.getName(),false);
        }
        else{
            orgRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);

    }
}
