package app.modules.inventory.controller;


import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.inventory.dto.InventoryDTO;
import app.modules.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "http://localhost:4200")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody InventoryDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = inventoryService.create(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody InventoryDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = inventoryService.edit(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id)
            throws RuntimeException{
        MsgResponse response = inventoryService.delete(id);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response = inventoryService.getList(dto);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

}
