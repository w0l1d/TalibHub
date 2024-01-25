package org.ilisi.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.CommentDto;
import org.ilisi.backend.dto.PosteDto;
import org.ilisi.backend.model.Comment;
import org.ilisi.backend.model.Poste;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.model.User;
import org.ilisi.backend.repository.CommentRepository;
import org.ilisi.backend.repository.PosteRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
class PosteControllerIntegrationTests {

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
    private PosteRepository posteRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void beforeEachSetup() throws Exception {
        //studentRepository.deleteAll();
        User student = sampleStudent("test-cne", "test-first-name", "test-last-name", "testemail@gmail.com", "test-phone", "test-cin");
        student = studentRepository.save((Student) student);
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
        commentRepository.deleteAll();
        posteRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void getAllPostesReturnsPostes() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Poste poste1 = samplePoste("titre", "description", "image", student, List.of());
        Poste poste2 = samplePoste("titre2", "description2", "image2", student, List.of());
        Poste poste3 = samplePoste("titre3", "description3", "image3", student, List.of());
        //act
        List<Poste> postes = posteRepository.saveAll(List.of(poste1, poste2, poste3));
        ResultActions test = mockMvc.perform(get("/postes")
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    List<Poste> resultPostes = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertNotNull(resultPostes);
                    assertEquals(postes.size(), resultPostes.size());

                });
    }

    @Test
    void getPosteByIdReturnsPoste() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Poste poste1 = samplePoste("titre", "description", "image", student, List.of());
        Poste poste2 = samplePoste("titre2", "description2", "image2", student, List.of());
        Poste poste3 = samplePoste("titre3", "description3", "image3", student, List.of());
        //act
        posteRepository.saveAll(List.of(poste1, poste2, poste3));
        ResultActions test = mockMvc.perform(get("/postes/" + poste1.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Poste resultPoste = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertNotNull(resultPoste);
                    assertEquals(poste1.getTitre(), resultPoste.getTitre());

                });
    }

    @Test
    void addNewPosteReturnsPoste() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Poste poste = samplePoste("titre", "description", "image", student, List.of());
        PosteDto posteDto = samplePosteDto("titre", "description");

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(post("/postes")
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(posteDto)));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Poste resultPoste = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertNotNull(resultPoste);
                    assertEquals(poste.getTitre(), resultPoste.getTitre());

                });
    }

    @Test
    void updatePosteReturnsPoste() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Poste poste = samplePoste("titre", "description", "image", student, List.of());
        PosteDto posteDto = samplePosteDto("titre1", "description1");

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(put("/postes/" + poste.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(posteDto)));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Poste resultPoste = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertNotNull(resultPoste);
                    assertEquals(posteDto.getTitre(), resultPoste.getTitre());

                });
    }

    @Test
    void deletePosteReturnsPoste() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Poste poste = samplePoste("titre", "description", "image", student, List.of());
        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(delete("/postes/" + poste.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));
        //assert
        test.andExpect(status().isOk());
    }

    //comments
    @Test
    void getPosteCommentsReturnsComments() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Comment comment1 = sampleComment("content1", student);
        Comment comment2 = sampleComment("content2", student);
        Comment comment3 = sampleComment("content3", student);
        List<Comment> comments = List.of(comment1, comment2, comment3);
        Poste poste = samplePoste("titre", "description", "image", student, comments);

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(get("/postes/" + poste.getId() + "/comments")
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    List<Comment> resultComments = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertNotNull(resultComments);
                    assertEquals(poste.getComments().size(), resultComments.size());

                });
    }

    @Test
    void addCommentReturnsPoste() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Comment comment = sampleComment("content", student);
        CommentDto commentDto = sampleCommentDto("content", student);
        Poste poste = samplePoste("titre", "description", "image", student, List.of(comment));

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(post("/postes/" + poste.getId() + "/comments")
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Poste resultPoste = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertNotNull(resultPoste);
                    assertEquals(poste.getComments().size() + 1, resultPoste.getComments().size());

                });
    }

    @Test
    void updateCommentReturnsPoste() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Comment comment = sampleComment("content", student);
        Comment comment1 = sampleComment("content1", student);
        CommentDto commentDto = sampleCommentDto("content1", student);
        Poste poste = samplePoste("titre", "description", "image", student, List.of(comment, comment1));

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(put("/postes/" + poste.getId() + "/comments/" + comment.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();

                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);

                    Poste resultPoste = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });

                    assertNotNull(resultPoste);
                    assertEquals(commentDto.getContent(), resultPoste.getComments().get(0).getContent());

                });
    }

    @Test
    void deleteCommentReturnsPoste() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Comment comment = sampleComment("content", student);
        Comment comment1 = sampleComment("content1", student);
        Poste poste = samplePoste("titre", "description", "image", student, List.of(comment, comment1));

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(delete("/postes/" + poste.getId() + "/comments/" + comment.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));
        //assert
        test.andExpect(status().isOk());
    }

    @Test
    void addReplyReturnsComment() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Comment comment = sampleComment("content", student);
        Comment comment1 = sampleComment("content1", student);
        CommentDto replyDto = sampleCommentDto("reply1", student);
        Poste poste = samplePoste("titre", "description", "image", student, List.of(comment, comment1));

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(post("/postes/" + poste.getId() + "/comments/" + comment.getId() + "/replies")
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDto)));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();
                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);
                    Comment resultComment = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });
                    assertNotNull(resultComment);
                    assertEquals(comment.getReplies().size() + 1, resultComment.getReplies().size());
                });
    }

    @Test
    void updateReplyReturnsComment() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Comment comment = sampleComment("content", student);
        Comment reply = sampleComment("reply", student);
        comment.addReply(reply);
        comment.addReply(sampleComment("reply1", student));

        Comment comment1 = sampleComment("content1", student);
        CommentDto replyDto = sampleCommentDto("reply1", student);
        Poste poste = samplePoste("titre", "description", "image", student, List.of(comment, comment1));

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(put("/postes/" + poste.getId() + "/comments/" + comment.getId() + "/replies/" + reply.getId())
                .header("Authorization", "Bearer " + JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDto)));
        //assert
        test.andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    log.info("Response content: {}", content);
                    assert !content.isEmpty();
                    log.info("Response content: {}", content);
                    JsonNode jsonNode = objectMapper.readTree(content);
                    Comment resultComment = objectMapper.convertValue(jsonNode, new TypeReference<>() {
                    });
                    assertNotNull(resultComment);
                    assertEquals(replyDto.getContent(), resultComment.getReplies().get(0).getContent());
                });
    }

    /*
    TODO: fix this test, "comment violates foreign key constraint comment_replies"

    @Test
    void deleteReplyReturnsComment() throws Exception {
        //arrange
        User student = studentRepository.findAll().get(0);
        Comment comment = sampleComment("content", student);
        Comment reply = sampleComment("reply", student);
        comment.addReply(reply);

        Comment comment1 = sampleComment("content1", student);
        Poste poste = samplePoste("titre", "description", "image", student, List.of(comment, comment1));

        //act
        posteRepository.save(poste);
        ResultActions test = mockMvc.perform(delete("/postes/"+poste.getId()+"/comments/"+comment.getId()+"/replies/"+reply.getId())
                        .header("Authorization", "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON));
        //assert
        test.andExpect(status().isOk());
    }*/


    private Poste samplePoste(String titre, String description, String image, User user, List<Comment> comments) {
        return Poste.builder()
                .id(UUID.randomUUID().toString())
                .titre(titre)
                .description(description)
                .image(image)
                .user(user)
                .comments(comments)
                .build();
    }

    private PosteDto samplePosteDto(String titre, String description) {
        return PosteDto.builder()
                .titre(titre)
                .description(description)
                .build();
    }

    private Comment sampleComment(String content, User user) {
        return Comment.builder()
                .id(UUID.randomUUID().toString())
                .content(content)
                .user(user)
                .replies(new ArrayList<>())
                .build();
    }

    private CommentDto sampleCommentDto(String content, User user) {
        return CommentDto.builder()
                .content(content)
                .user(user)
                .build();
    }

    private User sampleStudent(String cne, String firstName, String lastName, String email, String phone, String cin) {
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
        student.setCne(cne);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setCin(cin);
        student.setEnabled(true);
        student.setEnrollmentYear(Year.now());
        student.setBirthDate(LocalDate.now());
        student.setPassword(passwordEncoder.encode("123456789"));
        return student;
    }
}
