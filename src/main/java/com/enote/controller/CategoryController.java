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
import com.enote.dto.GenericResponse;
import com.enote.service.ICategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	
	@Autowired
	private ICategoryService categoryService;

	@PostMapping("/add-category")
	public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) throws Exception{
		categoryService.add(categoryDto);
//		return new ResponseEntity<>("Category added successfully.",HttpStatus.CREATED);
		return GenericResponse.buildResponse("Success", "Category added.", null, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllCategory(){
		Collection<CategoryDto> allCategories = categoryService.all();
		if(CollectionUtils.isEmpty(allCategories)) {
			return GenericResponse.buildResponse("Success", "Categories not available.", allCategories, HttpStatus.NO_CONTENT);
		}else {
			return GenericResponse.buildResponse("Success", "All Categories", allCategories, HttpStatus.OK);
		}
	}
	
	@GetMapping("/active-category")
	public ResponseEntity<?> getAllActiveCategory(){
		Collection<CategoryResponse> allCategories = categoryService.activeCategory();
		if(CollectionUtils.isEmpty(allCategories)) {
			return GenericResponse.buildResponse("Success", "Categories not available.", allCategories, HttpStatus.NO_CONTENT);
		}else {
			return GenericResponse.buildResponse("Success", "All Categories", allCategories, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id){
		CategoryDto categoryDto = categoryService.categoryById(id);
		return GenericResponse.buildResponse("Success", "Found category", categoryDto, HttpStatus.OK);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) throws Exception{
		Boolean isDeleted = categoryService.deleteCategory(id);
		if(isDeleted) {
			return GenericResponse.buildResponse("Success", "Category deleted successfully.", null, HttpStatus.OK);
		}else {
			throw new Exception("failed to delete");
		}
	}
	
}
