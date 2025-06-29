package app.modules.inventory.service;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.entity.Brand;
import app.common.util.CommonUtil;
import app.modules.inventory.dto.InventoryDTO;
import app.modules.inventory.entity.Inventory;
import app.modules.inventory.entity.StockBalance;
import app.modules.inventory.repo.InventoryRepo;
import app.modules.inventory.repo.StockBalanceRepo;
import app.modules.organization.entity.Branch;
import app.modules.organization.repo.BranchRepo;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private StockBalanceRepo stockBalanceRepo;

    @Autowired
    private OrgRepo orgRepo;

    Map<String ,Object> validate(InventoryDTO dto){
        Map<String ,Object> mp = new HashMap<>();
        mp.put("hasError",false);
        if(dto.getName()==null || dto.getBranchId()==null || dto.getName().length()<1){
            mp.put("hasError",true);
            mp.put("message","Name , Branch is required field");
            return mp;
        }

        Branch branch = branchRepo.findById(dto.getBranchId()).get();
         dto.setOrgId(branch.getOrg().getId());
         dto.setOrgName(branch.getOrg().getName());
         dto.setBranchName(branch.getName());
        if(dto.getId()==null){
            if(inventoryRepo.existsByNameAndBranchId(dto.getName(),dto.getBranchId())){
                mp.put("hasError",true);
                mp.put("message","This name already exist against branch "+branch.getName());
                return mp;
            }
        }else{
            if(inventoryRepo.existsByNameAndBranchIdAndIdNotIn(dto.getName(),dto.getBranchId(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message", dto.getName()+" already exist against branch "+branch.getName());
                return mp;
            }
            Inventory inv = inventoryRepo.findById(dto.getId()).get();
            if(!inv.getOrgId().equals(branch.getOrg().getId())){
                mp.put("hasError",true);
                mp.put("message"," The edited branch is must be under same organization for this inventory");
                return mp;
            }

        }

        return mp;
    }


    public MsgResponse create(InventoryDTO dto) {
        Map<String ,Object> mp = validate(dto);
        if((Boolean)mp.get("hasError")){
            return new MsgResponse((String) mp.get("message"),false);
        }
        Inventory inv = new Inventory();
        BeanUtils.copyProperties(dto,inv);
        inventoryRepo.save(inv);
        return new MsgResponse("Successfully created Inventory",true);
    }

    public Inventory getById(Long id) {
        return inventoryRepo.findById(id).orElse(null);
    }

    public MsgResponse edit(InventoryDTO dto) {
        return create(dto);
    }

    public MsgResponse delete(Long id) {
        Inventory inv = inventoryRepo.findById(id).orElse(null);
        if(inv==null){
            return new MsgResponse("No inventory exist under id = "+id,false);
        }
        StockBalance stock = stockBalanceRepo.findTopByInventoryId(inv.getId());
        if(stock!=null){
            return new MsgResponse("Can not delete this inventory because it is related with Stock "+stock.getInventoryName(),false);
        }
        inventoryRepo.delete(inv);
        return new MsgResponse("Successfully deleted inventory ",false);

    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable = PageRequest.of((dto.pageNum-1),dto.pageSize, Sort.by(dto.sortField).descending());
        Page<Object> page = inventoryRepo.getList(dto.orgId, dto.branchId,pageable);
        return CommonUtil.responseFromObjectPage(page);
    }

}
