package custom.dev.service.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import custom.dev.service.model.User;

@RestController
@CrossOrigin
public class UserController {

    @RequestMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody User user) {

        System.out.println("Authenticating");
        System.out.println(user);
        System.out.println("--------------");

        if (user.getUsername().equals("user") && user.getPassword().equals("password")) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}