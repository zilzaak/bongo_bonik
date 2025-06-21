package app.modules.sales.service;

import app.common.dto.MsgResponse;
import app.common.repo.ProductRepo;
import app.common.util.CommonUtil;
import app.modules.inventory.entity.StockBalance;
import app.modules.inventory.repo.InventoryRepo;
import app.modules.inventory.repo.StockBalanceRepo;
import app.modules.purchase.dto.PurchaseDTO;
import app.modules.sales.dto.SaleDTO;
import app.modules.sales.dto.SaleItemDTO;
import app.modules.sales.repo.SaleItemRepo;
import app.modules.sales.repo.SaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SalesService {

    @Autowired
    private SaleRepo saleRepo;
    @Autowired
    private SaleItemRepo saleItemRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private StockBalanceRepo stockBalanceRepo;


    Integer totalSellQuantity(SaleDTO dto , Long product){
        Integer quantity=0;
        for(SaleItemDTO dtl : dto.getDetails()){
            if(dtl.getProduct().equals(product)){
                quantity=quantity+dtl.getQuantity();
            }
        }
        return quantity;
    }

    Map<String,Object> validate(SaleDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        //check required field
        if(dto.getInventory()==null){
            mp.put("hasError",true);
            mp.put("message","Inventory is missing");
            return mp;
        }
        if(dto.getCustomer()==null){
            mp.put("hasError",true);
            mp.put("message","Customer is missing");
            return mp;
        }
        if(dto.getDetails().size()<1){
            mp.put("hasError",true);
            mp.put("message","Product is missing");
            return mp;
        }

        // now check the product stock balance and selling quantity missmatch or not in details list

             Integer slNo=0;
        for(SaleItemDTO dtl : dto.getDetails()){
            slNo++;
            if(!CommonUtil.prdctTypes.contains(dtl.getProductType())){
                mp.put("hasError",true);
                mp.put("message","Product Type is missing , it must be either"+CommonUtil.prdctTypes.get(0)+" or "+CommonUtil.prdctTypes.get(1));
                return mp;
            }
            if(dtl.getProduct()==null){
                mp.put("hasError",true);
                mp.put("message","Product is missing");
                return mp;
            }

            Integer quantity = this.totalSellQuantity(dto,dtl.getProduct());
            Integer bal = stockBalanceRepo.stockQbalanceOfProduct(dtl.getProduct(),dto.getInventory()).orElse(0);
            if(bal<quantity){
                String productName=productRepo.getProductName(dtl.getProduct());
                mp.put("hasError",true);
                mp.put("message","Insufficient "+productName+" stock balance is"+bal+" but selling quantity is "+quantity+" for slNo="+slNo);
                return mp;
            }



        }



        return mp;
    }


    public MsgResponse create(SaleDTO dto) {

        return null;
    }

    public MsgResponse edit(SaleDTO dto) {

        return null;

    }


}
