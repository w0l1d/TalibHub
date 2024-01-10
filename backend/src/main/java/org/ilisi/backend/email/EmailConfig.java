package org.ilisi.backend.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Slf4j
public class EmailConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("sandbox.smtp.mailtrap.io"); // smtp server
        javaMailSender.setPort(587); // port
        javaMailSender.setUsername("34ad65830ec669"); // email
        javaMailSender.setPassword("26657233f844b4"); // password
        Properties javaMailProperties = javaMailSender.getJavaMailProperties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");

        return javaMailSender;
    }


    @Bean
    CommandLineRunner emailTestingCommandLineRunner(JavaMailSender javaMailSender) {
        return args -> {
            log.info("Sending Email...");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("w0l1d.dev@gmail.com");
            message.setSubject("Testing from Spring Boot");
            message.setText("Hello World \n Spring Boot Email");
            javaMailSender.send(message);
            log.info("Email Sent!");

        };

    }
}
