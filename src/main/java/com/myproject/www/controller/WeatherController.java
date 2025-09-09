package com.myproject.www.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.www.api.WeatherShortTermAPIHandler;
import com.myproject.www.domain.WeatherBodyItemsDTO;
import com.myproject.www.domain.WeatherItemVO;
import com.myproject.www.domain.WeatherResponse;
import com.myproject.www.domain.WeatherShortObj;
import com.myproject.www.domain.WeatherShortObjForJS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/weather/*")
public class WeatherController {
	// 초기화 
	 // 날씨 핸들러
	private final WeatherShortTermAPIHandler wstAPIhandler;
	 // ObjectMapper
	@Autowired
	private ObjectMapper objectMapper;
	
	
	// @GetMapping() - JS 에서 보낸 비동기 요청의 매핑
	@GetMapping(value="/{gridX}/{gridY}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<WeatherShortObj>> getWeatherInfo(@PathVariable("gridX") int gridX
			, @PathVariable("gridY") int gridY) throws IOException, JsonProcessingException{
		
		/** 날씨 API 호출 후 파싱
		  *  
		  *  > nx=61, ny=125 는 서울시 강남구 역삼1동의 좌표 
		  *  
		  *  > String weatherResponseBody;
		  *  	 WeatherResponse response = objectMapper.readValue(weatherResponseBody, WeatherResponse.class); 
		  *    m.addAttribute("res", response.getResponse().getBody().getItems().getItem().get(0)); 의 출력 형태
		  *    
		  *    WeatherItemVO(baseDate=20250703, baseTime=2000, category=TMP, fcstDate=20250703
	    *     							, fcstTime=2100, fcstValue=28, nx=61, ny=125)
	    *     
	    *  ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣   
	    *	 > 파싱 방법
	    *    
	    *    WeatherResponse response = objectMapper.readValue(weatherResponseBody, WeatherResponse.class); 
	    *  
	    *  ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣   
	    *  > 4일간 (당일~글피) 의 날씨를 가져오는 경우, getWeather 는 getWeather(int numOfRows, int pageNo,
	    *    int nx, int ny) 의 형태 
	    *    
	    *    # numOfRows 는 출력할 데이터의 양, pageNo 는 페이지 넘버 (시작 페이지는 1), nx 와 ny 는 
	    *      격자 X,Y 의 좌표
	    *  
	    *  ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣  
	    *  > 수정 전 코드 
	    *  
					String weatherResponseStr = wstAPIhandler.getWeather(1016, 1, gridX, gridY);
					 // 파싱
					WeatherResponse responseBody = objectMapper.readValue(weatherResponseStr, WeatherResponse.class);
					 // 
					List<WeatherItemVO> items = responseBody.getResponse().getBody().getItems().getItem();
					 // 확인
					log.info("Item 의 baseDate Type : {}", items.get(0).getBaseDate() instanceof String); // true
	    *  
		  * */ 
		 // 날씨 정보 처리
		ArrayList<WeatherShortObj> s_obj = getFourDaysWeatherInfo(gridX, gridY);
		
		return new ResponseEntity<ArrayList<WeatherShortObj>>(s_obj, HttpStatus.OK);
	}
	
	
	
	/** getFourDaysWeatherInfo() - 4일간 (당일~글피) 의 날씨 정보를 받아 4일간의 날씨 정보를 건네줌 
	 * 
		 * 	> 단기예보 오픈 API 를 통해 가져와야 할 정보 
		 *    TMN/TMX (일 최저/최고기온), PTY (강수 형태), POP (강수 확률), SKY (하늘 상태)
		 *    , PCP (1시간 강수량), SNO (1시간 신적설), REH (습도) 
		 *    
		 *  > 일 최저/최고 기온에서 당일 최저/최고기온은 0200 만 제공하며 글피까지는 모든 시간에서 제공
		 * @throws IOException 
		 * @throws JsonMappingException 
		 *  
		 *  > 1일차 ~ 4일차 날씨 정보를 호출하여 각각 초기화 
		 * 
		 * */
	private ArrayList<WeatherShortObj> getFourDaysWeatherInfo(int gridX, int gridY) 
			throws JsonMappingException, IOException {
		// 초기화
		ArrayList<WeatherShortObj> weatherList = new ArrayList<>();
		
		// 현재 날짜 (당일) 기준으로 일주일간의 일수를 반환하는 배열 생성 
		String[] weekArr = printDaysStrOfNextWeekFromNow();
		
		
		/** if(item.getBaseDate() == weekArr[0]) - 1일차 날씨 정보 가져오기
		 * 
		 * > 당일의 최저/최고 기온은 baseTime 이 0200 인 경우에만 해당 정보를 가져올 수 있기에 API 를 호출한 시간과 관계 없이 
		 *   baseTime == 0200 으로 API 를 한 번 호출하여 당일의 최저/최고 기온을 초기화 해놓아야 함 
		 *   
		 * > 하루의 날씨를 12:00 시의 정보를 기준으로 설정하여 당일 12:00 의 정보 (SKY (하늘 상태), PTY (강수 형태), POP (강수 확률)
		 *   , PCP (1시간 강수량), SNO (1시간 신적설), REH (습도)) 를 가져옴
		 *   
		 * > 데이터를 줄 때, TMN (06:00) -> SKY (12:00) -> PTY (12:00) -> POP (12:00) -> REH (12:00) -> TNX (15:00) 순으로 줌
		 * 
		 * > "TMN".equals(day1_item.getCategory()) 대신 day1_item.getCategory().equals("TMN") 으로도 사용할 수 있지만 이렇게 사용하면 
		 *   day1_item.getCategory() 이 null 일 때, NullPointerException 이 발생할 수 있음 
		 *   
		 *   즉, day1_item.getCategory() 이 절대 null 이 될 수 없음을 100% 보장할 수 있다면 day1_item.getCategory().equals("TMN") 으로
		 *   사용해도 되지만 그렇지 않은 경우 "TMN".equals(day1_item.getCategory()) 사용을 권장
		 * 
		 * */	
		 // baseTime == 0200 으로 설정한 URL 로 API 를 호출하여 직렬화
		String weatherResponseDay1Str = wstAPIhandler.getWeatherDaysInfo("254","1", "0", gridX, gridY);
		 // 파싱
		WeatherResponse day1ResponseBody = objectMapper.readValue(weatherResponseDay1Str, WeatherResponse.class);
		List<WeatherItemVO> day1_items = day1ResponseBody.getResponse().getBody().getItems().getItem();
		
		 // WeatherShortObj weatherObj = new WeatherShortObj(); 를 한 블록안에서만 유효하게 하기 위한 if 문 
		if(day1_items.size() > 0) {
			WeatherShortObj weatherShortObj = new WeatherShortObj();
			 // 날짜 설정
			weatherShortObj.setBaseDate(weekArr[0]);
			
			// 1일차 날씨정보 설정
			for(WeatherItemVO day1_item : day1_items) {
				setDaysWeatherInfo(day1_item, weatherShortObj,weekArr[0]);
				
			} // for of day1 if fin 
			
			// ArrayList<WeahterShortObj> items[0]; 
			weatherList.add(weatherShortObj);
		}
		
		
		// 2 일차 날씨 정보 설정 
		String weatherResponseDay2Str = wstAPIhandler.getWeatherDaysInfo("540", "1", "1", gridX, gridY);
		 // 파싱
		WeatherResponse day2ResponseBody = objectMapper.readValue(weatherResponseDay2Str, WeatherResponse.class);
//		List<WeatherItemVO> day2_items = day2ResponseBody.getResponse().getBody().getItems().getItem();
		List<WeatherItemVO> day2_items = day2ResponseBody.getResponse().getBody().getItems().getItem();
		
		if(day2_items.size() > 0) {
			WeatherShortObj weatherShortObj = new WeatherShortObj();
			 // 날짜 설정
			weatherShortObj.setBaseDate(weekArr[1]);
			
			// 2일차 날씨 정보 설정
			for(WeatherItemVO day2_item : day2_items) {
				setDaysWeatherInfo(day2_item, weatherShortObj, weekArr[1]);
			}
			
			weatherList.add(weatherShortObj);
		}
		
		
		// 3 일차 날씨 정보 설정
		String weatherResponseDay3Str = wstAPIhandler.getWeatherDaysInfo("700","1", "1", gridX, gridY);
		 // 파싱
		WeatherResponse day3ResponseBody = objectMapper.readValue(weatherResponseDay3Str, WeatherResponse.class);
		List<WeatherItemVO> day3_items = day3ResponseBody.getResponse().getBody().getItems().getItem();
		
		if(day3_items.size() > 0) {
			WeatherShortObj weatherShortObj = new WeatherShortObj();
			 // 날짜 설정
			weatherShortObj.setBaseDate(weekArr[2]);
			
			// 3일차 날씨 정보 설정
			for(WeatherItemVO day3_item : day3_items) {
				setDaysWeatherInfo(day3_item, weatherShortObj, weekArr[1]);
			}
			
			weatherList.add(weatherShortObj);
		}
		
		
		// 4 일차 날씨 정보 설정
		String weatherResponseDay4Str = wstAPIhandler.getWeatherDaysInfo("880","1", "1", gridX, gridY);
		 // 파싱
		WeatherResponse day4ResponseBody = objectMapper.readValue(weatherResponseDay4Str, WeatherResponse.class);
		List<WeatherItemVO> day4_items = day4ResponseBody.getResponse().getBody().getItems().getItem();
		
		if(day4_items.size() > 0) {
			WeatherShortObj weatherShortObj = new WeatherShortObj();
			 // 날짜 설정
			weatherShortObj.setBaseDate(weekArr[3]);
			
			// 4일차 날씨 정보 설정
			for(WeatherItemVO day4_item : day4_items) {
				setDaysWeatherInfo(day4_item, weatherShortObj, weekArr[3]);
			}
			
			weatherList.add(weatherShortObj);
		}
		
		// 확인
		log.info("weatherList : {}", weatherList);
		
		return weatherList;
	}	
	
	
	/** setDaysWeatherInfo(WeatherItemVO daysWeather, WeatherShortObj weatherObj)
	 * 
	 * 	> 하루의 날씨 정보를 설정하는 
	 * 
	 * */
	private void setDaysWeatherInfo(WeatherItemVO daysWeather, WeatherShortObj weatherShortObj, String weekArrIdx) {	
		// 최저 기온 설정
		if("TMN".equals(daysWeather.getCategory()) && weekArrIdx.equals(daysWeather.getFcstDate())) {
			weatherShortObj.setTmn(daysWeather.getFcstValue());
		}

		
		// 하늘 상태 설정
		if("SKY".equals(daysWeather.getCategory()) && weekArrIdx.equals(daysWeather.getFcstDate()) 
				&& "1200".equals(daysWeather.getFcstTime()) ) {
			switch(daysWeather.getFcstValue()) {
				case "1" :  
					weatherShortObj.setSky("맑음");
					break;
					
				case "3" : 
					weatherShortObj.setSky("구름많음");
					break;
					
				case "4" :
					weatherShortObj.setSky("흐림");
					break;
			}
		}
		
		// 강수 형태 설정 
		if("PTY".equals(daysWeather.getCategory()) && weekArrIdx.equals(daysWeather.getFcstDate())
				&& "1200".equals(daysWeather.getFcstTime()) ) {
			switch(daysWeather.getFcstValue()) {
				case "0" : 
					weatherShortObj.setPty("없음"); break; 
					
				case "1" : 
					weatherShortObj.setPty("비"); break;
					
				case "2" : 
					weatherShortObj.setPty("비/눈"); break;
					
				case "3" : 
					weatherShortObj.setPty("눈"); break;
					
				case "4" :
					weatherShortObj.setPty("소나기"); break;
			}
		}
		
		// 강수 확률 설정
		if("POP".equals(daysWeather.getCategory()) && weekArrIdx.equals(daysWeather.getFcstDate()) 
				&& "1200".equals(daysWeather.getFcstTime()) ) {
			weatherShortObj.setPop(daysWeather.getFcstValue());
		}
		
		// 습도 설정 
		if("REH".equals(daysWeather.getCategory()) && weekArrIdx.equals(daysWeather.getFcstDate()) 
				&& "1200".equals(daysWeather.getFcstTime()) ) {
			weatherShortObj.setReh(daysWeather.getFcstValue());
		}
		
		// 최고 기온 설정
		if("TMX".equals(daysWeather.getCategory()) && weekArrIdx.equals(daysWeather.getFcstDate()) ) {
			weatherShortObj.setTmx(daysWeather.getFcstValue());
		}
	}
	
	
	/** printDaysStrOfNextWeekFromNow() - 현재 날짜부터 1주일 간의 날짜를 문자열 배열 형태로 반환 
	 * 
	 * 	> e.g., String[] weekArr = { {20250706}, {20250707}, ... {20250712} };
 	 * 
	 * */
	private String[] printDaysStrOfNextWeekFromNow(){
		// 초기화
		LocalDate today = LocalDate.now();
		 // return 배열
		String[] weekArr = new String[7];
		
		for(int i = 0; i < 7; i++) {
			weekArr[i] = today.plusDays(i).toString().replace("-","");
		}
		
		return weekArr;
	}
}
