package app.modules.role.create.service;


import app.common.dto.CustomException;
import app.modules.security.entity.Role;
import app.modules.security.entity.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateRoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Map<String,Object> checkValidData(Role role, String operation){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);
        mp.put("message","no error exist");


        if(role.getAuthority()==null ||  role.getAuthority().trim().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Authority is required");
            return mp;
        }

        if(operation.equalsIgnoreCase("create")){
            if(roleRepository.existsByAuthority(role.getAuthority())){
                mp.put("hasError",true);
                mp.put("message","Authority already exists");
                return mp;
            }
        }else{
            if(roleRepository.existsByAuthorityAndIdNotIn(role.getAuthority(), Arrays.asList(role.getId()))){
                mp.put("hasError",true);
                mp.put("message","Authority already exists");
                return mp;
            }
        }
        return mp;

    }


    public void create(Role role) throws CustomException {

        Map<String,Object> resp = checkValidData(role,"create");
        if((boolean)resp.get("hasError")){
            throw new CustomException((String)resp.get("message"));
        }
            roleRepository.saveAndFlush(role);

    }

    public void edit(Role role) throws CustomException {
        Map<String,Object> resp = checkValidData(role,"edit");
        if((boolean)resp.get("hasError")){
            throw new CustomException((String)resp.get("message"));
        }

        Role entity = roleRepository.findById(role.getId()).orElse(null);
        if(entity==null){
            throw new CustomException("role is not found");
        }
        entity.setAuthority(role.getAuthority());
        roleRepository.saveAndFlush(entity);

    }

    public Role getById(Map<String, String> param) throws CustomException {
        try{
            Role role = roleRepository.findById(Long.parseLong(param.get("id"))).get();
            return role;
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    public void delete(Map<String, String> param) throws CustomException {
        try{
            roleRepository.deleteById(Long.parseLong(param.get("id")));
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }


    }
}
