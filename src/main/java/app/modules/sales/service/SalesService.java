package app.modules.sales.service;

import app.common.counter.service.CounterService;
import app.common.dto.MsgResponse;
import app.common.entity.Product;
import app.common.repo.ProductRepo;
import app.common.util.CommonUtil;
import app.modules.customer.entity.Customer;
import app.modules.customer.service.CustomerService;
import app.modules.inventory.entity.Inventory;
import app.modules.inventory.entity.StockBalance;
import app.modules.inventory.repo.InventoryRepo;
import app.modules.inventory.repo.StockBalanceRepo;
import app.modules.inventory.service.StockBalanceService;
import app.modules.purchase.dto.PurchaseDTO;
import app.modules.sales.dto.SaleDTO;
import app.modules.sales.dto.SaleItemDTO;
import app.modules.sales.entity.Sales;
import app.modules.sales.entity.SalesItems;
import app.modules.sales.repo.SaleItemRepo;
import app.modules.sales.repo.SaleRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private CounterService counterService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private StockBalanceService stockBalanceService;


    Integer totalSellQuantity(SaleDTO dto , Long product){
        Integer quantity=0;
        for(SaleItemDTO dtl : dto.getDetails()){
            if(dtl.getProduct().equals(product)){
                quantity=quantity+dtl.getQuantity();
            }
        }
        return quantity;
    }

    Integer totalSellQuantity(List<SalesItems>  details , Long product){
        Integer quantity=0;
        for(SalesItems dtl : details){
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

             Integer slNo=0; Double totalDiscount=0.0 ; Double totalVat=0.0;
             Double totalAmount=0.0;
             List<SalesItems> itemList = new ArrayList<>();
             List<Long> processedProductId=new ArrayList<>();
        for(SaleItemDTO dtl : dto.getDetails()){
            SalesItems item = new SalesItems();
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

            if(dtl.getUnitPrice()==null || dtl.getUnitPrice()<0){
                mp.put("hasError",true);
                mp.put("message","Invalid Product price in slNo"+slNo);
                return mp;
            }

            if(dtl.getQuantity()==null || dtl.getQuantity()<= 0 ){
                String productName=productRepo.getProductName(dtl.getProduct());
                mp.put("hasError",true);
                mp.put("message","Product quantity is invalid for slNo="+slNo+" of product "+productName);
                return mp;
            }

            if(!processedProductId.contains(dtl.getProduct())){
                Integer quantity = this.totalSellQuantity(dto,dtl.getProduct());
                Integer bal = stockBalanceRepo.stockQbalanceOfProduct(dtl.getProduct(),dto.getInventory()).orElse(0);
                if(bal<quantity){
                    String productName=productRepo.getProductName(dtl.getProduct());
                    mp.put("hasError",true);
                    mp.put("message","Insufficient "+productName+" , stock balance = "+bal+" but selling quantity is "+quantity+" for slNo="+slNo);
                    return mp;
                }
                dtl.setTotalQuantity(quantity);
            }


            Double amount = dtl.getUnitPrice()*dtl.getQuantity();
            totalAmount=totalAmount+amount;
            dtl.setAmount(amount);
            if(dtl.getDiscAmount()!=null){
                if(dtl.getDiscAmount() < 0){
                    String productName=productRepo.getProductName(dtl.getProduct());
                    mp.put("hasError",true);
                    mp.put("message","Invalid discount amount for product"+productName+" in slNo="+slNo);
                    return mp;
                }
                totalDiscount=totalDiscount+dtl.getDiscAmount();
                dtl.setNetAmount(dtl.getAmount()-dtl.getDiscAmount());
            }
            if(dtl.getVatAmount()!=null){
                if(dtl.getVatAmount() < 0){
                    String productName=productRepo.getProductName(dtl.getProduct());
                    mp.put("hasError",true);
                    mp.put("message","Invalid vat amount for product"+productName+" in slNo="+slNo);
                    return mp;
                }
                totalVat=totalVat+dtl.getVatAmount();
                dtl.setNetAmount(dtl.getNetAmount()+dtl.getVatAmount());
            }

            //set the child list object
            BeanUtils.copyProperties(dtl,item);
            Product product = new Product();
            product.setId(dtl.getId());
            item.setProduct(product);
            itemList.add(item);
        }

        dto.setAmount(totalAmount);
        dto.setDiscount(totalDiscount);
        dto.setVat(totalVat);
        dto.setNetAmount(totalAmount-totalDiscount+totalVat);
        mp.put("itemList",itemList);
        return mp;
    }


    @Transactional
    public MsgResponse create(SaleDTO dto) {
        Map<String,Object> mp = validate(dto);
        if((boolean)mp.get("hasError")){
           return new MsgResponse((String) mp.get("message"),false) ;
        }

        Sales sales=new Sales();
        Customer customer = new Customer();
        Inventory inventory = new Inventory();
        List<SalesItems> existedDbItems = new ArrayList<>();
        if(dto.getId()==null){
            inventory= inventoryRepo.findById(dto.getInventory()).get();
            Map<String,Object> attr =  CommonUtil.counterAttribute("INVOICE");
            dto.setCode(counterService.getCounterCode(inventory.getOrgId(),inventory.getBranchId(), (String) attr.get("name"), (String) attr.get("prefix")));
            customer.setId(dto.getCustomer());
        }else{
            sales = saleRepo.findById(dto.getId()).get();
            existedDbItems = sales.getDetails();
            customer = sales.getCustomer();
            inventory=sales.getInventory();
        }
        sales.setPaid(dto.getPaid());
        sales.setInstallment(dto.getInstallment());
        sales.setNetAmount(dto.getNetAmount());
        sales.setAmount(dto.getAmount());
        sales.setDiscount(dto.getDiscount());
        sales.setVat(dto.getVat());
        sales.setCustomer(customer);
        sales.setInventory(inventory);
        sales.setDue(sales.getNetAmount()-sales.getPaid());
        List<SalesItems> itemList = (List<SalesItems>) mp.get("itemList");

        // set the update the stock balance because in case if creation the stock update by substraction of product quntity
        // but in case of edit operation the stock may be increase or decrese for editing the product quntity
        // check for creation
        if(dto.getId()==null){
            for(SalesItems item : itemList){
                item.setSales(sales);
            }
            saleRepo.save(sales);
            saleItemRepo.saveAll(itemList);
            stockBalanceService.subTractStockAfterSales(sales);
        }else{
            List<Long> processedProductEdit = new ArrayList<>();
            List<Long> processedProductDB = new ArrayList<>();
            List<SaleItemDTO> newlyAddedItemOnEdit = new ArrayList<>();
            List<SalesItems> deletedDBitemOnEdit = new ArrayList<>();
            List<Integer> deleteIndex = new ArrayList<>();

          for(SaleItemDTO editedObj : dto.getDetails()){
               Integer editedProductSoldQty = editedObj.getTotalQuantity();
              if(!processedProductEdit.contains(editedObj.getProduct())){
                    boolean itemExistInDbList=false;
                  for(SalesItems dbObj : existedDbItems){
                                  if(dbObj.getProduct().getId().equals(editedObj.getProduct()) &&
                                          !processedProductDB.contains(dbObj.getProduct().getId())){
                                      Integer dbProductSoldQty = this.totalSellQuantity(existedDbItems,dbObj.getProduct().getId()) ;
                                      if(editedProductSoldQty > dbProductSoldQty){
                                          Integer diffrence = editedProductSoldQty-dbProductSoldQty;
                                          stockBalanceService.subTractStockForIncreaseInEdit(editedObj.getProduct(), sales.getInventory().getId(),diffrence);
                                      }
                                      if(editedProductSoldQty < dbProductSoldQty){
                                          Integer diffrence = dbProductSoldQty-editedProductSoldQty;
                                          stockBalanceService.subTractStockForIncreaseInEdit(editedObj.getProduct(), sales.getInventory().getId(),diffrence);
                                      }
                                      processedProductDB.add(dbObj.getProduct().getId());
                                      itemExistInDbList=true;
                                  }
                                  //check dbObj is previously exist but on edit the usewr deleted the item
                      boolean dbObjExistInEditedList = false;
                      for(SaleItemDTO x : dto.getDetails()){
                          if(x.getProduct().equals(dbObj.getProduct().getId())){
                              dbObjExistInEditedList = true;
                              break;
                          }
                      }

                      if(!dbObjExistInEditedList){
                          deletedDBitemOnEdit.add(dbObj);
                          deleteIndex.add(existedDbItems.indexOf(dbObj));
                      }

                  }
                  processedProductEdit.add(editedObj.getProduct());
                  if(!itemExistInDbList){
                      newlyAddedItemOnEdit.add(editedObj);
                  }
              }

          }

            stockBalanceService.subTractStockAfterSales(newlyAddedItemOnEdit,dto.getInventory());
            stockBalanceService.addStockAfterSales(deletedDBitemOnEdit,dto.getInventory());

            for(Integer index : deleteIndex){
                sales.getDetails().remove(index);
            }
            for(SalesItems item : itemList){
                item.setSales(sales);
                boolean newAdded=true;
                for(SalesItems db : sales.getDetails()){
                   if(db.getId().equals(item.getId())){
                       BeanUtils.copyProperties(item,db,"updated");
                       newAdded=false;
                   }
                }
                if(newAdded){
                    sales.getDetails().add(item);
                }
            }
            saleRepo.save(sales);


        }
        return new MsgResponse("Successfully created sales invoice",true);
    }

    @Transactional
    public MsgResponse edit(SaleDTO dto) {
        return create(dto);
    }


}
