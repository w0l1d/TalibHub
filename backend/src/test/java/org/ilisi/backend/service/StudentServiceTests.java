package org.ilisi.backend.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.ilisi.backend.email.EmailService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Set;

@Slf4j
class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createStudentsReturnsStudents() {

        //arrange
        Student student1 = createStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Student student2 = createStudent("CNE2", "firstName2", "lastName2", "email2", "phone2", "CIN2");

        List<Student> students = List.of(student1, student2);
        List<Profile> profiles = List.of(Profile.builder().student(student1).build(), Profile.builder().student(student2).build());
        //act
        Mockito.when(studentRepository.saveAll(students)).thenReturn(students);
        Mockito.when(profileRepository.saveAll(profiles)).thenReturn(profiles);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");
        List<Student> result = studentService.createStudents(students);
        //assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void createStudentsThrowsException() {

        //arrange
        Student student1 = createStudent("CNE1", "firstName1", "lastName1", "email@gmail.com", "phone1", "CIN");
        Student student2 = createStudent("CNE2", "firstName2", "lastName2", "email@gmail.com", "phone2", "CIN");

        List<Student> students = List.of(student1, student2);
        List<Profile> profiles = List.of(Profile.builder().student(student1).build(), Profile.builder().student(student2).build());
        //act
        //set violation of email and cin ( uniques )
        Set<ConstraintViolation<Student>> violations = Set.of(
                createConstraintViolation(student1, "email"),
                createConstraintViolation(student2, "cin")
        );
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Student Already Exists", violations);
        Mockito.when(studentRepository.saveAll(students)).thenThrow(constraintViolationException);
        Mockito.when(profileRepository.saveAll(profiles)).thenReturn(profiles);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");


        //assert
        ConstraintViolationException exception = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> studentService.createStudents(students)
        );
        //assert that the exception contains the expected violations
        Assertions.assertEquals(2, exception.getConstraintViolations().size());
        Assertions.assertTrue(exception.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("email")));
        Assertions.assertTrue(exception.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("cin")));
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

    private ConstraintViolation<Student> createConstraintViolation(Student rootBean,
                                                                   String propertyPath) {
        return new ConstraintViolationImpl<>("Student Already Exists", rootBean, null, propertyPath, Student.class, null);
    }

    private static class ConstraintViolationImpl<T> implements ConstraintViolation<T> {
        private final String message;
        private final T rootBean;
        private final String propertyPath;

        public ConstraintViolationImpl(String message, T rootBean, Object o, String propertyPath, Class<T> studentClass, Object o1) {
            this.message = message;
            this.rootBean = rootBean;
            this.propertyPath = propertyPath;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public T getRootBean() {
            return rootBean;
        }

        @Override
        public Class<T> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return propertyPath != null ? PathImpl.createPathFromString(propertyPath) : null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> aClass) {
            return null;
        }
    }


}
