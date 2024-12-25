package com.enote.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enote.dao.CategoryRepository;
import com.enote.dto.CategoryDto;
import com.enote.dto.CategoryResponse;
import com.enote.entity.Category;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean add(CategoryDto categoryDto) {
//		Category category = new Category();
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIsActive(categoryDto.getIsActive());
		
		Category category = mapper.map(categoryDto, Category.class);
		category.setIsDeleted(false);
		category.setCreatedOn(new Date());

		Category savedCategory = categoryRepo.save(category);
		if (ObjectUtils.isEmpty(savedCategory)) {
			return false;
		}
		return true;
	}

	@Override
	public Collection<CategoryDto> all() {
		 List<Category> categories = categoryRepo.findAll();
//		 List<CategoryDto> categoryDtos = new ArrayList<>();
//		 categories.stream().forEach(cat -> 
//					 {
//						 CategoryDto categoryDto = new CategoryDto();
//						 categoryDto.setCreatedBy(cat.getCreatedBy());
//						 categoryDto.setCreatedOn(cat.getCreatedOn());
//						 categoryDto.setDescription(cat.getDescription());
//						 categoryDto.setId(cat.getId());
//						 categoryDto.setIsActive(cat.getIsActive());
//						 categoryDto.setName(cat.getName());
//						 categoryDto.setUpdatedBy(cat.getUpdatedBy());
//						 categoryDto.setUpdatedOn(cat.getUpdatedOn());
//						 categoryDtos.add(categoryDto);
//					 }
//				 );
//		 return categoryDtos;
		 return categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
	}

	@Override
	public Collection<CategoryResponse> activeCategory() {
		List<Category> categories = categoryRepo.findByIsActiveTrue();
		return categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
	}

}
