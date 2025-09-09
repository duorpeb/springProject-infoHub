package com.myproject.www.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*  board TABLE
 * 
 *  CREATE TABLE board(
		bno BIGINT AUTO_INCREMENT PRIMARY KEY,
		category VARCHAR(100) NOT NULL,
		title VARCHAR(300) NOT NULL,
		writer VARCHAR(200) NOT NULL,
		content TEXT,
		is_del VARCHAR(5) DEFAULT 'N',
		reg_date DATETIME DEFAULT NOW(),
		views INT DEFAULT 0,
		cmt_cnt INT DEFAULT 0,
		report_cnt INT DEFAULT 0,
		recommend INT DEFAULT 0,
		
		FOREIGN KEY(writer) REFERENCES user(nick_name) ON DELETE CASCADE
		); 
 * 
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardVO {
	private long bno;
	private String category;
	private String title;
	private String writer;
	private String content;
	private String isDel;
	private String regDate;
	private int views;
	private int cmtCnt;
	private int reportCnt;
	private int recommend;
}
