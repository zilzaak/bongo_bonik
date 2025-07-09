package app.modules.inventory.controller;

import app.common.dto.MsgResponse;
import app.modules.inventory.dto.PricingDTO;
import app.modules.inventory.service.CostPriceService;
import app.modules.inventory.service.SellPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pricing")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductPriceController {

    @Autowired
    private SellPriceService sellPriceService;
    @Autowired
    private CostPriceService costPriceService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody PricingDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = new MsgResponse();
        if(dto.getEntity().equalsIgnoreCase("SellPrice")){
            response =  sellPriceService.create(dto);
        }else{
            response =  costPriceService.create(dto);
        }

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody PricingDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = new MsgResponse();
        if(dto.getEntity().equalsIgnoreCase("SellPrice")){
            response =  sellPriceService.edit(dto);
        }else{
            response =  costPriceService.edit(dto);
        }
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<?> getList(@RequestParam Map<String,String> params)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

}
