package com.myproject.www.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/* LoginSuccessHandler
 * 
 * > LoginSuccessHandler 클래스는 로그인 성공 후 실행되는 커스텀 핸들러
 *   로 크게 세 가지 기능을 수행
 *   
 * > implements AuthenticationSuccessHandler 는 Spring Security 가 로그인 성공 시
 *   호출하는 인터페이스
 *  
 * >    
 *   
 *   
 * */
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	// 초기화
	 // DefaultRedirectStrategy() 는 리다이렉트 수행 객체로 redirect 데이터를 가지고 
	 // 경로로 이동하는 역할을 수행
	private RedirectStrategy redStr = new DefaultRedirectStrategy();
	
	 // Session 의 Cash 정보를 담을 변수로 HttpSessionRequestCache() 는 
	 // Session 의 캐쉬 정보와 직전의 URL 정보를 가지고 있음 
	private RequestCache reqCache = new HttpSessionRequestCache();

	
	/* onAuthenticationSuccess()
	 * 
	 * > LoginSuccessHandler 는 로그인 성공 시 사용되는 Handler 로 사용자가 원하는 작업을 실행 (분기)
	 * 
	 * > 로그인 성공 시 하는 작업
	 * 	#1 로그인 후 가야하는 경로 설정
	 * 
	 *  #2 로그인 실패 기록 삭제
	 *  	- Security 에서 Login 을 시도해서 실패하면 실패기록이 남게 되는데 로그인 성공 시 
	 *      이 실패 기록을 지워야 함 
	 *      
	 *    - 세션에 남아 있는 WebAttributes.AUTHENTICATION_EXCEPTION 속성을 제거하여 
	 *      과거 로그인 실패 정보가 남아 재로그인 시에도 실패 상태가 될 수있는 가능성을 없앰
	 *      
	 *    - 실패 기록을 활용해서 5회 이상 틀릴 경우 잠금과 같은 기능을 구현할 수 있음 
	 * */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 초기화
		 /* HttpSession ses = request.getSession();
		  * 
		  * > HttpSession ses = request.getSession(); 는 세션 객체를 불러오는 코드
		  * 
		  * > 웹 애플리케이션에서 세션 (Session) 은 클라이언트 (브라우저) 와 서버 간의 상태 (state) 를 
		  *   유지하기 위해 서버가 HTTP 요청 간에 별도의 저장 공간을 제공하는 메커니즘
		  *   
		  * > HTTP는 기본적으로 무상태 (stateless) 프로토콜이기 때문에 로그인 정보, 장바구니 내역, 
		  *   사용자 설정 같은 연속된 대화를 유지하려면 세션이 필요
		  *   
		  * > getSession() 호출 시 기존에 클라이언트로부터 전달된 세션 ID (JSESSIONID 쿠키 등)
		  *   가 없으면 새 세션을 만들고 있으면 해당 세션 객체를 반환
		  * 
		  * */ 
		HttpSession ses = request.getSession();
		 // 기본 리다이렉트 경로 설정
		String authUrl = "/";
		
		/* if(ses == null) { return; }
		 * 
		 * > if(ses == null) { return; } 는 세션 객체가 존재할 때만 작업을 수행하겠다는 의미로
		 *   이 null 체크는 NullPointerException 방지용이며 세션이 있을 때만 실패 기록을 지우는 
		 *   안전장치 역할을 수행
		 * 
		 * > request.getSession() 는 기본적으로 세션이 없으면 새로 생성해 주지만 Security 내부에서 
		 *   호출될 땐 이미 없을 수도 있음
		 *   
		 * > 세션이 존재할 때만, 다음 줄의 로그인 실패 예외 기록 삭제를 안전하게 수행
		 * */ 
		if(ses == null) { return; }
		
		else {
			// Session 의 로그인 실패 기록 삭제 
			ses.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
		
		// 로그인 직전 요청이 있으면 그 URL로, 없으면 기본 경로로 리다이렉트
		// response 객체를 가지고 redirect 경로로 이동 
		SavedRequest saveReq = reqCache.getRequest(request, response);
		
		redStr.sendRedirect(request, response, saveReq != null ? saveReq.getRedirectUrl() : authUrl);
	}

}
