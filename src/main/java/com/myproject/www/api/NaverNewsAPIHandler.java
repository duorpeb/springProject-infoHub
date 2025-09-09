package com.myproject.www.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


/** NaverNewsAPIHandler
 * 
 * 	> InputStream: 바이트 스트림으로부터 데이터를 읽기 위한 추상 클래스
 * 
 * 	> InputStreamReader: 바이트 스트림을 문자 스트림으로 변환합니다 (문자 인코딩 지정 가능
 * 
 * 	> UnsupportedEncodingException: 지원되지 않는 문자 인코딩을 사용할 때 발생하는 예외
 * 
 * 	> HttpURLConnection: HTTP 또는 HTTPS 요청을 보내고 응답을 받기 위한 클래스
 * 
 * 	> MalformedURLException: 유효하지 않은 URL 형식일 때 발생하는 예외
 * 
 * 	> URL: URL(Uniform Resource Locator) 객체를 표현
 * 
 * */
@Slf4j
@Component
public class NaverNewsAPIHandler {
	// 초기화
	 // Client 등록 키
	private String clientId = "nrTHWl1GXAGLIvmacd4I";
	private String clientSecret = "HbL30iOo34";
	
	
	
	/** getNews(String apiUrl, Map<String, String> requestHeaders) - API 호출과 응답 처리 로직
	 * 
	 * 	> Map, HashMap 은 요청 헤더를 키 · 값 형태의 쌍으로 저장하기 위한 선언 
	 * 
	 * */
	public String getNews(String searchKeyword, int start, int rows) {
		// 초기화 
		 // News API URL - XML 과 JSON 형식 중 JSON 형식으로 반환하는 URL 
		String apiUrl;
		 // apiUrl 의 query 뒤부분에 추가할 멤버 변수s
		String addUrlTxt;
		 //
		HttpURLConnection connector;
		 //
		Map<String, String> requestHeaders = new HashMap<>();
		
		
		// Set Client
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);

		/** 검색어 인코딩
		 * 
		 * 	> 한글 검색어를 URL 에 안전하게 포함시키기 위해 UTF-8 로 Encoding 하며 Exception 발생 시 
		 *  	RuntimeException 으로 Wrapping 하여 즉시 종료 
		 *  
		 *  > URLEncoder.encode("검색어", "Encoding 방식"); 의 형식으로 선언 
		 *  
		 * */  
		try {
			addUrlTxt = URLEncoder.encode(searchKeyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// 검색어 Encoding 하는 경우에서 예외 발생 시 예외 던짐
			throw new RuntimeException("검색어 인코딩 실패..!", e);
		}
		
		
		// API URL 초기화
		apiUrl = "https://openapi.naver.com/v1/search/news?query=" + addUrlTxt + "&start=" + start + "&display=" + rows; 
		
		// 연결 
		connector = connect(apiUrl);
		
		
		/**
		 * 
		 * */
		try {
			// method : 'GET'
			connector.setRequestMethod("GET");
			
			for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
				connector.setRequestProperty(header.getKey(), header.getValue());
			}
			
			// 응답 확인
			int responseCode = connector.getResponseCode();
			
			// 
			if(responseCode == HttpURLConnection.HTTP_OK) {
				return readBody(connector.getInputStream());
			} // 오류 발생 Case
				else {
					return readBody(connector.getErrorStream());
			}
			
			
		} catch (IOException e) {
			// 
			throw new RuntimeException("API 요청과 응답 실패", e);
			
		} finally { connector.disconnect(); }
	}
	
	
	/** HttpURLConnection connect(String apiUrl) - URL 의 객체화 및 커넥션 객체를 반환 
	 * 
	 * 	> URL, HttpURLConnection 은 HTTP 요청을 보내고 응답을 받기 위한 네트워크 관련 API 
	 * @throws IOException 
	 * 
	 * */
	private HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			
			return (HttpURLConnection)url.openConnection();
			
		} catch (MalformedURLException e) {
			// 
			throw new RuntimeException("API URL 이 잘못되었습니다..!" + apiUrl, e); 
		
		} catch (IOException e) {
			//
			throw new RuntimeException("연결이 실패했습니다..!" + apiUrl, e);
		}
	}
	
	
	/** readBody(InputStream body) - Stream 을 한 줄씩 읽어 String 으로 결합
	 * 
	 * 	> readBody(InputStream body) 는 Stream 을 한 줄씩 읽어 String 으로 결합하며 try-with-resources 로 
	 *    BufferedReader 자동 해제
	 * 
	 * 	> InputStream, InputStreamReader, BufferedReader 등은 입출력 스트림 처리에 사용
	 * 
	 * */
	private String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
		
		try(BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();
			
			String line;
			
			while((line = lineReader.readLine()) != null) { responseBody.append(line); }
			
			return responseBody.toString();
		
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다..!", e);
		} 
		
	}
	
}
