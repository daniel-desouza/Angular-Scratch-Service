package custom.dev.service.services;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class ThymeleafEmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(String subject, String to, String replacementVariable) {
    	        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("replacementVariable", replacementVariable);
        
        MimeMessage message1 = emailSender.createMimeMessage();
        MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message1,
			        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
			        StandardCharsets.UTF_8.name());

	        Context context = new Context();
	        context.setVariables(model);
	        String html = templateEngine.process("thymeleaf-template", context);
	        
	        helper.setTo(to);
	        helper.setText(html, true);
	        helper.setSubject(subject);
	        helper.setFrom("scratch-app@DO-NOT-REPLY.com");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
        emailSender.send(message1);
    }
}
