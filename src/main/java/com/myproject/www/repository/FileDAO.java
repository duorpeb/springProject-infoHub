package com.myproject.www.repository;

import java.util.List;

import com.myproject.www.domain.FileVO;

public interface FileDAO {
	// 파일 등록
	int insertFile(FileVO fvo);
	
	// 파일 리스트 불러오기
	List<FileVO> getList(long bno);
}
