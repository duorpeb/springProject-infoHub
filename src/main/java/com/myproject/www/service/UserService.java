package com.myproject.www.service;

import org.springframework.stereotype.Service;

import com.myproject.www.domain.UserVO;


public interface UserService {
	// 회원가입
	int membershipReg(UserVO uvo);
	
	// 회원정보 불러오기
	UserVO getUser(String id);
	
	// 회원정보 업데이트 - 비밀번호도 업데이트
	void update(UserVO uvo);

	// 회원정보 업데이트 - 비밀번호 없이 업데이트 
	void subPwdUpdate(UserVO uvo);

	// 회원 정보 업데이트 - 파일 업로드 없이 업데이트
	void subFileUpdate(UserVO uvo);

  // 회원 정보 업데이트 - 비밀번호 변경, 파일 업로드 없이 업데이트
	void subPwdFileUpdate(UserVO uvo);

	// 회원 탈퇴
	int userDelete(String id);
	
	// 회원 가입 시 아이디 중복 검사
	int checkId(String id);

	// 회원 가입 시 닉네임 중복 검사
	int checkNick(String nick);

}
