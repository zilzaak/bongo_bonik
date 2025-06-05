package app.modules.inventory.service;


import app.common.counter.service.CounterService;
import app.common.repo.ProductRepo;
import app.common.util.CommonUtil;
import app.common.util.CounterEnum;
import app.common.util.ProductEnum;
import app.modules.inventory.entity.StockBalance;
import app.modules.inventory.repo.StockBalanceRepo;
import app.modules.purchase.entity.Purchase;
import app.modules.purchase.entity.PurchaseDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockBalanceService {

@Autowired
private StockBalanceRepo stockBalanceRepo;

@Autowired
private CounterService counterService;

@Autowired
private PdctBarCodeService barCodeService;

@Autowired
private ProductRepo productRepo;

    public void saveStockAfterPurchase(Purchase purchase){

        for(PurchaseDetails dtl : purchase.getDetails()){
            StockBalance balance = stockBalanceRepo.findByProductIdAndInventoryId(dtl.getProductId(),purchase.getInventoryId());
             if(dtl.getProductType().equals(ProductEnum.BARCODED_PRODUCT.name())){
                 Map<String, Object> attr = CommonUtil.counterAttribute(CounterEnum.BARCODE.name());
                 List<String> barCodes = counterService.getBarCode((String) attr.get("name"),(String) attr.get("prefix") , dtl.getQuantity(),
                         purchase.getOrgId(),purchase.getBranchId());
                 if(balance==null){
                    balance = new StockBalance();
                     BeanUtils.copyProperties(purchase,balance,"id","created","updated");
                     balance.setProductId(dtl.getProductId());
                     balance.setQuantity(dtl.getQuantity());
                     balance.setProductName(productRepo.getProductName(dtl.getProductId()));
                     balance.setUnitPrice(dtl.getUnitPrice());
                 }else{
                     balance.setQuantity(Optional.ofNullable(balance.getQuantity()).orElse(0)+dtl.getQuantity());
                     balance.setUnitPrice(dtl.getUnitPrice());
                 }
                 stockBalanceRepo.save(balance);
                 barCodeService.saveNewBarcodes(barCodes,balance);

             }
             else if(dtl.getProductType().equals(ProductEnum.NORMAL_PRODUCT)){
                 balance.setQuantity(Optional.ofNullable(balance.getQuantity()).orElse(0)+dtl.getQuantity());
                 balance.setUnitPrice(dtl.getUnitPrice());
                 stockBalanceRepo.save(balance);
             }

        }


    }


}
