package org.ilisi.backend.controller;

import lombok.RequiredArgsConstructor;
import org.ilisi.backend.dto.ReviewDto;
import org.ilisi.backend.model.Review;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("")
    public Review addNewReview(@RequestBody ReviewDto reviewDto, Principal principal) {
        Student student = ((Student) ((Authentication) principal).getPrincipal());
        reviewDto.setStudent(student);
        return reviewService.addNewReview(reviewDto);
    }

    @PutMapping("/{reviewId}")
    public Review updateReview(@PathVariable String reviewId, @RequestBody ReviewDto reviewDto, Principal principal) {
        Student student = ((Student) ((Authentication) principal).getPrincipal());
        reviewDto.setStudent(student);
        return reviewService.updateReview(reviewId, reviewDto);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
