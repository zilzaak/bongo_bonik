package app.modules.user.manageRole.menuWise.service;


import app.common.dto.CustomException;
import app.modules.user.security.entity.AuthorityPermission;
import app.modules.user.security.repo.AuthorityPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class MenuWisePermissionService {

    @Autowired
    private AuthorityPermissionRepository authorityPermissionRepository;


    Map<String,Object> checkValidData(AuthorityPermission obj,String operation){
        Map<String,Object> mp=new HashMap<>();
        mp.put("hasError",false);
        if(obj.getApiPattern()==null || obj.getApiPattern().trim().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Api value missing");
            return mp;
        }

        if(obj.getRoleName()==null || obj.getRoleName().trim().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Role name is missing ");
            return mp;
        }

        if(operation.equals("create")){
if(authorityPermissionRepository.existsByRoleNameAndApiPattern(obj.getRoleName(),obj.getApiPattern())){
    mp.put("hasError",true);
    mp.put("message","Api permission against this Role is already exist");
    return mp;
          }
        }else{
            if(authorityPermissionRepository.existsByRoleNameAndApiPatternAndIdNotIn(
                    obj.getRoleName(),
                    obj.getApiPattern(),
                    Arrays.asList(obj.getId()))){
                mp.put("hasError",true);
                mp.put("message","Api permission against this Role is already exist");
                return mp;
            }
        }


return  mp;
    }
    public void create(AuthorityPermission obj) throws CustomException {
        Map<String,Object> mp=this.checkValidData(obj,"create");
        if((boolean)mp.get("hasError")){
            throw new CustomException((String)mp.get("message"));
        }
         authorityPermissionRepository.save(obj);
    }

    public void edit(AuthorityPermission obj) throws CustomException {
        Map<String,Object> mp=this.checkValidData(obj,"edit");
        if((boolean)mp.get("hasError")){
            throw new CustomException((String)mp.get("message"));
        }
        authorityPermissionRepository.save(obj);
    }

    public AuthorityPermission getById(Map<String, String> param) {
        return authorityPermissionRepository.findById(Long.parseLong(param.get("id"))).get();
    }

    public void delete(Map<String, String> param) {
         authorityPermissionRepository.deleteById(Long.parseLong(param.get("id")));
    }
}
