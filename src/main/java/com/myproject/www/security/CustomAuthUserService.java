package com.myproject.www.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.myproject.www.domain.UserVO;
import com.myproject.www.repository.UserDAO;

import lombok.extern.slf4j.Slf4j;

/* CustomUserService
 * 
 * > UserDetailsService 은 Spring Security 가 로그인 처리 시 호출하는 인터페이스로
 *   단 하나의 메서드 (여기선 loadUserByUsername) 만을 구현해야 함 
 * 
 * > 로그인 시도 시, Spring Security가 CustomAuthUserService.loadUserByUsername(id) 호출
 *   한 뒤 AuthUser 객체 (implements UserDetails) 로 래핑하여 반환  
 *   
 * > 반환한 AuthUser 객체를 Spring Security 가 PasswordEncoder로 비밀번호 검증 후 인증 처리
 * */
@Slf4j
public class CustomAuthUserService implements UserDetailsService{
	// 초기화
	@Autowired
	private UserDAO udao;
	
	// username 은 로그인을 시도하는 id 를 의미 
	@Override
	public UserDetails loadUserByUsername(String username) {
		/*
		 * > username, password 의 인증용 토큰을 DB 의 데이터와 비교하여 DB User 테이블에서
		 *   username 과 일치하는 객체를 리턴  
		 *   
		 * > return type 은 UserDetails 로 UserDetails 는 인증된 객체를 리턴하며 
		 *   리턴 받기 위해 <id, password, auth> 가 필요함 		
		 * */ 
		UserVO uvo = udao.getUser(username);
		
		// DB 에 해당 사용자의 정보가 없으면 UsernameNotFoundException 발생 
		 // Spring Security가 “아이디 없음”으로 로그인 실패 처리
		if(uvo == null) { throw new UsernameNotFoundException(username); }
		
		// DB 에 해당 사용자의 정보가 있으면 해당 사용자의 권한 정보를 가져와 권한 설정
		uvo.setAuthList(udao.getAuthList(username));
		log.info("User Info : {}", uvo);
		
		/*
		 * > UserDetails 는 <String id, String password, Collection<? extends GrantedAuthority> auth> 
		 *   의 형태
		 *   
		 * > AuthUser 클래스에서 만들어둔 3번 생성자를 통해 UserDetails 형태로 반환
		 * */ 
		return new AuthUser(uvo);
	}
}
