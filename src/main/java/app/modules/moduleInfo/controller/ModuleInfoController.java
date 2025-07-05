package app.modules.moduleInfo.controller;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.inventory.dto.InventoryDTO;
import app.modules.moduleInfo.service.ModuleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/module")
public class ModuleInfoController {

    @Autowired
    private ModuleInfoService moduleInfoService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody InventoryDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = moduleInfoService.create(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody InventoryDTO dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = moduleInfoService.edit(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id)
            throws RuntimeException{
        MsgResponse response = moduleInfoService.delete(id);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response = moduleInfoService.getList(dto);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

}
