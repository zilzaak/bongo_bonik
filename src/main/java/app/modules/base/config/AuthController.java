package app.modules.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/getToken")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            authenticationManager.authenticate(authToken);
            final String jwt = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}

