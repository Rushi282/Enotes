package com.enote.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {

	Boolean existsByTitle(String title);
	
	Page<Note> findByCreatedBy(Integer id, Pageable pageable);

}
