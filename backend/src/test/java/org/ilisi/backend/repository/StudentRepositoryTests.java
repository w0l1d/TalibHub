package org.ilisi.backend.repository;

import org.ilisi.backend.model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StudentRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;
    @Test
    public void StudentRepository_saveStudents_ReturnsStudents() {

        //Arrange
        Student student1 = createStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Student student2 = createStudent("CNE2", "firstName2", "lastName2", "email2", "phone2", "CIN2");
        Student student3 = createStudent("CNE3", "firstName3", "lastName3", "email3", "phone3", "CIN3");
        List<Student> students = List.of(student1, student2, student3);

        //act
        List<Student> savedStudents = studentRepository.saveAll(students);

        //assert
        Assertions.assertFalse(savedStudents.isEmpty());
        Assertions.assertEquals(students.size(), savedStudents.size());
    }


    private static Student createStudent(String cne, String firstName, String lastName, String email, String phone, String cin) {
        Student student = new Student();
        student.setCne(cne);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setCin(cin);
        student.setEnrollmentYear(Year.now());
        student.setBirthDate(LocalDate.now());
        student.setPassword("password");
        return student;
    }
}
