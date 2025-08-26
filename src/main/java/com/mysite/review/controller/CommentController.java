package com.mysite.review.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.review.dto.CommentForm;
import com.mysite.review.entity.Comment;
import com.mysite.review.entity.Review;
import com.mysite.review.service.CommentService;
import com.mysite.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
	
	private final ReviewService reviewService;
	private final CommentService commentService;
	
	@PostMapping("/create/{id}")
	public String createComment(Model model, @PathVariable("id") Long id, 
			@Valid CommentForm commentForm, BindingResult bindingResult) {
		
		Review review = this.reviewService.getReviewEntity(id);
		if (bindingResult.hasErrors()) {
			model.addAttribute("review", review);
			return "review_detail";
		}
		
		Comment comment = this.commentService.create(review, commentForm.getContent());
		
		return String.format("redirect:/review/detail/%s#comment_%s",
				comment.getReview().getId(), comment.getId());
		
	}
	
	@GetMapping("/modify/{id}")
	public String commentModify(CommentForm commentForm, @PathVariable("id") Long id) {
		Comment comment = this.commentService.getComment(id);
		commentForm.setContent(comment.getContent());
		return "comment_form";
	}
	
	@PostMapping("/modify/{id}")
	public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult, 
			@PathVariable("id") Long id) {
		if (bindingResult.hasErrors()) {
			return "comment_form";
		}
		Comment comment = this.commentService.getComment(id);
		this.commentService.modify(comment, commentForm.getContent());
		return String.format("redirect:/review/detail/%s#comment_%s", 
				comment.getReview().getId(), comment.getId());
	}
	
	@GetMapping("/delete/{id}")
	public String commentDelete(@PathVariable("id") Long id) {
		Comment comment = this.commentService.getComment(id);
		this.commentService.delete(comment);
		return String.format("redirect:/review/detail/%s",comment.getReview().getId());
	}

}
