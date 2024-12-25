package com.enote.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enote.dto.CategoryDto;
import com.enote.dto.CategoryResponse;
import com.enote.service.ICategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	
	@Autowired
	private ICategoryService categoryService;

	@PostMapping("/add-category")
	public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto){
		Boolean isAdded = categoryService.add(categoryDto);
		if(isAdded) {
			return new ResponseEntity<>("Category added successfully.",HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("Failed to add Category",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/category")
	public ResponseEntity<?> getAllCategory(){
		Collection<CategoryDto> allCategories = categoryService.all();
		if(CollectionUtils.isEmpty(allCategories)) {
			return ResponseEntity.noContent().build();
		}else {
			return ResponseEntity.ok(allCategories);
		}
	}
	
	@GetMapping("/active-category")
	public ResponseEntity<?> getAllActiveCategory(){
		Collection<CategoryResponse> allCategories = categoryService.activeCategory();
		if(CollectionUtils.isEmpty(allCategories)) {
			return ResponseEntity.noContent().build();
		}else {
			return ResponseEntity.ok(allCategories);
		}
	}
}
