package app.modules.base.moduleInfo.controller;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.base.moduleInfo.dto.MenuDTO;
import app.modules.base.moduleInfo.service.ModuleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/base/module")
public class ModuleInfoController {

    @Autowired
    private ModuleInfoService moduleInfoService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody List<MenuDTO> dto)
            throws RuntimeException{
        MsgResponse response=null;
        if(dto==null){
            response=new MsgResponse("null data exist in form",false);
            return new ResponseEntity<>(response , HttpStatus.OK);
        }
         response = moduleInfoService.create(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody List<MenuDTO> dto)
            throws RuntimeException{
        if(dto==null){
            throw new RuntimeException("null data exist in form");
        }
        MsgResponse response = moduleInfoService.edit(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    ResponseEntity<?> delete(@RequestBody CommonDTO commonDTO)
            throws RuntimeException{
        MsgResponse response = moduleInfoService.delete(commonDTO.getId());
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response = moduleInfoService.getList(dto);
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }

}
