package com.enote.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enote.dao.CategoryRepository;
import com.enote.dao.FileRepository;
import com.enote.dao.NoteRepository;
import com.enote.dto.NoteDto;
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
		if(noteRepo.existsByTitle(noteDto.getTitle())) {
			throw new ResourceAlreadyExistException(noteDto.getTitle() + " is already present.");
		}
		
		categoryRepo.findByIdAndIsDeletedFalse(noteDto.getCategory().getId())
		.orElseThrow(()-> new ResourceNotFoundException("Invalid category id."));
		
		Note newNote = mapper.map(noteDto, Note.class);
		
		FileDetails fileDetails = saveFileDetails(file);
		if(!ObjectUtils.isEmpty(fileDetails)) {
			newNote.setFileDetails(fileDetails);
		}else {
			newNote.setFileDetails(null);
		}
		Note savedNote = noteRepo.save(newNote);
		NoteDto savedNoteDto = mapper.map(savedNote, NoteDto.class);
		return savedNoteDto;
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

}
