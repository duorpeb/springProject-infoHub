package com.myproject.www.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myproject.www.domain.CommentVO;
import com.myproject.www.domain.PagingVO;
import com.myproject.www.handler.PagingHandler;
import com.myproject.www.repository.BoardDAO;
import com.myproject.www.repository.CommentDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
	// 초기화
	private final CommentDAO cdao;
	private final BoardDAO bdao;
	
	// 댓글 등록 
	public int postCmt(CommentVO cvo) {
		bdao.increCmtQty(cvo.getBno());
		
		return cdao.postCmt(cvo);
	}

	// 댓글 수정
	@Override
	public int updateCmt(CommentVO cvo) {
		return cdao.updateCmt(cvo);
	}
	
	// 댓글 삭제
	@Override
	public int deleteCmt(long cno) {
	  // bno 값을 알기 위한 select 
		long bno = cdao.getBno(cno);
		
		bdao.decreCmtQty(bno);
		
		return cdao.deleteCmt(cno);
	}

	// 댓글 불러오기 (댓글 페이징)
	@Override
	public PagingHandler<CommentVO> getList(long bno, PagingVO pgvo) {
		List<CommentVO> list = cdao.getList(bno,pgvo);
		int ttc = cdao.getTotal(bno);
		
		PagingHandler<CommentVO>  ph = new PagingHandler<CommentVO>(ttc, pgvo, list);
		
		return ph;
	}

}
