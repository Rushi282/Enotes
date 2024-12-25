package com.enote.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) throws Exception{
		categoryService.add(categoryDto);
		return new ResponseEntity<>("Category added successfully.",HttpStatus.CREATED);
	}
	
	@GetMapping("/")
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
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id){
		CategoryDto categoryDto = categoryService.categoryById(id);
		return ResponseEntity.ok(categoryDto);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) throws Exception{
		Boolean isDeleted = categoryService.deleteCategory(id);
		if(isDeleted) {
			return ResponseEntity.ok("Category deleted successfully");
		}else {
			throw new Exception("failed to delete");
		}
	}
	
}
