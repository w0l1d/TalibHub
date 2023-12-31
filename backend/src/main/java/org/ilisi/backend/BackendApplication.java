package org.ilisi.backend;

import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(org.ilisi.backend.security.JwtProperties.class)
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }



    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository,
                                               PasswordEncoder passwordEncoder) {
        // create user
        return args -> {
            if (userRepository.findByEmail("simo@gmail.com").isPresent()) {
                return;
            }
            Student student = new Student();
            student.setFirstName("mohamed");
            student.setLastName("mohamed");
            student.setCne("CNE");
            student.setCin("CIN");
            student.setEmail("simo@gmail.com");
            student.setPhone("0612345678");
            student.setBirthDate(java.time.LocalDate.now());
            student.setEnrollmentYear(java.time.Year.now());
            student.setEnabled(true);
            // encode password with BCryptPasswordEncoder
            student.setPassword(passwordEncoder.encode("12345678"));
            userRepository.save(student);
        };
    }
}
