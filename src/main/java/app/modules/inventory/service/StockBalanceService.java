package app.modules.inventory.service;


import app.common.counter.service.CounterService;
import app.common.entity.Product;
import app.common.repo.ProductRepo;
import app.common.util.CommonUtil;
import app.common.util.CounterEnum;
import app.common.util.ProductEnum;
import app.modules.inventory.entity.StockBalance;
import app.modules.inventory.repo.StockBalanceRepo;
import app.modules.purchase.entity.Purchase;
import app.modules.purchase.entity.PurchaseDetails;
import app.modules.sales.dto.SaleItemDTO;
import app.modules.sales.entity.Sales;
import app.modules.sales.entity.SalesItems;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Transactional
    public void subTractStockAfterSales(Sales sales) {
        Long inventoryId = sales.getInventory().getId();
        // Deduct stock for each item in the sales details
        sales.getDetails().forEach(item -> {
            Product product = item.getProduct();
            if (product != null && item.getQuantity() > 0) {
                if(stockBalanceRepo.existsByInventoryIdAndProductIdAndQuantityLessThan(inventoryId,product.getId(),item.getQuantity())){
                    throw new RuntimeException("Stock is not available to remove ");
                }else{
                    stockBalanceRepo.deductStock(product.getId(), inventoryId, item.getQuantity());
                }
            }
        });
    }

    @Transactional
    public void subTractStockAfterSales(List<SaleItemDTO> newlyAddedItemOnEdit,Long inventoryId) {
        // Deduct stock for each item in the sales details
        newlyAddedItemOnEdit.forEach(item -> {
            if (item.getProduct() != null && item.getQuantity() > 0) {
                if(stockBalanceRepo.existsByInventoryIdAndProductIdAndQuantityLessThan(inventoryId,item.getProduct(),item.getQuantity())){
                    throw new RuntimeException("Stock is not available to remove ");
                }else{
                    stockBalanceRepo.deductStock(item.getProduct(), inventoryId, item.getQuantity());
                }
            }
        });
    }

    @Transactional
    public void subTractStockForIncreaseInEdit(Long product , Long inventoryId , Integer subQty) {
        if(stockBalanceRepo.existsByInventoryIdAndProductIdAndQuantityLessThan(inventoryId,product,subQty)){
            throw new RuntimeException("Stock is not available to remove ");
        }else{
            stockBalanceRepo.deductStock(product, inventoryId, subQty);
        }
    }

    @Transactional
    public void addStockForDecreaseInEdit(Long product , Long inventoryId , Integer decreasedQty) {
        stockBalanceRepo.addStock(product, inventoryId, decreasedQty);
    }

    @Transactional
    public void addStockAfterSales(List<SalesItems> deletedExistItemOnEdit,Long inventoryId) {
        deletedExistItemOnEdit.forEach(item -> {
            if (item.getProduct() != null && item.getQuantity() > 0) {
                stockBalanceRepo.addStock(item.getProduct().getId(), inventoryId, item.getQuantity());
            }
        });
    }


}
