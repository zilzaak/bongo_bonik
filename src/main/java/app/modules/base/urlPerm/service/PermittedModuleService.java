package app.modules.base.urlPerm.service;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.moduleInfo.repo.MenuHierarchyRepo;
import app.modules.base.role.entity.Role;
import app.modules.base.role.repo.RoleRepository;
import app.modules.base.user.entity.User;
import app.modules.base.user.repo.UserRepository;
import app.modules.base.urlPerm.dto.PrmttedApiDTO;
import app.modules.base.urlPerm.dto.PrmttedMdleDTO;
import app.modules.base.urlPerm.entity.PermittedApi;
import app.modules.base.urlPerm.entity.PermittedModule;
import app.modules.base.urlPerm.repo.PermittedApiRepository;
import app.modules.base.urlPerm.repo.PermittedModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermittedModuleService {

//    @Autowired
//    private PermittedApiRepository permittedApiRepository;
//
//    @Autowired
//    private PermittedModuleRepository permittedModuleRepository;
//
//    @Autowired
//    private MenuHierarchyRepo apiAgainstModuleRepo;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    private Map<String, Object> validate(PrmttedMdleDTO dto) {
//        Map<String,Object> mp = new HashMap<>();
//        mp.put("hasError",false);
//
//        User user=null;
//        Role role=null;
//
//        if(dto.getDetails().size()<1){
//            mp.put("hasError",true);
//            mp.put("message","No Api is selected unde the module permission");
//            return mp;
//        }
//        if(dto.getModuleId()==null || !moduleInfoRepo.existsById(dto.getModuleId())){
//            mp.put("hasError",true);
//            mp.put("message","Module is required");
//            return mp;
//        }
//
//        if(dto.getUser()==null && dto.getRole()==null){
//            mp.put("hasError",true);
//            mp.put("message","User or Role is required field");
//            return mp;
//        }
//
//        if(dto.getUser()!=null && dto.getRole()!=null){
//            mp.put("hasError",true);
//            mp.put("message","Either User or Role , maximum one can be select");
//            return mp;
//        }
//
//        if(dto.getUser()!=null){
//             user = userRepository.findById(dto.getUser()).orElse(null);
//            if(user==null){
//                mp.put("hasError",true);
//                mp.put("message","No user found with id = "+dto.getUser());
//                return mp;
//            }
//
//        }else{
//             role = roleRepository.findById(dto.getRole()).orElse(null);
//            if(role==null){
//                mp.put("hasError",true);
//                mp.put("message","No role found with id = "+dto.getUser());
//                return mp;
//            }
//
//        }
//
//
//
//     if(dto.getId()==null){ //create
//         if(dto.getUser()!=null){
//            if(permittedModuleRepository.existsByModuleIdAndUser(dto.getModuleId(),user)){
//                mp.put("hasError",true);
//                mp.put("message","Module permission for the user already created");
//                return mp;
//             }
//         }else{
//             if(permittedModuleRepository.existsByModuleIdAndRole(dto.getModuleId(),role)){
//                 mp.put("hasError",true);
//                 mp.put("message","Module permission for the role already created");
//                 return mp;
//             }
//         }
//
//     }else{  //update
//         if(!permittedModuleRepository.existsById(dto.getId())){
//             mp.put("hasError",true);
//             mp.put("message","Permission for the module is not found with id = "+dto.getId());
//             return mp;
//         }
//
//         if(dto.getUser()!=null){
//             if(permittedModuleRepository.existsByModuleIdAndUserAndIdNotIn(dto.getModuleId(),user, Arrays.asList(dto.getId()))){
//                 mp.put("hasError",true);
//                 mp.put("message","Module permission for the user already created");
//                 return mp;
//             }
//         }else{
//             if(permittedModuleRepository.existsByModuleIdAndRoleAndIdNotIn(dto.getModuleId(),role,Arrays.asList(dto.getId()))){
//                 mp.put("hasError",true);
//                 mp.put("message","Module permission for the role already created");
//                 return mp;
//             }
//
//
//         }
//     }
//
//        int index=0;
//        for(PrmttedApiDTO apiDto : dto.getDetails()){
//            index++;
//            if(apiDto.getApi()==null || !apiAgainstModuleRepo.existsById(apiDto.getApi())){
//                mp.put("hasError",true);
//                mp.put("message","The "+index +"th api don't exist under selected module ");
//                return mp;
//            }
//        }
//
//        mp.put("user",user);
//        mp.put("role",role);
//        return mp;
//    }
//
//
//    public MsgResponse getList(SearchParamDTO dto) {
//        Pageable pageable = CommonUtil.getPageable(dto);
//        Page<Map<String,Object>> page = permittedModuleRepository.getList(dto.getModuleId(),dto.getUsername(),dto.getRoleId(),dto.getApiPattern(),pageable);
//        return CommonUtil.responseFromPage(page);
//    }
//
//    public MsgResponse delete(Long id) {
//
//
//        return null;
//    }
//
//    public MsgResponse edit(PrmttedMdleDTO dto) {
//        Map<String,Object> mp = this.validate(dto);
//
//        return null;
//    }
//
//    public MsgResponse create(PrmttedMdleDTO dto) {
//         Map<String,Object> mp = this.validate(dto);
//          if((boolean)mp.get("hasError")){
//              return  new MsgResponse((String) mp.get("message"),false);
//          }
//
//          PermittedModule permittedModule = new PermittedModule();
//          if(dto.getId()!=null){
//             permittedModule = permittedModuleRepository.findById(dto.getId()).get();
//              permittedModule.setModuleId(dto.getModuleId());
//              permittedModule.setRole((Role) mp.get("role"));
//              permittedModule.setUser((User) mp.get("user"));
//
//             List<PermittedApi> newlyAdded = new ArrayList<>();
//
//             boolean onlyFirstTimeCount = true;
//             for(PermittedApi dtl : permittedModule.getDetails()){
//
//                 for(PrmttedApiDTO api : dto.getDetails()){
//                      if(api.getId()!=null && api.getId().equals(dtl.getId())){
//                          MenuHierarchy apiAgainstModule = new MenuHierarchy();
//                          apiAgainstModule.setId(api.getApi());
//                          dtl.setApi(apiAgainstModule);
//                     }
//                      if(api.getId()==null && onlyFirstTimeCount){
//                          PermittedApi obj =new PermittedApi();
//                          obj.setPermittedModule(permittedModule);
//                          MenuHierarchy apiAgainstModule = new MenuHierarchy();
//                          apiAgainstModule.setId(api.getApi());
//                          obj.setApi(apiAgainstModule);
//                          newlyAdded.add(obj);
//                      }
//                 }
//
//                 onlyFirstTimeCount=false;
//             }
//
//             if(newlyAdded.size()>0){
//                 permittedModule.getDetails().addAll(newlyAdded);
//             }
//
//          }else{
//              permittedModule.setModuleId(dto.getModuleId());
//              permittedModule.setRole((Role) mp.get("role"));
//              permittedModule.setUser((User) mp.get("user"));
//            List<PermittedApi> apiList = new ArrayList<>();
//            for(PrmttedApiDTO api : dto.getDetails()){
//                PermittedApi dtl = new PermittedApi();
//                dtl.setPermittedModule(permittedModule);
//                MenuHierarchy apiAgainstModule = new MenuHierarchy();
//                apiAgainstModule.setId(api.getApi());
//                dtl.setApi(apiAgainstModule);
//                apiList.add(dtl);
//            }
//              permittedModule.getDetails().addAll(apiList);
//          }
//
//            permittedModuleRepository.save(permittedModule);
//
//        return new MsgResponse(dto.getId()==null?"Successfully Created ":"Successfully edited ",true);
//    }
//
//
//        public MsgResponse getMenu() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName(); // comes from the token subject
//        User user = userRepository.findByUsername(username);
//        List<Map<String,Object>> permittedModules = permittedModuleRepository.getMenu(user,user.getRoles());
//
//            Map<String,List<Map<String,Object>>> modules = new HashMap<>();
//            for(Map<String,Object> mp : permittedModules){
//                if(!modules.containsKey((String)mp.get("moduleName"))){
//                    List<Map<String,Object>> apiPatterns = new ArrayList<>();
//                    Map<String,Object> elmnt = new HashMap<>();
//                    elmnt.put("apiPattern", mp.get("apiPattern"));
//                    elmnt.put("moduleId", mp.get("moduleId"));
//                    apiPatterns.add(elmnt);
//                    modules.put((String) mp.get("moduleName"),apiPatterns);
//                }else{
//                    List<Map<String,Object>> apiPatterns = modules.get((String) mp.get("moduleName"));
//                    Map<String,Object> elmnt = new HashMap<>();
//                    elmnt.put("apiPattern", mp.get("apiPattern"));
//                    elmnt.put("moduleId", mp.get("moduleId"));
//                    apiPatterns.add(elmnt);
//                    modules.put((String) mp.get("moduleName"),apiPatterns);
//                }
//            }
//
//
//        return new MsgResponse("Found Data",modules,true);
//
//    }

}
