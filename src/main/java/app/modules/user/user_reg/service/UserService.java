package app.modules.user.user_reg.service;


import app.common.dto.CustomException;
import app.modules.user.security.entity.Role;
import app.modules.user.security.entity.RoleRepository;
import app.modules.user.security.entity.User;
import app.modules.user.security.entity.UserRepository;
import app.modules.user.user_reg.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

public Map<String, Object> checkValidData(UserDTO dto, String operation){
    Map<String, Object> mp = new HashMap<>();
    mp.put("hasError",false);
    mp.put("message","no error exist");

    if(dto.getUsername()==null || dto.getUsername().trim().isEmpty()){
        mp.put("hasError",true);
        mp.put("message","Invalid username");
        return mp;
    }



    if(operation.equalsIgnoreCase("create")){

        if(dto.getPassword()==null || dto.getPassword().trim().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Invalid password");
            return mp;
        }

        if(userRepository.existsByUsername(dto.getUsername())){
            mp.put("hasError",true);
            mp.put("message","Need unique username");
            return mp;
        }

    }else{
        if(userRepository.existsByUsernameAndIdNotIn(dto.getUsername(),Arrays.asList(dto.getId()))){
            mp.put("hasError",true);
            mp.put("message","Need unique username");
            return mp;
        }

    }


    for(String role : dto.getRoles()){
        if(!roleRepository.existsByAuthority(role)){
            mp.put("hasError",true);
            mp.put("message","This assigned role "+role+" is not exist in system");
            return mp;
        }
    }

    return mp;
}


    public void create(UserDTO userDTO) throws CustomException {
        Map<String,Object> resp = checkValidData(userDTO,"create");
        if((boolean)resp.get("hasError")){
            throw new CustomException((String)resp.get("message"));
        }

        Set<Role> roleList = new HashSet<>();
        for(String Authority : userDTO.getRoles()){
            Role role = roleRepository.findByAuthority(Authority);
            roleList.add(role);
             }
        User user = new User();
        user.setRoles(roleList);
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        try{
            userRepository.saveAndFlush(user);
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    public void edit(UserDTO userDTO) throws CustomException {
        Map<String,Object> resp = checkValidData(userDTO,"edit");
        if((boolean)resp.get("hasError")){
            throw new CustomException((String)resp.get("message"));
        }

        Set<Role> roleList = new HashSet<>();
        for(String Authority : userDTO.getRoles()){
            Role role = roleRepository.findByAuthority(Authority);
            roleList.add(role);
        }

        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if(user==null){
            throw new CustomException("No user found with id "+ userDTO.getId());
        }
        user.setRoles(roleList);
        user.setUsername(userDTO.getUsername());
        try{
            userRepository.saveAndFlush(user);
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }


    }

    public User getByUser(Map<String, String> param) throws CustomException {
        try{
            User user = userRepository.findById(Long.parseLong(param.get("id"))).get();
            return user;
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    public void delete(Map<String, String> param) throws CustomException {
        try{
        userRepository.deleteById(Long.parseLong(param.get("id")));
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }

    }
}
