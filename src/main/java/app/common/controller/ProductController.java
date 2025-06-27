package app.common.controller;

import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.dto.SearchParamDTO;
import app.common.entity.Product;
import app.common.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody ProductDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = productService.create(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody ProductDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = productService.edit(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    ResponseEntity<?> getById(@PathVariable Long id)
            throws RuntimeException{
        Product product = productService.getById(id);
        return new ResponseEntity<>(product ,HttpStatus.OK);
    }


    @RequestMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response =productService.getList(dto);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

}
