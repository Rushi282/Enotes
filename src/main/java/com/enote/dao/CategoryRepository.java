package com.enote.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
