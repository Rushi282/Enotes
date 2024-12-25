package com.enote.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enote.dao.CategoryRepository;
import com.enote.entity.Category;

@Service
public class CategoryService implements ICategoryService{
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public Boolean add(Category category) {
		category.setIsDeleted(false);
		category.setCreatedBy(null);
		category.setCreatedOn(new Date());
		Category savedCategory = categoryRepo.save(category);
		if(ObjectUtils.isEmpty(savedCategory)){
			return false;
		}
		return true;
	}

	@Override
	public Collection<Category> all() {
		return categoryRepo.findAll();
	}

}
