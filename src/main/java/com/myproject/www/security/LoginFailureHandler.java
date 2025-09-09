package com.myproject.www.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/* LoginFailureHandler
 * 
 * > LoginFailureHandler 클래스는 Spring Security에서 로그인 실패 시 호출되어,
 *   실패 원인에 맞는 메시지를 생성하고 다시 로그인 페이지로 포워드해 주는 커스텀 핸들러
 * */
@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// 초기화
		 /* String authId = request.getParameter("id");
		  * 
		  * > 로그인이 성공했을 때만 인증 객체가 만들어지기에 실패 기록을 가져오기 위해선
		  *   아래와 같이 getParameter 로 값을 가져와야 함
		  * 
		  * > 로그인을 시도한 id 파라미터를 login.jsp 에서 꺼내서 보존
		  * 
		  * */ 
		String authId = request.getParameter("id");
		 // 에러 메시지를 담을 변수 
		String errMsg = null;
		
		 // 에러 메시지
		log.info("오류 발생 : {}", exception.getMessage().toString());
		
		/* 예외 타입에 따라 에러 메시지를 분기하여 처리
		 * 
		 * > BadCredentialsException (비밀번호 오류) 혹은 InternalAuthenticationServiceException 
		 *   (인증 관련 오류) 혹은 UsernameNotFoundException 가 발생하면 메시지를 저장 
		 * */ 
		if(exception instanceof UsernameNotFoundException) {
			
			errMsg = "아이디가 일치하지 않습니다..!";
			
		} else if(exception instanceof BadCredentialsException) {
			
			errMsg = "비밀번호가 일치하지 않습니다..!";
			
		} else if(exception instanceof InternalAuthenticationServiceException) {
			
			errMsg = "관리자에게 문의하세요";
			
		} else {
			
			errMsg = "관리자에게 문의하세요";
			
		}
	
		// 뷰로 전송할 속성 세팅
		request.setAttribute("id", authId);
		request.setAttribute("errMsg", errMsg);
		
		/* request.getRequestDispatcher("/user/login").forward(request, response);
		 * 
		 * > 로그인 페이지로 포워드하는 코드
		 * 
		 * > 기존 요청(request)․응답(response)을 그대로 살려 로그인 페이지에서 
		 *   ${id}, ${errMsg}로 폼과 에러 메시지를 렌더링할 수 있게 함
		 *   
		 * */
		request.getRequestDispatcher("/user/login").forward(request, response);
	}

}
