package com.mysite.review.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.mysite.review.dto.CastDTO;
import com.mysite.review.dto.CommentDTO;
import com.mysite.review.dto.CreditsResponse;
import com.mysite.review.dto.MovieDTO;
import com.mysite.review.dto.MovieSearchResponse;
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
	public String detail(Model model, @PathVariable("id") Long id, CommentDTO commentDTO) {
		ReviewDTO reviewDTO = this.reviewService.getReview(id);
		Integer categoryId = reviewDTO.getCategory().getId();
		
		Long movId = reviewDTO.getTmdbId();
		String title = reviewDTO.getTitle();
		int idx = title.indexOf("(");
		if (idx != -1) {
			title = title.substring(0, idx);
		}
		
		// movId(tmdb id) 가 null 이면
		// movId(tmdb id) 가 있으면 배우리스트를 가져와서 보여준다.
		if (movId == null) {
			String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
			//return "redirect:/movies/search?title=" + encodedTitle + "&reviewId=" + id + "&category=" + categoryId;

			model.addAttribute("castList", null);
	        model.addAttribute("movie", null);
			model.addAttribute("review", reviewDTO);
			return "review_detail";
		} else {
			//MovieDTO movie = movieService.getMovieById(movId);
			//CreditsResponse credits = movieService.getMovieCredits(movId);
			
			MovieDTO movie = movieService.getTmdbById(movId, categoryId);
			CreditsResponse credits = movieService.getTmdbCredits(movId, categoryId);
			
			List<CastDTO> castList = Optional.ofNullable(credits.getCast())
			        .orElse(Collections.emptyList());
			
			int castListCount = castList.size() < 8 ? castList.size() : 8;

			for (int i = 0; i < castListCount; i++) {
			    CastDTO actor = castList.get(i);
			    if (actor == null) {
			        System.out.println("배우 " + i + "번이 null 입니다.");
			    } else {
			        System.out.println("배우 " + i + "번: name=" + actor.getName() 
			                           + ", profilePath=" + actor.getProfilePath());
			    }
			}
			
//			List<CastDTO> safeCastList = credits.getCast().stream()
//			        .filter(Objects::nonNull) // actor 자체가 null인 경우 제거
//			        .filter(actor -> actor.getProfilePath() != null) // profilePath 없는 경우 제거
//			        .collect(Collectors.toList());
	        
			model.addAttribute("castList", credits.getCast());
	        model.addAttribute("movie", movie);
			model.addAttribute("review", reviewDTO);
			//model.addAttribute("base_path", "https://image.tmdb.org/t/p/w300_and_h450_bestv2");
			
			return "review_detail";
		}
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

	@GetMapping("/review/searchtmdb/{categoryId}")
	public String reviewCreateTmdb(Model model, 
			@PathVariable("categoryId") Integer categoryId) {
		// tmdb 검색 화면
		model.addAttribute("categories", categoryService.getList());
		model.addAttribute("selectedCategory", categoryId);
		model.addAttribute("title", "");
		return "review_tmdb";
	}

	@GetMapping("/movies/search")
	public String search(@RequestParam("title") String title, 
			@RequestParam("reviewId") Long reviewId,
			@RequestParam("category") Integer category,
			Model model) {
		// tmdb 검색 화면 - 영화 제목으로 포스터 가져오기
		//MovieSearchResponse response = movieService.searchMovie(title).block(); // 동기 처리
		MovieSearchResponse response = movieService.searchTmdb(title, category).block(); // 동기 처리
		
		model.addAttribute("categories", categoryService.getList());
		model.addAttribute("selectedCategory", category);
		model.addAttribute("title", title);
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("movies", response.getResults());
		return "review_tmdb";
	}
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/movies/select")
    public String selectMovie(@RequestParam("movieId") Long movieId,
    		@RequestParam("movieTitle") String movieTitle,
    		@RequestParam("reviewId") Long reviewId,
    		@RequestParam("categoryId") Integer categoryId,
    		Principal principal) {
		
		// reviewId>0: review에 있는 거는 tmdb_id만 update
		// reviewId=0: review에 없는 거는 tmdb_id, title 등을 insert
		if (reviewId > 0) {
			System.out.println("================> reviewId: " + reviewId);
			this.reviewService.updateTmdb(reviewId, movieId);
		} else {
			// tmdb 검색 화면 - 선택한 포스터로 tmdb 영화 id 가져와서 저장
			Category category = this.categoryService.getCategory(categoryId);
			SiteUser siteUser = this.userService.getUser(principal.getName());
			this.reviewService.createTmdb(movieTitle, movieId, category, siteUser);
		}
		
		return String.format("redirect:/review/list/%s", "1");
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
