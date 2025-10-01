package com.mysite.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CastDTO {
	
	private String name;       // 배우 이름
    private String character;  // 배역 이름

    @JsonProperty("profile_path")
    private String profilePath; // 배우 사진 경로

}
