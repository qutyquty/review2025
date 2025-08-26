package com.mysite.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.review.dto.CommentDTO;
import com.mysite.review.dto.ReviewDTO;
import com.mysite.review.entity.Category;
import com.mysite.review.service.CategoryService;
import com.mysite.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
	
	private final ReviewService reviewService;
	private final CategoryService categoryService;
	
	@GetMapping("/review/list/{categoryId}")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, 
			@RequestParam(value = "kw", defaultValue = "") String kw, 
			@PathVariable("categoryId") Integer categoryId) {
		
		Category category = this.categoryService.getCategory(categoryId);
		Page<ReviewDTO> paging = this.reviewService.getList(page, category, kw);
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("currentCategory", category);
		
		return "review_list";
	}
	
	@GetMapping("/review/detail/{id}")
	public String detail(Model model, @PathVariable("id") Long id, CommentDTO commentDTO) {
		ReviewDTO reviewDTO = this.reviewService.getReview(id);
		model.addAttribute("review", reviewDTO);
		
		return "review_detail";
	}
	
	@GetMapping("/review/create/{categoryId}")
	public String reviewCreate(ReviewDTO reviewDTO, Model model, 
			@PathVariable("categoryId") Integer categoryId) {
		model.addAttribute("categories", categoryService.getList());
		model.addAttribute("selectedCategory", categoryId);
		model.addAttribute("visiable", "t");
		return "review_form";
	}
	
	@PostMapping("/review/create/{categoryId}")
	public String reviewCreate(@Valid ReviewDTO reviewDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "review_form";
		}
		
		this.reviewService.create(reviewDTO);
		
		return String.format("redirect:/review/list/%s", reviewDTO.getCategory().getId());
	}
	
	@GetMapping("/review/modify/{id}")
	public String reviewModify(ReviewDTO reviewDTO, @PathVariable("id") Long id, 
			Model model) {
		ReviewDTO dto = this.reviewService.getReview(id);
		reviewDTO.setTitle(dto.getTitle());
		reviewDTO.setContent(dto.getContent());
		model.addAttribute("visiable", "f");
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
