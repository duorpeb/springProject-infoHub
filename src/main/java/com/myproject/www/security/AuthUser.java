package com.myproject.www.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.myproject.www.domain.UserVO;

import lombok.Getter;

/* AuthUser
 * 
 * > Spring Security의 기본 User(implements UserDetails)를 상속하여, 
 *   도메인 객체(UserVO)를 그대로 인증·인가 정보로 활용할 수 있게 만든 래퍼
 * 
 * > User 클래스 는 Spring Security가 제공하는 기본 인증 객체로 사용자 이름(username), 
 *   , 비밀번호(password), 활성화 여부(enabled), 권한 목록(authorities) 등을 
 *   내부 필드로 가지고 있음
 * */
public class AuthUser extends User {
	// 초기화 
	private static final long serialVersionUID = 1L;
	
	@Getter
	private UserVO uvo;
	
	/* public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities)
	 * 
	 * > 기본 User 생성자 래핑
	 * 
	 * > Spring Security가 기대하는 시그니처(username, password, authorities)를 그대로 받아 
	 *   부모 생성자를 호출
	 *   
	 * > Collection 은 java.utill 의 Collection
	 * */ 
	public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	
	/* public AuthUser(UserVO uvo)
	 * 
	 * > uvo.getEmail(), uvo.getPwd()로 인증용 자격 증명(username, password) 설정
	 * 
	 * > uvo.getAuthList()(예: List<AuthVO>)를 스트림 처리해 각 AuthVO 에서 권한 문자열
	 *   (ROLE_ADMIN 등)을 꺼내 SimpleGrantedAuthority 객체로 매핑하고 
	 *   List<GrantedAuthority>로 수집(collection)
	 *   
	 * > super() 를 통해 부모 (User) 생성자에 전달하여 Spring Security 가 인가 (authorization) 
	 *   정보를 활용하도록 함
	 *   
	 * > 마지막에 this.uvo = uvo; 로 도메인 객체 전체를 보관 이는 컨트롤러나 View 에서 
	 *   추가 사용자 정보 (이름, 이메일, 프로필 등) 조회 시 편리
	 * */
	// UserVO 기반 생성자 
	public AuthUser(UserVO uvo) {
		super(uvo.getId(), uvo.getPwd(), uvo.getAuthList().stream().map(
				authVO -> new SimpleGrantedAuthority(authVO.getAuth()))
				.collect(Collectors.toList()));
		
		this.uvo = uvo;
	}

}
