package com.myproject.www.service;

import org.springframework.stereotype.Service;

import com.myproject.www.domain.UserVO;
import com.myproject.www.repository.UserDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserDAO udao;
	
	// 회원가입
	public int membershipReg(UserVO uvo) {
		
		int isOk = udao.membershipReg(uvo);
		
		return isOk > 0 ? udao.insertAuth(uvo.getId()) : -1; 
	}
	
	// 회원 정보 불러오기
	@Override
	public UserVO getUser(String id) {
		// TODO Auto-generated method stub
		return udao.getUser(id);
	}

	// 회원 정보 업데이트 - 비밀번호도 업데이트
	@Override
	public void update(UserVO uvo) {
		udao.getUpdate(uvo);
	}

	// 회원 정보 업데이트 - 비밀번호 없이 업데이트
	@Override
	public void subPwdUpdate(UserVO uvo) {
		udao.getSubPwdUpdate(uvo);
		
	}

	
	// 회원 정보 업데이트 - 파일 업로드 없이 업데이트
	@Override
	public void subFileUpdate(UserVO uvo) {
		udao.getSubFileUpdate(uvo);
	}

	
  // 회원 정보 업데이트 - 비밀번호 변경, 파일 업로드 없이 업데이트
	@Override
	public void subPwdFileUpdate(UserVO uvo) {
		udao.getSubPwdFileUpdate(uvo);
	}
	
	
	// 회원 탈퇴
	@Override
	public int userDelete(String id) {
		
		return udao.userDelete(id);
	}
	
	
	// 회원 가입 시 ID 중복 검사
	@Override
	public int checkId(String id) {
		// TODO Auto-generated method stub
		return udao.checkId(id);
	}

	// 회원 가입 시 닉네임 중복 검사
	@Override
	public int checkNick(String nick) {
		// TODO Auto-generated method stub
		return udao.checkNick(nick);
	}
}
