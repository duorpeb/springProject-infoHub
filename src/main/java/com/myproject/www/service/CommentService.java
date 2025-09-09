package com.myproject.www.service;

import com.myproject.www.domain.CommentVO;
import com.myproject.www.domain.PagingVO;
import com.myproject.www.handler.PagingHandler;

public interface CommentService {
	// 댓글 등록
	int postCmt(CommentVO cvo);
	
	// 댓글 불러오기 
	
	// 댓글 삭제
	int deleteCmt(long cno);
	
	// 댓글 수정
	int updateCmt(CommentVO cvo);
	
	// 댓글 불러오기 (댓글 페이징)
	PagingHandler<CommentVO> getList(long bno, PagingVO pgvo);

}
