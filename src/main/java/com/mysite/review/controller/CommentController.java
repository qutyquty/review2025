package com.mysite.review.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.review.dto.CommentDTO;
import com.mysite.review.dto.ReviewDTO;
import com.mysite.review.entity.Comment;
import com.mysite.review.entity.SiteUser;
import com.mysite.review.service.CommentService;
import com.mysite.review.service.ReviewService;
import com.mysite.review.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
	
	private final ReviewService reviewService;
	private final CommentService commentService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createComment(Model model, @PathVariable("id") Long id, 
			@Valid CommentDTO commentDTO, BindingResult bindingResult, 
			Principal principal) {
		
		ReviewDTO reviewDTO = this.reviewService.getReview(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute("review", reviewDTO);
			return "review_detail";
		}
		
		Comment comment = this.commentService.create(reviewDTO, commentDTO.getContent(), siteUser);
		
		return String.format("redirect:/review/detail/%s#comment_%s",
				comment.getReview().getId(), comment.getId());
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String commentModify(CommentDTO commentDTO, @PathVariable("id") Long id, 
			Principal principal) {
		Comment comment = this.commentService.getComment(id);
		if (!comment.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		commentDTO.setContent(comment.getContent());
		return "comment_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String commentModify(@Valid CommentDTO commentDTO, BindingResult bindingResult, 
			@PathVariable("id") Long id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "comment_form";
		}
		Comment comment = this.commentService.getComment(id);
		if (!comment.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.commentService.modify(comment, commentDTO.getContent());
		return String.format("redirect:/review/detail/%s#comment_%s", 
				comment.getReview().getId(), comment.getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String commentDelete(@PathVariable("id") Long id, Principal principal) {
		Comment comment = this.commentService.getComment(id);
		if (!comment.getAuthor().getUsername().equals(principal.getName()) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.commentService.delete(comment);
		return String.format("redirect:/review/detail/%s",comment.getReview().getId());
	}

}
