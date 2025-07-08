package app.modules.security.role.menuWise.controller;

import app.common.dto.CustomException;
import app.common.dto.MsgResponse;
import app.modules.security.role.menuWise.service.MenuWisePermissionService;
import app.modules.security.entity.AuthorityPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/apiPerm")
@Controller
public class MenuWisePermissionController {

    @Autowired
    private MenuWisePermissionService menuWisePermissionService;

    @PostMapping("/create")
    private ResponseEntity<?> create(@RequestBody AuthorityPermission authorityPermission) throws CustomException {
        menuWisePermissionService.create(authorityPermission);
        return new ResponseEntity<>(new MsgResponse("Successfully created permission",true), HttpStatus.OK);
    }

    @PutMapping("/edit")
    private ResponseEntity<?> edit(@RequestBody AuthorityPermission authorityPermission) throws CustomException {
        menuWisePermissionService.edit(authorityPermission);
        return new ResponseEntity<>(new MsgResponse("Successfully edited permission",true), HttpStatus.OK);
    }

    @GetMapping("/get")
    private ResponseEntity<?> get(@RequestParam Map<String,String> param) throws CustomException {
        AuthorityPermission obj = menuWisePermissionService.getById(param);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/delete")
    private ResponseEntity<?> delete(@RequestParam Map<String,String> param) throws CustomException {
        menuWisePermissionService.delete(param);
        return new ResponseEntity<>(new MsgResponse("Successfully deleted permission",true), HttpStatus.OK);
    }




}
