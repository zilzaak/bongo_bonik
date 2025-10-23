package app.modules.sales.customer.controller;

import app.common.dto.MsgResponse;
import app.modules.sales.customer.dto.CustomerDTO;
import app.modules.sales.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sales/customer")

public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody CustomerDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = customerService.create(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody CustomerDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = customerService.edit(dto);
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
