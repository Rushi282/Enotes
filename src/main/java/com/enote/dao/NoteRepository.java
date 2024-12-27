package com.enote.dao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {

	Boolean existsByTitle(String title);
	
	Page<Note> findByCreatedByAndIsDeletedFalse(Integer id, Pageable pageable);
	
	Collection<Note> findByCreatedByAndIsDeletedTrue(Integer userId);

	List<Note> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime dayToBeDelete);

	

}
