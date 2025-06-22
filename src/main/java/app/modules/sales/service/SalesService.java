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


    int totalSellQuantity(SaleDTO dto , Long product){
        int quantity=0;
        for(SaleItemDTO dtl : dto.getDetails()){
            if(dtl.getProduct().equals(product)){
                quantity=quantity+dtl.getQuantity();
            }
        }
        return quantity;
    }

    int totalSellQuantity(List<SalesItems>  details , Long product){
        int quantity=0;
        for(SalesItems dtl : details){
            if(dtl.getProduct().getId().equals(product)){
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

             int slNo=0; Double totalDiscount=0.0 ; Double totalVat=0.0;
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
                Integer totalQuantity = this.totalSellQuantity(dto,dtl.getProduct());
                Integer bal = stockBalanceRepo.stockQbalanceOfProduct(dtl.getProduct(),dto.getInventory()).orElse(0);
                if(bal<totalQuantity || bal==0){
                    String productName=productRepo.getProductName(dtl.getProduct());
                    mp.put("hasError",true);
                    mp.put("message","Insufficient "+productName+" , stock balance = "+bal+" but selling quantity is "+totalQuantity+" for slNo="+slNo);
                    return mp;
                }
                dtl.setTotalQuantity(totalQuantity);
                item.setTotalQuantity(totalQuantity);
                processedProductId.add(dtl.getProduct());
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
        Inventory inventory = null;
        List<SalesItems> existedDBitems = new ArrayList<>();
        if(dto.getId()==null){
            inventory= inventoryRepo.findById(dto.getInventory()).get();
            Map<String,Object> attr =  CommonUtil.counterAttribute("INVOICE");
            dto.setCode(counterService.getCounterCode(inventory.getOrgId(),inventory.getBranchId(), (String) attr.get("name"), (String) attr.get("prefix")));
            customer.setId(dto.getCustomer());
        }else{
            sales = saleRepo.findById(dto.getId()).get();
            existedDBitems = sales.getDetails();
            customer = sales.getCustomer();
        }
        sales.setPaid(dto.getPaid());
        sales.setInstallment(dto.getInstallment());
        sales.setNetAmount(dto.getNetAmount());
        sales.setAmount(dto.getAmount());
        sales.setDiscount(dto.getDiscount());
        sales.setVat(dto.getVat());
        sales.setCustomer(customer);
        sales.setDue(dto.getNetAmount() - dto.getPaid());
        List<SalesItems> itemList = (List<SalesItems>) mp.get("itemList");

        // set the update the stock balance because in case if creation the stock update by substraction of product quantity
        // but in case of edit operation the stock may be increase or decrease for editing the product quantity / inventory / product
        // check for creation
        if(dto.getId()==null){
            for(SalesItems item : itemList){
                item.setSales(sales);
            }
            sales.setInventory(inventory);
            saleRepo.save(sales);
            saleItemRepo.saveAll(itemList);
            stockBalanceService.subTractStockAfterSales(sales);
        }else{

            if(sales.getInventory().getId().equals(dto.getInventory())){
                // this case where inventory is not changed in edit
                this.processWhenInventoryNotChanged(sales,dto,itemList,existedDBitems);
            }else{
                // here the inventory is changed on edit , as a result all items
                // with previous inventory stock  should be restored/returned , and in new inventory , new items will be removed from stock
                inventory= inventoryRepo.findById(dto.getInventory()).get();
                this.processWhenInventoryIsChanged(sales,inventory,itemList);
            }



        }
        return new MsgResponse("Successfully created sales invoice",true);
    }

    @Transactional
    private void processWhenInventoryIsChanged(Sales sales,Inventory editedInventory, List<SalesItems> itemList) {
        //restore db items to its stock
        stockBalanceService.addStockAfterSales(sales.getDetails(),sales.getInventory().getId());

        List<SalesItems> notMatchedIdList = new ArrayList<>();

        for(SalesItems DBitem : sales.getDetails()){
            boolean exist=false;
            for(SalesItems edt : itemList){
               if(edt.getId()!=null && edt.getId().equals(DBitem.getId())){
                   exist=true;
                   BeanUtils.copyProperties(edt,DBitem,"created");
                   saleItemRepo.save(DBitem);
               }
            }
             if(!exist){
                 notMatchedIdList.add(DBitem);
             }
        }

        for(SalesItems notMachted : notMatchedIdList){
            sales.getDetails().remove(notMachted);
        }

        sales.setInventory(editedInventory);
        for(SalesItems edited : itemList){
           if(edited.getId()==null){
               edited.setSales(sales);
               sales.getDetails().add(edited);
               saleItemRepo.save(edited);
           }
           if(edited.getTotalQuantity()!=null && edited.getTotalQuantity()>0){
               stockBalanceService.subTractStockForIncreaseInEdit(edited.getProduct().getId(),editedInventory.getId(),edited.getTotalQuantity());
           }
        }

        saleRepo.save(sales);

    }


    @Transactional
    private void processWhenInventoryNotChanged(Sales sales , SaleDTO dto , List<SalesItems> itemList , List<SalesItems> existedDBitems) {
       /* suppose  Edited List is A = [1,4,5]     DB  List  is  B = [1,2,3]

         1> For Common product  (this means we will retrieve product list from two List A> edited List  B> DB product List against that sales )
                the common product from List A and B is = [1]
                now two case may occurs here , the edited quantity of product '1' may increased or decreased
                if increased then==>>ReduceStock----------------------(M)
                if decreased then==>>IncreaseStock--------------------(N)
         2> For Un Common product  ( Retrieve product List which is in list A but not in List B , and also those which are in List B but not in List A )
              elements exist in A but not in B = [4,5]  ---------------(X)
              elements exist in B but not in A = [2,3]  ---------------(Y)

              so (x) products are newly added and these item will be subtracted from stock
                 (y) products are not exist edited/lates list , that means [2,3] product will be restored/add to stock
         */

        List<Long> processedProductEdit = new ArrayList<>();
        List<Long> processedProductDB = new ArrayList<>();
        List<SaleItemDTO> newlyAddedItemOnEdit = new ArrayList<>();
        List<SalesItems> deletedDBitemOnEdit = new ArrayList<>();

        for(SaleItemDTO editedObj : dto.getDetails()){

            if(!processedProductEdit.contains(editedObj.getProduct())){

                boolean commonProductEdit_DB=false;

          for(SalesItems dbProduct : existedDBitems){

                 if(dbProduct.getProduct().getId().equals(editedObj.getProduct()) && !processedProductDB.contains(dbProduct.getProduct().getId())){

                  int dbProductSoldQty = this.totalSellQuantity(existedDBitems,dbProduct.getProduct().getId()) ;

                        if(editedObj.getTotalQuantity() > dbProductSoldQty){
                            Integer diff = editedObj.getTotalQuantity()-dbProductSoldQty;
                            stockBalanceService.subTractStockForIncreaseInEdit(editedObj.getProduct(), sales.getInventory().getId(),diff); // ------EQUATION (N)
                        }
                        if(editedObj.getTotalQuantity() < dbProductSoldQty){
                            Integer diff = dbProductSoldQty-editedObj.getTotalQuantity();
                            stockBalanceService.addStockForDecreaseInEdit(editedObj.getProduct(), sales.getInventory().getId(),diff); // --------- EQUATION (M)
                        }
                          processedProductDB.add(dbProduct.getProduct().getId());
                          commonProductEdit_DB=true;
                      }

                    //check db product is previously exist but after edit the user deleted/removed the product
                    if(dto.getDetails().indexOf(editedObj)==0){
                        boolean common = false;
                        for(SaleItemDTO editedProduct : dto.getDetails()){
                            if(editedProduct.getProduct().equals(dbProduct.getProduct().getId())){
                                common = true; break;
                                 }
                               }
                        if(!common){deletedDBitemOnEdit.add(dbProduct);}
                    }

                }

                processedProductEdit.add(editedObj.getProduct());
                if(!commonProductEdit_DB){
                    newlyAddedItemOnEdit.add(editedObj);
                }
            }

        }

        stockBalanceService.subTractStockAfterSales(newlyAddedItemOnEdit,dto.getInventory()); // ------------- equation (X)
        stockBalanceService.addStockAfterSales(deletedDBitemOnEdit,dto.getInventory());       // --------------equation (Y)

        for(SalesItems dbObj : deletedDBitemOnEdit){
            sales.getDetails().remove(dbObj);
        }
        for(SalesItems item : itemList){
            item.setSales(sales);
            boolean newAdded=true;
            for(SalesItems db : sales.getDetails()){
                if(item.getId()!=null && db.getId().equals(item.getId())){
                    BeanUtils.copyProperties(item,db,"updated");
                    newAdded=false;
                }
            }
            if(newAdded){
                saleItemRepo.save(item);
                sales.getDetails().add(item);
            }
        }
        saleRepo.save(sales);

    }


    @Transactional
    public MsgResponse edit(SaleDTO dto) {
        return create(dto);
    }


}
