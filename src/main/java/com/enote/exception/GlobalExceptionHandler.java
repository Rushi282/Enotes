package com.enote.exception;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleException(Exception e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

//	ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
	}
	
//	ResourceAlreadyExistException
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ProblemDetail handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
	}
	
//	IOException
	@ExceptionHandler(IOException.class)
	public ProblemDetail handleIOException(IOException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}
	
//	FileNotFoundException
	@ExceptionHandler(FileNotFoundException.class)
	public ProblemDetail handleFileNotFoundException(FileNotFoundException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
	}
}
