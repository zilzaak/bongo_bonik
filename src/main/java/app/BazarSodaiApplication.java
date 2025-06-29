package app;

import app.modules.user.security.entity.*;
import app.modules.user.security.repo.AuthorityPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class BazarSodaiApplication implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityPermissionRepository authorityPermissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BazarSodaiApplication.class, args);

    }

    // when first time the application run then the system by default create a user named as admin
    // and create a role as super admin
    // and admin will be assigned super_admin role who can change anything in  whole system
    // or edit anything , the admin user only created first time only.

    @Override
    public void run(String... args) throws Exception {

        Role role = new Role();
        role.setAuthority("SUPER_ADMIN");
        if(!roleRepository.existsByAuthority("SUPER_ADMIN")){
            roleRepository.saveAndFlush(role);
        }
        Set<Role> roleList = new HashSet<>();
        roleList.add(role);

        User super_admin=new User();
        super_admin.setEnabled(Boolean.TRUE);
        super_admin.setUsername("admin");
        super_admin.setPassword(passwordEncoder.encode("123456"));
        super_admin.setRoles(roleList);
        if(userRepository.count()<1){
            userRepository.saveAndFlush(super_admin);

        }

        // by default create PERMIT_ALL role so that every body can login via /auth/getToken api
        if(!roleRepository.existsByAuthority("PERMIT_ALL")){
            Role login = new Role();
            login.setAuthority("PERMIT_ALL");
            roleRepository.saveAndFlush(login);
        }

        AuthorityPermission perm = new AuthorityPermission("PERMIT_ALL","/auth/getToken");

        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("PERMIT_ALL","/auth/getToken")){
            authorityPermissionRepository.save(perm);
        }

        AuthorityPermission roleAcess=new AuthorityPermission("SUPER_ADMIN","/role/**");
        AuthorityPermission menuAcess=new AuthorityPermission("SUPER_ADMIN","/apiPerm/**");
        AuthorityPermission userReg=new AuthorityPermission("SUPER_ADMIN","/userReg/**");
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/userReg/**")){
            authorityPermissionRepository.save(userReg);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/role/**")){
            authorityPermissionRepository.save(roleAcess);
        }
        if(!authorityPermissionRepository.existsByRoleNameAndApiPattern("SUPER_ADMIN","/apiPerm/**")){
            authorityPermissionRepository.save(menuAcess);
        }

    }
}
