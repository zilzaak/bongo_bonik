package app.common.controller;


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
  ResponseEntity<?> create(@RequestBody Map<String,Object> map)
          throws RuntimeException{
      MsgResponse response = new MsgResponse();

      return new ResponseEntity<>(response ,HttpStatus.OK);
  }

    @PostMapping("/update")
    ResponseEntity<?> update(@RequestBody Map<String,Object> map)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();

        return new ResponseEntity<>(response ,HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    ResponseEntity<?> delete(@RequestBody Map<String,Object> map)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }



}
