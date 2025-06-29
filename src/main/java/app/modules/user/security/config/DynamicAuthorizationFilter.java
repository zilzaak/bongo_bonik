package app.modules.user.security.config;



import app.modules.user.security.service.DynamicPermissionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DynamicAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private DynamicPermissionService dynamicPermissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Get permissions from the database
        Map<String, String> permissions = dynamicPermissionService.getPermissions();
        String requestURI = request.getRequestURI();
        // Check if the request URI has specific permissions
        List<String> apiParts=Arrays.asList(requestURI.split("/"));
        String rolesForUri = null;
        String tempApi=null;
          for(String apiPart : apiParts ){
              if(apiPart.isEmpty() || apiPart.trim().equals("")){
                  continue;
              }
              if(tempApi==null){
                  tempApi="/"+apiPart;
              }else{
                  tempApi=tempApi+"/"+apiPart;
              }
              for(String apiPattern : permissions.keySet()){
                    String roles=permissions.get(apiPattern);
                   if(apiPattern.equals(tempApi) || apiPattern.equals(tempApi+"/"+"**")){
                      if(rolesForUri==null){
                          rolesForUri= roles;
                      }else{
                          rolesForUri=rolesForUri+","+ roles;
                      }
                   }
                }
            }

          if (rolesForUri != null) {
            String[] requiredRoles = rolesForUri.split(",");
            boolean openUrl=false;
            for(int i=0;i<requiredRoles.length;i++){
                if(requiredRoles[i].equals("PERMIT_ALL")){
                    openUrl=true;
                    break;
                }
            }

            if(!openUrl){
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                boolean hasRole = Arrays.stream(requiredRoles)
                        .anyMatch(role -> auth.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role)));
                if (!hasRole) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return;
                }
                }
        }

        filterChain.doFilter(request, response);
    }
}
