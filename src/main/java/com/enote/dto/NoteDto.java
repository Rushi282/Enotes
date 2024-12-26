package com.enote.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoteDto {

	private Integer id;
	
	private String title;
	
	private String description;
	
	private CategoryDto category;
	
	private Integer createdBy;
	
	private Date createdOn;
	
	private Integer updatedBy;
	
	private Date updatedOn;
	
	private FileDto fileDetails;
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	public static class FileDto{
		
		private Integer id;
		
		private String originalFileName;
		
		private String displayFileName;
	
	}
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	public static class CategoryDto{
		
		private Integer id;
		
		private String name;
	}
}
