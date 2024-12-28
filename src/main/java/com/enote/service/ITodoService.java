package com.enote.service;

import java.util.Collection;

import com.enote.dto.TodoDto;

public interface ITodoService {
	
	TodoDto addTodo(TodoDto todoDto);
	
	TodoDto getTodoById(Integer id);
	
	Collection<TodoDto> getTodosByUser();
}
