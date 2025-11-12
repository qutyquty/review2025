package com.mysite.review.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mysite.review.dto.MovieCreditDTO;
import com.mysite.review.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MovieController {
	
	private final MovieService movieService;
	
	@GetMapping("/actors/{id}/movies")
	public String getMoviesByActor(@PathVariable("id") Long id, Model model) {
		List<MovieCreditDTO> movies = movieService.getMoviesByActorId(id);
		
		log.info("======================> movies: {}", movies);
		
		model.addAttribute("movies", movies);
		return "review_movies";
	}

}
