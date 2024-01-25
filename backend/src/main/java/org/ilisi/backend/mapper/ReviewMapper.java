package org.ilisi.backend.mapper;

import org.ilisi.backend.dto.ReviewDto;
import org.ilisi.backend.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review reviewDtoToReview(ReviewDto reviewDto) {
        return Review.builder()
                .id(reviewDto.getId())
                .student(reviewDto.getStudent())
                .institut(reviewDto.getInstitut())
                .review(reviewDto.getReview())
                .rating(reviewDto.getRating())
                .build();
    }

    public ReviewDto mapToReviewDto(Review review) {
        return ReviewDto.builder()
                .student(review.getStudent())
                .institut(review.getInstitut())
                .review(review.getReview())
                .rating(review.getRating())
                .build();
    }

}
