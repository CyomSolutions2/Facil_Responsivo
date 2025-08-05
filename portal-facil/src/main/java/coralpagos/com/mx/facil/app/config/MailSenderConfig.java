package coralpagos.com.mx.facil.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {
	
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private Boolean starttls;
	
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private Integer puerto;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String auth;
	
	@Bean("javaMailSender")
    JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);      
        sender.setPort(puerto);                     
        sender.setUsername(username); 
        sender.setPassword(password);         

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", "true");

        return sender;
    }

}
