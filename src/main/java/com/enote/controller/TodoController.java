package com.enote.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enote.dto.GenericResponse;
import com.enote.dto.TodoDto;
import com.enote.service.ITodoService;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

	@Autowired
	private ITodoService todoService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addTodo(@RequestBody TodoDto dto){
		return GenericResponse.buildResponse("Success", "ToDo created", todoService.addTodo(dto), HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getTodo(@PathVariable Integer id){
		return GenericResponse.buildResponse("Success", "ToDo of id: "+id, 
				todoService.getTodoById(id), HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getTodosOfUser(){
		Collection<TodoDto> usersTodo = todoService.getTodosByUser();
		if(usersTodo.isEmpty()) {
			return GenericResponse.buildResponse("Success", "No ToDo available.", usersTodo, HttpStatus.NO_CONTENT);
		}
		return GenericResponse.buildResponse("Success", "All ToDo's", usersTodo, HttpStatus.OK);
	}
}
