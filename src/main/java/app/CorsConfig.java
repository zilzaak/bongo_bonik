package app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/auth/**")
//                .allowedOrigins("http://localhost:4200")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .exposedHeaders("Authorization") // if you use JWT tokens
//                .allowCredentials(true); // if sending cookies/tokens

        registry.addMapping("/**")  // Apply to all endpoints
                .allowedOriginPatterns(
                        "http://localhost:4200",
                        "http://localhost:4201",
                        "http://localhost:4202",
                        "http://localhost:4203",
                        "http://localhost:4204"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}