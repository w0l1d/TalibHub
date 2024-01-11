package org.ilisi.backend.specification;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.StudentRepository;
import org.ilisi.backend.specs.StudentSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@Slf4j
class StudentSpecsTests {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest");
    private static Student studentYassin;
    private static Student studentAnass;
    @Autowired
    private StudentRepository studentRepository;

    @Test
    void searchByString_whenKeywordMatchesFirstName_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.searchByString("yassin");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentYassin));
        assertFalse(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordMatchesLastName_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.searchByString("Jrayfy");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentYassin));
        assertFalse(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordMatchesEmail_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.searchByString("ilisi.com");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentYassin));
        assertTrue(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordMatchesEnrollmentYear_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.searchByString("2022");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertFalse(students.contains(studentYassin));
        assertTrue(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordDoesNotMatchAnyField_returnsEmptyList() {
        Specification<Student> spec = StudentSpecifications.searchByString("nonexistent keyword");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.isEmpty());
    }


    /*
     * This method is used to initialize test data before each test.
     * It is annotated with @Bean and @Test so that it is run before the tests.
     * It is static so that it can be run before the application context is created.
     * It is also annotated with @Transactional so that the data is rolled back after each test.
     * This ensures that the data is in a clean state before each test.
     * The method returns a CommandLineRunner, which is a functional interface that is used to run code at application startup.
     */
    @Bean
    @Transactional
    CommandLineRunner initTestData(StudentRepository studentRepository) {
        return args -> {
            var student1 = new Student();
            student1.setFirstName("yassine");
            student1.setLastName("Jrayfy");
            student1.setEmail("yassine-jrayfi@ilisi.com");
            student1.setCne("R1529635888");
            student1.setCin("EE123456");
            student1.setEnrollmentYear(Year.of(2021));
            student1.setBirthDate(java.time.LocalDate.of(2001, 12, 12));
            student1.setPhone("0612345678");

            var student2 = new Student();
            student2.setFirstName("anass");
            student2.setLastName("Kabila");
            student2.setEmail("anass-kabila@ilsi.com");
            student2.setCne("T859619499");
            student2.setCin("SR123456");
            student2.setEnrollmentYear(Year.of(2022));
            student2.setBirthDate(java.time.LocalDate.of(2002, 12, 12));
            student2.setPhone("0612348898");


            studentYassin = studentRepository.save(student1);
            studentAnass = studentRepository.save(student2);
        };
    }

}