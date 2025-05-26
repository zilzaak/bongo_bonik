package app.common.controller;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/common")
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


  @PostMapping("/create")
  ResponseEntity<?> create(@RequestBody CommonDTO dto)
          throws RuntimeException{
      MsgResponse response = new MsgResponse();
      if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
          throw new RuntimeException("Under which entity you will create is not given");
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
      else if(dto.getEntity().equalsIgnoreCase("UnitOfMeasure")){
          response = uomService.create(dto);
      }

      return new ResponseEntity<>(response ,HttpStatus.OK);
  }

    @PostMapping("/update")
    ResponseEntity<?> update(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
            throw new RuntimeException("Under which entity you will create is not given");
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
            throw new RuntimeException("Under which entity you will create is not given");
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
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

    @PostMapping("/list")
    ResponseEntity<?> getList(@RequestParam Map<String,String> params)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(params.isEmpty()){
            throw new RuntimeException("Under which entity you will create is not given");
        }
        if(params.containsKey("Brand")){
            response = brandService.getList(params);
        }
        else if(params.containsKey("ProductCat")){
            response = catService.getList(params);
        }
        else if(params.containsKey("ProductModel")){
            response = modelService.getList(params);
        }
        else if(params.containsKey("ProductColor")){
            response = colorService.getList(params);
        }
        else if(params.containsKey("ProductSize")){
            response = sizeService.getList(params);
        }
        else if(params.containsKey("UnitOfMeasure")){
            response = uomService.getList(params);
        }

        return new ResponseEntity<>(response ,HttpStatus.OK);
    }



}
