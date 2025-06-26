package app.common.service;


import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.dto.SearchParamDTO;
import app.common.entity.*;
import app.common.repo.*;
import app.common.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
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


    //1>>first check required field are null
    //2>>check all criteria of product belongs to same organization
    //3>>check unique name exist or not
      Map<String,Object> validate(ProductDTO dto){
          Map<String,Object> mp = new HashMap<>();
          mp.put("hasError",false);
          if(dto.getOrgId()==null || dto.getCatId()==null || dto.getBrandId()==null || dto.getUomId()==null){
           mp.put("hasError",true);
           mp.put("message","Organization , category , brand , unit of measurement is required ");
           return mp;
          }

          Long org = dto.getOrgId();
          ProductCat cat = catRepo.findById(dto.getCatId()).get();
          Brand brand = brandRepo.findById(dto.getBrandId()).get();
          ProductModel model = dto.getModelId()!=null?modelRepo.findById(dto.getModelId()).get():null;
          ProductSize size = dto.getSizeId()!=null?sizeRepo.findById(dto.getSizeId()).get():null;
          ProductColor color = dto.getColorId()!=null?colorRepo.findById(dto.getColorId()).get():null;
          MadeWith madeWith = dto.getMadeWithId()!=null?madeWithRepo.findById(dto.getMadeWithId()).get():null;
          UnitOfMeasure uom = dto.getUomId()!=null?uomRepo.findById(dto.getUomId()).get():null;

          String errorMessage = productCriteriaMaintainClassification(org,cat,brand,model,size,color,madeWith,uom);
          if(errorMessage!=null){
              mp.put("hasError",true);
              mp.put("message",errorMessage);
              return mp;
          }


          String fullName = CommonUtil.getProductFullname(dto.getName(),cat,brand,model,madeWith,size,color,dto.getQtyPerUnit(),dto.getQtyUnit(),uom);

          if(dto.getId()==null){
             //check duplicate fullName
              if(productRepo.existsByOrgIdAndFullName(dto.getOrgId(),fullName)){
                  mp.put("hasError",true);
                  mp.put("message","The product already exist");
                  return mp;
              }
          }else{
              if(productRepo.existsByOrgIdAndFullNameAndIdNotIn(dto.getOrgId(),fullName, Arrays.asList(dto.getId()))){
                  mp.put("hasError",true);
                  mp.put("message","The product already exist");
                  return mp;
              }
              Product prdct = productRepo.findById(dto.getId()).get();
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
        if(!brand.getOrgId().equals(org)){
            return "Brand is under another organization";
        }
        if(model!=null && !model.getOrgId().equals(org)){
            return "Model is under another organization";
        }
        if(model!=null && !model.getBrandId().equals(brand.getId())){
            return "Select model under selected brand";
        }
        if(size!=null){
            return "Selected size is under another organization";
        }
        if(color!=null && color.getOrgId().equals(org)){
            return "Selected color is under another organization";
        }
        if(madeWith!=null && madeWith.getOrgId().equals(org)){
            return "Selected made with is under another organization";
        }
        if(uom!=null && uom.getOrgId().equals(org)){
            return "Selected unit of measurement is under another organization";
        }
        return null;

    }

    public MsgResponse create(ProductDTO dto) {

         Map<String,Object> mp = validate(dto);
         if((boolean)mp.get("hasError")){
             return new MsgResponse((String)mp.get("message"),false);
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
}
