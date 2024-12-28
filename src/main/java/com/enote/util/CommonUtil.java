package com.enote.util;

import org.apache.commons.io.FilenameUtils;

import com.enote.dto.TodoDto;
import com.enote.dto.TodoDto.TodoPriorityDto;
import com.enote.dto.TodoDto.TodoStatusDto;
import com.enote.enums.TodoPriority;
import com.enote.enums.TodoStatus;

public class CommonUtil {

	public static String getContentType(String originalFileName) {
		String extension = FilenameUtils.getExtension(originalFileName);
		
		switch(extension) {
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
		case "txt":
			return "text/plain";
		case "jpg":
		case "jpeg":
			return "image/jpeg";
		case "png":
			return "image/png";
		case "docx":
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		default:
			return "application/octet-stream";
		}
	}
	
	public static void validateTodoStatus(TodoDto todoDto) {
		TodoStatusDto reqStatus = todoDto.getStatus();
		TodoStatus[] statuses = TodoStatus.values();
		
		Boolean statusFound = false;
		for (TodoStatus todoStatus : statuses) {
			if(todoStatus.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		
		if(!statusFound) {
			throw new IllegalArgumentException("Invalid status !!");
		}
	}
	
	public static void validateTodoPriority(TodoDto todoDto) {
		TodoPriorityDto reqPriority = todoDto.getPriority();
		TodoPriority[] priorities = TodoPriority.values();
		
		Boolean foundPriority = false;
		for(TodoPriority todoPriority : priorities) {
			if(todoPriority.getId().equals(reqPriority.getId())) {
				foundPriority = true;
			}
		}
		
		if(!foundPriority) {
			throw new IllegalArgumentException("Invalid priority !!");
		}
	}
}
