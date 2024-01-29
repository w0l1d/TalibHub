package org.ilisi.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.email.EmailService;
import org.ilisi.backend.model.Manager;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.ilisi.backend.repository.UserRepository;
import org.ilisi.backend.service.FileSystemStorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
class StudentControllerIntegrationTests {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    private static String JWT_TOKEN;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @MockBean
    private FileSystemStorageService fileSystemStorageService;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void beforeEachSetup() throws Exception {
        Manager manager = createManager("test-cne", "test-first-name", "test-last-name", "testemail@gmail.com", "test-phone", "test-cin");
        userRepository.save(manager);
        String tokens = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", manager.getEmail(),
                                "password", "123456789"
                        ))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info("Tokens: {}", tokens);
        JsonNode tokensJson = objectMapper.readTree(tokens);
        Map<String, Object> tokensMap = objectMapper.convertValue(tokensJson, new TypeReference<>() {
        });
        JWT_TOKEN = (String) tokensMap.get("accessToken");

    }

    @AfterEach
    void afterEachSetup() {
        cleanDatabase();
    }

    void cleanDatabase() {
        profileRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void createStudentsReturnStudents() throws Exception {

        //arrange
        Student student1 = createStudent("CNE1", "firstName1", "lastName1", "email1@gmail.com", "phone1", "CIN1");
        Student student2 = createStudent("CNE2", "firstName2", "lastName2", "email2@gmail.com", "phone2", "CIN2");

        String studentJson = mockMvc.perform(post("/students/saveAll")
                        .header("Authorization", "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(student1, student2))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //act
        JsonNode jsonNode = objectMapper.readTree(studentJson);
        List<Student> studentsList = objectMapper.convertValue(jsonNode, new TypeReference<List<Student>>() {
        });

        //assert
        assertFalse(studentsList.isEmpty());
        assertEquals(2, studentsList.size());
    }

    @Test
    void createStudentsThrowsException() throws Exception {

        //arrange
        Student student1 = createStudent("CNE1", "firstName1", "lastName1", "email@gmail.com", "phone1", "CIN");
        Student student2 = createStudent("CNE2", "firstName2", "lastName2", "email@gmail.com", "phone2", "CIN");

        //act
        String studentJson = mockMvc.perform(post("/students/saveAll")
                        .header("Authorization", "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(student1, student2))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //assert exception
        JsonNode jsonNode = objectMapper.readTree(studentJson);
        String exceptionMessage = jsonNode.get("message").asText();
        assertFalse(exceptionMessage.isEmpty());
    }


    private Student createStudent(String cne, String firstName, String lastName, String email, String phone, String cin) {
        Student student = new Student();
        student.setCne(cne);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setCin(cin);
        student.setEnrollmentYear(Year.now());
        student.setBirthDate(LocalDate.now());
        student.setEnabled(true);
        student.setPassword(passwordEncoder.encode("123456789"));
        return student;
    }

    private Manager createManager(String cne, String firstName, String lastName, String email, String phone, String cin) {
        Manager manager = new Manager();
        manager.setFirstName(firstName);
        manager.setLastName(lastName);
        manager.setEmail(email);
        manager.setPhone(phone);
        manager.setCin(cin);
        manager.setEnabled(true);
        manager.setPassword(passwordEncoder.encode("123456789"));
        return manager;
    }

}
