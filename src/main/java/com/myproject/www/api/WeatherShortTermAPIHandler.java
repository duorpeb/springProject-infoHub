package com.myproject.www.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 	> 중기예보 조회서비스 - 일반 인증키 (Encoding)
 *  	87y%2BRknOldm6MoNnyuiRmheOm0QNAR8gc4RRrN%2Fh1m1cqLF%2FojtNfQSctxTSLt0aXUjMUeJhCwttbaMH2qg%2FWg%3D%3D
 * 
 * 	> 중기 예보 조회 서비스 - 일반 인증키 (Decoding)
 * 		87y+RknOldm6MoNnyuiRmheOm0QNAR8gc4RRrN/h1m1cqLF/ojtNfQSctxTSLt0aXUjMUeJhCwttbaMH2qg
 * 
 * 	> 예시좌표 - 강남 역삼1동(61, 125)
 * 
 * 
 * 	> 단기예보 조회서비스 - 일반 인증키 (Encoding) 
 * 		87y%2BRknOldm6MoNnyuiRmheOm0QNAR8gc4RRrN%2Fh1m1cqLF%2FojtNfQSctxTSLt0aXUjMUeJhCwttbaMH2qg%2FWg%3D%3D
 * 
 * 	> 단기예보 조회서비스 - 일반 인증키 (Decoding) 
 * 		87y+RknOldm6MoNnyuiRmheOm0QNAR8gc4RRrN/h1m1cqLF/ojtNfQSctxTSLt0aXUjMUeJhCwttbaMH2qg
 * 
 * */
