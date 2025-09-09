package com.myproject.www.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* comment TABLE	
 * 
 *  CREATE TABLE comment(
		cno BIGINT AUTO_INCREMENT PRIMARY KEY,
		bno BIGINT NOT NULL,
		writer VARCHAR(500) NOT NULL,
		content TEXT,
		reg_date DATETIME DEFAULT NOW(),
		FOREIGN KEY(writer) REFERENCES user(nick_name) ON DELETE CASCADE
		);
 * 
 * 
 * */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
	private long cno;
	private long bno;
	private String writer;
	private String content;
	private String regDate;
	private String fileName;
	private String edited;
}
