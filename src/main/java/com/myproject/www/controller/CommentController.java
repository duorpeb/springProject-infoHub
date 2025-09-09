package com.myproject.www.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myproject.www.domain.CommentVO;
import com.myproject.www.domain.PagingVO;
import com.myproject.www.handler.PagingHandler;
import com.myproject.www.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/comment/*")
public class CommentController {
	// 초기화
	private final CommentService csv;
	
	// 댓글 등록
	@ResponseBody
	@PostMapping("/post")
	public String post(@RequestBody CommentVO cvo) {
		int isOk = csv.postCmt(cvo);
		
		return (isOk > 0) ? "1" : "0";
	}
	
	
	// 댓글 수정
	@ResponseBody
	@PutMapping("/update")
	public String update(@RequestBody CommentVO cvo) {
		int isOk = csv.updateCmt(cvo);
		
		return (isOk > 0) ? "1" : "0"; 
		/* @ResponserBody annotation 을 사용하지 않는 경우 다음과 같이 표현 가능 
		 * 
		 * @PostMapping
		 * > public String update(@RequestBody CommentVO cvo) {
		 * 		int isOk = csv.updateCmt(cvo);
		 *  
		 * 		return isOk > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) 
		 * 			: new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
		 * 	 } 
		 * */ 
	}
	
	// 댓글 삭제 
	@ResponseBody
	@DeleteMapping("/{cno}")
	public String delete(@PathVariable("cno") long cno) {
		int isOk = csv.deleteCmt(cno);
		
		return (isOk > 0) ? "1" : "0";
	}
	
	
	/* 더보기 버튼 클릭 시 해당 게시물에 작성된 댓글 로드
	 * 
	 * > JS 에서 getCommentListFromServer(bno, page) 호출 시 결과를 resp.json() 으로 응답받기에 
	 *   RepsonseEntity<T> 로 리턴해주어야 함
	 *  
	 * > 댓글 페이징을 해주어야 하기에 ResponseEntity<PagingHandler> 객체로 반환
	 *
	 * > value= 는 URL 매핑을 의미 @GetMapping("/foo") 은 @GetMapping(value="/foo") 와 동일
	 * 
	 * > produces 는 이 핸들러가 어떤 미디어 타입(콘텐츠 타입) 을 “생산”하는지 선언하며 
	 *   produce = MediaType.APPLICATION_JSON_VALUE) 는 이건 JSON을 반환합니다라고 명확히 선언하고 
	 *   싶을 때 사용
	 * 
	 * > 생략해도 JSON 변환은 되지만, 명시적으로 JSON 전용 API로 제한하거나 Content-Type 헤더를 
	 *   정확히 지정하고 싶을 때 produces를 사용 
	 * 
	 * */ 
	@ResponseBody
	@GetMapping(value="/{bno}/{page}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagingHandler<CommentVO>> list(@PathVariable("bno") long bno, @PathVariable("page") int page){
		// commentMapper.xml 에서 사용할 #{limit} 의 값 초기화
		PagingVO pgvo = new PagingVO(page, 5);
		
		PagingHandler<CommentVO> ph = csv.getList(bno, pgvo);
		
		// HTTP 응답 본문 (body) 에 ph 와 상태 코드 (200) 를 설정하여 리턴 
		return new ResponseEntity<PagingHandler<CommentVO>>(ph, HttpStatus.OK);
	}
}







