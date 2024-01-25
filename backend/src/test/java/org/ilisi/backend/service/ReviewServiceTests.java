package org.ilisi.backend.service;


import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ReviewDto;
import org.ilisi.backend.mapper.ReviewMapper;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Review;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;

@Slf4j
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private InstitutRepository institutRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addNewReviewReturnsReview() {
        //arrange
        Student student = createStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Institut institut = Institut.builder().id("id").build();

        ReviewDto reviewDto = ReviewDto.builder().student(student).institut(institut).build();

        //act
        Mockito.when(institutRepository.findById(institut.getId())).thenReturn(Optional.of(institut));
        Mockito.when(reviewMapper.reviewDtoToReview(reviewDto)).thenCallRealMethod();

        Review review = reviewMapper.reviewDtoToReview(reviewDto);
        Mockito.when(reviewRepository.save(review)).thenReturn(review);

        Review result = reviewService.addNewReview(reviewDto);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(institut.getId(), result.getInstitut().getId());
        Assertions.assertEquals(student.getCin(), result.getStudent().getCin());
    }

    @Test
    void updateReviewReturnsReview() {
        //arrange
        Student student = createStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Institut institut = Institut.builder().id("id").build();

        ReviewDto reviewDto = ReviewDto.builder().student(student).institut(institut).build();

        //act
        Mockito.when(reviewMapper.reviewDtoToReview(reviewDto)).thenCallRealMethod();

        Review review = reviewMapper.reviewDtoToReview(reviewDto);
        review.setId("id");
        Mockito.when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        Mockito.when(reviewRepository.save(review)).thenReturn(review);

        Review result = reviewService.updateReview(review.getId(), reviewDto);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(institut.getId(), result.getInstitut().getId());
        Assertions.assertEquals(student.getCin(), result.getStudent().getCin());
    }

    @Test
    void deleteReviewReturnsVoid(){
        //arrange
        Student student = createStudent("CNE1", "firstName1", "lastName1", "email1", "phone1", "CIN1");
        Institut institut = Institut.builder().id("id").build();
        Review review = Review.builder().id("id").student(student).institut(institut).build();

        //act
        reviewService.deleteReview(review.getId());

        //assert
        Mockito.verify(reviewRepository, Mockito.times(1)).deleteById(review.getId());
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
