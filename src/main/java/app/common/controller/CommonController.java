package app.common.controller;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.service.*;
import app.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/setting/productCriteria/")
public class CommonController {

    @Autowired
    private ProductModelService modelService;
    @Autowired
    private ProductCatService catService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductColorService colorService;
    @Autowired
    private ProductSizeService sizeService;
    @Autowired
    private UomService uomService;
    @Autowired
    private MadeWithService madeWithService;


  @PostMapping("/create")
  ResponseEntity<?> create(@RequestBody CommonDTO dto){
      MsgResponse response = new MsgResponse();
      if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
          response.setSuccess(false);
          response.setMessage("Entity is required field its value may be Brand/ProductCat/ProductModel/ProductColor/ProductSize/MadeWith/UnitOfMeasure");
          return new ResponseEntity<>(response ,HttpStatus.OK);
      }
      if(!CommonUtil.validOrg(dto.getOrgId())){
          response.setSuccess(false);
          response.setMessage("The logged user is not the owner of the selected organization");
          return new ResponseEntity<>(response ,HttpStatus.OK);
      }
      if(dto.getEntity().equalsIgnoreCase("Brand")){
          response = brandService.create(dto);
      }
      else if(dto.getEntity().equalsIgnoreCase("ProductCat")){
          response = catService.create(dto);
      }
      else if(dto.getEntity().equalsIgnoreCase("ProductModel")){
          response = modelService.create(dto);
      }
      else if(dto.getEntity().equalsIgnoreCase("ProductColor")){
          response = colorService.create(dto);
      }
      else if(dto.getEntity().equalsIgnoreCase("ProductSize")){
          response = sizeService.create(dto);
      }
      else if(dto.getEntity().equalsIgnoreCase("MadeWith")){
          response = madeWithService.create(dto);
      }
      else if(dto.getEntity().equalsIgnoreCase("UnitOfMeasure")){
          response = uomService.create(dto);
      }

      return new ResponseEntity<>(response ,HttpStatus.OK);
  }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
            response.setSuccess(false);
            response.setMessage("Entity is required field its value may be Brand or ProductCat or ProductModel or ProductColor or ProductSize or MadeWith or UnitOfMeasure");
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        if(!CommonUtil.validOrg(dto.getOrgId())){
            response.setSuccess(false);
            response.setMessage("The logged user is not the owner of the selected organization");
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        if(dto.getEntity().equalsIgnoreCase("Brand")){
            response = brandService.create(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductCat")){
            response = catService.create(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductModel")){
            response = modelService.create(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductColor")){
            response = colorService.create(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductSize")){
            response = sizeService.create(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("MadeWith")){
            response = madeWithService.create(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("UnitOfMeasure")){
            response = uomService.create(dto);
        }

        return new ResponseEntity<>(response ,HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    ResponseEntity<?> delete(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
            response.setSuccess(false);
            response.setMessage("Entity is required field its value may be Brand or ProductCat or ProductModel or ProductColor or ProductSize or MadeWith or UnitOfMeasure");
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        if(!CommonUtil.validOrg(dto.getOrgId())){
            response.setSuccess(false);
            response.setMessage("The logged user is not the owner of the selected organization");
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        if(dto.getEntity().equalsIgnoreCase("Brand")){
            response = brandService.delete(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductCat")){
            response = catService.delete(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductModel")){
            response = modelService.delete(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductColor")){
            response = colorService.delete(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("ProductSize")){
            response = sizeService.delete(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("UnitOfMeasure")){
            response = uomService.delete(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("MadeWith")){
            response = madeWithService.delete(dto);
        }
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(dto.entity==null){
            response.setSuccess(false);
            response.setMessage("Entity is required field its value may be Brand or ProductCat or ProductModel or ProductColor or ProductSize or MadeWith or UnitOfMeasure");
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        if(!CommonUtil.validOrg(dto.getOrgId())){
            response.setSuccess(false);
            response.setMessage("The logged user is not the owner of the selected organization");
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        if(dto.entity.equalsIgnoreCase("Brand")){
            response = brandService.getList(dto);
        }
        else if(dto.entity.equalsIgnoreCase("ProductCat")){
            response = catService.getList(dto);
        }
        else if(dto.entity.equalsIgnoreCase("ProductModel")){
            response = modelService.getList(dto);
        }
        else if(dto.entity.equalsIgnoreCase("ProductColor")){
            response = colorService.getList(dto);
        }
        else if(dto.entity.equalsIgnoreCase("ProductSize")){
            response = sizeService.getList(dto);
        }
        else if(dto.entity.equalsIgnoreCase("UnitOfMeasure")){
            response = uomService.getList(dto);
        }

        else if(dto.entity.equalsIgnoreCase("MadeWith")){
            response = madeWithService.getList(dto);
        }

        return new ResponseEntity<>(response ,HttpStatus.OK);
    }



}
