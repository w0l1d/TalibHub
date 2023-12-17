package org.ilisi.backend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /*@Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            studentRepository.save(Student.builder().firstName("John").lastName("Doe").build());
            studentRepository.save(Student.builder().firstName("Jane").lastName("Doe").build());
            studentRepository.save(Student.builder().firstName("Jack").lastName("Doe").build());
        };
    }*/

}
