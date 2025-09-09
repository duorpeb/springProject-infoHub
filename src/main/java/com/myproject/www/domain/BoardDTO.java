package com.myproject.www.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* @AllArgsConstructor, @NoArgsConstructor
 * 
 * > (@AllArgsConstructor, @NoArgsConstructor 등)은 컴파일 타임에 소스코드에 
 *   생성자를 추가해 주는 역할
 * 
 * > 직접 작성한 생성자만 컴파일러가 보는 것은 아니고 Lombok 어노테이션에 
 *   따라 자동으로 추가된 생성자도 함께 컴파일러가 인식
 * 
 * > @AllArgsConstructor, @NoArgsConstructor 은 모든 
 * 
 * */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	private BoardVO bvo;
	private List<FileVO> fvoList;
	private String bdFileName;
	
	// detail.jsp 에 정보를 뿌리기 위한 생성자
	public BoardDTO(BoardVO bvo, List<FileVO> fvoList) {
		this.bvo = bvo;
		this.fvoList = fvoList;
	}
	
	// 보드의 닉네임을 가져오기 위한 생성자 
	public BoardDTO(BoardVO bvo, String bdFileName) {
		this.bvo = bvo;
		this.bdFileName = bdFileName;
	}
}
