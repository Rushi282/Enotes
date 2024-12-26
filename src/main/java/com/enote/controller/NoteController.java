package com.enote.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enote.dto.GenericResponse;
import com.enote.dto.NoteDto;
import com.enote.service.NoteService;

@RestController
@RequestMapping("/api/v1/note")
public class NoteController {
	
	@Autowired
	private NoteService noteService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createNote(@RequestBody NoteDto noteDto){
		NoteDto addedNote = noteService.addNote(noteDto);
		return GenericResponse.buildResponse("Success", "Note added", addedNote, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> allNotes(){
		Collection<NoteDto> allNotes = noteService.getAllNotes();
		if(allNotes.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return GenericResponse.buildResponse("Success", "All notes", allNotes, HttpStatus.OK);
	}

}
