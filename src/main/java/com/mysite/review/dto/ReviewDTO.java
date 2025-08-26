package com.mysite.review.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mysite.review.entity.Category;
import com.mysite.review.entity.Review;

import jakarta.validation.constraints.NotEmpty;
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
	private String title;
	
	@NotEmpty(message = "내용은 필수항목입니다.")
	private String content;
	
	private Category category;
	
	private List<Comment> commentList = new ArrayList<>();  // 댓글 가져오기
	
	private LocalDateTime createdTime;
	
	private LocalDateTime updatedTime;
	
	@Getter
    @Setter
    public static class Comment {
        private Long id;
        private String content;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
    }
	
	public static ReviewDTO toReviewDTO(Review review) {
		ReviewDTO dto = ReviewDTO.builder()
				.id(review.getId())
				.title(review.getTitle())
				.content(review.getContent())
				.category(review.getCategory())
//				.commentList(review.getCommentList().stream().map(
//							r -> CommentDTO.toCommentDTO(r)
//						).collect(Collectors.toList()))
				.createdTime(review.getCreatedTime())
				.updatedTime(review.getUpdatedTime())
				.build();
		return dto;
	}

}
