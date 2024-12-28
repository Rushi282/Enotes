package com.enote.service;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enote.dao.TodoRepository;
import com.enote.dto.TodoDto;
import com.enote.dto.TodoDto.TodoPriorityDto;
import com.enote.dto.TodoDto.TodoStatusDto;
import com.enote.entity.Todo;
import com.enote.enums.TodoPriority;
import com.enote.enums.TodoStatus;
import com.enote.exception.ResourceNotFoundException;
import com.enote.util.CommonUtil;

@Service
public class TodoService implements ITodoService {
	
	@Autowired
	private TodoRepository todoRepo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public TodoDto addTodo(TodoDto todoDto) {

		//validate ToDo status and priority
		CommonUtil.validateTodoStatus(todoDto);
		CommonUtil.validateTodoPriority(todoDto);
		
		Todo newTodo = mapper.map(todoDto, Todo.class);
		
		newTodo.setStatusId(todoDto.getStatus().getId());
		newTodo.setPriorityId(todoDto.getPriority().getId());
		
		Todo savedTodo = todoRepo.save(newTodo);
		
		 TodoDto dto = mapper.map(savedTodo, TodoDto.class);
		 setStatus(todoDto, savedTodo);
		 setPrority(todoDto, savedTodo);
		 return dto;
	}

	@Override
	public TodoDto getTodoById(Integer id) {
		Todo foundTodo = todoRepo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Todo not found of id: "+id));
		 TodoDto todoDto = mapper.map(foundTodo, TodoDto.class);
		 setStatus(todoDto, foundTodo);
		 setPrority(todoDto, foundTodo);
		return todoDto;
	}

	private void setPrority(TodoDto todoDto, Todo foundTodo) {
		for(TodoPriority priority : TodoPriority.values()) {
			if(priority.getId().equals(foundTodo.getPriorityId())) {
				TodoPriorityDto priorityDto = TodoPriorityDto.builder().id(priority.getId()).name(priority.getName()).build();
				todoDto.setPriority(priorityDto);
			}
		}
		
	}

	private void setStatus(TodoDto todoDto, Todo foundTodo) {
		for(TodoStatus status : TodoStatus.values()) {
			if(status.getId().equals(foundTodo.getStatusId())) {
				TodoStatusDto statusDto = TodoStatusDto.builder().id(status.getId()).name(status.getName()).build();
				todoDto.setStatus(statusDto);
			}
		}	
	}

	@Override
	public Collection<TodoDto> getTodosByUser() {
		Integer userId = 1;
		List<Todo> todos = todoRepo.findByCreatedBy(userId);
		 List<TodoDto> todoDtos = todos.stream().map(todo -> mapper.map(todo, TodoDto.class)).toList();
		 return todoDtos;
	}

}
