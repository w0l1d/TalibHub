package org.ilisi.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Slf4j
public class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createStudentsReturnsStudents() {

        //arrange
        Student student1 = createStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Student student2 = createStudent("CNE2", "firstName2", "lastName2", "email2", "phone2", "CIN2");

        List<Student> students = List.of(student1, student2);
        List<Profile> profiles = List.of(Profile.builder().student(student1).build(), Profile.builder().student(student2).build());
        //act
        Mockito.when(studentRepository.saveAll(students)).thenReturn(students);
        Mockito.when(profileRepository.saveAll(profiles)).thenReturn(profiles);
        List<Student> result = studentService.createStudents(students);
        //assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
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
