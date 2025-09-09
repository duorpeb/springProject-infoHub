package com.myproject.www.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.myproject.www.domain.BoardDTO;
import com.myproject.www.domain.BoardVO;
import com.myproject.www.domain.FileVO;
import com.myproject.www.domain.PagingVO;

public interface BoardDAO {
	// 게시물 작성
	int getInsert(BoardVO boardVO);
	
	// 파일 저장을 위한 bno 불러오기
	Long getBno();
	
	// 이전글 이동을 위한 bno 가져오기
	Long getPrevContent(@Param("bno") long bno);

	// 다음글 이동을 위한 bno 가져오기
	Long getNextContent(@Param("bno") long bno);

	// detail.jsp 에 정보를 뿌리기 위한 select
	BoardVO getDetail(@Param("bno") long bno);

	// 조회수 1 증가
	int getCntUp(long bno);

	// 조회수 1 감소
	int getCntDown(long bno);
	
	// 리스트 가져오기
	List<BoardVO> getList(@Param("pgvo") PagingVO pgvo, @Param("category") String category);
	
	// 리스트 출력 시 프로필 이미지 출력을 위한 파일네임 가져오기
	String getFileName(String writer);
	
	// 댓글 수 1 증가
	void increCmtQty(long bno);
	
	// 댓글 수 1 감소
	void decreCmtQty(long bno);

	// 게시글 수정
	int getUpdate(BoardVO bvo);

	// 좋아요 1 증가 
	void increRecommend(long boardBno);

	// 좋아요 1 감소
	void decreRecommend(long boardBno);

	// 좋아요 많은 순으로 TOP 8 게시글 가져오기
	List<BoardVO> getLikeBvoList();
	
	// 댓글 많은 순으로 TOP 8 게시글 가져오기
	List<BoardVO> getCmtBvoList();
	
	// 조회수 많은 순으로 TOP 8 게시글 가져오기
	List<BoardVO> getViewsBvoList();

	// 게시글 삭제
	void delete(long bno);

	// 전체 게시글 개수 조회 
	int getTc(@Param("pgvo") PagingVO pagingVO, @Param("category") String category);
}
