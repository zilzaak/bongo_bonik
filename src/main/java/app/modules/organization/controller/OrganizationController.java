package app.modules.organization.controller;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.organization.service.BranchService;
import app.modules.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/organization")
public class OrganizationController {


    @Autowired
    private OrganizationService orgService;

    @Autowired
    private BranchService branchService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
            response.setMessage("Entity is required , entity = Organization or Branch ");
            response.setSuccess(false);
            return new ResponseEntity<>(response , HttpStatus.OK);
        }
        if(dto.getEntity().equalsIgnoreCase("Organization")){
            response = orgService.create(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("Branch")){
            response = branchService.create(dto);
        }

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
            response.setMessage("Entity is required , entity = Organization or Branch ");
            response.setSuccess(false);
            return new ResponseEntity<>(response , HttpStatus.OK);
        }
        if(dto.getEntity().equalsIgnoreCase("Organization")){
            response = orgService.edit(dto);
        }
        else if(dto.getEntity().equalsIgnoreCase("Branch")){
            response = branchService.edit(dto);
        }

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
        if(dto.getEntity()==null || dto.getEntity().trim().isEmpty()){
            response.setMessage("Entity is required , entity = Organization or Branch ");
            response.setSuccess(false);
            return new ResponseEntity<>(response , HttpStatus.OK);
        }

        if(dto.entity.equalsIgnoreCase("Organization")){
            response = orgService.getList(dto);
        }
        else if(dto.entity.equalsIgnoreCase("Branch")){
            response = branchService.getList(dto);
        }
        return new ResponseEntity<>(response , HttpStatus.OK);
    }



}
