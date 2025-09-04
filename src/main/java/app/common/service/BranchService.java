package app.common.service;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.inventory.entity.Inventory;
import app.modules.inventory.repo.InventoryRepo;
import app.modules.inventory.repo.StockBalanceRepo;
import app.common.entity.Branch;
import app.modules.base.org.entity.Organization;
import app.common.repo.BranchRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class BranchService {

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private StockBalanceRepo stockBalanceRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    Map<String,Object> validate(CommonDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getOrgId()==null || dto.getAddress()==null || dto.getPhone()==null){
            mp.put("hasError",true);
            mp.put("message","name , organization , address , phone are required field");
            return mp;
        }

        Organization org = new Organization();
        org.setId(dto.getOrgId());

        if(dto.getId()==null){
            if(branchRepo.checkExistName(dto.getName(),org.getId())>0){
                mp.put("hasError",true);
                mp.put("message","This branch already exist against selected Organization");
                return mp;
            }
        }else{
            Branch branch = branchRepo.findById(dto.getId()).orElse(null);

            if(branch==null){
                mp.put("hasError",true);
                mp.put("message","No data found for id");
                return mp;
            }

            if(branchRepo.checkExistNameEdit(dto.getName(), org.getId() , Arrays.asList(dto.getId()))>0){
                mp.put("hasError",true);
                mp.put("message","This branch already exist against selected Organization");
                return mp;
            }

            if(!branch.getOrg().getId().equals(dto.getOrgId())){
                if(inventoryRepo.existsByBranchId(branch.getId())){
                    Inventory inv = inventoryRepo.findTopByBranchId(branch.getId());
                    mp.put("hasError",true);
                    mp.put("message","This branch can not be delete , it is used in Inventory "+inv.getId()+"-"+inv.getName());
                    return mp;
                }

                return mp;
            }

            mp.put("branch",branch);

        }
        mp.put("org",org);
        return mp;

    }

    public MsgResponse create(CommonDTO dto) {
        Map<String,Object> mp = validate(dto);
       if((boolean)mp.get("hasError")){
           return new MsgResponse((String)validate(dto).get("message"),false);
       }
       Branch branch = new Branch();
       Organization org = (Organization) mp.get("org");
       if(dto.getId()==null){
           branch.setOrg(org);
           branch.setName(dto.getName());
           branch.setPhone(dto.getPhone());
           branch.setAddress(dto.getAddress());
           branch.setLocation(dto.getLocation());
       }else{
           branch = (Branch) mp.get("branch");
           BeanUtils.copyProperties(dto,branch,"created","updated");
           branch.setOrg(org);
       }

       branchRepo.save(branch);
       return  new MsgResponse(dto.getId()==null?"Successfully created":"Successfully updated",false);
    }

    public MsgResponse edit(CommonDTO dto) {
        return  create(dto);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = CommonUtil.getPageable(dto);
        Page<Map<String,Object>> page = branchRepo.getList(dto.id,dto.orgId,pageable);
        return CommonUtil.responseFromPage(page);
    }

    public MsgResponse delete(CommonDTO dto) {
        if(!branchRepo.existsByIdAndOrgId(dto.getId(),dto.getOrgId())){
            return  new MsgResponse("This branch with id="+dto.getId()+" don't exist in DB",false);
        }
        if(inventoryRepo.existsByBranchId(dto.getId())){
            Inventory inv = inventoryRepo.findTopByBranchId(dto.getId());
            return  new MsgResponse("This branch can not be delete , it is used in Inventory "+inv.getId()+"-"+inv.getName(),false);
        }else{
            branchRepo.deleteById(dto.getId());
        }
        return  new MsgResponse("Deleted successfully",true);
    }


}
