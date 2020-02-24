package custom.dev.service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import custom.dev.service.model.User;
import custom.dev.service.repository.UserRepository;

@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
    UserRepository userRepository;

    @RequestMapping("/api/auth/users")
    public List<User> getUsers() {
    	System.out.println("in /users");
    	return userRepository.findAll();

    }
}