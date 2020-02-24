package custom.dev.service.services;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@SuppressWarnings("deprecation")
@Service
public class VelocityEmailService {
	
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
	private VelocityEngine velocityEngine;
 
    public void sendEmail(String subject, String to, String replacementVariable) {
    	
        MimeMessagePreparator preparator = getMessagePreparator(subject, to, "scratch-app@DO-NOT-REPLY.com", replacementVariable);
        
        try {
            emailSender.send(preparator);
            System.out.println("Message has been sent.............................");
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
 
    private MimeMessagePreparator getMessagePreparator(String subject, String to, String from, String replacementVariable){
         
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
 
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
  
                helper.setSubject(subject);
                helper.setFrom(from);
                helper.setTo(to);
      
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("replacementVariable", replacementVariable);
                 
                String text = geVelocityTemplateContent(model);
 
                // use the true flag to indicate you need a multipart message
                helper.setText(text, true);
 
            }
        };
        return preparator;
    }
    
    public String geVelocityTemplateContent(Map<String, Object> model){
    	StringBuffer content = new StringBuffer();
        try{
            content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/velocity-template.vm", model));
            return content.toString();
        }catch(Exception e){
            System.out.println("Exception occured while processing velocity template:"+e.getMessage());
        }
        return "";
    }
}