package com.mysite.review.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MovieCreditDTO {
	
	private String title;
	private String releaseDate;
	private String character;
	private String posterPath;
	private String mediaType; // movie 또는 tv

}
