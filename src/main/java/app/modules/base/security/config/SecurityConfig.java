package app.modules.base.security.config;

import app.common.util.CommonUtil;
import app.modules.base.security.auth.service.CustomUserDetailsService;
import app.modules.base.security.auth.service.DynamicAuthorizationFilter;
import app.modules.base.security.auth.service.DynamicPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private DynamicPermissionService dynamicPermissionService;

    // Expose AuthenticationManager as a Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // HTTP Security configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {


                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                     String x = null;
                     Set<String> y = Collections.emptySet();

                    if (auth != null && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
                        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
                        x = user.getUsername();
                        y = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                    }
                   final String userId = x;
                   final Set<String> userRoles = y;



                    Map<String, String> permissions = dynamicPermissionService.getPermissions();
                    permissions.forEach((key, value) -> {
                        if ("PERMIT_ALL".equals(value)) {
                            authorize.requestMatchers(key).permitAll();
                        } else {
                            List<String> accessRules = Arrays.asList(value.split(","));
                            authorize.requestMatchers(key).access((authentication, object) -> {
                                boolean hasAccess = false;
                                if(accessRules.contains(userId)){
                                    hasAccess=true;
                                }
                                for(String role : userRoles){
                                    if(accessRules.contains(role)){
                                        hasAccess=true;
                                        break;
                                    }
                                }
                                return new AuthorizationDecision(hasAccess);
                            });
                        }
                    });
                    authorize.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(dynamicAuthorizationFilter(), JwtRequestFilter.class);

        return http.build();
    }

    // Create a bean for DynamicAuthorizationFilter
    @Bean
    public DynamicAuthorizationFilter dynamicAuthorizationFilter() {
        return new DynamicAuthorizationFilter();
    }
}


