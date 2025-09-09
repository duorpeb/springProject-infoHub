package com.myproject.www.controller;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.myproject.www.api.JacksonConfig;
import com.myproject.www.api.NaverNewsAPIHandler;
import com.myproject.www.api.WeatherShortTermAPIHandler;
import com.myproject.www.domain.BoardDTO;
import com.myproject.www.domain.BoardVO;
import com.myproject.www.domain.NewsDTO;
import com.myproject.www.domain.PagingVO;
import com.myproject.www.domain.WeatherResponse;
import com.myproject.www.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for the application home page.
 */
@RequiredArgsConstructor
@Controller
@Slf4j
public class HomeController {
	// 초기화
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	 /* boardService
	  * 
	  * > @Autowired 사용 시 절대 static 필드에 붙이지 말고
	  *   스프링이 관리하는 인스턴스 필드(또는 생성자, 세터 메서드)에 붙여야 함
	  *   
	  * > 1번 형태 - 필드 주입 : private BoardService boardService;
	  *   
	  *   2번 형태 - 세터 주입  
	  *   	@Autowired
	  *   	public void setBoardService(BoardService boardService) {
	  *   		this.boardService = boardService;
	  *   	}
	  *   
	  *   3번 형태 - 생성자 주입 (가장 권장)
	  *    // @Autowired 생략 가능 (Spring 4.3+ 단일 생성자일 때)
    		 @Autowired
    		 public HomeController(BoardService boardService) {
        	 this.boardService = boardService;
				 }
	  * */ 
	private final BoardService bsv;
	 // news Handler
	private final NaverNewsAPIHandler nnh;
	 // API Handler
	private final WeatherShortTermAPIHandler wstAPIhandler;
	
	 // ObjectMapper 
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model m) throws JsonMappingException, JsonProcessingException {
		// 초기화 
		 // api 호출 시 응답을 저장할 변수
		String newsResponseBody;
		 // 날씨, String weatherResponseBody;
		
		// 페이징 
		PagingVO pgvo = new PagingVO(1,4);
		
		// 최근 피드 4개씩 가져옴 
		String category = null;
		
		List<BoardDTO> list = bsv.getList(pgvo, category);
		logger.info("/list 의 list : {},", list);
		
		// 좋아요 많은 순으로 TOP 8 게시글 가져오기
		List<BoardVO> likeBvoList = bsv.getLikeBvoList();
		
		// 댓글 많은 순으로 TOP 8 게시글 가져오기
		List<BoardVO> cmtBvoList = bsv.getCmtBvoList();
		
		// 조회수 많은 순으로 TOP 8 게시글 가져오기
		List<BoardVO> viewsBvoList = bsv.getViewsBvoList(); 
		
		
		/** Naver 검색 API 로 뉴스 불러오기
		 * 
		 * 	> NaverNewsHandler 의 getNews() 는 getNews(String searchKeyword, int start, int rows) 의 형태로 
		 *    검색어, 검색 시작 위치, 출력할 기사 수를 매개 변수로 건네주기만 하면 해당되는 정보를 가져옴 
		 * 
		 * */ 
		String keyword = "뉴스";
		
		
	 /** News API 호출 후 파싱
	  *  
	  * */ 
		newsResponseBody = nnh.getNews(keyword, 1, 10);
		 // JSON 으로 파싱 
		NewsDTO ndto = objectMapper.readValue(newsResponseBody, NewsDTO.class);
		
		// 날씨정보는 WeatherController 를 통해 처리하고 요일 출력만 HomeController 로 처리
		String[] weekArr = wstAPIhandler.printDaysOfNextWeekFromNow();
		m.addAttribute("week", weekArr);
		
		// 뉴스 
		m.addAttribute("news", ndto);
		
		// 피드 
		m.addAttribute("pgvo", pgvo);
		m.addAttribute("list", list);
		
		// 좋아요, 댓글, 조회수
		m.addAttribute("likeList", likeBvoList);
		m.addAttribute("cmtList", cmtBvoList);
		m.addAttribute("viewsList",viewsBvoList);
		
		// 확인
		log.info("HomeController >>>>> likeList : {}", likeBvoList);
		log.info("HomeController >>>>> cmtList : {}", cmtBvoList);
		log.info("HomeController >>>>> viewsList : {}", viewsBvoList);
		

		
		return "home";
	}
	
}
