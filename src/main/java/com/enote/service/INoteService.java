package com.enote.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

import com.enote.dto.NoteDto;
import com.enote.entity.FileDetails;

public interface INoteService {

	NoteDto addNote(NoteDto noteDto);
	
	NoteDto addNoteWithFile(NoteDto noteDto, MultipartFile file) throws IOException;
	
	Collection<NoteDto> getAllNotes();
	
	byte[] downloadFile(FileDetails foundFileDetails) throws IOException;
	
	public FileDetails getFileDetails(Integer id);
}
