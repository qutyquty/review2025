package com.mysite.review.dto;

import java.time.LocalDateTime;

import com.mysite.review.entity.Category;
import com.mysite.review.entity.Review;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
	
	private Long id;
	
	@NotEmpty(message = "제목은 필수항목입니다.")
	@Size(max = 150)
	private String title;
	
	@NotEmpty(message = "내용은 필수항목입니다.")
	private String content;
	
	@NotNull(message = "카테고리는 필수 항목입니다.")
	private Category category;
	
	private LocalDateTime createdTime;
	
	private LocalDateTime updatedTime;
	
	public static ReviewDTO toReviewDTO(Review review) {
		ReviewDTO dto = ReviewDTO.builder()
				.id(review.getId())
				.title(review.getTitle())
				.content(review.getContent())
				.category(review.getCategory())
				.createdTime(review.getCreatedTime())
				.updatedTime(review.getUpdatedTime())
				.build();
		return dto;
	}

}
