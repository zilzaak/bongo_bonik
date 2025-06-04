package app.modules.inventory.service;


import app.common.counter.service.CounterService;
import app.common.util.CommonUtil;
import app.common.util.CounterEnum;
import app.common.util.ProductEnum;
import app.modules.inventory.repo.StockBalanceRepo;
import app.modules.purchase.entity.Purchase;
import app.modules.purchase.entity.PurchaseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StockBalanceService {

@Autowired
private StockBalanceRepo stockBalanceRepo;

@Autowired
private CounterService counterService;

    public void saveStockAfterPurchase(Purchase purchase){

        for(PurchaseDetails dtl : purchase.getDetails()){
             if(dtl.getProductType().equals(ProductEnum.BARCODED_PRODUCT.name())){
                 Map<String, Object> attr = CommonUtil.counterAttribute(CounterEnum.BARCODE.name());
                 List<String> getBarcodes = counterService.getBarCode((String) attr.get("name"),(String) attr.get("prefix") , dtl.getQuantity(),
                         purchase.getOrgId(),purchase.getBranchId());

             }else{

             }

        }


    }


}
