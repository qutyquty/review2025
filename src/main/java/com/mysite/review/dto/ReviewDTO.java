package com.mysite.review.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mysite.review.entity.Category;
import com.mysite.review.entity.Comment;
import com.mysite.review.entity.Review;
import com.mysite.review.entity.SiteUser;

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
	
	private String overview;
	
	private String posterPath;
	
	private Category category;
	
	private SiteUser author;
	
	private Set<SiteUser> voter;
	
	private List<Comment> commentList = new ArrayList<>();  // 댓글 가져오기
	
	private LocalDateTime createdTime;
	
	private LocalDateTime updatedTime;
	
	public static ReviewDTO toReviewDTO(Review review) {
		ReviewDTO dto = ReviewDTO.builder()
				.id(review.getId())
				.title(review.getTitle())
				.content(review.getContent())
				.overview(review.getOverview())
				.posterPath(review.getPosterPath())
				.category(review.getCategory())
				.author(review.getAuthor())
				.commentList(review.getCommentList())
				.voter(review.getVoter())
				.createdTime(review.getCreatedTime())
				.updatedTime(review.getUpdatedTime())
				.build();
		
		return dto;
	}

}
