package com.enote.controller;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enote.dto.GenericResponse;
import com.enote.dto.NoteDto;
import com.enote.dto.NotePageDto;
import com.enote.entity.FileDetails;
import com.enote.service.NoteService;
import com.enote.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@PostMapping("/add")
	public ResponseEntity<?> createNoteWithFile(@RequestPart String noteDto, 
			@RequestPart(required = false) MultipartFile file) throws IOException{
		NoteDto dto = convertToNoteDto(noteDto);
		NoteDto addedNote = noteService.addNoteWithFile(dto, file);
		return GenericResponse.buildResponse("Success", "Note created", addedNote, HttpStatus.CREATED);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws IOException{
		FileDetails foundFileDetails = noteService.getFileDetails(id);
		byte[] file = noteService.downloadFile(foundFileDetails);
		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(foundFileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", foundFileDetails.getOriginalFileName());
		return ResponseEntity.ok().headers(headers).body(file);
	}

	private NoteDto convertToNoteDto(String noteString) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(noteString, NoteDto.class);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> allNotes(){
		Collection<NoteDto> allNotes = noteService.getAllNotes();
		if(allNotes.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return GenericResponse.buildResponse("Success", "All notes", allNotes, HttpStatus.OK);
	}
	
	@GetMapping("/user-notes")
	public ResponseEntity<?> allNotesByUser(
			@RequestParam(name ="pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name ="pageSize", defaultValue = "5") Integer pageSize
			){
		Integer userId = 1;
		NotePageDto allNotesByUser = noteService.getAllNotesByUser(userId, pageNo, pageSize);
		if(allNotesByUser.getNoteDtos().isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return GenericResponse.buildResponse("Success", "All notes of user : "+userId, allNotesByUser, HttpStatus.OK);
	}

}
