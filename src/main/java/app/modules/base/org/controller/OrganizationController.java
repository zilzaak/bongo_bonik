package app.modules.base.org.controller;


import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.service.BranchService;
import app.common.util.CommonUtil;
import app.modules.base.org.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/base/organization")
@CrossOrigin(origins = "http://localhost:4200")
public class OrganizationController {


    @Autowired
    private OrganizationService orgService;

    @Autowired
    private BranchService branchService;

    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody CommonDTO dto)
            throws RuntimeException{

        MsgResponse response = new MsgResponse();
        if(dto.getEntity().equalsIgnoreCase("Branch")){
            if(!CommonUtil.validUserOrg(dto.getOrgId())){
                return new ResponseEntity<>(new MsgResponse("ORGANIZATION IS INVALID ",false) , HttpStatus.OK);
            }
            response = branchService.create(dto);
        }
        if(dto.getEntity().equalsIgnoreCase("Organization")){
            response = orgService.create(dto);
        }

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> update(@RequestBody CommonDTO dto)
            throws RuntimeException{

        MsgResponse response = new MsgResponse() ;
        if(dto.getEntity().equalsIgnoreCase("Branch")){
            if(!CommonUtil.validUserOrg(dto.getOrgId())){
                return new ResponseEntity<>(new MsgResponse("ORGANIZATION IS INVALID ",false) , HttpStatus.OK);
            }
            response = orgService.edit(dto);
        }
        if(dto.getEntity().equalsIgnoreCase("Organization")){
            response = orgService.edit(dto);
        }
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
        if(dto.getEntity().equalsIgnoreCase("Organization")){
            response = orgService.delete(dto);
        }
        return new ResponseEntity<>(response ,HttpStatus.OK);
    }


    @GetMapping("/list")
    ResponseEntity<?> getList(SearchParamDTO dto)
            throws RuntimeException{
        MsgResponse response=null;
        if(dto.getEntity().equalsIgnoreCase("Branch")){
            if(!CommonUtil.validUserOrg(dto.getOrgId())){
                return new ResponseEntity<>(new MsgResponse("ORGANIZATION IS INVALID ",false) , HttpStatus.OK);
            }
            response = branchService.getList(dto);
        }
        else{
            response = orgService.getList(dto);
        }
        return new ResponseEntity<>(response , HttpStatus.OK);
    }



}
