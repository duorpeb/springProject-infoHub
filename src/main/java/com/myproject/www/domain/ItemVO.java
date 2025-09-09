package com.myproject.www.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO {
	private String title;
	private String originallink;
	private String link;
	private String description;
	
	/** LocalDateTime pubDate 의 Error
	 * 
	 * > java.time.LocalDateTime은 ISO 8601 표준을 따르는 yyyy-MM-dd'T'HH:mm:ss 형식의 날짜와 시간만 처리할 수 있는데
	 *   JSON에서 넘어온 pubDate의 형식은 요일, 월 이름, 타임존 오프셋(+0900) 등을 포함하고 있어 LocalDateTime 의 
	 *   기본 파싱 형식과 맞지 않는 문제가 발생
	 *   
	 * > @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss Z", locale = "en") 를 작성하면 
	 *   Error 가 발생하지 않음 
	 *   
	 * > ItemVO의 pubDate 필드 타입을 java.time.OffsetDateTime 또는 java.util.Date로 변경
	 * 
	 * > JSON 형식에 맞춰 LocalDateTIme 을 OffsetDateTime 으로 변경하고, 포맷 지정
	 * 
	 * */ 
	/*
	 * @JsonFormat(shape= JsonFormat.Shape.STRING, pattern =
	 * "EEE, dd MMM yyyy HH:mm:ss Z", locale = "en") private LocalDateTime pubDate;
	 */
}
