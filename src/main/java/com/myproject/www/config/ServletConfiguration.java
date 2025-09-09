package com.myproject.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
/* ServletConfiguration
 * 
 * > Spring 이 이미 ServletConfig 를 가지고 있기 때문에 ServletConfigration 으로 클래스 이름을 설정
 * 
 * > @EnableScheduling
 *   - @Scheduled 어노테이션이 붙은 메서드를 스케줄러가 찾아 실행할 수 있도록 활성화합니다.
 *   
 * ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > @EnableWebMvc
 *   - Spring MVC의 기본 설정을 모두 로드하고, DispatcherServlet을 통한 요청 매핑·뷰 리졸버·핸들러 어댑터 등을 자동으로 등록
 *  
 * ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > @ComponentScan
 *   - 스프링이 빈으로 관리할 컴포넌트(@Controller, @Service, @Component, @Repository 등) 를 지정한 패키지들 내에서 탐색하여 자동 등록
 *   
 * ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > implements WebMvcConfigurer
 *   - XML 설정으로 하던 각종 MVC 고급 설정(Resource 핸들링, 인터셉터 등록, 메시지 컨버터 추가 등)을
 *     Java 메서드 오버라이드 방식으로 커스터마이즈할 수 있게 해줌 
 * 
 * ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * */
@EnableScheduling
@EnableWebMvc
@ComponentScan(basePackages = {"com.myproject.www.controller","com.myproject.www.service"
			,"com.myproject.www.handler","com.myproject.www.api"})
public class ServletConfiguration implements WebMvcConfigurer{

	// 정적 리소스 매핑 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	  /* registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	   * 
	   * > 정적 리소스(css, js, 이미지) 요청을 "/resources/**"로 매핑 
	   *   - /resources/** 요청은 webapp/resources/ 폴더 아래 정적 리소스를 매핑 
	   *   
	   * > /upload/** 요청은 로컬 드라이브 D:\...\_fileUpload\ 폴더의 파일을 직접 내려줌 
	   */
		 // addResourceHandler() 는 사용자가 정하는 이름, addResourceLocations 은 실제 경로를 의미 
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		
		
		// .addResourceLocations("file:///D:\\Jstl_Servlet_Spring\\") 처럼 루트 디렉터리를 가리키면 그 디렉터리와 모든 하위 폴더를 대상으로 리소스를 찾음
		registry.addResourceHandler("/upload/**")
		.addResourceLocations("file:///D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\");
		
	// /spring_myproject_up/profileImg/ 이하 요청을
    // D:\Jstl_Servlet_Spring\spring_myproject_up\profileImg\ 폴더로 매핑
    registry.addResourceHandler("/spring_myproject_up/profileImg/**")
            .addResourceLocations("file:///D:/Jstl_Servlet_Spring/spring_myproject_up/profileImg/");
	}

	/* InternalResourceViewResolver
	 * 
	 * > 논리 viewName(예: "home") → 실제 JSP 경로(/WEB-INF/views/home.jsp)로 변환
	 * 
	 * > JSTL 태그를 사용하려면 JstlView.class 지정
	 * 
	 * */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// 초기화
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		
		// 경로 설정
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setViewClass(JstlView.class);
		registry.viewResolver(viewResolver);
	}
	
	
	/* multipartResolver
	 * 
	 * > 스프링 MVC에서 <form enctype="multipart/form-data"> 요청을 받으면 이 빈이 자동으로 파일 파트(Part)를 분리해 주어,
	 *   컨트롤러의 @RequestParam("file") MultipartFile file 등에 바인딩할 수 있게 해줌
	 *   
	 * > multipartResolver 로 등록하지 않으면 스프링 MVC 가 MultipartResolver 를 탐색하지 못하고
	 *   multipart 요청이 일반 Servlet API (HttpServletRequest.getParts()) 형태로만 처리되어
	 *   스프링은 각 폼 필드를 Part (ApplicationPart) 로 보고 그걸 userVO.email 등 
	 *   String 타입에 바인딩하려다 실패 
	 * */
	@Bean(name="multipartResolver")
	public MultipartResolver multipartResolver() {
		// 초기화
		StandardServletMultipartResolver ssmr = new StandardServletMultipartResolver();
		
		return ssmr;
	}
}
