package com.mysite.review.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MovieSearchResponse {
	
    private int page;
    private List<Movie> results;

    @Data
    public static class Movie {
        private int id;
        private String title;

        @JsonProperty("original_title")
        private String originalTitle;

        @JsonProperty("release_date")
        private String releaseDate;

        private String overview;

        @JsonProperty("poster_path")
        private String posterPath;
    }


}
