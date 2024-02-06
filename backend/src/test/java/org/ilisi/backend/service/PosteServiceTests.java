package org.ilisi.backend.service;


import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.CommentDto;
import org.ilisi.backend.dto.PosteDto;
import org.ilisi.backend.mapper.CommentMapper;
import org.ilisi.backend.mapper.PosteMapper;
import org.ilisi.backend.model.Comment;
import org.ilisi.backend.model.Poste;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.model.User;
import org.ilisi.backend.repository.CommentRepository;
import org.ilisi.backend.repository.PosteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
class PosteServiceTests {

    @Mock
    private PosteRepository posteRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PosteMapper posteMapper;
    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private PosteService posteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPostesReturnsPostes() {
        log.info("testGetAllPostes");

        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        List<Poste> postes = List.of(
                samplePoste("titre1", "description1", "image1", student, List.of(sampleComment("content1", student))),
                samplePoste("titre2", "description2", "image2", student, List.of(sampleComment("content2", student)))
        );

        //act
        when(posteRepository.findAll()).thenReturn(postes);

        List<Poste> result = posteService.getAllPostes();

        //assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findPosteByIdReturnsPoste() {
        log.info("findPosteByIdReturnsPoste");

        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(sampleComment("content1", student)));

        //act
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));

        Poste result = posteService.findPosteById(poste.getId());

        //assert
        assertNotNull(result);
        assertEquals(poste, result);
        assertEquals(poste.getTitre(), result.getTitre());
    }

    @Test
    void addNewPosteReturnsPoste() {
        log.info("addNewPosteReturnsPoste");

        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        PosteDto posteDto = samplePosteDto("titre1", "description1", student);

        //act
        when(posteMapper.posteDtoToPoste(posteDto)).thenCallRealMethod();
        Poste poste = posteMapper.posteDtoToPoste(posteDto);
        when(posteRepository.save(poste)).thenReturn(poste);

        Poste result = posteService.addNewPoste(posteDto);

        //assert
        assertNotNull(result);
        assertEquals(poste, result);
        assertEquals(poste.getTitre(), result.getTitre());
    }

    @Test
    void updatePosteReturnsPoste() {
        log.info("updatePosteReturnsPoste");

        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        PosteDto posteDto = samplePosteDto("titre1", "description1", student);

        //act
        when(posteMapper.posteDtoToPoste(posteDto)).thenCallRealMethod();
        Poste poste = posteMapper.posteDtoToPoste(posteDto);
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        when(posteRepository.save(poste)).thenReturn(poste);

        Poste result = posteService.updatePoste(poste.getId(), posteDto);

        //assert
        assertNotNull(result);
        assertEquals(poste, result);
        assertEquals(poste.getTitre(), result.getTitre());
    }

    @Test
    void deletePosteReturnsVoid() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        PosteDto posteDto = samplePosteDto("titre1", "description1", student);

        //act
        posteService.deletePoste(posteDto.getId());

        //assert
        verify(posteRepository, times(1)).deleteById(posteDto.getId());
    }


    @Test
    void getPosteCommentsReturnsComments() {

        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(sampleComment("content1", student), sampleComment("content2", student)));

        //act
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        List<Comment> result = posteService.getPosteComments(poste.getId());

        //assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findCommentByIdReturnsComment() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(sampleComment("content1", student), sampleComment("content2", student)));
        //act
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        Comment result = posteService.findCommentById(poste.getId(), poste.getComments().get(0).getId());

        //assert
        assertNotNull(result);
        assertEquals(poste.getComments().get(0), result);
    }

    @Test
    void addCommentReturnsPoste() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        List<Comment> comments = new ArrayList<>(List.of(sampleComment("content1", student), sampleComment("content2", student)));
        Poste poste = samplePoste("titre1", "description1", "image1", student, comments);
        int sizeBefore = poste.getComments().size();
        CommentDto commentDto = sampleCommentDto("content3", student);
        //act
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        when(commentMapper.commentDtoToComment(commentDto)).thenCallRealMethod();
        when(posteRepository.save(poste)).thenReturn(poste);
        Poste result = posteService.addComment(poste.getId(), commentDto, student);

        //assert
        assertNotNull(result);
        assertEquals(result.getComments().size(), sizeBefore + 1);
    }

    @Test
    void updateCommentReturnsPoste() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(sampleComment("content1", student), sampleComment("content2", student)));
        CommentDto commentDto = sampleCommentDto("content3", student);
        //act
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        when(commentMapper.commentDtoToComment(commentDto)).thenCallRealMethod();
        when(posteRepository.save(poste)).thenReturn(poste);
        Poste result = posteService.updateComment(poste.getId(), poste.getComments().get(0).getId(), commentDto, student);

        //assert
        assertNotNull(result);
        assertEquals(result.getComments().get(0).getContent(), commentDto.getContent());
    }

    @Test
    void deleteCommentReturnsVoid() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(sampleComment("content1", student), sampleComment("content2", student)));
        //act
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        posteService.deleteComment(poste.getId(), poste.getComments().get(0).getId());

        //assert
        verify(commentRepository, times(1)).deleteById(poste.getComments().get(0).getId());
    }

    @Test
    void addReplyReturnsComment() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Comment commentToReply = sampleComment("content1", student);
        commentToReply.setReplies(new ArrayList<>());
        int sizeBefore = commentToReply.getReplies().size();
        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(commentToReply, sampleComment("content2", student)));
        CommentDto replyDto = sampleCommentDto("content3", student);

        //act
        when(commentMapper.commentDtoToComment(replyDto)).thenCallRealMethod();
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        when(commentRepository.findById(commentToReply.getId())).thenReturn(Optional.of(commentToReply));
        when(commentRepository.save(commentToReply)).thenReturn(commentToReply);

        Comment result = posteService.addReply(poste.getId(), commentToReply.getId(), replyDto, student);

        //assert
        assertNotNull(result);
        assertEquals(result.getReplies().size(), sizeBefore + 1);
    }

    @Test
    void updateReplyReturnsComment() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");

        Comment commentToReply = sampleComment("content1", student);
        commentToReply.setReplies(List.of(sampleComment("reply1", student)));

        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(commentToReply, sampleComment("content2", student)));
        CommentDto replyDto = sampleCommentDto("reply2", student);

        //act
        when(commentMapper.commentDtoToComment(replyDto)).thenCallRealMethod();
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        when(commentRepository.findById(commentToReply.getId())).thenReturn(Optional.of(commentToReply));
        when(commentRepository.save(commentToReply)).thenReturn(commentToReply);

        Comment result = posteService.updateReply(poste.getId(), commentToReply.getId(), commentToReply.getReplies().get(0).getId(), replyDto, student);

        //assert
        assertNotNull(result);
        assertEquals(result.getReplies().get(0).getId(), commentToReply.getReplies().get(0).getId());
        assertEquals(result.getReplies().get(0).getContent(), replyDto.getContent());


    }

    @Test
    void deleteReplyReturnsVoid() {
        //arrange
        User student = sampleStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Comment commentToReply = sampleComment("content1", student);
        commentToReply.setReplies(List.of(sampleComment("reply1", student)));
        Poste poste = samplePoste("titre1", "description1", "image1", student, List.of(commentToReply, sampleComment("content2", student)));

        //act
        when(posteRepository.findById(poste.getId())).thenReturn(Optional.of(poste));
        when(commentRepository.findById(commentToReply.getId())).thenReturn(Optional.of(commentToReply));

        posteService.deleteReply(poste.getId(), commentToReply.getId(), commentToReply.getReplies().get(0).getId());

        //assert
        verify(commentRepository, times(1)).deleteById(commentToReply.getReplies().get(0).getId());
    }


    private Poste samplePoste(String titre, String description, String image, User user, List<Comment> comments) {
        return Poste.builder()
                .id(UUID.randomUUID().toString())
                .titre(titre)
                .description(description)
                .imageUri(image)
                .user(user)
                .comments(comments)
                .build();
    }

    private PosteDto samplePosteDto(String titre, String description, User user) {
        return PosteDto.builder()
                .titre(titre)
                .description(description)
                .user(user)
                .build();
    }

    private Comment sampleComment(String content, User user) {
        return Comment.builder()
                .id(UUID.randomUUID().toString())
                .content(content)
                .user(user)
                .replies(List.of())
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
        student.setEnrollmentYear(Year.now());
        student.setBirthDate(LocalDate.now());
        student.setPassword("password");
        return student;
    }
}
