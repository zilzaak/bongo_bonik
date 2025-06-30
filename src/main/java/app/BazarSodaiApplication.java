package app;


import app.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class BazarSodaiApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BazarSodaiApplication.class, args);

    }

    // when first time the application run then the system by default create a user named as admin
    // and create a role as super admin
    // and admin will be assigned super_admin role who can change anything in  whole system
    // or edit anything , the admin user only created first time only.
    @Override
    public void run(String... args) throws Exception {
        this.userService.createDefaultUser();
    }
}
