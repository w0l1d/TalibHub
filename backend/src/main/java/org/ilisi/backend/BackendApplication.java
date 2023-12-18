package org.ilisi.backend;


import org.ilisi.backend.entity.UserRole;
import org.ilisi.backend.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /*@Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            //create two roles USER,ADMIN
            UserRole userRole = new UserRole("1","USER");
        };
    }*/

}
