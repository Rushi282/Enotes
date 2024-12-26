package com.enote.service;

import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enote.dao.CategoryRepository;
import com.enote.dao.NoteRepository;
import com.enote.dto.NoteDto;
import com.enote.entity.Note;
import com.enote.exception.ResourceAlreadyExistException;
import com.enote.exception.ResourceNotFoundException;

@Service
public class NoteService implements INoteService {
	
	@Autowired
	private NoteRepository noteRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public NoteDto addNote(NoteDto noteDto) {
	
		Boolean isExist = noteRepo.existsByTitle(noteDto.getTitle());
		if(isExist) {
			throw new ResourceAlreadyExistException(noteDto.getTitle() + " is already present.");
		}
		
		categoryRepo.findByIdAndIsDeletedFalse(noteDto.getCategory().getId())
				.orElseThrow(()-> new ResourceNotFoundException("Invalid category id."));
		
		Note newNote = mapper.map(noteDto, Note.class);
		Note savedNote = noteRepo.save(newNote);
		
		NoteDto savedNoteDto = mapper.map(savedNote, NoteDto.class);
		
		return savedNoteDto;
	}

	@Override
	public Collection<NoteDto> getAllNotes() {
		
		return noteRepo.findAll().stream().map(note -> mapper.map(note, NoteDto.class)).toList();
	}

}
