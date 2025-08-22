package com.mysite.review.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.review.DataNotFoundException;
import com.mysite.review.dto.ReviewDTO;
import com.mysite.review.entity.Category;
import com.mysite.review.entity.Review;
import com.mysite.review.repository.ReviewRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	
	private final ReviewRepository reviewRepository;
	
	public List<ReviewDTO> getList() {
		List<Review> entities = this.reviewRepository.findAll();
		List<ReviewDTO> dtos = entities.stream()
				.map(ReviewDTO::toReviewDTO)
				.collect(Collectors.toList());
				
		return dtos;
	}
	
	public Page<ReviewDTO> getList(int page, Category category, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdTime"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		//Specification<Review> spec = search(kw);
		
		Page<Review> reviewPage = this.reviewRepository.findAllByKeywordAndCategory(kw, category, pageable);
		Page<ReviewDTO> dtos = reviewPage.map(review -> ReviewDTO.toReviewDTO(review));
		
		return dtos;
	}
	
	public ReviewDTO getReview(Long id) {
		Optional<Review> review = this.reviewRepository.findById(id);
		if (review.isPresent()) {
			return ReviewDTO.toReviewDTO(review.get());
		} else {
			throw new DataNotFoundException("review not found");
		}
	}
	
	public void create(ReviewDTO reviewDTO) {
		Review review = Review.toReviewCreate(reviewDTO);
		this.reviewRepository.save(review);
	}
	
	public void modify(ReviewDTO reviewDTO, Long id) {
		Optional<Review> review = this.reviewRepository.findById(id);
		if (review.isPresent()) {
			review.get().setTitle(reviewDTO.getTitle());
			review.get().setContent(reviewDTO.getContent());
			this.reviewRepository.save(review.get());
		}
	}
	
	public void delete(Long id) {
		Optional<Review> review = this.reviewRepository.findById(id);
		if (review.isPresent()) {
			this.reviewRepository.delete(review.get());
		}	
	}
	
	private Specification<Review> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Review> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				return cb.or(cb.like(r.get("title"), "%" + kw + "%"), 
						cb.like(r.get("content"), "%" + kw + "%"));
			}
		};
	}

}
