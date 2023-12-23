package org.ilisi.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.*;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
class StudentProfileControllerIntegrationTests {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    static ObjectMapper objectMapper = new ObjectMapper();
    private static String JWT_TOKEN;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private InstitutRepository institutRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    //get the JWT token before each test
    @BeforeEach
    void beforeEachSetup() throws Exception {
        Student student = saveStudent("test-cne", "test-first-name", "test-last-name", "testemail@gmail.com", "test-phone", "test-cin");
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
        profileRepository.deleteAll();
        educationRepository.deleteAll();
        experienceRepository.deleteAll();
        institutRepository.deleteAll();
    }


    @Test()
    void getProfilesReturnsProfiles() throws Exception {
        // arrange
        List<Profile> givenProfiles = saveListOfValidTestProfiles();
        // act
        ResultActions test = mockMvc.perform(get("/profiles")
                .header("Authorization", "Bearer " + JWT_TOKEN));
        // assert
        test.andExpect(result -> {
            String content = result.getResponse().getContentAsString();
            assert !content.isEmpty();

            log.info("Response content: {}", content);
            // parse content to JSON
            ArrayList profiles = objectMapper.readValue(content, ArrayList.class);

            assertFalse(profiles.isEmpty());
            assertEquals(givenProfiles.size(), profiles.size());
        });
    }

    @Test
    void addEducationReturnsProfile() throws Exception {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .student(saveStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"))
                .build();
        profileRepository.save(profile);

        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().name("institut").build()).build();

        //act
        ResultActions test = mockMvc.perform(post(String.format("/profiles/%s/educations", profile.getId()))
                        .header("Authorization", "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                String.format(
                                        "{\"title\": \"%s\", \"studyField\": \"%s\", \"startAt\": \"%s\", \"endAt\": \"%s\", \"institut\": {\"name\": \"%s\"}}",
                                        educationDto.getTitle(),
                                        educationDto.getStudyField(),
                                        educationDto.getStartAt(),
                                        educationDto.getEndAt(),
                                        educationDto.getInstitut().getName())));

        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    // parse content to JSON
                    Profile profile1 = objectMapper.readValue(content, Profile.class);

                    assertFalse(profile1.getEducations().isEmpty());
                    assertEquals(1, profile1.getEducations().size());
                });
    }


    private List<Profile> saveListOfValidTestProfiles() {
        return profileRepository.saveAll(List.of(
                Profile.builder()
                        .id(UUID.randomUUID().toString())
                        .student(saveStudent("test-cne-1", "test-first-name-1", "test-last-name-1", "testemail1@gmail.com", "test-phone-1", "test-cin-1"))
                        .build(),
                Profile.builder()
                        .id(UUID.randomUUID().toString())
                        .student(saveStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"))
                        .build()
        ));
    }


    private Student saveStudent(String cne, String firstName, String lastName, String email, String phone, String cin) {
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
        return studentRepository.save(student);
    }
}
