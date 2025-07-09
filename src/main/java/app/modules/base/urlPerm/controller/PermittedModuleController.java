package app.modules.base.urlPerm.controller;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.base.urlPerm.dto.PrmttedMdleDTO;
import app.modules.base.urlPerm.service.PermittedModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/base/permittedModule")
public class PermittedModuleController {

//    @Autowired
//    private PermittedModuleService permittedModuleService;
//
//    @PostMapping("/create")
//    ResponseEntity<?> create(@RequestBody PrmttedMdleDTO dto)
//            throws RuntimeException{
//        if(dto==null){
//            throw new RuntimeException("null data exist in form");
//        }
//        MsgResponse response = permittedModuleService.create(dto);
//        return new ResponseEntity<>(response , HttpStatus.OK);
//    }
//
//    @PutMapping("/update")
//    ResponseEntity<?> update(@RequestBody PrmttedMdleDTO dto)
//            throws RuntimeException{
//        if(dto==null){
//            throw new RuntimeException("null data exist in form");
//        }
//        MsgResponse response = permittedModuleService.edit(dto);
//        return new ResponseEntity<>(response , HttpStatus.OK);
//    }
//
//
//    @DeleteMapping("/delete/{id}")
//    ResponseEntity<?> delete(@PathVariable Long id)
//            throws RuntimeException{
//        MsgResponse response = permittedModuleService.delete(id);
//        return new ResponseEntity<>(response ,HttpStatus.OK);
//    }
//
//    @GetMapping("/getMenu")
//    ResponseEntity<?> getMenu()
//            throws RuntimeException{
//        MsgResponse response = permittedModuleService.getMenu();
//        return new ResponseEntity<>(response ,HttpStatus.OK);
//    }
//
//    @GetMapping("/list")
//    ResponseEntity<?> getList(SearchParamDTO dto)
//            throws RuntimeException{
//        MsgResponse response = permittedModuleService.getList(dto);
//        return new ResponseEntity<>(response ,HttpStatus.OK);
//    }


}
