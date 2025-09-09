package com.myproject.www.config;

import java.util.List;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;

/* WebConfig
 * 
 * > getRootConfigClasses(), getServletConfigClasses(), getServletMappings(), getSetvletFilters(), customizeRegistration() 을 @Override
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > AbstractAnnotationConfigDispatcherServletInitializer 를 상속하여 Servlet 3.0 이상의 환경에서 XML 없이 순수 Java 설정
 *   으로 Spring MVC 애플리케이션을 초기화하는 클래스
 *   
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > getRootConfigClasses() 는 ContextLoaderListener 가 생성할 루트 애플리케이션 컨텍스트 설정 클래스 목록을 반환하며
 * 	 주로 비즈니스 로직·공통 컴포넌트·보안 설정 등을 담는 @Configuration 클래스들 (RootConfig, SecurityConfig) 을 지정
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > getServletConfigClasses() 는 DispatcherServlet 전용 애플리케이션 컨텍스트 설정 클래스 목록을 반환하며
 *   Spring MVC (@EnableWebMvc, ViewResolver, HandlerMapping 등) 관련 설정을 담은 ServletConfigration 클래스를 지정
 *   
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > getServletMappings() 는 DispatcherServlet이 매핑될 URL 패턴을 지정하며 "/" 로 설정하면 모든 요청을 DispatcherServlet 이 처리
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > getServletFilters() 는 DispatcherServlet 앞단에 적용할 Filter 배열을 반환
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > ustomizeRegistration() 는 DispatcherServlet 등록 시점에 추가로 설정을 주입할 때 사용
 * 	 - throwExceptionIfNoHandlerFound=true 는 매핑된 핸들러가 없을 때 404가 아닌 NoHandlerFoundException 예외를 던지도록 함
 * 
 *   - 업로드 파일을 저장할 디스크 경로(uploadLocation)와 파일별 최대 크기, 전체 요청 크기, 임시 저장 기준 등을 지정하여
 *     Servlet 3.0 방식의 파일 업로드를 지원
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * */
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer{

	
	// Override
	@Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class[] {RootConfig.class, SecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		
		return new Class[] {ServletConfiguration.class};
	}

	@Override
	protected String[] getServletMappings() {

		return new String[] {"/"};
	}
	
	// CharacterEncodingFilter 를 등록해 요청(request)·응답(response) 모두 UTF-8 로 강제 인코딩하도록 설정
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter enco = new CharacterEncodingFilter();
		
		enco.setEncoding("UTF-8");
		enco.setForceEncoding(true);

		return new Filter[] {enco};
	}

	@Override
	protected void customizeRegistration(Dynamic registration) {
		// 초기화
		 // 경로 지정
		String uploadLocation = "D:\\Jstl_Servlet_Spring\\spring_myproject_up";
		 // 파일 용량 사이즈 설정 
		int maxFileSize = 1024 * 1024 * 20;
		 // 파입 업로드 시 최대 용량 
		int maxReqSize = maxFileSize * 3;
		 // 
		int fileSizeThreshold = maxFileSize;
		
		// Multipart 설정 
		MultipartConfigElement multPartElem = new MultipartConfigElement(uploadLocation, maxFileSize, maxReqSize, fileSizeThreshold);
		
		// 404 설정 
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		
		registration.setMultipartConfig(multPartElem);
	}

}
