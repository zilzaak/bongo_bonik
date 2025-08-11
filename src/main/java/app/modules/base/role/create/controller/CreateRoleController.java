package app.modules.base.role.create.controller;


import app.common.dto.CustomException;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.base.role.create.service.CreateRoleService;
import app.modules.base.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/base/role")
public class CreateRoleController {

@Autowired
private CreateRoleService manageRoleService;

    @PostMapping("/create")
    private ResponseEntity<?> create(@RequestBody Role role) {
        MsgResponse resp = manageRoleService.create(role);
        return new ResponseEntity<>(new MsgResponse("Successfully created role",true), HttpStatus.OK);
    }

    @PutMapping("/update")
    private ResponseEntity<?> edit(@RequestBody Role role) {
       MsgResponse resp = manageRoleService.edit(role);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/get")
    private ResponseEntity<?> get(@RequestParam Map<String,String> param) throws CustomException {
        Role role = manageRoleService.getById(param);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/list")
    private ResponseEntity<?> list(SearchParamDTO dto) throws CustomException {
        MsgResponse response = manageRoleService.list(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<?> delete(@RequestParam Map<String,String> param) throws CustomException {
        manageRoleService.delete(param);
        return new ResponseEntity<>(new MsgResponse("Successfully deleted role",true), HttpStatus.OK);
    }





}
