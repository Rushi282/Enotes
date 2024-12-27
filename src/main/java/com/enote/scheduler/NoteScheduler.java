package com.enote.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enote.dao.NoteRepository;
import com.enote.entity.Note;

@Component
public class NoteScheduler {
	
	@Autowired
	private NoteRepository noteRepo;
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteNoteScheduler() {
		LocalDateTime dayToBeDelete = LocalDateTime.now().minusDays(7);
		List<Note> deletedNotes=noteRepo.findAllByIsDeletedAndDeletedOnBefore(true,dayToBeDelete);
		noteRepo.deleteAll(deletedNotes);
	}

}
