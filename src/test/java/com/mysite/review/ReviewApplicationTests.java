package com.mysite.review;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.review.dto.ReviewDTO;
import com.mysite.review.service.ReviewService;

@SpringBootTest
class ReviewApplicationTests {

	@Autowired
	private ReviewService reviewService;
	
	@Test
	void testJpa() {
		for (int i = 1; i <= 300; i++) {
			String title = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			ReviewDTO dto = ReviewDTO.builder()
					.title(title)
					.content(content)
					.build();
			this.reviewService.create(dto);
		}
	}

}
