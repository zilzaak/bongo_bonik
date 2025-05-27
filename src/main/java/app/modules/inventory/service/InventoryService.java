package app.modules.inventory.service;

import app.common.dto.MsgResponse;
import app.modules.inventory.dto.InventoryDTO;
import app.modules.inventory.repo.InventoryRepo;
import app.modules.organization.repo.BranchRepo;
import app.modules.organization.repo.OrgRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private OrgRepo orgRepo;

    Map<String ,Object> validate(InventoryDTO dto){
        Map<String ,Object> mp = new HashMap<>();
        mp.put("hasError",false);


        return mp;
    }


    public MsgResponse create(InventoryDTO dto) {


        return null;
    }

    public MsgResponse edit(InventoryDTO dto) {


           return null;
    }
}
