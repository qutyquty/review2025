package com.mysite.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.review.DataNotFoundException;
import com.mysite.review.entity.Category;
import com.mysite.review.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public List<Category> getList() {
		return this.categoryRepository.findAll();
	}
	
	public Category getCategory(Integer id) {
		Optional<Category> oc = this.categoryRepository.findById(id);
		
		if (oc.isPresent()) {
			return oc.get();
		} else {
			throw new DataNotFoundException("category not found");
		}
	}

}
