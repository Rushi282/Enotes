package com.enote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FavouriteNoteDto {

	private Integer id;
	
	private NoteDto note;
	
	private Integer userId;
}
