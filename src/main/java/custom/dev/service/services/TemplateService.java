package custom.dev.service.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class TemplateService {
	
    @GetMapping("/template")
    public ResponseEntity<String> authenticateUser() {
 
    	System.out.println("in /template");
    	    	
        return ResponseEntity.ok("{\"message\":\"template-here\"}");
    }

}
