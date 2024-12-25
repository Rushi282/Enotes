package com.enote.service;

import java.util.Collection;

import com.enote.entity.Category;

public interface ICategoryService {

	Boolean add(Category category);
	
	Collection<Category> all();
	
	
}
