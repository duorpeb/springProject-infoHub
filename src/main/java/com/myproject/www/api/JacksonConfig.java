package com.myproject.www.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {
	/** objectMapper() 
	 * 
	 * */
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
    // LocalDateTime 등의 JavaTimeModule 등록
    om.registerModule(new JavaTimeModule());
    // 알 수 없는 속성 무시 설정 등 추가 구성
    om.configure(
      com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
      false
    );
    return om;
	}
}
