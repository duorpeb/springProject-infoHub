package com.myproject.www.repository;

import com.myproject.www.domain.LikeVO;

public interface LikeDAO {
	// 좋아요 조회
	int checkDuplicLikeNo(LikeVO lvo);

	// 좋아요 활성화
	void insertLike(LikeVO lvo);

	// 좋아요 삭제
	void deleteLike(LikeVO lvo);

}
