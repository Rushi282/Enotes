package com.enote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.FavouriteNote;

public interface FavouriteNoteRepository extends JpaRepository<FavouriteNote, Integer> {

	List<FavouriteNote> findByUserId(int userId);

}
