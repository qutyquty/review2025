package com.mysite.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDTO {
	
    private Long id;
    private String title;
    
    @JsonProperty("overview")
    private String overview;
    
    @JsonProperty("poster_path")
    private String posterPath;

}
