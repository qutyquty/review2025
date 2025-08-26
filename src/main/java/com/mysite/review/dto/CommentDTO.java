package com.mysite.review.dto;

import java.time.LocalDateTime;

import com.mysite.review.entity.Comment;

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
public class CommentDTO {
	
	private Long id;
	
	@NotEmpty(message = "내용은 필수항목입니다.")	
	private String content;
	
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	
	public static CommentDTO toCommentDTO(Comment comment) {
		CommentDTO dto = CommentDTO.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.createdTime(comment.getCreatedTime())
				.updatedTime(comment.getUpdatedTime())
				.build();
		return dto;
	}

}
