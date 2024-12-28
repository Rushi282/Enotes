package com.enote.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enote.dao.CategoryRepository;
import com.enote.dao.FavouriteNoteRepository;
import com.enote.dao.FileRepository;
import com.enote.dao.NoteRepository;
import com.enote.dto.FavouriteNoteDto;
import com.enote.dto.NoteDto;
import com.enote.dto.NoteDto.FileDto;
import com.enote.dto.NotePageDto;
import com.enote.entity.FavouriteNote;
import com.enote.entity.FileDetails;
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
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Autowired
	private FileRepository fileRepo;
	
	@Autowired
	private FavouriteNoteRepository favouriteNoteRepo;

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
	public NoteDto addNoteWithFile(NoteDto noteDto, MultipartFile file) throws IOException {
		
		if(!ObjectUtils.isEmpty(noteDto.getId())) {
			updateNote(noteDto, file);
		}
		
		
		if(noteRepo.existsByTitle(noteDto.getTitle())) {
			throw new ResourceAlreadyExistException(noteDto.getTitle() + " is already present.");
		}
		
		categoryRepo.findByIdAndIsDeletedFalse(noteDto.getCategory().getId())
		.orElseThrow(()-> new ResourceNotFoundException("Invalid category id."));
		
		noteDto.setIsDeleted(false);
		noteDto.setDeletedOn(null);
		
		Note newNote = mapper.map(noteDto, Note.class);
		
		FileDetails fileDetails = saveFileDetails(file);
		if(!ObjectUtils.isEmpty(fileDetails)) {
			newNote.setFileDetails(fileDetails);
		}else {
			if(ObjectUtils.isEmpty(noteDto.getId())) {
				newNote.setFileDetails(null);				
			}
		}
		Note savedNote = noteRepo.save(newNote);
		NoteDto savedNoteDto = mapper.map(savedNote, NoteDto.class);
		return savedNoteDto;
	}

	private void updateNote(NoteDto noteDto, MultipartFile file) {
		Note existingNote = noteRepo.findById(noteDto.getId())
		.orElseThrow(() -> new ResourceNotFoundException("Note not found of id: "+noteDto.getId()));
		
		if(file.isEmpty()) {
			FileDto fileDto = mapper.map(existingNote.getFileDetails(), FileDto.class);
			noteDto.setFileDetails(fileDto);
		}
		
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if(!file.isEmpty() && !ObjectUtils.isEmpty(file)) {
			String originalFilename = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFilename);
			
			List<String> extensions = Arrays.asList("pdf","xlsx","jpg","jpeg","docx","png");
			
			if(!extensions.contains(extension)) {
				throw new IllegalArgumentException(extension+" file format not allowed!");
			}
			
			FileDetails fileDetails = new FileDetails();
			fileDetails.setOriginalFileName(originalFilename);
			
			fileDetails.setDisplayFileName(getDisplayName(originalFilename));
			
			String randomString = UUID.randomUUID().toString();
			String uploadFileName = randomString+"."+extension;
			fileDetails.setUploadedFileName(uploadFileName);
			
			fileDetails.setFileSize(file.getSize());
			
			File newFile = new File(uploadPath);
			if(!newFile.exists()) {
				newFile.mkdir();
			}
			String storePath = uploadPath+File.separator+uploadFileName;
			fileDetails.setPath(storePath);	
			
			long fileBytes = Files.copy(file.getInputStream(), Paths.get(storePath));
			if(fileBytes !=0) {
				return fileRepo.save(fileDetails);
			}
		}
		return null;
	}

	private String getDisplayName(String originalFilename) {
		//java_programming.pdf --- original file name
		//java_pro.pdf --- display file name
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);
		if(fileName.length()>10) {
			fileName = fileName.substring(0, 9);
		}
		return fileName+"."+extension;
	}

	@Override
	public Collection<NoteDto> getAllNotes() {
		return noteRepo.findAll().stream().map(note -> mapper.map(note, NoteDto.class)).toList();
	}

	public byte[] downloadFile(FileDetails foundFileDetails) throws IOException {
		InputStream resourseFile = new FileInputStream(foundFileDetails.getPath());
		return StreamUtils.copyToByteArray(resourseFile);
	}

	public FileDetails getFileDetails(Integer id) {
		return fileRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found of id: "+id));
	}

	public NotePageDto getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Note> userNotePage = noteRepo.findByCreatedByAndIsDeletedFalse(userId, pageable);
		List<Note> notes = userNotePage.getContent();
		List<NoteDto> noteDtos = notes.stream().map(note -> mapper.map(note, NoteDto.class)).toList();
		int currentPage = userNotePage.getNumber();
		long totalElemets = userNotePage.getTotalElements();
		int totalPages = userNotePage.getTotalPages();
		boolean isFirst = userNotePage.isFirst();
		boolean isLast = userNotePage.isLast();
		
		return NotePageDto.builder().isFirst(isFirst).isLast(isLast).noteDtos(noteDtos).pageNo(currentPage).pageSize(userNotePage.getSize()).totalElements(totalElemets).totalPages(totalPages).build();
	}

	public void softDeleteNote(Integer id) {
		Note existingNote = noteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Note not found of id: "+id));
		existingNote.setIsDeleted(true);
		existingNote.setDeletedOn(LocalDateTime.now());
		noteRepo.save(existingNote);
	}

	@Override
	public NoteDto restoreNoteById(Integer id) {
		Note existingNote = noteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Note not found of id: "+id));
		existingNote.setIsDeleted(false);
		existingNote.setDeletedOn(null);
		Note restoredNote = noteRepo.save(existingNote);
		return mapper.map(restoredNote, NoteDto.class);
	}

	@Override
	public List<NoteDto> getUserRecycleBin(Integer userId) {
		Collection<Note> usersDeletedNotes = noteRepo.findByCreatedByAndIsDeletedTrue(userId);
		return usersDeletedNotes.stream().map(note -> mapper.map(note, NoteDto.class)).toList();
	}

	@Override
	public void hardDeleteNote(Integer id) {
		Note existingNote = noteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Note not found of id: "+id));
		
		if(existingNote.getIsDeleted()) {
			noteRepo.delete(existingNote);
		}else {
			throw new IllegalArgumentException("Sorry!! cannot delete directly");
		}
	}

	@Override
	public void deleteUsersNotesFromRecycleBin(Integer userId) {
		Collection<Note> softDeletedNotes = noteRepo.findByCreatedByAndIsDeletedTrue(userId);
		if(!softDeletedNotes.isEmpty()) {
			noteRepo.deleteAll(softDeletedNotes);
		}
	}

	@Override
	public void favNote(Integer noteId) {
		int userId = 1;
		Note existingNote = noteRepo.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note not found of id: "+noteId));
		
		FavouriteNote favouriteNote = FavouriteNote.builder()
				.note(existingNote)
				.userId(userId).build();
		favouriteNoteRepo.save(favouriteNote);
	}

	@Override
	public void unFavNote(Integer favNoteId) {
		FavouriteNote existingFavNote = favouriteNoteRepo.findById(favNoteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note not found of id: "+favNoteId));
		favouriteNoteRepo.delete(existingFavNote);
	}

	@Override
	public List<FavouriteNoteDto> getFavNotesOfUser() {
		int userId = 1;
		List<FavouriteNote> userFavNotes = favouriteNoteRepo.findByUserId(userId);
		return userFavNotes.stream().map(fav -> mapper.map(fav, FavouriteNoteDto.class)).toList();
	}

	@Override
	public NoteDto copyNote(Integer noteId) {
		Note existingNote = noteRepo.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note not found of id: "+noteId));
		
		Note copyNote = Note.builder()
				.category(existingNote.getCategory())
				.deletedOn(null)
				.description(existingNote.getDescription())
				.fileDetails(null)
				.isDeleted(false)
				.title(existingNote.getTitle()+" copy")
				.build();
		
		// TODO : need to check user validation
		Note savedNote = noteRepo.save(copyNote);
		return mapper.map(savedNote, NoteDto.class);
	}

}
