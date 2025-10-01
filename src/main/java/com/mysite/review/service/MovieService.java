package com.mysite.review.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mysite.review.dto.CreditsResponse;
import com.mysite.review.dto.MovieDTO;

@Service
public class MovieService {
	
	private final WebClient webClient;

    public MovieService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public MovieDTO getMovieById(Long movieId) {
    	String tmdb_apiKey = "a5b57bbbffa91e1b1a044b0516974c3c"; // TMDB사이트에서 제공받은 API키.
    	
        return webClient.get()
                .uri("/movie/{id}?api_key={apiKey}&language=ko-KR", movieId, tmdb_apiKey)
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block(); // 동기적으로 결과 받기
    }
    
    public CreditsResponse getMovieCredits(Long movieId) {
    	String tmdb_apiKey = "a5b57bbbffa91e1b1a044b0516974c3c"; // TMDB사이트에서 제공받은 API키.
    	
        return webClient.get()
                .uri("/movie/{id}/credits?api_key={apiKey}&language=ko-KR", movieId, tmdb_apiKey)
                .retrieve()
                .bodyToMono(CreditsResponse.class)
                .block();
    }

    
}
