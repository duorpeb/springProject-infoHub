package com.myproject.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.myproject.www.security.CustomAuthUserService;
import com.myproject.www.security.LoginFailureHandler;
import com.myproject.www.security.LoginSuccessHandler;


/* SecurityConfig
 * 
 * > @EnableWebSecurity 은 Spring Security 필터 체인을 활성화하고 WebSecurityConfigurerAdapter 를 통해 보안 설정을 커스터마이징할 수 있도록 해줌
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > extends WebSecurityConfigurerAdapter 를 상속받음으로써 기본 보안 설정 및 필요한 메서드를 오버라이드해 인증·인가 로직을 정의
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > bcPasswordEncoder() : 비밀번호 암호화 Bean
 * 
 *   # 사용자 비밀번호를 BCrypt 해시 방식으로 암호화/검증하며 이후 AuthenticationManagerBuilder 나 DaoAuthenticationProvider 에서 이 빈을 주입받아 사용
 *   
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > authSuccessHandler() / authFailureHandler() : 로그인 성공/실패 핸들러 Bean 
 *  
 *   # LoginSuccessHandler 와 LoginFailureHandler 는 직접 구현하는 클래스로
       로그인 성공·실패 시 추가 로직 (e.g., 리다이렉트, 메시지 처리 등) 을 이들 핸들러에서 제어할 수 있음
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > customDetailService() : 사용자 정보 서비스(UserDetailsService) Bean
 * 
 *   # CustomAuthUserService 는 DB에서 사용자 정보를 조회해 UserDetails 객체로 변환해 주는 커스텀 구현체
 *     로 이 빈은 인증 처리 시 아이디로 사용자 조회 역할을 함
 *     
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > DaoAuthenticationProvider Bean 
 * 
 *   # DaoAuthenticationProvider 는 UserDetailsService 로 사용자 조회를 한 뒤 PasswordEncoder 로 비밀번호를 비교하여 
 *     인증 로직(예외 포장 등) 을 실행
 *     
 *   # setHideUserNotFoundExceptions(false)를 통해, 아이디 미등록 시 UsernameNotFoundException이 그대로 노출되도록 함  
 *   
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > configure(AuthenticationManagerBuilder auth) throws Exception 
 * 
 *   # 커스텀 DaoAuthenticationProvider 를 AuthenticationManager에 등록하고 주입된 UserDetailsService와 PasswordEncoder를 사용해 인증 처리
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > configure(HttpSecurity http) throws Exception : 인가 및 CSRF·폼 로그인·로그아웃 설정
 * 
 *   # CharacterEncodingFilter 는 CSRF 토큰 전송 과정에서 한글이 깨지는 현상을 방지
 * 
 *   # authorizeRequests() 는 경로별로 hasRole(), permitAll(), authenticated() 등을 사용해 인가 규칙 지정
 *   
 *   # formLogin() 는 로그인 폼 파라미터 이름, 로그인 페이지 URL, 성공/실패 핸들러 지정
 *   
 *   # logout() 는 로그아웃 처리 URL, 세션 무효화, 쿠키 삭제, 성공 리다이렉트 경로 설정
 *   
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > 전체 흐름 
 *   
 *   # 인증(Authentication)
 *   	- 로그인 요청 → DaoAuthenticationProvider → UserDetailsService + PasswordEncoder
 *   
 *   # 인가(Authorization)
 *   	- 요청된 URL → HttpSecurity의 authorizeRequests() 규칙 검사
 *   
 *   # 인코딩 및 CSRF 처리
 *   	- CharacterEncodingFilter 적용 → CSRF 토큰 유효성 검사
 *   
 *   # 로그인/로그아웃 핸들링
 *   	- 폼 로그인 성공·실패 → 커스텀 핸들러
 *   
 *   	- 로그아웃 → 세션 무효화·쿠키 삭제
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	// 비밀번호 암호화 Bean
	@Bean
	public PasswordEncoder bcPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 로그인 성공 핸들러 
	@Bean 
	public AuthenticationSuccessHandler authSuccessHandler() {
		// LoginSuccessHandler() 은 커스텀 구현체 
		return new LoginSuccessHandler();
	}
	
	// 로그인 실패 핸들러
	@Bean
	public AuthenticationFailureHandler authFailureHandler() {
		// LoginFailureHandler() 은 커스텀 구현체 
		return new LoginFailureHandler();
	}
	
	// 이 빈은 인증 처리 시 아이디로 사용자 조회 역할을 하며 DB 에서 사용자 정보를 조회해 UserDetails 객체로 변환
	@Bean
	public UserDetailsService customDetailService() {
		// CustomAuthUserService() 은 커스텀 구현체
		return new CustomAuthUserService();
	}
	
	
	// 인증 로직 수행 
	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		// 초기화
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		// 사용자 조회
		provider.setUserDetailsService(userDetailsService);
		// 비밀번호 비교 
		provider.setPasswordEncoder(passwordEncoder);
		
		// 아이디가 틀린지 비밀번호가 틀린지 확인하기 위한 구문
		 // setHideUserNotFoundExceptions(false)를 통해, 아이디 미등록 시 UsernameNotFoundException 이 그대로 노출
		provider.setHideUserNotFoundExceptions(false);
		
		return provider;
	}

	
	// 인증 처리 
	 // provider (커스텀한 DaoAuthenticationProvider) 를 AuthenticationManager에 등록하고 주입된 UserDetailsService 와 PasswordEncoder 를 사용해 인증 처리
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 인증용 객체 생성 매니저 설정
		auth.authenticationProvider(authenticationProvider(customDetailService(), bcPasswordEncoder()));
	}

	
	
	// 인가 및 CSRF·폼 로그인·로그아웃 설정
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// CSRF 토큰 전송 과정에서 한글이 깨지는 현상을 방지하기 위한 코드

		CharacterEncodingFilter filter = new CharacterEncodingFilter("utf-8");
   	 // CharacterEncodingFilter 에 지정한 인코딩(UTF-8 등)을 요청(request)과 응답(response) 모두에 강제 적용하도록 함
		 // 이 옵션이 없으면 이미 커넥션에 설정된 인코딩이 우선권을 가져 멀티파트나 CSRF 토큰 전송 시 한글이 깨질 수 있음
		filter.setForceEncoding(true);
		 // Spring Security의 필터 체인에서 CsrfFilter가 동작하기 바로 전 단계에 CharacterEncodingFilter 를 삽입하라는 의미
		 // 순서상 CSRF 토큰을 읽어들이기 전에 요청의 인코딩이 이미 올바르게 설정되어 있어야 토큰 값이 깨지지 않고 정상 검증될 수 있기 때문에
		   // 반드시 CSRF 필터보다 앞에 추가하는 것
		http.addFilterBefore(filter, CsrfFilter.class);
		
		
		// authorizeRequests() 는 경로별로 hasRole(), permitAll(), authenticated() 등을 사용해 인가 규칙 지정
		 // antMatchers() 는 접근 경로를 의미 
		 // permitAll() 은 누구나 접근 가능한 경로를 의미, authenticated() 는 인증된 사용자만 접근 가능한 경로를 의미
		 // hasRole('권한') 은 해당 권한을 확인하며 ROLE_ 를 자동으로 포함하여 확인하기에 DB 에 권한을 부여하는 경우 "ROLE_권한" 형태로 작성해야 함 
		http.authorizeRequests().antMatchers("/user/report").hasRole("ADMIN")
		 // 이 사이에 페이지 작성할 때 마다 추가 .antMatchers()
//		.antMatchers("/", "/user/login", "/user/register")
		.anyRequest().permitAll();
		
		// formLogin() 는 로그인 폼 파라미터 이름, 로그인 페이지 URL, 성공/실패 핸들러 지정
		http.formLogin().usernameParameter("id").passwordParameter("pwd")
		.loginPage("/user/login")
		.successHandler(authSuccessHandler()).failureHandler(authFailureHandler());
		
		
		/* logout() 
		 * 
		 * > logout() 은 는 로그아웃 처리 URL, 세션 무효화, 쿠키 삭제, 성공 리다이렉트 경로 설정
		 * 
		 * > 이렇게 설정하면, POST /user/logout 요청은 곧장 LogoutFilter 로 들어가며 
		 *   LogoutFilter 가 세션 무효화 (SecurityContextLogoutHandler 호출), 쿠키 삭제
		 *   , 리다이렉트(/ 등) 등의 과정을 필터 단계에서 처리해 버리기 때문에 컨트롤러로 
		 *   요청이 넘어오지 않음
		 *   
		 * */ 
		http.logout().logoutUrl("/user/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/");
	}
	
}
