package com.mysite.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.review.entity.Category;
import com.mysite.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	Page<Review> findAll(Pageable pageable);
	
	Page<Review> findAll(Specification<Review> spec, Pageable pageable);
	
	@Query("select "
		       + "distinct r "
		       + "from Review r "
		       + "where "
		       + "   (r.category = :category) "
		       + "   and ( "
		       + "   r.title like %:kw% "
		       + "   or r.content like %:kw% "
		       + "   )")
	Page<Review> findAllByKeywordAndCategory(@Param("kw") String kw, @Param("category") Category category, Pageable pageable);

}
