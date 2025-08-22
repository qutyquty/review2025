package com.mysite.review.entity;

import com.mysite.review.dto.ReviewDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends Base {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50)
	private String title;
	
	@Column(length = 500)
	private String content;
	
	public static Review toReviewCreate(ReviewDTO reviewDTO) {
		Review review = Review.builder()
				.title(reviewDTO.getTitle())
				.content(reviewDTO.getContent())
				.build();
		return review;
	}

}
