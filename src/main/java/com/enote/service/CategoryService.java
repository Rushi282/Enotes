package com.enote.service;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enote.dao.CategoryRepository;
import com.enote.dto.CategoryDto;
import com.enote.dto.CategoryResponse;
import com.enote.entity.Category;
import com.enote.exception.ResourceAlreadyExistException;
import com.enote.exception.ResourceNotFoundException;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean add(CategoryDto categoryDto) throws Exception {
//		Category category = new Category();
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIsActive(categoryDto.getIsActive());
		
		Boolean isexist = categoryRepo.existsByName(categoryDto.getName());
		if(isexist) {
			throw new ResourceAlreadyExistException(categoryDto.getName()+" category is already present");
		}
		
		Category category = mapper.map(categoryDto, Category.class);
		
		if(ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false);
//			category.setCreatedBy(null);
//			category.setCreatedOn(new Date());
		}else {
			updateCategory(category);
		}
		Category savedCategory = categoryRepo.save(category);
		if (ObjectUtils.isEmpty(savedCategory)) {
			throw new Exception("Failed to add Category");
		}
		return true;
	}

	private void updateCategory(Category category) {
		Category existingCategory = categoryRepo.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("category not found of id: "+ category.getId()));
		
		category.setCreatedBy(existingCategory.getCreatedBy());
		category.setCreatedOn(existingCategory.getCreatedOn());
		category.setIsDeleted(existingCategory.getIsDeleted());
//		category.setUpdatedBy(null);
//		category.setUpdatedOn(new Date());
	}

	@Override
	public Collection<CategoryDto> all() {
//		 List<Category> categories = categoryRepo.findAll();
		List<Category> categories = categoryRepo.findByIsDeletedFalse();
		 
		 
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
//		List<Category> categories = categoryRepo.findByIsActiveTrue();
		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
		return categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
	}

	@Override
	public CategoryDto categoryById(Integer id) {
//		Category foundCat = categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("category not found of id: "+id));
		Category foundCat = categoryRepo.findByIdAndIsDeletedFalse(id)
				.orElseThrow(()-> new ResourceNotFoundException("category not found of id: "+id));
		return mapper.map(foundCat, CategoryDto.class);
	}

	@Override
	public Boolean deleteCategory(Integer id) {
		Category foundCat = categoryRepo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("category not found of id: "+id));
		foundCat.setIsDeleted(true);
		categoryRepo.save(foundCat);
		return true;
	}

}
