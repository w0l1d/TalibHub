package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ReviewDto;
import org.ilisi.backend.exception.EntityNotFoundException;
import org.ilisi.backend.mapper.ReviewMapper;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Review;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ReviewService {

    private ReviewRepository reviewRepository;
    private InstitutRepository institutRepository;
    private ReviewMapper reviewMapper;

    public Review addNewReview(ReviewDto reviewDto) {
        Institut institut = institutRepository.findById(reviewDto.getInstitut().getId()).orElse(null);
        reviewDto.setInstitut(institut);
        Review review = reviewMapper.reviewDtoToReview(reviewDto);
        return reviewRepository.save(review);
    }

    public Review updateReview(String reviewId, ReviewDto reviewDto) {
        reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException(String.format("Review with id %s not found", reviewId), "REVIEW_NOT_FOUND"));
        Review review = reviewMapper.reviewDtoToReview(reviewDto);
        review.setId(reviewId);
        return reviewRepository.save(review);
    }

    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
