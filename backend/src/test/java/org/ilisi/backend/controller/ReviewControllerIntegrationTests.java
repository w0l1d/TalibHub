package org.ilisi.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ReviewDto;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Review;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.ReviewRepository;
import org.ilisi.backend.repository.StudentRepository;
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
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.Year;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
class ReviewControllerIntegrationTests {

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
    private ReviewRepository reviewRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private FileSystemStorageService fileSystemStorageService;

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
        reviewRepository.deleteAll();
        institutRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void addNewReviewReturnsReview() throws Exception {
        //arrange
        Institut institut = saveInstitut();

        ReviewDto reviewDto = ReviewDto.builder()
                .institut(institut)
                .review("test-review")
                .rating(5)
                .build();

        //act
        ResultActions test = mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Review review = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });
                    log.info("Review: {}", review);
                    assertNotNull(review);
                    assertEquals(reviewDto.getReview(), review.getReview());
                });


    }


    @Test
    void updateReviewReturnsReview() throws Exception {
        //arrange
        Institut institut = saveInstitut();
        Review newReview = saveReview("test-review", 5, institut);
        ReviewDto reviewDto = ReviewDto.builder()
                .institut(institut)
                .review("test-review-update")
                .rating(2)
                .build();

        //act
        ResultActions test = mockMvc.perform(put("/reviews/" + newReview.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Review review = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });
                    log.info("Review: {}", review);
                    assertNotNull(review);
                    assertEquals(newReview.getId(), review.getId());
                    assertEquals(reviewDto.getReview(), review.getReview());
                    assertEquals(reviewDto.getRating(), review.getRating());
                });
    }

    @Test
    void updateReviewReturnsThrowsException() throws Exception {
        //arrange
        Institut institut = saveInstitut();
        saveReview("test-review", 5, institut);
        ReviewDto reviewDto = ReviewDto.builder()
                .institut(institut)
                .review("test-review-update")
                .rating(2)
                .build();

        //act
        ResultActions test = mockMvc.perform(put("/reviews/" + "wrong-id")
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        //assert
        test.andExpect(result -> {
            String content = result.getResponse().getContentAsString();
            assert !content.isEmpty();

            log.info("Response content: {}", content);
            // parse content to JSON
            Map<String, Object> errorJson = objectMapper.readValue(content, Map.class);
            assertEquals("REVIEW_NOT_FOUND", errorJson.get("errorCode"));
        });
    }

    @Test
    void deleteReviewReturnVoid() throws Exception {
        //arrange
        Institut institut = saveInstitut();
        Review newReview = saveReview("test-review", 5, institut);

        //act
        ResultActions test = mockMvc.perform(delete("/reviews/" + newReview.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));

        //assert
        test.andExpect(status().isOk());
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

    private Institut saveInstitut() {
        return institutRepository.save(Institut.builder().id("id").build());
    }

    private Review saveReview(String review, int rating, Institut institut) {
        return reviewRepository.save(
                Review.builder()
                        .review(review)
                        .rating(rating)
                        .institut(institut)
                        .build()
        );
    }

}
