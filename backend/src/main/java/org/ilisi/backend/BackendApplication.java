package org.ilisi.backend;

import org.ilisi.backend.entity.Student;
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

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            studentRepository.save(Student.builder().firstName("John").lastName("Doe").build());
            studentRepository.save(Student.builder().firstName("Jane").lastName("Doe").build());
            studentRepository.save(Student.builder().firstName("Jack").lastName("Doe").build());
        };
    }

}
