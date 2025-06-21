package app.modules.purchase.service;

import app.common.counter.entity.SystemCounter;
import app.common.counter.service.CounterService;
import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.util.CommonUtil;
import app.common.util.CounterEnum;
import app.modules.inventory.entity.Inventory;
import app.modules.inventory.service.InventoryService;
import app.modules.inventory.service.StockBalanceService;
import app.modules.purchase.dto.PurchaseDTO;
import app.modules.purchase.dto.PurchaseDetailsDTO;
import app.modules.purchase.entity.Purchase;
import app.modules.purchase.entity.PurchaseDetails;
import app.modules.purchase.repo.PurchaseRepo;
import app.modules.supplier.entity.Supplier;
import app.modules.supplier.service.SupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepo purchaseRepo;
    @Autowired
    private CounterService counterService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private StockBalanceService stockBalanceService;

    PurchaseDetails getByIdFromList(List<PurchaseDetails> list , Long id){
        for(PurchaseDetails dtl : list){
            if(dtl.getId().equals(id)){
                return dtl;
            }
        }
      return null;
    }

    Map<String,Object> validate(PurchaseDTO dto){

        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getInventoryId()==null){
            mp.put("hasError",true);
            mp.put("message","No inventory is selected");
            return mp;
        }
        if(dto.getDtls().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","No item is selected");
            return mp;
        }
        if(dto.getSupplierId()==null){
            mp.put("hasError",true);
            mp.put("message","No supplier is selected");
            return mp;
        }

        Inventory inv = inventoryService.getById(dto.getInventoryId());
        Supplier supplier = supplierService.getById(dto.getSupplierId());

        if(!supplier.getOrgId().equals(inv.getOrgId())){
            mp.put("hasError",true);
            mp.put("message","The supplier you selected is under another organization");
            return mp;
        }

        mp.put("supplier",supplier);
        mp.put("inv",inv);

        int index=0;

        List<PurchaseDetails> list =  new ArrayList<>();

        for(PurchaseDetailsDTO dtl : dto.getDtls()){

           index++;

           if(dtl.getQuantity()==null || dtl.getQuantity() < 1){
               mp.put("hasError",true);
               mp.put("message","Product quantity is required in SL NO"+index);
               return mp;
           }

        if(dtl.getProductId()==null){
                mp.put("hasError",true);
                mp.put("message","Product is missing in item SL NO"+index);
                return mp;
            }

         if(dtl.getUnitPrice()==null || dtl.getUnitPrice() < 0 ){
            mp.put("hasError",true);
            mp.put("message","Product price is missing in item SL no"+index);
            return mp;
           }

        if(dtl.getProductType()==null || !CommonUtil.prdctTypes.contains(dtl.getProductType())){
            mp.put("hasError",true);
            mp.put("message","Product type must be either 'BARCODED_PRODUCT' or 'NORMAL_PRODUCT' in SL NO"+index);
            return mp;
        }

            PurchaseDetails obj = new PurchaseDetails();
            obj.setId(dtl.getId());
            obj.setAmount(dtl.getAmount());
            obj.setQuantity(dtl.getQuantity());
            obj.setProductId(dtl.getProductId());
            obj.setUnitPrice(dtl.getUnitPrice());
            obj.setAmount(dtl.getQuantity()*dtl.getUnitPrice());
            list.add(obj);

       }
        mp.put("list",list);
        return mp;
    }


    public MsgResponse create(PurchaseDTO dto) {
        Map<String,Object> mp = validate(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse((String) mp.get("message"),false);
        }

        Inventory inv = (Inventory) mp.get("inv");

        Purchase purchase = new Purchase();
        if(dto.getId()!=null){
            purchase = purchaseRepo.findById(dto.getId()).get();
        }
            purchase.setInventoryId(inv.getId());
            purchase.setInventoryName(inv.getName());
            purchase.setOrgId(inv.getOrgId());
            purchase.setBranchId(inv.getBranchId());
            purchase.setOrgName(inv.getOrgName());
            purchase.setBranchName(inv.getBranchName());
            purchase.setInventoryName(inv.getName());

            List<PurchaseDetails> dtls =(List<PurchaseDetails>) mp.get("list");


            if(purchase.getId()!=null){
                for(PurchaseDetails obj : dtls){
                    if(obj.getId()!=null){
                       PurchaseDetails existDB_Obj = getByIdFromList(purchase.getDetails(),obj.getId());
                       BeanUtils.copyProperties(existDB_Obj,obj,"created","purchase");
                    }else{
                        obj.setPurchase(purchase);
                        purchase.getDetails().add(obj);
                    }
                }
            }else{
                Map<String,Object> cnt = CommonUtil.counterAttribute(CounterEnum.PURCHASE.name());
                String code = counterService.getCounterCode(inv.getOrgId(),inv.getBranchId(), (String) cnt.get("name"), (String) cnt.get("prefix"));
                purchase.setCode(code);
                for(PurchaseDetails obj : dtls){
                    obj.setPurchase(purchase);
                }
               }

            boolean saveSucceed=true;
            try{
                purchaseRepo.save(purchase);
            }catch (Exception e){
                saveSucceed=false;
            }
          if(saveSucceed){
              stockBalanceService.saveStockAfterPurchase(purchase);
          }

        return new MsgResponse("Successfully purchase product",true);
    }

    public MsgResponse edit(PurchaseDTO dto) {
        return create(dto);
    }
}
