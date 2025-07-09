package app.modules.base.user.service;


import app.common.dto.CustomException;
import app.modules.base.security.auth.entity.*;
import app.modules.base.security.auth.repo.AuthorityPermissionRepository;
import app.modules.base.role.entity.Role;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.user.dto.UserDTO;
import app.modules.base.user.entity.User;
import app.modules.base.user.repo.UserRepository;
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

    @Autowired
    private AuthorityPermissionRepository authorityPermissionRepository;



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


    public void createDefaultUser(){

        Set<Role> defaultRoleList = new HashSet<>();


        if(!roleRepository.existsByAuthority("SUPER_ADMIN")){
            Role superAdmin = new Role();
            superAdmin.setAuthority("SUPER_ADMIN");
            roleRepository.saveAndFlush(superAdmin);
            defaultRoleList.add(superAdmin);
        }

        if(userRepository.count()<1){
            User super_admin=new User();
            super_admin.setEnabled(Boolean.TRUE);
            super_admin.setUsername("admin");
            super_admin.setPassword(passwordEncoder.encode("123456"));
            super_admin.setRoles(defaultRoleList);
            userRepository.saveAndFlush(super_admin);
        }


        if(!roleRepository.existsByAuthority("PERMIT_ALL")){
            Role permAllRole = new Role();
            permAllRole.setAuthority("PERMIT_ALL");
            roleRepository.saveAndFlush(permAllRole);
        }

        // by default create PERMIT_ALL role so that every body can login via /auth/getToken api
        AuthorityPermission permitAll = new AuthorityPermission("PERMIT_ALL","/auth/getToken");
        AuthorityPermission getMenu = new AuthorityPermission("PERMIT_ALL","/permittedModule/getMenu");
        AuthorityPermission roleAcess=new AuthorityPermission("SUPER_ADMIN","/role/**");
        AuthorityPermission menuAcess=new AuthorityPermission("SUPER_ADMIN","/apiPerm/**");
        AuthorityPermission userAccess=new AuthorityPermission("SUPER_ADMIN","/user/**");
        AuthorityPermission moduleAccess=new AuthorityPermission("SUPER_ADMIN","/module/**");
        AuthorityPermission permittedModule=new AuthorityPermission("SUPER_ADMIN","/permittedModule/**");

        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("PERMIT_ALL","/permittedModule/getMenu")){
            authorityPermissionRepository.save(getMenu);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("PERMIT_ALL","/auth/getToken")){
            authorityPermissionRepository.save(permitAll);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/user/**")){
            authorityPermissionRepository.save(userAccess);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/role/**")){
            authorityPermissionRepository.save(roleAcess);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/apiPerm/**")){
            authorityPermissionRepository.save(menuAcess);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/module/**")){
            authorityPermissionRepository.save(moduleAccess);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/permittedModule/**")){
            authorityPermissionRepository.save(permittedModule);
        }

    }

}
