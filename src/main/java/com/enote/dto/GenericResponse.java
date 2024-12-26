package com.enote.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse {
	
	private HttpStatus httpStatus;
	
	private String status;
	
	private String message;
	
	private Object data;
	
	private ResponseEntity<?> create(){
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", status);
		response.put("message", message);
		if(!ObjectUtils.isEmpty(data)) {
			response.put("data", data);
		}
		return new ResponseEntity<>(response, httpStatus);
	}
	
	public static ResponseEntity<?> buildResponse(String sts, String msg, Object dt, HttpStatus code) {
		GenericResponse genericResponse = new GenericResponse(code, sts, msg, dt);
		return genericResponse.create();
	}

}
