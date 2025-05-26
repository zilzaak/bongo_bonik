package app.common.controller;


import app.common.dto.MsgResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/common")
public class CommonController {


  @PostMapping("/create")
  ResponseEntity<?> create(@RequestBody Map<String,Object> map)
          throws RuntimeException{
      MsgResponse response = new MsgResponse();

      return new ResponseEntity<>(response ,HttpStatus.OK);
  }

    @PostMapping("/update")
    ResponseEntity<?> update(@RequestBody Map<String,Object> map)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();

        return new ResponseEntity<>(response ,HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    ResponseEntity<?> delete(@RequestBody Map<String,Object> map)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }



}
