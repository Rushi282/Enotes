package com.enote.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NotePageDto {

	private List<NoteDto> noteDtos;
	
	private Integer pageNo;
	
	private Integer pageSize;
	
	private Long totalElements;
	
	private Integer totalPages;
	
	private Boolean isFirst;
	
	private Boolean isLast;
}
