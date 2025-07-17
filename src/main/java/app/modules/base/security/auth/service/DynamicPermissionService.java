package app.modules.base.security.auth.service;

import app.modules.base.role.entity.Role;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.urlPerm.repo.PermittedApiRepository;
import app.modules.base.user.entity.User;
import app.modules.base.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DynamicPermissionService {

    @Autowired
    private PermittedApiRepository permittedApiRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public Map<String, String> getPermissions() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User  user = null;
        User u = null;
        Set<Role> role=new HashSet<>();
        Role free = roleRepository.findByAuthority("PERMIT_ALL");
        if(auth!=null){
            user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            u=userRepository.findByUsername(user.getUsername());
            role=u.getRoles();
            System.out.println(user.getUsername());
        }
        role.add(free);

        List<Map<String,Object>> permissions = permittedApiRepository.getUsersPermittedMenu(role);

        Map<String, String> maps = new HashMap<>();
          for(Map<String,Object> obj :  permissions ){
             if(maps.isEmpty()){
                 maps.put((String)obj.get("apiPattern"), (String)obj.get("authority"));
             }else{
                 if(maps.containsKey((String)obj.get("apiPattern"))){
                  String val=maps.get((String)obj.get("apiPattern"));
                  val=val+","+obj.get("authority");
                  maps.put((String)obj.get("apiPattern"),val);
              }else{
                  maps.put((String)obj.get("apiPattern"), (String) obj.get("authority"));
                 }

             }
          }
        return  maps;
    }
}
