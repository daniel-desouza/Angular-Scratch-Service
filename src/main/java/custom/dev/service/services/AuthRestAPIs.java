package custom.dev.service.services;

import java.util.HashSet;
import java.util.Set;
 
import javax.validation.Valid;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import custom.dev.service.services.request.LoginForm;
import custom.dev.service.services.request.RegisterForm;
import custom.dev.service.services.response.JwtResponse;
import custom.dev.service.model.Role;
import custom.dev.service.model.RoleName;
import custom.dev.service.model.User;
import custom.dev.service.repository.RoleRepository;
import custom.dev.service.repository.UserRepository;
import custom.dev.service.security.JwtProvider;
 
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
 
    @Autowired
    AuthenticationManager authenticationManager;
 
    @Autowired
    UserRepository userRepository;
 
    @Autowired
    RoleRepository roleRepository;
 
    @Autowired
    PasswordEncoder encoder;
 
    @Autowired
    JwtProvider jwtProvider;
 
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
 
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
 
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
 
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterForm signUpRequest) {
    	System.out.println("in /register");
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("{ \"message\": \"Username is already taken\"}",
                    HttpStatus.BAD_REQUEST);
        }
 
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("{ \"message\": \"Email is already in use\"}",
                    HttpStatus.BAD_REQUEST);
        }
 
        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
 
        String rolex = signUpRequest.getRole();
        
        Set<String> strRoles = new HashSet<>();
        strRoles.add(rolex);
        
        Set<Role> roles = new HashSet<>();
 
        strRoles.forEach(role -> {
          switch(role) {
          case "admin":
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                  .orElseThrow(() -> new RuntimeException("{ \"message\": \"Role provided was not found\"}"));
            roles.add(adminRole);
            
            break;
          case "pm":
                Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                  .orElseThrow(() -> new RuntimeException("{ \"message\": \"Role provided was not found\"}"));
                roles.add(pmRole);
                
            break;
          default:
              Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                  .orElseThrow(() -> new RuntimeException("{ \"message\": \"Role provided was not found\"}"));
              roles.add(userRole);
          }
        });
        
        user.setRoles(roles);
        userRepository.save(user);
        
        return ResponseEntity.ok().body("{ \"message\": \"User registration successful\"}");
    }
}