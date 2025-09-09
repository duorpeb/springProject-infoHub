package com.myproject.www.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.myproject.www.domain.CommentVO;
import com.myproject.www.domain.PagingVO;
import com.myproject.www.handler.PagingHandler;

public interface CommentDAO {
	// 댓글 등록
	int postCmt(CommentVO cvo);
	
	// 댓글 수정
	int updateCmt(CommentVO cvo);

	// 댓글 삭제
	int deleteCmt(long cno);
	
	// 댓글 불러오기 (댓글 페이징)
	List<CommentVO> getList(@Param("bno") long bno, @Param("pgvo") PagingVO pgvo);
	
	// 댓글 삭제 시 댓글 수 1 감소를 위한 bno select
	long getBno(long cno);

	int getTotal(long bno);

}
