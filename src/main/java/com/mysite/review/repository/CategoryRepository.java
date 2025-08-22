package com.mysite.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.review.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
