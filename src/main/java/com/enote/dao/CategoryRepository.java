package com.enote.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByIsActiveTrue();

	Optional<Category> findByIdAndIsDeletedFalse(Integer id);

	List<Category> findByIsDeletedFalse();

	List<Category> findByIsActiveTrueAndIsDeletedFalse();

	Boolean existsByName(String name);

}
