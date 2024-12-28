package com.enote.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

	Optional<Todo> findByTitle(String title);

	List<Todo> findByCreatedBy(Integer userId);

}
