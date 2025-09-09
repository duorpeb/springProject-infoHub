package com.myproject.www.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

/*
 * > @ControllerAdvice
 * 
 * */
@Slf4j
@ControllerAdvice
public class CommonExceptionAdvice {
	/* handler404
	 * 
	 * */
	
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handler404(NoHandlerFoundException nfe, Model e) {
		// 확인
		log.info("Exception : {}", nfe.getMessage());
		e.addAttribute("exception", nfe.getMessage());
		
		return "custom404";
	}
	
	
//	@ExceptionHandler(Exception.class)
//	public String exception(Exception ex, Model m) {
//		// 다른 예외를 처리하고 싶다면 새로 페이지를 만들어 연결
//		return "0";
//	}
}
