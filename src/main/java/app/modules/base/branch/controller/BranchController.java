package app.modules.base.branch.controller;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.base.branch.service.BranchService;
import app.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/base/branch")

public class BranchController {
    @Autowired
    private BranchService branchService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse();
            if(!CommonUtil.validUserOrg(dto.getOrgId())){
                return new ResponseEntity<>(new MsgResponse("ORGANIZATION IS INVALID ",false) , HttpStatus.OK);
            }
            response = branchService.create(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response = new MsgResponse() ;
            if(!CommonUtil.validUserOrg(dto.getOrgId())){
                return new ResponseEntity<>(new MsgResponse("ORGANIZATION IS INVALID ",false) , HttpStatus.OK);
            }
            response = branchService.edit(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    ResponseEntity<?> delete(@RequestBody CommonDTO dto)
            throws RuntimeException{
        MsgResponse response=null;
        if(dto.getEntity().equalsIgnoreCase("Branch")){
            if(!CommonUtil.validUserOrg(dto.getOrgId())){
                return new ResponseEntity<>(new MsgResponse("ORGANIZATION IS INVALID ",false) , HttpStatus.OK);
            }
            response = branchService.delete(dto);
        }
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }


    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response=null;
            if(!CommonUtil.validUserOrg(dto.getOrgId())){
                return new ResponseEntity<>(new MsgResponse("ORGANIZATION IS INVALID ",false) , HttpStatus.OK);
            }
            response = branchService.getList(dto);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

}
