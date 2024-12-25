package com.enote.service;

import java.util.Collection;

import com.enote.dto.CategoryDto;
import com.enote.dto.CategoryResponse;

public interface ICategoryService {

	Boolean add(CategoryDto category) throws Exception;
	
	Collection<CategoryDto> all();

	Collection<CategoryResponse> activeCategory();

	CategoryDto categoryById(Integer id);

	Boolean deleteCategory(Integer id);
	
	
}
