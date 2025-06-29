package app.modules.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PassEncodeClss {
       @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
        }
        // Add your security configuration here

}
