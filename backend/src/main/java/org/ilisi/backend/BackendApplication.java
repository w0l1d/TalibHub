package org.ilisi.backend;

import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /*@Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository ) {
        return args -> {
            Student student = new Student();
            student.setFirstName("Mohamed");
            student.setLastName("Bouhlel");
            student.setCne("CNE123456");
            student.setCin("CIN123456");
            student.setEmail("test@gmail.com");
            student.setPassword("password");
            student.setPhone("0612345678");
            student.setBirthDate(java.time.LocalDate.now());
            student.setEnrollmentYear(java.time.Year.now());
            studentRepository.save(student);
        };
    }*/

}
