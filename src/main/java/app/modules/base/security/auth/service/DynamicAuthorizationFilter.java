package app.modules.base.security.auth.service;



import app.modules.base.role.entity.Role;
import app.modules.base.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class DynamicAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private DynamicPermissionService dynamicPermissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User  user  = null;
        String username=null;
        if(auth!=null){
            user  = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            username=user.getUsername();
        }


           String requestURI = request.getRequestURI();
           boolean noNeedAuthenticationUrl=false;
            Map<String, String> permissions = dynamicPermissionService.getPermissions();
            List<String> apiParts=Arrays.asList(requestURI.split("/"));
            String rolesOrUserToAccessUri = null;
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
                    String data=permissions.get(apiPattern);
                    if(apiPattern.equals(tempApi) || apiPattern.equals(tempApi+"/"+"**")){
                        if(rolesOrUserToAccessUri==null){
                            rolesOrUserToAccessUri= data;
                        }else{
                            rolesOrUserToAccessUri=rolesOrUserToAccessUri+","+ data;
                        }
                    }
                }
            }

            if (rolesOrUserToAccessUri != null) {
                List<String> perms  = Arrays.asList(rolesOrUserToAccessUri.split(","));
                  if(perms.contains("PERMIT_ALL")){
                        noNeedAuthenticationUrl=true;
                    }

                if(!noNeedAuthenticationUrl){
                    boolean hasRole = false;
                    for(GrantedAuthority au : user.getAuthorities()){
                        if(perms.contains(au.getAuthority())){
                            hasRole=true;
                            break;
                        }
                    }
                    boolean hasUser=perms.contains(username);
                    if (!hasRole && !hasUser ) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                        return;
                    }
                }
            }
        System.out.println("your requested uri is "+requestURI+"  no permission need for this url ?? "+(noNeedAuthenticationUrl?"yes":"no"));
              if(rolesOrUserToAccessUri == null && !noNeedAuthenticationUrl){
                  response.sendError(HttpServletResponse.SC_FORBIDDEN, "No role or permission found against the requested url");
                  return;
              }

            filterChain.doFilter(request, response);

    }
}
