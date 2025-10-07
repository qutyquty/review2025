package com.mysite.review.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mysite.review.dto.CreditsResponse;
import com.mysite.review.dto.MovieDTO;
import com.mysite.review.dto.MovieSearchResponse;

import reactor.core.publisher.Mono;

@Service
public class MovieService {
	
	private final WebClient webClient;
	
	private static final String API_KEY = "a5b57bbbffa91e1b1a044b0516974c3c";

    public MovieService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public MovieDTO getMovieById(Long movieId) {
        return webClient.get()
                .uri("/movie/{id}?api_key={apiKey}&language=ko-KR", movieId, API_KEY)
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block(); // 동기적으로 결과 받기
    }
    
    public MovieDTO getTmdbById(Long movieId, Integer categoryId) {
    	String searchType = categoryId == 1 ? "movie" : "tv";
    	
        return webClient.get()
                .uri("/{searchType}/{id}?api_key={apiKey}&language=ko-KR", searchType, movieId, API_KEY)
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block(); // 동기적으로 결과 받기
    }
    
    public CreditsResponse getMovieCredits(Long movieId) {
        return webClient.get()
                .uri("/movie/{id}/credits?api_key={apiKey}&language=ko-KR", movieId, API_KEY)
                .retrieve()
                .bodyToMono(CreditsResponse.class)
                .block();
    }

    public CreditsResponse getTmdbCredits(Long movieId, Integer categoryId) {
    	String searchType = categoryId == 1 ? "movie" : "tv";
    	
        return webClient.get()
                .uri("/{searchType}/{id}/credits?api_key={apiKey}&language=ko-KR", searchType, movieId, API_KEY)
                .retrieve()
                .bodyToMono(CreditsResponse.class)
                .block();
    }    

    public Mono<MovieSearchResponse> searchMovie(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("api_key", API_KEY)
                        .queryParam("query", query)
                        .queryParam("language", "ko-KR")
                        .build())
                .retrieve()
                .bodyToMono(MovieSearchResponse.class);
    }
    
    public Mono<MovieSearchResponse> searchTmdb(String query, Integer categoryId) {
    	String searchType = categoryId == 1 ? "/movie" : "/tv";
    	
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search" + searchType)
                        .queryParam("api_key", API_KEY)
                        .queryParam("query", query)
                        .queryParam("language", "ko-KR")
                        .build())
                .retrieve()
                .bodyToMono(MovieSearchResponse.class);
    }
    
}
