package com.enote.service;

import java.util.Collection;

import com.enote.dto.CategoryDto;
import com.enote.dto.CategoryResponse;

public interface ICategoryService {

	Boolean add(CategoryDto category);
	
	Collection<CategoryDto> all();

	Collection<CategoryResponse> activeCategory();
	
	
}
