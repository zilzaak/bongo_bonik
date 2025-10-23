package app.modules.purchase.supplier.controller;

import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.dto.SearchParamDTO;
import app.modules.purchase.supplier.dto.SupplierDTO;
import app.modules.purchase.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchase/supplier")

public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody List<SupplierDTO> list)
            throws RuntimeException{
        if(list.isEmpty()){
            throw new RuntimeException("null data exist in form");
        }
        int errorCount=0;
        List<String> errorMesg=new ArrayList<>();
        for(SupplierDTO dto : list){
            MsgResponse resp = supplierService.create(dto);
            if(!resp.isSuccess()){
                errorCount++;
                errorMesg.add("SL NO="+(list.indexOf(dto)+1)+" "+resp.getMessage());
            }
        }
        if(errorCount<1){
            return new ResponseEntity<>(new MsgResponse("Successfully created",true) , HttpStatus.OK);
        }else{
            String msg=errorCount==list.size()?"Invalid form data ":"Some data can not be save because of following issue";
            return new ResponseEntity<>(new MsgResponse(msg,errorMesg,false) , HttpStatus.OK);
        }
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody List<SupplierDTO> list)
            throws RuntimeException{
        return this.create(list);
    }


    @DeleteMapping("/delete")
    ResponseEntity<?> delete(@RequestBody ProductDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response = supplierService.getList(dto);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }


}
