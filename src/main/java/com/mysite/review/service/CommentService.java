package com.mysite.review.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.review.DataNotFoundException;
import com.mysite.review.entity.Comment;
import com.mysite.review.entity.Review;
import com.mysite.review.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	public Comment create(Review review, String content) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setReview(review);
		this.commentRepository.save(comment);
		return comment;
	}
	
	public Comment getComment(Long id) {
		Optional<Comment> comment = this.commentRepository.findById(id);
		if (comment.isPresent()) {
			return comment.get();
		} else {
			throw new DataNotFoundException("comment not found");
		}
	}
	
	public void modify(Comment comment, String content) {
		comment.setContent(content);
		this.commentRepository.save(comment);
	}
	
	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}

}
