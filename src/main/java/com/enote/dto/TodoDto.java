package com.enote.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TodoDto {

	private Integer id;
	
	private String title;
	
	private String description;
	
	private TodoPriorityDto priority;
	
	private TodoStatusDto status;
	
	private Integer createdBy;
	
	private Date createdOn;
	
	private Integer updatedBy;
	
	private Date updatedOn;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	@Builder
	public static class TodoStatusDto{
		private Integer id;
		private String name;
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	@Builder
	public static class TodoPriorityDto{
		private Integer id;
		private String name;
	}
}
