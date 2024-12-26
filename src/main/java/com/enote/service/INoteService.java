package com.enote.service;

import java.util.Collection;

import com.enote.dto.NoteDto;

public interface INoteService {

	NoteDto addNote(NoteDto noteDto);
	
	Collection<NoteDto> getAllNotes();
}
