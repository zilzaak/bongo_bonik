package app.common.service;


import app.common.counter.service.CounterService;
import app.common.dto.MsgResponse;
import app.common.dto.PriceDTO;
import app.common.dto.ProductDTO;
import app.common.dto.SearchParamDTO;
import app.common.entity.*;
import app.common.repo.*;
import app.common.util.CommonUtil;
import app.common.util.CounterEnum;
import app.modules.base.org.entity.Organization;
import app.modules.base.org.repo.OrgRepo;
import app.modules.inventory.repo.CostPriceRepo;
import app.modules.inventory.repo.SellPriceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

     @Autowired
     private BrandRepo brandRepo;
    @Autowired
    private ProductModelRepo modelRepo;

    @Autowired
    private ProductCatRepo catRepo;

    @Autowired
    private ProductSizeRepo sizeRepo;

    @Autowired
    private ProductColorRepo colorRepo;

    @Autowired
    private MadeWithRepo madeWithRepo;

    @Autowired
    private UnitOfMeasureRepo uomRepo;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CounterService service;

    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private SellPriceRepo sellPriceRepo;
    @Autowired
    private CostPriceRepo costPriceRepo;
    @Autowired
    private BranchService branchService;


    String getStr(String arr[] , int toIndex){
        String k=null;
        for(int i=0;i<toIndex;i++){
            if(k==null){
                k=arr[i];
            }else{
                k=k+","+arr[i];
            }
        }
        return k;
    }

    public List<String> units=Arrays.asList("ml","l","kg","gm","mg","ton","feet","inch","meter","piece","dozen","pair","sqft");
    // product full name = name->Cat->brand->model->madeWith->size->color->amount per unit->measure by

    boolean checkSimilarity(Map<String,Object> mp , ProductDTO dto,boolean compareAllCatIds, int compareTill){

        String catIds=dto.getCriteriaIds();
        if(!compareAllCatIds){
            catIds=this.getStr(catIds.split(","),compareTill);
        }

        List<Map<String,Object>>  existProduct=productRepo.similarProduct(dto.getOrgId(),dto.getName(),catIds,dto.getId());
        if(compareAllCatIds){
            for(Map<String,Object> objm : existProduct){
                if(objm.get("name").equals(dto.getName())){
                    mp.put("hasError",true);
                    mp.put("message","Duplicate Product found");
                    return true;
                }
            }
        }
        if(existProduct.size()>0 && !dto.getConfirmSimilarity()){
            mp.put("hasError",true);
            mp.put("message","Similar product name exist , be confirm duplicity or not before create ");
            mp.put("existsData",existProduct);
            return true;
        }
        return false;
    }

    //1>>first check required field are null
    //2>>check all criteria of product belongs to same organization
    //3>>check unique name exist or not
      Map<String,Object> validate(ProductDTO dto){
          Map<String,Object> mp = new HashMap<>();
          mp.put("hasError",false);
          if(dto.getName()==null || dto.getName().isBlank() || dto.getOrgId()==null ||
                  dto.getCatId()==null || dto.getPrice().getDefaultCostPrice()==null
                  || dto.getPrice().getDefaultSellPrice()==null){
           mp.put("hasError",true);
           mp.put("message","Product Name , Organization , category , sell price , cost price is required ");
           return mp;
          }

          if(!CommonUtil.validUserOrg(dto.getOrgId())){
              mp.put("hasError",true);
              mp.put("message","Invalid User Organization selected ");
              return mp;
          }

              if(dto.getPrice().getSellBranchIds()!=null && !dto.getPrice().getSellBranchIds().isBlank()){
                  String branches[]  = dto.getPrice().getSellBranchIds().split(",");
                  String prices[]=dto.getPrice().getSellPrices().split(",");
                  if(branches.length!=prices.length){
                      mp.put("hasError",true);
                      mp.put("message","Sell Price is missing for a specific branch");
                      return mp;
                  }
                  int index=-1;
                  for(String bn : branches){
                      index++;
                      Double pv=Double.parseDouble(prices[index]);
                      if(!branchService.validBranch(dto.getOrgId(),Long.parseLong(bn))){
                          mp.put("hasError",true);
                          mp.put("message","Invalid branch is selected");
                          return mp;
                      }
                  }
              }

          if(dto.getPrice().getCostBranchIds()!=null && !dto.getPrice().getCostBranchIds().isBlank()){
              String branches[]  = dto.getPrice().getCostBranchIds().split(",");
              String prices[]=dto.getPrice().getCostPrices().split(",");
              if(branches.length!=prices.length){
                  mp.put("hasError",true);
                  mp.put("message","Cost Price is missing for a specific branch");
                  return mp;
              }
              int index=-1;
              for(String bn : branches){
                  index++;
                  Double pv=Double.parseDouble(prices[index]);
                  if(!branchService.validBranch(dto.getOrgId(),Long.parseLong(bn))){
                      mp.put("hasError",true);
                      mp.put("message","Invalid branch is selected");
                      return mp;
                  }
              }
          }


          Long org = dto.getOrgId();
          ProductCat cat = catRepo.findById(dto.getCatId()).orElse(null);
          Brand brand = dto.getBrandId()!=null?brandRepo.findById(dto.getBrandId()).orElse(null):null;
          UnitOfMeasure uom = dto.getUomId()!=null?uomRepo.findById(dto.getUomId()).orElse(null):null;
          ProductModel model = dto.getModelId()!=null?modelRepo.findById(dto.getModelId()).get():null;
          ProductSize size = dto.getSizeId()!=null?sizeRepo.findById(dto.getSizeId()).get():null;
          ProductColor color = dto.getColorId()!=null?colorRepo.findById(dto.getColorId()).get():null;
          MadeWith madeWith = dto.getMadeWithId()!=null?madeWithRepo.findById(dto.getMadeWithId()).get():null;

          if(dto.getQtyPerUnit()!=null  && ( dto.getUnitName()==null || !dto.getUnitName().isBlank())){
              mp.put("hasError",true);
              mp.put("message","Amount/quantity unit is not provided for amount"+dto.getQtyPerUnit());
              return mp;
          }
          if(dto.getUnitName()!=null && !dto.getUnitName().isBlank() && dto.getQtyPerUnit()==null ){
              mp.put("hasError",true);
              mp.put("message","Amount/quantity unit is not provided for unit "+dto.getUnitName());
              return mp;
          }
          if(dto.getUnitName()!=null && !dto.getUnitName().isBlank() && !this.units.contains(dto.getUnitName().toLowerCase())){
              mp.put("hasError",true);
              mp.put("message","Unit name is missing ");
              return mp;
          }

          if(!orgRepo.existsById(dto.getOrgId())){
              mp.put("hasError",true);
              mp.put("message","No Organization exist under id="+dto.getOrgId());
              return mp;
          }

          if(dto.getCatId()!=null && cat==null){
              mp.put("hasError",true);
              mp.put("message","No category exist under id="+dto.getCatId());
              return mp;
          }
          if(dto.getBrandId()!=null && brand==null){
              mp.put("hasError",true);
              mp.put("message","No brand exist under id="+dto.getBrandId());
              return mp;
          }
          if(dto.getUomId()!=null && uom==null){
              mp.put("hasError",true);
              mp.put("message","No Uom exist under id="+dto.getUomId());
              return mp;
          }

          String errorMessage = productCriteriaMaintainClassification(org,cat,brand,model,size,color,madeWith,uom);
          if(errorMessage!=null){
              mp.put("hasError",true);
              mp.put("message",errorMessage);
              return mp;
          }

          Map<String,Object> naming = CommonUtil.getProductFullname(dto.getName(),cat,brand,model,madeWith,size,color,dto.getQtyPerUnit(),dto.getUnitName());
          String fullName = (String) naming.get("fullName");
          String criteriaIds = (String) naming.get("criteriaIds");
          dto.setCriteriaIds(criteriaIds);
          dto.setFullName(fullName);
          Product prdct=new Product();

          if(dto.getId()!=null){
              prdct = productRepo.findById(dto.getId()).get();

              if(!prdct.getOrg().getId().equals(dto.getOrgId())){
                  mp.put("hasError",true);
                  mp.put("message","Organization can not edit bcz it is a sensitive data and related with accounting ");
                  return mp;
              }
              if(cat!=null){
                  if(!cat.getOrgId().equals(prdct.getOrg().getId())){
                      mp.put("hasError",true);
                      mp.put("message","Product category and and product must be under same Organization ");
                      return mp;
                  }
              }

              if(model!=null){
                  if(!model.getOrg().getId().equals(prdct.getOrg().getId())){
                      mp.put("hasError",true);
                      mp.put("message","Product model and and product must be under same Organization ");
                      return mp;
                  }
              }

              if(uom!=null){
                  if(!uom.getOrg().getId().equals(prdct.getOrg().getId())){
                      mp.put("hasError",true);
                      mp.put("message","OUM and product must be under same Organization ");
                      return mp;
                  }
              }

              if(size!=null){
                  if(!size.getOrgId().equals(prdct.getOrg().getId())){
                      mp.put("hasError",true);
                      mp.put("message","Product size and product must be under same Organization ");
                      return mp;
                  }
              }

              if(color!=null){
                  if(!color.getOrgId().equals(prdct.getOrg().getId())){
                      mp.put("hasError",true);
                      mp.put("message","Product color and product must be under same Organization ");
                      return mp;
                  }
              }

              if(madeWith!=null){
                  if(!madeWith.getOrgId().equals(prdct.getOrg().getId())){
                      mp.put("hasError",true);
                      mp.put("message","Made with and product must be under same Organization ");
                      return mp;
                  }
              }

              if(brand!=null){
                  if(!brand.getOrg().getId().equals(prdct.getOrg().getId())){
                      mp.put("hasError",true);
                      mp.put("message","Brand and product must be under same Organization ");
                      return mp;
                  }
              }
          }

          boolean similarFound=this.checkSimilarity(mp,dto,true,dto.getCriteriaIds().length());
          if(similarFound){
              return mp;
          }else{
              String arr[]=dto.getCriteriaIds().split(",");
              int length=arr.length;
              if(length>3){
                  for(int i=1;i<4;i++){
                      similarFound=this.checkSimilarity(mp,dto,false,length-i);
                      if(similarFound){
                          return mp;
                      }
                  }
              }
          }

          Organization orgn=new Organization();
          orgn.setId(dto.getOrgId());
          prdct.setOrg(orgn);
          prdct.setBrand(brand);
          prdct.setCat(cat);
          prdct.setModel(model);
          prdct.setSize(size);
          prdct.setColor(color);
          prdct.setMadeWith(madeWith);
          prdct.setUom(uom);
          mp.put("product",prdct);
          return mp;
      }

    private String productCriteriaMaintainClassification(Long org, ProductCat cat, Brand brand, ProductModel model,
                                                         ProductSize size, ProductColor color, MadeWith madeWith,
                                                         UnitOfMeasure uom) {
        if(!cat.getOrgId().equals(org)){
            return "Product category is under another organization";
        }
        if(brand!=null && !brand.getOrg().getId().equals(org)){
            return "Brand is under another organization";
        }
        if(model!=null && !model.getOrg().getId().equals(org)){
            return "Model is under another organization";
        }
        if(model!=null && brand==null){
            return "Select model under selected brand";
        }
        if(model!=null && !model.getBrand().getId().equals(brand.getId())){
            return "Select model under selected brand";
        }
        if(size!=null && !size.getOrgId().equals(org)){
            return "Selected size is under another organization";
        }
        if(color!=null && !color.getOrgId().equals(org)){
            return "Selected color is under another organization";
        }
        if(madeWith!=null && !madeWith.getOrgId().equals(org)){
            return "Selected made with is under another organization";
        }
        if(uom!=null && !uom.getOrg().getId().equals(org)){
            return "Selected unit of measurement is under another organization";
        }
        return null;

    }

    @Transactional
    public MsgResponse create(ProductDTO dto) {

         Map<String,Object> mp = validate(dto);
         if((boolean)mp.get("hasError")){
             return new MsgResponse((String)mp.get("message"),mp,false);
         }
         Product product=(Product) mp.get("product");
         BeanUtils.copyProperties(dto,product);
         if(dto.getId()==null){
             product.setCreateBy(CommonUtil.currentUser());
             product.setCode(service.getCounterCode(dto.getOrgId(),null, CounterEnum.PRODUCT.getValue(),"PDCT" ));
         }else{
             product.setUpdateBy(CommonUtil.currentUser());
         }
          productRepo.save(product);
         Pricing sellPrice=sellPriceRepo.findByProduct(product);
         if(sellPrice==null){
             sellPrice=new Pricing();
             sellPrice.setProduct(product);
             BeanUtils.copyProperties(dto.getPrice(),sellPrice);
         }else{
             BeanUtils.copyProperties(dto.getPrice(),sellPrice,"id");
         }
         sellPriceRepo.save(sellPrice);
        return new MsgResponse("Successfully created product",true);
    }

    @Transactional
    public MsgResponse edit(ProductDTO dto) {

        return this.create(dto);
    }

    public MsgResponse getList(SearchParamDTO dto) {

        Pageable pageable = PageRequest.of((dto.pageNum-1),dto.pageSize, Sort.by(dto.sortField).descending());
        if(dto.getDropDown()!=null){
            if(dto.getName()==null || (dto.getName().length()%2==0)){
                Page<Map<String,Object>> page = productRepo.getList(dto.orgId,dto.getName(),pageable);
                return CommonUtil.responseFromPage(page);
            }
                return new MsgResponse();
        }else{
            Page<Map<String,Object>> page = productRepo.getList(dto.id,dto.orgId,dto.brandId,
                    dto.catId,dto.modelId,dto.getName(),pageable);
            return CommonUtil.responseFromPage(page);
        }
    }

    public Product getById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    public MsgResponse delete(Long id) {
          return new MsgResponse();
    }
}
