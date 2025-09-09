package com.myproject.www.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PagingVO {
	// 초기화
	 // 선택하는 페이지의 번호
	private int pageNo;
	 // 한 페이지에 표시되는 컨텐츠의 수 
	private int qty;
	 // Search 변수
	private String type;
	private String keyword;
	
	// 생성자
	public PagingVO() {
		this.pageNo = 1;
		this.qty = 10;
	}
	
	 // 
	public PagingVO(int pageNo, int qty) {
		this.pageNo = pageNo;
		this.qty = qty;
	}
	
	
	// DB 에서 사용될 시작 번지 구하기
	 // SELECT * FROM board LIMIT 번지, 개수 
	
	// getPageStart() - 로드될 때, 최대 페이지 개수
	 // pageNo = 1 -> 0, 10 ∧ pageNo = 2 -> 11,20
	public int getPageStart() {
		return (this.pageNo - 1) * this.qty;
	}
	
	
	// 제목 + 작성자, 작성자 + 내용처럼 여러 값을 검색하기 위해 배열로 설정 
	 // e.g., type = "all" ["t","w","c"]
	public String[] getTypeToArray() {
		
		return this.type == null ? new String[] {} : this.type.split("");
	}
}

