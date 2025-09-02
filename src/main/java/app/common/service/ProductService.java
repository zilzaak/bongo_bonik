package app.common.service;


import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.dto.SearchParamDTO;
import app.common.entity.*;
import app.common.repo.*;
import app.common.util.CommonUtil;
import app.modules.base.org.repo.OrgRepo;
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
    private OrgRepo orgRepo;


    //1>>first check required field are null
    //2>>check all criteria of product belongs to same organization
    //3>>check unique name exist or not
      Map<String,Object> validate(ProductDTO dto){
          Map<String,Object> mp = new HashMap<>();
          mp.put("hasError",false);
          if(dto.getOrgId()==null || dto.getCatId()==null || dto.getUomId()==null){
           mp.put("hasError",true);
           mp.put("message","Organization , category , unit of measurement is required ");
           return mp;
          }

          Long org = dto.getOrgId();
          ProductCat cat = catRepo.findById(dto.getCatId()).orElse(null);
          Brand brand = brandRepo.findById(dto.getBrandId()).orElse(null);
          UnitOfMeasure uom = dto.getUomId()!=null?uomRepo.findById(dto.getUomId()).orElse(null):null;

          if(!orgRepo.existsById(dto.getOrgId())){
              mp.put("hasError",true);
              mp.put("message","No Organization exist under id="+dto.getOrgId());
              return mp;
          }

          if(cat==null){
              mp.put("hasError",true);
              mp.put("message","No category exist under id="+dto.getCatId());
              return mp;
          }
          if(dto.getBrandId()!=null && brand==null){
              mp.put("hasError",true);
              mp.put("message","No brand exist under id="+dto.getBrandId());
              return mp;
          }
          if(uom==null){
              mp.put("hasError",true);
              mp.put("message","No Uom exist under id="+dto.getUomId());
              return mp;
          }


          ProductModel model = dto.getModelId()!=null?modelRepo.findById(dto.getModelId()).get():null;
          ProductSize size = dto.getSizeId()!=null?sizeRepo.findById(dto.getSizeId()).get():null;
          ProductColor color = dto.getColorId()!=null?colorRepo.findById(dto.getColorId()).get():null;
          MadeWith madeWith = dto.getMadeWithId()!=null?madeWithRepo.findById(dto.getMadeWithId()).get():null;

          String errorMessage = productCriteriaMaintainClassification(org,cat,brand,model,size,color,madeWith,uom);
          if(errorMessage!=null){
              mp.put("hasError",true);
              mp.put("message",errorMessage);
              return mp;
          }


          Map<String,Object> naming = CommonUtil.getProductFullname(dto.getName(),cat,brand,model,madeWith,size,color,dto.getQtyPerUnit(),dto.getUnitName(),uom);
          String fullName = (String) naming.get("fullName");
          String criteriaIds = (String) naming.get("criteriaIds");
          dto.setCriteriaIds(criteriaIds);
          dto.setFullName(fullName);

          if(dto.getId()==null){
             //check duplicate fullName
              List<Map<String,Object>> existProduct=productRepo.similarProduct(dto.getOrgId(),dto.getName(),dto.getCriteriaIds());
              if(existProduct.size()>0){
                  mp.put("hasError",true);
                  mp.put("message","Similar product name exist , be confirm duplicity or not before create ");
                  mp.put("existsData",existProduct);
                  return mp;
              }
          }else{
              List<Map<String,Object>> existProduct=productRepo.similarProduct(dto.getOrgId(),dto.getName(),dto.getCriteriaIds(),dto.getId());
              if(existProduct.size()>0){
                  mp.put("hasError",true);
                  mp.put("message","Similar product name exist , be confirm duplicity or not before create ");
                  mp.put("existsData",existProduct);
                  return mp;
              }
              Product prdct = productRepo.findById(dto.getId()).get();

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

              mp.put("product",prdct);

          }

          mp.put("brand",brand);
          if(model!=null){
              mp.put("model",model);
          }
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
        if(size!=null && size.getOrgId().equals(org)){
            return "Selected size is under another organization";
        }
        if(color!=null && color.getOrgId().equals(org)){
            return "Selected color is under another organization";
        }
        if(madeWith!=null && madeWith.getOrgId().equals(org)){
            return "Selected made with is under another organization";
        }
        if(uom!=null && uom.getOrg().getId().equals(org)){
            return "Selected unit of measurement is under another organization";
        }
        return null;

    }

    public MsgResponse create(ProductDTO dto) {

         Map<String,Object> mp = validate(dto);
         if((boolean)mp.get("hasError")){
             return new MsgResponse((String)mp.get("message"),mp,false);
         }

         Product product = new Product();
         if(dto.getId()!=null){
             BeanUtils.copyProperties(dto,product);
             product.setBrand((Brand) mp.get("brand"));
             if(mp.containsKey("model")){
              product.setModel((ProductModel) mp.get("model"));
             }
         }else{
             product = (Product) mp.get("product");
             BeanUtils.copyProperties(dto,product,"created","updated");
             product.setBrand((Brand) mp.get("brand"));
             if(mp.containsKey("model")){
                 product.setModel((ProductModel) mp.get("model"));
             }
         }

         productRepo.save(product);

          return new MsgResponse("Successfully created product",true);
    }

    public MsgResponse edit(ProductDTO dto) {

        return this.create(dto);
    }

    public MsgResponse getList(SearchParamDTO dto) {

        Pageable pageable = PageRequest.of((dto.pageNum-1),dto.pageSize, Sort.by(dto.sortField).descending());
        Page<Map<String,Object>> page = productRepo.getList(dto.productId,dto.orgId,dto.brandId,
                dto.catId,dto.modelId,dto.sizeId,dto.colorId ,pageable);
        return CommonUtil.responseFromPage(page);

    }

    public Product getById(Long id) {
        return productRepo.findById(id).orElse(null);
    }
}
