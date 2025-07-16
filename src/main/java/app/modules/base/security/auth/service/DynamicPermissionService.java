package app.modules.base.security.auth.service;

import app.modules.base.urlPerm.repo.PermittedApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicPermissionService {

    @Autowired
    private PermittedApiRepository permittedApiRepository;

    public Map<String, String> getPermissions() {
        List<Map<String,Object>> permissions = permittedApiRepository.getUsersPermittedMenu();
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
