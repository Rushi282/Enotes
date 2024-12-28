package com.enote.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enote.dto.FavouriteNoteDto;
import com.enote.dto.NoteDto;
import com.enote.dto.NotePageDto;
import com.enote.entity.FileDetails;

public interface INoteService {

	NoteDto addNote(NoteDto noteDto);
	
	NoteDto addNoteWithFile(NoteDto noteDto, MultipartFile file) throws IOException;
	
	Collection<NoteDto> getAllNotes();
	
	byte[] downloadFile(FileDetails foundFileDetails) throws IOException;
	
	public FileDetails getFileDetails(Integer id);
	
	NotePageDto getAllNotesByUser(Integer userId,Integer pageNo, Integer pageSize);
	
	void softDeleteNote(Integer id);

	NoteDto restoreNoteById(Integer id);

	List<NoteDto> getUserRecycleBin(Integer userId);
	
	void hardDeleteNote(Integer id);

	void deleteUsersNotesFromRecycleBin(Integer userId);
	
	void favNote(Integer noteId);
	
	void unFavNote(Integer noteId);
	
	List<FavouriteNoteDto> getFavNotesOfUser();

	NoteDto copyNote(Integer noteId);
}
