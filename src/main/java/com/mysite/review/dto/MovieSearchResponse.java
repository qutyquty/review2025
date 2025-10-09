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

        // 영화 제목
        private String title;

        // TV 제목
        private String name;

        @JsonProperty("original_title")
        private String originalTitle;

        @JsonProperty("release_date")
        private String releaseDate;

        private String overview;

        @JsonProperty("poster_path")
        private String posterPath;
        
        /**
         * 영화/TV 구분 없이 공통적으로 사용할 수 있는 제목
         */
        public String getDisplayTitle() {
            return (title != null && !title.isEmpty()) ? title : name;
        }
    }

}