@Component
@Slf4j
public class WeatherShortTermAPIHandler {
	// 초기화
	 // 인증키 (Encoding)
	private String serviceKey = "87y%2BRknOldm6MoNnyuiRmheOm0QNAR8gc4RRrN%2Fh1m1cqLF%2FojtNfQSctxTSLt0aXUjMUeJhCwttbaMH2qg%2FWg%3D%3D";

	
	/** getWeatherTMNAndTMX(int nx, int ny) - 당일 날씨 정보 (최저/최고 기온 등) 를 가져오기 위한 메서드
	 * 
	 * */
	public String getWeatherDaysInfo(String numOfRows, String pageNo, String baseTime, int nx, int ny) {
		// 초기화
		 // API 를 요청할 URL 
		String apiUrl;
		 // URL 과 연결할 변수 
		HttpURLConnection connector;
		 // baseDate 는 오늘날짜를 기준으로 출력 (API 의 base_date)
		String baseDate = LocalDate.now().toString().replace("-", "");
		 // baseTime 을 설정하기 위한 변수로 현재시간을 기준으로 02·05·08·11 시와 가장 가까운 시간대로 baseTime 을 설정
		 // baseTime 이전의 시간은 API 로 불러올 수 없음 
		String nowTime = LocalDateTime.now().toString().substring(11,13);
		
		if(baseTime.equals("0")) {
			switch(nowTime) {
				case "00" : case "01" : 
					baseDate = LocalDate.now().minusDays(1).toString().replace("-","");
					baseTime = "2300";
					break; 
					
				default :
					baseTime = "0200";
					break;
			}
			
		} else if(baseTime.equals("1")) {
				
				switch(nowTime) {
					case "00" : case "01" : 
						baseDate = LocalDate.now().minusDays(1).toString().replace("-","");
						baseTime = "2300";
						break;
						
					case "02" : case "03" : case "04" :
						baseTime = "0200";
						break;
					
					case "05" : case "06" : case "07" : 
						baseTime = "0500";
						break;
						
					case "08" : case "09" : case "10" :
						baseTime = "0800";
						break;
					
					case "11" : case "12" : case "13" :
						baseTime = "1100";
						break;	
					
					default : 
						baseTime = "1100"; break;
				}
		} 
		
		
		apiUrl= "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + serviceKey
						+ "&dataType=JSON" + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&base_date=" + baseDate + "&base_time=" + baseTime 
						+ "&nx=" + nx + "&ny=" + ny ;
		
		log.info("apiUrl : {}", apiUrl);
		
		
		// 연결 
		connector = connect(apiUrl);
		
		try {
			// method : 'GET'
			connector.setRequestMethod("GET");
			
			// 응답 확인
			int responseCode = connector.getResponseCode();
			
			if(responseCode == HttpURLConnection.HTTP_OK) {
				return readBody(connector.getInputStream());
				
			} else {
				return readBody(connector.getErrorStream());
			}
			
		} catch (IOException e) {
			throw new RuntimeException("API 요청 및 응답 실패..!", e);
			
		} finally { 
			// 연결 종료
			connector.disconnect(); 
		}
		
	}
	
	
	/** getWeather() -  
	 * 
	 * 	> 날씨 API 를 조회하기 위해 설정해야할 파라미터는 ServiceKey, numOfRows, pageNo, dataType, base_date
	 *    , base_time, nx (예보지점 x 좌표), ny (예보지점 y 좌표)
	 * 
	 * 		# 값을 고정할 파라미터는 ServiceKey, numOfRows, dataType
	 * 
	 * 		# numOfRows 는 출력할 데이터의 양을 지정하는 파라미터이고 dataType 은 JSON 으로 고정
	 * 
	 * 		# numOfRows 를 254 로 요청하면 1일치 모든 데이터가 출력되며 일수 * 254 로 필요한 일수만큼의 
	 *      정보를 요청할 수 있음	
	 *  
	 * 
	 * ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣
	 * 
	 * 	> LocalDate, LocalDateTime 사용 시 참고  
	 * 
	 * 		String today = LocalDate.now().toString().replace("-", ""); // 출력 : 20250703
				m.addAttribute("today", today);

				String baseDate = LocalDateTime.now().toString(); // 출력 : 2025-07-03T14:37:57.955066600
				String h = baseDate.substring(11,13); // 출력 : 14
				m.addAttribute("todayTime", baseDate);
				m.addAttribute("h", h);
	 * 
	 * */ 
	public String getWeather(int numOfRows, int pageNo, int nx, int ny) {
		// 초기화
		 // API 를 요청할 URL 
		String apiUrl;
		 // URL 과 연결할 변수 
		HttpURLConnection connector;
		 // baseDate 는 오늘날짜를 기준으로 출력 (API 의 base_date)
		String baseDate = LocalDate.now().toString().replace("-", "");
		 // baseTime 은 현재 시간과 가장 가까운 지정 시간대로 출력 (API 의 base_time) 
		String baseTime = ""; 
		 // baseTime 을 설정하기 위한 변수로 현재시간을 기준으로 02·05·08·11·14시와 가장 가까운 시간대로 baseTime 을 설정
		String nowTime = LocalDateTime.now().toString().substring(11,13);

		
		/** switch(nowTime) { ... } 
		 * 
		 * 	> switch(nowTime) { ... } 은 업데이트를 반영하기 위한 swtich 문 
		 * 
		 * 	> 4일 (당일, 내일, 내일모레, 글피) 은 단기 예보로 알려주고 나머지 3일 (그글피, 엿새, 이레) 은 중기 예보로 알려줌 
		 * 		
		 * 		# 5일까지 알려주지만 여기선 당일~4일까지의 정보만 활용
		 * 
		 * ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣
		 * 	> Java 12부터는 Switch Expressions 를 사용하여 switch 문의 case에 다중 값 (multiple values) 표기 가능
		 * 
		 * */
		switch(nowTime) {
			case "02" : case "03" : case "04" :
				baseTime = "0200";
				break;
			
			case "05" : case "06" : case "07" : 
				baseTime = "0500";
				break;
				
			case "08" : case "09" : case "10" :
				baseTime = "0800";
				break;
			
			case "11" : case "12" : case "13" :
				baseTime = "1100";
				break;
				
			case "14" : case "15" : case "16" : 
				baseTime = "1400"; 
				break;
				
			case "17" : case "18" : case "19" : 
				baseTime = "1700";
				break;
				
			case "20" : case "21" : case "22" : 
				baseTime = "2000";
				break;
				
			case "23" : case "24" : 
				baseTime ="2300";
				break;
			
			case "00" : case "01" : 
				baseDate = LocalDate.now().minusDays(1).toString().replace("-","");
				baseTime = "2300";
				break;
		}
		
		
		/** 파라미터 인코딩 
		 * 
		 *  URLEncoder.encode(numOfRows, "UTF-8");
		
		 * */
		
		
		/** API URL 초기화
		 * 
		 * > ﻿http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=인증키
		 *   &numOfRows=${numOfRows}&pageNo=${pageNo}&base_date=${baseDate}&base_time=${baseTime}
		 *   &nx=${nx}&ny=${ny}
		 *   
		 * > 예제 코드 
		 * 
		 *   http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=
		 * 	 87y%2BRknOldm6MoNnyuiRmheOm0QNAR8gc4RRrN%2Fh1m1cqLF%2FojtNfQSctxTSLt0aXUjMUeJhCwttbaMH2qg%2FWg%3D%3D
		 * 	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        &numOfRows=254&pageNo=1&dataType=JSON&base_date=20250703&base_time=0200&nx=61&ny=125
		 * 
		 * */
		apiUrl= "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + serviceKey
						+ "&dataType=JSON" + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&base_date=" 
						+ baseDate + "&base_time=" + baseTime + "&nx=" + nx + "&ny=" + ny ;
		
		log.info("apiUrl : {}", apiUrl);
		
		
		// 연결 
		connector = connect(apiUrl);
		
		try {
			// method : 'GET'
			connector.setRequestMethod("GET");
			
			// 응답 확인
			int responseCode = connector.getResponseCode();
			
			if(responseCode == HttpURLConnection.HTTP_OK) {
				return readBody(connector.getInputStream());
				
			} else {
				return readBody(connector.getErrorStream());
			}
			
		} catch (IOException e) {
			throw new RuntimeException("API 요청 및 응답 실패..!", e);
			
		} finally { 
			// 연결 종료
			connector.disconnect(); 
		}
		
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
			/** MalformedURLException 
			 * 
			 * 	> MalformedURLException 은 new URL(String spec) 혹은 URI.toURL() 등을 호출할 때 전달된 
			 *    URL 문자열이 RFC-2396 에 정의된 형식을 벗어나 있을 경우 던져지는 체크 예외(checked exception) 
			 * */ 
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
		// 초기화
		InputStreamReader streamReader = new InputStreamReader(body);
		
		try (BufferedReader lineReader = new BufferedReader(streamReader)){
			// 초기화
			StringBuilder responseBody = new StringBuilder();
			String line;
			
			while((line = lineReader.readLine()) != null) { responseBody.append(line); }
			
			return responseBody.toString();
			
		} catch (IOException e) {
			//
			throw new RuntimeException("API 응답을 읽는데 실패했습니다..!", e);
		}
	}
	
	
	/** getTMNAndTMXOfNowDays() - 당일의 최저/최고 기온을 
	 * 
	 * */
	
	
	
	/** printDaysOfNextWeekFromNow() - 현재 날짜부터 1주일 간의 일수를 배열 형태로 반환 
 	 * 
	 * */
	public String[] printDaysOfNextWeekFromNow(){
		// 초기화
		LocalDate today = LocalDate.now();
		 // return 배열
		String[] weekArr = new String[7];
		
		for(int i = 0; i < 7; i++) {
			weekArr[i] = today.plusDays(i).toString().substring(8);
			
			if(weekArr[i].startsWith("0")) {
				weekArr[i] = today.plusDays(i).toString().substring(9);
			}
		}
		
		return weekArr;
	}
	
	
	/** 
	 * d
	 * 
	 * 
	 * */
}
