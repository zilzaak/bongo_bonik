package app.modules.inventory.service;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.base.org.entity.Organization;
import app.modules.base.org.repo.OrgRepo;
import app.common.util.CommonUtil;
import app.modules.base.user.repo.UserOrgRepository;
import app.modules.base.user.repo.UserRepository;
import app.modules.inventory.dto.InventoryDTO;
import app.modules.inventory.entity.Inventory;
import app.modules.inventory.entity.StockBalance;
import app.modules.inventory.repo.InventoryRepo;
import app.modules.inventory.repo.StockBalanceRepo;
import app.common.entity.Branch;
import app.common.repo.BranchRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private UserOrgRepository userOrgRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockBalanceRepo stockBalanceRepo;

    @Autowired
    private OrgRepo orgRepo;

    Map<String ,Object> validate(InventoryDTO dto){
        Map<String ,Object> mp = new HashMap<>();
        mp.put("hasError",false);
        if(dto.getName()==null || dto.getName().isBlank() || dto.getBranchId()==null){
            mp.put("hasError",true);
            mp.put("message","Name , Name and Branch is required field");
            return mp;
        }
        Branch branch = branchRepo.findById(dto.getBranchId()).get();
        if(!CommonUtil.validUserOrg(branch.getOrg().getId())){
            mp.put("hasError",true);
            mp.put("message","Selected branch is not under user's Organization");
            return mp;
        }

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
            //user is editing other persons inventory
            if(!CommonUtil.validUserOrg(inv.getOrg().getId())){
                mp.put("hasError",true);
                mp.put("message","You are editing other persons inventory which is punishable");
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
        Branch b=new Branch();
        b.setId(dto.getBranchId());
        Organization org=new Organization();
        org.setId(dto.getOrgId());
        inv.setOrg(org);
        inv.setBranch(b);
        BeanUtils.copyProperties(dto,inv);
        if(dto.getId()==null){
            inv.setCreateBy(CommonUtil.currentUser());
        }else{
            inv.setUpdateBy(CommonUtil.currentUser());
        }
        inventoryRepo.save(inv);
        return new MsgResponse(dto.getId()==null?"Successfully created Inventory":"Successfully updated Inventory",true);
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
        if(dto.id!=null){
            Inventory inv = inventoryRepo.findById(dto.id).get();
            if(!CommonUtil.validUserOrg(inv.getOrg().getId())){
               throw new RuntimeException("Invalid organization");
            }
        }
        Pageable pageable = PageRequest.of((dto.pageNum-1),dto.pageSize, Sort.by(dto.sortField).descending());
        Page<Object> page=null;
        if(dto.getOrgId()==null){
            List<Organization> orgs=userOrgRepository.getPermittedOrg(userRepository.findByUsername(CommonUtil.currentUser()));
            page = inventoryRepo.getList(orgs, dto.branchId,dto.id,pageable);
        }else{
            if(dto.getDropDown()!=null){
                page = inventoryRepo.getList(dto.orgId, dto.branchId,dto.id,pageable);
            }else{
                Page<Map<String ,Object>> p = inventoryRepo.getListDrop(dto.orgId, dto.branchId,dto.id,pageable);
                return CommonUtil.responseFromPage(p);
            }
        }
        return CommonUtil.responseFromObjectPage(page);
    }

}
