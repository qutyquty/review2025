package com.mysite.review.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.review.dto.CommentDTO;
import com.mysite.review.dto.CreditsResponse;
import com.mysite.review.dto.MovieDTO;
import com.mysite.review.dto.ReviewDTO;
import com.mysite.review.entity.Category;
import com.mysite.review.entity.SiteUser;
import com.mysite.review.service.CategoryService;
import com.mysite.review.service.MovieService;
import com.mysite.review.service.ReviewService;
import com.mysite.review.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
	
	private final ReviewService reviewService;
	private final CategoryService categoryService;
	private final UserService userService;
	private final MovieService movieService;
	
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
	public String detail(Model model, @PathVariable("id") Long id, CommentDTO commentDTO) throws Exception {
		ReviewDTO reviewDTO = this.reviewService.getReview(id);
		
		Long movId = reviewDTO.getTmdbId();
		MovieDTO movie = movieService.getMovieById(movId);
		CreditsResponse credits = movieService.getMovieCredits(movId);
        
		model.addAttribute("castList", credits.getCast());
        model.addAttribute("movie", movie);
		model.addAttribute("review", reviewDTO);
		model.addAttribute("base_path", "https://image.tmdb.org/t/p/w300_and_h450_bestv2");
		
		return "review_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/review/create/{categoryId}")
	public String reviewCreate(ReviewDTO reviewDTO, Model model, 
			@PathVariable("categoryId") Integer categoryId) {
		model.addAttribute("categories", categoryService.getList());
		model.addAttribute("selectedCategory", categoryId);
		return "review_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/review/create/{categoryId}")
	public String reviewCreate(@Valid ReviewDTO reviewDTO, BindingResult bindingResult, 
			Principal principal) {
		if (bindingResult.hasErrors()) {
			return "review_form";
		}
		
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.reviewService.create(reviewDTO, siteUser);
		
		return String.format("redirect:/review/list/%s", reviewDTO.getCategory().getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/review/modify/{id}")
	public String reviewModify(ReviewDTO reviewDTO, @PathVariable("id") Long id, 
			Model model, Principal principal) {
		
		ReviewDTO dto = this.reviewService.getReview(id);
		if (!dto.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다."); 
		}
		
		reviewDTO.setTitle(dto.getTitle());
		reviewDTO.setContent(dto.getContent());
		model.addAttribute("categories", categoryService.getList());
		model.addAttribute("selectedCategory", dto.getCategory().getId());
		
		return "review_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/review/modify/{id}")
	public String reviewModify(@Valid ReviewDTO reviewDTO, BindingResult bindingResult, 
			@PathVariable("id") Long id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "review_form";
		}
		ReviewDTO dto = this.reviewService.getReview(id);
		if (!dto.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.reviewService.modify(reviewDTO, id);
		return String.format("redirect:/review/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/review/delete/{id}")
	public String reviewDelete(@PathVariable("id") Long id, Principal principal) {
		ReviewDTO dto = this.reviewService.getReview(id);
		if (!dto.getAuthor().getUsername().equals(principal.getName()) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		
		this.reviewService.delete(id);
		return "redirect:/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/review/vote/{id}")
	public String reviewVoter(Principal principal, @PathVariable("id") Long id) {
		ReviewDTO dto = this.reviewService.getReview(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.reviewService.vote(dto, siteUser);
		return String.format("redirect:/review/detail/%s", id);
	}

}
