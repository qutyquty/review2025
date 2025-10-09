package com.mysite.review.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDTO {
	
    private Long id;
    
    private String title; // 영화 제목
    private String name; // TV 제목
    
    @JsonProperty("overview")
    private String overview;
    
    @JsonProperty("poster_path")
    private String posterPath;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    // 공통적으로 사용할 수 있는 메서드
    public String getDisplayTitle() {
        return (title != null && !title.isEmpty()) ? title : name;
    }

}
