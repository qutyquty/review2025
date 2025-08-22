package com.mysite.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.review.dto.ReviewDTO;
import com.mysite.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
	
	private final ReviewService reviewService;
	
	@GetMapping("/review/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, 
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		
		Page<ReviewDTO> paging = this.reviewService.getList(page, kw, "영화");
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		return "review_list";
	}
	
	@GetMapping("/review/detail/{id}")
	public String detail(Model model, @PathVariable("id") Long id) {
		ReviewDTO dto = this.reviewService.getReview(id);
		model.addAttribute("review", dto);
		return "review_detail";
	}
	
	@GetMapping("/review/create")
	public String reviewCreate(ReviewDTO reviewDTO) {
		return "review_form";
	}
	
	@PostMapping("/review/create")
	public String reviewCreate(@Valid ReviewDTO reviewDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "review_form";
		}
		this.reviewService.create(reviewDTO);
		return "redirect:/review/list";
	}
	
	@GetMapping("/review/modify/{id}")
	public String reviewModify(ReviewDTO reviewDTO, @PathVariable("id") Long id) {
		ReviewDTO dto = this.reviewService.getReview(id);
		reviewDTO.setTitle(dto.getTitle());
		reviewDTO.setContent(dto.getContent());
		return "review_form";
	}
	
	@PostMapping("/review/modify/{id}")
	public String reviewModify(@Valid ReviewDTO reviewDTO, BindingResult bindingResult, 
			@PathVariable("id") Long id) {
		if (bindingResult.hasErrors()) {
			return "review_form";
		}
		this.reviewService.modify(reviewDTO, id);
		return String.format("redirect:/review/detail/%s", id);
	}
	
	@GetMapping("/review/delete/{id}")
	public String reviewDelete(@PathVariable("id") Long id) {
		this.reviewService.delete(id);
		return "redirect:/";
	}

}
