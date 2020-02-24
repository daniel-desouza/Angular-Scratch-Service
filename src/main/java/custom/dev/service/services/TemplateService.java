package custom.dev.service.services;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import freemarker.template.TemplateException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class TemplateService {
	
	@Autowired
	VelocityEmailService velocityEmailService;

	@Autowired
	ThymeleafEmailService thymeleafEmailService;

	@Autowired
	FreemarkerEmailService freemarkerEmailService;

	@GetMapping("/template")
    public ResponseEntity<String> fetchTemplate() {
 
    	System.out.println("in /template");
    	    	
        return ResponseEntity.ok("{\"message\":\"template-here\"}");
    }
	
	@GetMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String replacementVariable, @RequestParam String templateEngine) {
		
		switch (templateEngine) {
			case "velocity":
				velocityEmailService.sendEmail(subject, to, replacementVariable);
				break;
			case "freemarker":
				try {
					freemarkerEmailService.sendEmail(subject, to, replacementVariable);
				} catch (MessagingException | IOException | TemplateException e) {
					e.printStackTrace();
				}
				break;
			case "thymeleaf":
				thymeleafEmailService.sendEmail(subject, to, replacementVariable);
				break;
			default:
				break;
		}
		
    	    	
        return ResponseEntity.ok("{\"message\":\"sent\"}");
    }

}
