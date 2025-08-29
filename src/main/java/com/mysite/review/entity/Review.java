package com.mysite.review.entity;

import java.util.List;

import com.mysite.review.dto.ReviewDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@Column(length = 200)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@ManyToOne
	private Category category;
	
	@ManyToOne
	private SiteUser author;
	
	@OneToMany(mappedBy = "review", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Comment> commentList;
	
	public static Review toReviewCreate(ReviewDTO reviewDTO) {
		Review review = Review.builder()
				.title(reviewDTO.getTitle())
				.content(reviewDTO.getContent())
				.category(reviewDTO.getCategory())
				.build();
		return review;
	}
	
	public static Review toReviewComment(ReviewDTO reviewDTO) {
		Review review = Review.builder()
				.id(reviewDTO.getId())
				.build();
		return review;
	}

}
