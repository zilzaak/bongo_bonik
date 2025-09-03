package app.common.controller;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.dto.SearchParamDTO;
import app.common.entity.Product;
import app.common.service.ProductService;
import app.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/setting/product")
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

    @DeleteMapping("/delete")
    ResponseEntity<?> update(@RequestBody CommonDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = productService.delete(dto.getId());
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        if(!CommonUtil.validUserOrg(dto.orgId)){
            throw new RuntimeException("Invalid user organization selected");
        }
        MsgResponse response =productService.getList(dto);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

}
