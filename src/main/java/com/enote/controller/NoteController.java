package com.enote.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enote.dto.FavouriteNoteDto;
import com.enote.dto.GenericResponse;
import com.enote.dto.NoteDto;
import com.enote.dto.NotePageDto;
import com.enote.entity.FileDetails;
import com.enote.service.INoteService;
import com.enote.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/note")
public class NoteController {
	
	@Autowired
	private INoteService noteService;
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> createNote(@RequestBody NoteDto noteDto){
		NoteDto addedNote = noteService.addNote(noteDto);
		return GenericResponse.buildResponse("Success", "Note added", addedNote, HttpStatus.CREATED);
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> createNoteWithFile(@RequestPart String noteDto, 
			@RequestPart(required = false) MultipartFile file) throws IOException{
		NoteDto dto = convertToNoteDto(noteDto);
		NoteDto addedNote = noteService.addNoteWithFile(dto, file);
		return GenericResponse.buildResponse("Success", "Note created", addedNote, HttpStatus.CREATED);
	}
	
	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> allNotes(){
		Collection<NoteDto> allNotes = noteService.getAllNotes();
		if(allNotes.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return GenericResponse.buildResponse("Success", "All notes", allNotes, HttpStatus.OK);
	}
	
	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
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
	
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> deleteNote(@PathVariable Integer id){
		noteService.softDeleteNote(id);
		return GenericResponse.buildResponse("Success", "Note deleted of id "+id, null, HttpStatus.OK);
	}

	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreNote(@PathVariable Integer id){
		NoteDto restoredNote = noteService.restoreNoteById(id);
		return GenericResponse.buildResponse("Success", "Note Restored of id "+id, restoredNote, HttpStatus.OK);
	}
	
	@GetMapping("/recycleBin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> userRecycleBin(){
		Integer userId = 1;
		List<NoteDto> notesInBin = noteService.getUserRecycleBin(userId);
		if(notesInBin.isEmpty()) {
			return GenericResponse.buildResponse("Success", "Recycle bin is empty.", notesInBin, HttpStatus.OK);
		}
		return GenericResponse.buildResponse("Success", "Notes from recycle bin.", notesInBin, HttpStatus.OK);
	}
	
	@DeleteMapping("/hard-delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNote(@PathVariable Integer id){
		noteService.hardDeleteNote(id);
		return GenericResponse.buildResponse("Success", "Note deleted permanently of id: "+id, null, HttpStatus.OK);
	}
	
	@DeleteMapping("/hard-delete-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> deleteAllFromBin(){
		Integer userId = 1;
		noteService.deleteUsersNotesFromRecycleBin(userId );
		return GenericResponse.buildResponse("Success", "All notes deleted from bin.", null, HttpStatus.OK);
	}
	
	@GetMapping("/fav/{noteId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId){
		noteService.favNote(noteId);
		return GenericResponse.buildResponse("Success", "Note added to favourite", null, HttpStatus.CREATED);
	}
	
	@GetMapping("/un-fav/{favNoteId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> UnFavouriteNote(@PathVariable Integer favNoteId){
		noteService.unFavNote(favNoteId);
		return GenericResponse.buildResponse("Success", "Note removed from favourite", null, HttpStatus.OK);
	}
	
	@GetMapping("/fav-note")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllUserFavouriteNote(){
		List<FavouriteNoteDto> favNotesOfUser = noteService.getFavNotesOfUser();
		if(favNotesOfUser.isEmpty()) {
			return GenericResponse.buildResponse("Success", "No Favourite notes.", null, HttpStatus.OK);
		}
		return GenericResponse.buildResponse("Success","Favourite notes", favNotesOfUser, HttpStatus.OK);
	}
	
	@GetMapping("/copy/{noteId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNote(@PathVariable Integer noteId){
		NoteDto copiedNote = noteService.copyNote(noteId);
		return GenericResponse.buildResponse("Success", "Note copied.", copiedNote, HttpStatus.CREATED);
	}
}
