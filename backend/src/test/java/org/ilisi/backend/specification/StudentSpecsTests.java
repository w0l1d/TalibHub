package org.ilisi.backend.specification;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.*;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.ilisi.backend.specs.StudentSpecifications;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Year;
import java.time.YearMonth;
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
    @Autowired
    private InstitutRepository institutRepository;


    @Test
    void searchByString_whenKeywordMatchesFirstName_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("yassin");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentYassin));
        assertFalse(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordMatchesLastName_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("Jrayfy");
        List<Student> students = studentRepository.findAll(Specification.where(spec));
        log.info("students: {}", students);
        Assertions.assertAll(
                () -> assertTrue(students.contains(studentYassin)),
                () -> assertFalse(students.contains(studentAnass))
        );
    }

    @Test
    void searchByString_whenKeywordMatchesEmail_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("ilisi.com");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentYassin));
        assertTrue(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordMatchesEnrollmentYear_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("2022");
        List<Student> students = studentRepository.findAll(spec);
        log.info("*********************************************");
        log.info("students: {}", students);
        log.info("*********************************************");
        assertFalse(students.contains(studentYassin));
        assertTrue(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordDoesNotMatchAnyField_returnsEmptyList() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("nonexistent keyword");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.isEmpty());
    }


    @Test
    void searchByString_whenKeywordMatchesAboutInProfile_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("software engineer");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentYassin));
        assertFalse(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordMatchesEducationField_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("information technology");
        List<Student> students = studentRepository.findAll(spec);
        log.info("*********************************************");
        log.info("students: {}", students);
        log.info("*********************************************");
        assertTrue(students.contains(studentYassin));
        assertFalse(students.contains(studentAnass));
    }

    @Test
    void searchByString_whenKeywordMatchesExperienceField_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("Developed features");
        List<Student> students = studentRepository.findAll(spec);
        log.info("*********************************************");
        log.info("ALL students: {}", studentRepository.findAll());
        log.info("students: {}", students);
        log.info("*********************************************");
        assertTrue(students.contains(studentAnass));
        assertFalse(students.contains(studentYassin));
    }

    @Test
    void searchByString_whenKeywordMatchesMultipleFields_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("computer science");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentAnass));
        assertFalse(students.contains(studentYassin));
    }

    @Test
    void searchByString_whenKeywordMatchesPartialEmail_returnsStudents() {
        Specification<Student> spec = StudentSpecifications.hasKeyword("ilisi");
        List<Student> students = studentRepository.findAll(spec);
        log.info("students: {}", students);
        assertTrue(students.contains(studentYassin));
        assertTrue(students.contains(studentAnass));
    }

    /*
     * This method is used to initialize test data before each test.
     * It is annotated with @Bean and @Test so that it is run before the tests.
     * It is static so that it can be run before the application context is created.
     * It is also annotated with @Transactional so that the data is rolled back after each test.
     * This ensures that the data is in a clean state before each test.
     * The method returns a CommandLineRunner, which is a functional interface that is used to run code at application startup.
     */
    @BeforeEach
    public void initTestData() {
        // create Institut A
        var institutA = new Institut();
        institutA.setName("Tech University");
        institutA.setWebsite("https://techuniversity.edu");
        institutA = institutRepository.save(institutA);

        // create Institut B
        var institutB = new Institut();
        institutB.setName("Science Institute");
        institutB.setWebsite("https://scienceinstitute.org");
        institutB = institutRepository.save(institutB);

        // create Institut C
        var institutC = new Institut();
        institutC.setName("Engineering College");
        institutC.setWebsite("https://engineeringcollege.com");
        institutC = institutRepository.save(institutC);

        // create Institut D
        var institutD = new Institut();
        institutD.setName("Art and Design School");
        institutD.setWebsite("https://artanddesignschool.edu");
        institutD = institutRepository.save(institutD);


        // create Yassine student and his education and experience
        var student1 = new Student();
        student1.setFirstName("yassine");
        student1.setLastName("Jrayfy");
        student1.setEmail("yassine-jrayfi@ilisi.com");
        student1.setCne("R1529635888");
        student1.setCin("EE123456");
        student1.setEnrollmentYear(Year.of(2021));
        student1.setBirthDate(java.time.LocalDate.of(2001, 12, 12));
        student1.setPhone("0612345678");
        Profile e1 = new Profile();
        e1.setAbout("I am a software engineer.");
        e1.setStudent(student1);
        student1.setProfile(e1);


        var education1 = new Education();
        education1.setInstitut(institutA);
        education1.setTitle("Master's Degree");
        education1.setStudyField("Information Technology");
        education1.setStartAt(YearMonth.of(2017, 8));
        education1.setEndAt(YearMonth.of(2021, 5));

        var experience1 = new Experience();
        experience1.setTitle("Software Engineer");
        experience1.setInstitut(institutB);
        experience1.setStartAt(YearMonth.of(2020, 6));
        experience1.setEndAt(YearMonth.of(2022, 12));
        experience1.setDescription("Led a team in developing scalable software solutions.");

        e1.setEducations(List.of(education1));
        e1.setExperiences(List.of(experience1));

        studentYassin = studentRepository.save(student1);

        // create Anass student and his education and experience
        var student2 = new Student();
        student2.setFirstName("anass");
        student2.setLastName("Kabila");
        student2.setEmail("anass-kabila@ilisi.com");
        student2.setCne("T859619499");
        student2.setCin("SR123456");
        student2.setEnrollmentYear(Year.of(2022));
        student2.setBirthDate(java.time.LocalDate.of(2002, 12, 12));
        student2.setPhone("0612348898");
        Profile e2 = new Profile();
        e2.setAbout("I am a computer science student.");
        student2.setProfile(e2);

        var education2 = new Education();
        education2.setInstitut(institutC);
        education2.setTitle("Bachelor's Degree");
        education2.setStudyField("Computer Science");
        education2.setStartAt(YearMonth.of(2019, 9));
        education2.setEndAt(YearMonth.of(2023, 6));
        education2.setDescription("Studied computer science. Learned about algorithms, data structures, and databases.");


        var experience2 = new Experience();
        experience2.setTitle("Software Developer Intern");
        experience2.setInstitut(institutD);
        experience2.setStartAt(YearMonth.of(2022, 6));
        experience2.setEndAt(YearMonth.of(2022, 8));
        experience2.setDescription("Developed features for a mobile application.");

        e2.setEducations(List.of(education2));
        e2.setExperiences(List.of(experience2));

        studentAnass = studentRepository.save(student2);
    }

    @AfterEach
    public void cleanTestData() {
        studentRepository.deleteAll();
    }

}