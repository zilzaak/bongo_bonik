package app.modules.base.user.controller;

import app.common.dto.CustomException;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.base.user.entity.User;
import app.modules.base.user.dto.UserDTO;
import app.modules.base.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/base/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    private ResponseEntity<?> create(@RequestBody UserDTO userDTO) throws CustomException {
        userService.create(userDTO);
        return new ResponseEntity<>(new MsgResponse("Successfully created user",true), HttpStatus.OK);
    }

    @PutMapping("/edit")
    private ResponseEntity<?> edit(@RequestBody UserDTO userDTO) throws CustomException {
        userService.edit(userDTO);
        return new ResponseEntity<>(new MsgResponse("Successfully edited user",true), HttpStatus.OK);
    }

    @GetMapping("/get")
    private ResponseEntity<?> get(@RequestParam Map<String,String> param) throws CustomException {
        User user = userService.getByUser(param);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    private ResponseEntity<?> list(SearchParamDTO dto) throws CustomException {
        MsgResponse response = userService.list(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/delete")
    private ResponseEntity<?> delete(@RequestParam Map<String,String> param) throws CustomException {
        userService.delete(param);
        return new ResponseEntity<>(new MsgResponse("Successfully deleted user",true), HttpStatus.OK);
    }



}
