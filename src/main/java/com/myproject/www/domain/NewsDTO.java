package com.myproject.www.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
	/** @JsonProperty, @JsonFormat 
	 * 
	 * 	> JSON 필드명과 자바 필드명이 다르면 @JsonProperty 로 매핑
	 * 
	 *  >  JSON 필드명이 "lastBuildDate"이고, RFC_1123 포맷으로 오기 때문에 다음의 Annotation 사용
	 * 
	 * 	> Jackson 에게 날짜 문자열을 LocalDateTime 으로 바꿔 달라고 알려주려면 @JsonFormat 을 
	 *    꼭 붙여야 함 
	 * */ 
	@JsonProperty("lastBuildDate")
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss Z"
							,locale="en")
	private LocalDateTime lastBuildDate;
	private long total;
	private int start;
	private int display;
	
	@JsonProperty("items")
	private List<ItemVO> items;
}
