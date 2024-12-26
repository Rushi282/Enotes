package com.enote.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

import com.enote.dto.NoteDto;

public interface INoteService {

	NoteDto addNote(NoteDto noteDto);
	
	NoteDto addNoteWithFile(NoteDto noteDto, MultipartFile file) throws IOException;
	
	Collection<NoteDto> getAllNotes();
}
