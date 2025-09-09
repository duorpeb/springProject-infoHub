package com.myproject.www.repository;

import java.util.List;

import com.myproject.www.domain.AuthVO;
import com.myproject.www.domain.FileVO;
import com.myproject.www.domain.UserVO;

public interface UserDAO {
	
	// 인증 객체 생성을 위한 회원 정보 불러오기
	UserVO getUser(String username);
	
	// 권한 정보 가져오기 
	List<AuthVO> getAuthList(String username);

	// 회원 가입 
	int membershipReg(UserVO uvo);
	
	// 회원 가입 시 권한 부여
	int insertAuth(String id);

	// 회원 정보 업데이트
	void getUpdate(UserVO uvo);

	// 회원 정보 업데이트 (비밀번호 제외 업데이트)
	void getSubPwdUpdate(UserVO uvo);

	// 회원 정보 업데이트 - 파일 업로드 없이 업데이트
	void getSubFileUpdate(UserVO uvo);

  // 회원 정보 업데이트 - 비밀번호 변경, 파일 업로드 없이 업데이트
	void getSubPwdFileUpdate(UserVO uvo);
	
	// 회원 탈퇴
	int userDelete(String id);
	
	// 회원 가입 시 아이디 중복 검사
	int checkId(String id);
	
  // 회원 가입 시 닉네임 중복 검사
	int checkNick(String nick);

}
