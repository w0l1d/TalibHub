package org.ilisi.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ReviewDto;
import org.ilisi.backend.model.*;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
public class ReviewControllerIntegrationTests {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    private static String JWT_TOKEN;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private InstitutRepository institutRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void beforeEachSetup() throws Exception {
        //studentRepository.deleteAll();
        Student student = getSampleStudent("test-cne", "test-first-name", "test-last-name", "testemail@gmail.com", "test-phone", "test-cin");
        student = studentRepository.save(student);
        String tokens = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", student.getEmail(),
                                "password", "123456789"
                        ))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info("Tokens: {}", tokens);
        Map<String, Object> tokensMap = objectMapper.readValue(tokens, Map.class);
        JWT_TOKEN = (String) tokensMap.get("accessToken");
    }

    @AfterEach
    void afterEachSetup() {
        cleanDatabase();
    }

    void cleanDatabase() {
        studentRepository.deleteAll();
        institutRepository.deleteAll();
    }

    @Test
    void addNewReviewReturnsReview() throws Exception {
        //arrange
        Institut institut = Institut.builder().id("id").build();
        institutRepository.save(institut);

        ReviewDto reviewDto = ReviewDto.builder()
                .institut(institut)
                .review("test-review")
                .rating(5)
                .build();

        //act
        ResultActions test = mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "institut", Map.of(
                                        "id", institut.getId()
                                ),
                                "review", reviewDto.getReview(),
                                "rating", reviewDto.getRating()
                        ))));

        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Review review = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });
                    assertNotNull(review);
                    assertEquals(institut.getId(), review.getInstitut().getId());
                    assertEquals("test-cin", review.getStudent().getCin());
                });


    }

    private Student getSampleStudent(String cne, String firstName, String lastName, String email, String phone, String cin) {
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

}
