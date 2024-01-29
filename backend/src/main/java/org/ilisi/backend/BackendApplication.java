package org.ilisi.backend;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.config.StorageProperties;
import org.ilisi.backend.email.EmailProperties;
import org.ilisi.backend.model.Manager;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.UserRepository;
import org.ilisi.backend.security.JwtProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, EmailProperties.class, StorageProperties.class})
@Slf4j
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }


    @Bean
    @org.springframework.context.annotation.Profile("dev")
    public CommandLineRunner commandLineRunner(UserRepository userRepository,
                                               ProfileRepository profileRepository,
                                               PasswordEncoder passwordEncoder) {
        // create user
        return args -> {
            if (userRepository.findByEmail("simo@gmail.com").isEmpty()) {
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

                Profile profile = Profile
                        .builder()
                        .about("about")
                        .build();
                student.setProfile(profile);
                userRepository.save(student);
            }


            if (userRepository.findByEmail("manager@gmail.com").isPresent()) {
                return;
            }

            //create a manager
            Manager manager = new Manager();
            manager.setFirstName("mohamed");
            manager.setLastName("mohamed");
            manager.setCin("CIN2");
            manager.setEmail("manager@gmail.com");
            manager.setPhone("06123456789");
            manager.setEnabled(true);
            // encode password with BCryptPasswordEncoder
            manager.setPassword(passwordEncoder.encode("12345678"));
            userRepository.save(manager);
            log.info(String.format("User %s created", manager.getEmail()));
        };
    }
}
