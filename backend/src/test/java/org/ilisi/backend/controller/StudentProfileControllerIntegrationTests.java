package org.ilisi.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.model.*;
import org.ilisi.backend.repository.*;
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
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
class StudentProfileControllerIntegrationTests {

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
    private EducationRepository educationRepository;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private InstitutRepository institutRepository;
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
        profileRepository.deleteAll();
        studentRepository.deleteAll();
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
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemailed@gmail.com", "test-phone-2", "test-cin-2"));
        EducationDto educationDto = createEducationDto("title", "studyField", YearMonth.of(2019, 1), YearMonth.of(2020, 1), Institut.builder().name("institut").build());

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
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Profile profile1 = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });
                    List<Education> educations = profile1.getEducations();
                    assertFalse(educations.isEmpty());
                    assertEquals(1, educations.size());
                });
    }

    @Test
    void addEducationWithExistingInstitutReturnsProfile() throws Exception {

        //arrange
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"));
        Institut institut = saveInstitut("institut");
        EducationDto educationDto = createEducationDto("title", "studyField", YearMonth.of(2019, 1), YearMonth.of(2020, 1), institut);
        //act
        ResultActions test = mockMvc.perform(post(String.format("/profiles/%s/educations", profile.getId()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        String.format(
                                "{\"title\": \"%s\", \"studyField\": \"%s\", \"startAt\": \"%s\", \"endAt\": \"%s\", \"institut\": {\"id\": \"%s\"}}",
                                educationDto.getTitle(),
                                educationDto.getStudyField(),
                                educationDto.getStartAt(),
                                educationDto.getEndAt(),
                                educationDto.getInstitut().getId())));

        //assert
        test.andExpectAll(status().isOk(),
                result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Profile profile1 = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });


                    assertFalse(profile1.getEducations().isEmpty());
                    assertEquals(1, profile1.getEducations().size());
                    assertEquals(institut.getId(), profile1.getEducations().get(0).getInstitut().getId());
                    assertEquals(institut.getName(), profile1.getEducations().get(0).getInstitut().getName());
                });

    }

    @Test
    void addEducationWithInExistingProfileThrowsException() throws Exception {

        //arrange
        EducationDto educationDto = createEducationDto("title", "studyField", YearMonth.of(2019, 1), YearMonth.of(2020, 1), Institut.builder().name("institut").build());

        //act
        ResultActions test = mockMvc.perform(post(String.format("/profiles/%s/educations", UUID.randomUUID()))
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
        test.andExpect(result -> {
            String content = result.getResponse().getContentAsString();
            assert !content.isEmpty();

            log.info("Response content: {}", content);
            // parse content to JSON
            Map<String, Object> errorJson = objectMapper.readValue(content, Map.class);
            assertEquals("PROFILE_NOT_FOUND", errorJson.get("errorCode"));
        });
    }

    @Test
    void updateEducationReturnsProfile() throws Exception {

        //arrange
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"));
        Institut institut = saveInstitut("institut");
        Education education = Education.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(institut)
                .build();
        profile.setEducations(List.of(education));
        profileRepository.save(profile);

        //act
        ResultActions test = mockMvc.perform(put(String.format("/profiles/%s/educations/%s", profile.getId(), education.getId()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        String.format(
                                "{\"title\": \"%s\", \"studyField\": \"%s\", \"startAt\": \"%s\", \"endAt\": \"%s\", \"institut\": {\"id\": \"%s\"}}",
                                "title2",
                                "studyField2",
                                YearMonth.of(2019, 1),
                                YearMonth.of(2020, 1),
                                institut.getId())));

        //assert
        test.andExpectAll(status().isOk(),
                result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Profile profile1 = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertFalse(profile1.getEducations().isEmpty());
                    assertEquals(1, profile1.getEducations().size());
                    assertEquals("title2", profile1.getEducations().get(0).getTitle());
                    assertEquals("studyField2", profile1.getEducations().get(0).getStudyField());
                });
    }

    @Test
    void deleteEducationReturnsProfile() throws Exception {

        //arrange
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"));
        Institut institut = saveInstitut("institut");
        Education education = educationRepository.save(Education.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(institut)
                .build());
        profile.setEducations(List.of(education));
        profileRepository.save(profile);

        //act
        ResultActions test = mockMvc.perform(delete(String.format("/profiles/%s/educations/%s", profile.getId(), education.getId()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));

        //assert
        test.andExpectAll(status().isOk(),
                result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Profile profile1 = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertTrue(profile1.getEducations().isEmpty());
                });
    }

    @Test
    void addExperienceReturnsProfile() throws Exception {

        //arrange
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"));
        ExperienceDto experienceDto = createExperienceDto("title", "location", YearMonth.of(2019, 1), YearMonth.of(2020, 1), Institut.builder().name("institut").build());

        //act
        ResultActions test = mockMvc.perform(post(String.format("/profiles/%s/experiences", profile.getId()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        String.format(
                                "{\"title\": \"%s\", \"location\": \"%s\", \"startAt\": \"%s\", \"endAt\": \"%s\", \"institut\": {\"name\": \"%s\"}}",
                                experienceDto.getTitle(),
                                experienceDto.getLocation(),
                                experienceDto.getStartAt(),
                                experienceDto.getEndAt(),
                                experienceDto.getInstitut().getName())));

        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    // parse content to JSON
                    Map profileJson = objectMapper.readValue(content, Map.class);
                    List<Experience> experiences = (List<Experience>) profileJson.get("experiences");

                    assertFalse(experiences.isEmpty());
                    assertEquals(1, experiences.size());
                });
    }

    @Test
    void addExperienceWithExistingInstitutReturnsProfile() throws Exception {

        //arrange
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"));
        Institut institut = saveInstitut("institut");
        ExperienceDto experienceDto = createExperienceDto("title", "location", YearMonth.of(2019, 1), YearMonth.of(2020, 1), institut);
        //act
        ResultActions test = mockMvc.perform(post(String.format("/profiles/%s/experiences", profile.getId()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        String.format(
                                "{\"title\": \"%s\", \"location\": \"%s\", \"startAt\": \"%s\", \"endAt\": \"%s\", \"institut\": {\"id\": \"%s\"}}",
                                experienceDto.getTitle(),
                                experienceDto.getLocation(),
                                experienceDto.getStartAt(),
                                experienceDto.getEndAt(),
                                experienceDto.getInstitut().getId())));

        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    // parse content to JSON
                    Map<String, Object> profileJson = objectMapper.readValue(content, Map.class);
                    LinkedHashMap<String, Object> experienceJson = (LinkedHashMap<String, Object>) ((List) profileJson.get("experiences")).get(0);
                    List<Experience> experiences = (List<Experience>) profileJson.get("experiences");
                    Map<String, Object> institutJson = (Map<String, Object>) experienceJson.get("institut");
                    assertFalse(experiences.isEmpty());
                    assertEquals(1, experiences.size());
                    assertEquals(institut.getId(), institutJson.get("id"));
                    assertEquals(institut.getName(), institutJson.get("name"));
                });
    }

    @Test
    void addExperienceWithInExistingProfileThrowsException() throws Exception {

        //arrange
        ExperienceDto experienceDto = createExperienceDto("title", "location", YearMonth.of(2019, 1), YearMonth.of(2020, 1), Institut.builder().name("institut").build());

        //act
        ResultActions test = mockMvc.perform(post(String.format("/profiles/%s/experiences", UUID.randomUUID()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        String.format(
                                "{\"title\": \"%s\", \"location\": \"%s\", \"startAt\": \"%s\", \"endAt\": \"%s\", \"institut\": {\"name\": \"%s\"}}",
                                experienceDto.getTitle(),
                                experienceDto.getLocation(),
                                experienceDto.getStartAt(),
                                experienceDto.getEndAt(),
                                experienceDto.getInstitut().getName())));

        //assert
        test.andExpect(result -> {
            String content = result.getResponse().getContentAsString();
            assert !content.isEmpty();

            log.info("Response content: {}", content);
            // parse content to JSON
            Map<String, Object> errorJson = objectMapper.readValue(content, Map.class);
            assertEquals("PROFILE_NOT_FOUND", errorJson.get("errorCode"));
        });
    }

    @Test
    void updateExperienceReturnsProfile() throws Exception {

        //arrange
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"));
        Institut institut = saveInstitut("institut");
        Experience experience = experienceRepository.save(Experience.builder()
                .title("title")
                .location("location")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(institut)
                .build());
        profile.setExperiences(List.of(experience));
        profileRepository.save(profile);

        //act
        ResultActions test = mockMvc.perform(put(String.format("/profiles/%s/experiences/%s", profile.getId(), experience.getId()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        String.format(
                                "{\"title\": \"%s\", \"location\": \"%s\", \"startAt\": \"%s\", \"endAt\": \"%s\", \"institut\": {\"id\": \"%s\"}}",
                                "title2",
                                "location2",
                                YearMonth.of(2019, 1),
                                YearMonth.of(2020, 1),
                                institut.getId())));

        //assert
        test.andExpectAll(status().isOk(),
                result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Profile profile1 = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertFalse(profile1.getExperiences().isEmpty());
                    assertEquals(1, profile1.getExperiences().size());
                    assertEquals("title2", profile1.getExperiences().get(0).getTitle());
                    assertEquals("location2", profile1.getExperiences().get(0).getLocation());
                });
    }

    @Test
    void deleteExperienceReturnsProfile() throws Exception {

        //arrange
        Profile profile = saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"));
        Institut institut = saveInstitut("institut");
        Experience experience = experienceRepository.save(Experience.builder()
                .title("title")
                .location("location")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(institut)
                .build());
        profile.setExperiences(List.of(experience));
        profileRepository.save(profile);

        //act
        ResultActions test = mockMvc.perform(delete(String.format("/profiles/%s/experiences/%s", profile.getId(), experience.getId()))
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));

        //assert
        test.andExpectAll(status().isOk(),
                result -> {
                    String content = result.getResponse().getContentAsString();
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Profile profile1 = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertTrue(profile1.getExperiences().isEmpty());
                });
    }


    private List<Profile> saveListOfValidTestProfiles() {

        return List.of(
                saveProfile(getSampleStudent("test-cne-1", "test-first-name-1", "test-last-name-1", "testemail1@gmail.com", "test-phone-1", "test-cin-1")),
                saveProfile(getSampleStudent("test-cne-2", "test-first-name-2", "test-last-name-2", "testemail2@gmail.com", "test-phone-2", "test-cin-2"))
        );
    }

    private Profile saveProfile(Student student) {
        return profileRepository.save(Profile.builder()
                .student(student)
                .build());
    }

    private Institut saveInstitut(String name) {
        return institutRepository.save(Institut.builder().name(name).build());
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

    private EducationDto createEducationDto(String title, String studyField, YearMonth startAt, YearMonth endAt, Institut institut) {
        return EducationDto.builder()
                .title(title)
                .studyField(studyField)
                .startAt(startAt)
                .endAt(endAt)
                .institut(institut)
                .build();
    }

    private ExperienceDto createExperienceDto(String title, String location, YearMonth startAt, YearMonth endAt, Institut institut) {
        return ExperienceDto.builder()
                .title(title)
                .location(location)
                .startAt(startAt)
                .endAt(endAt)
                .institut(institut)
                .build();
    }
}
