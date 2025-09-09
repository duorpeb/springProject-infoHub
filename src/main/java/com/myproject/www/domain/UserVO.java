package com.myproject.www.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*	user TABLE
 * 
 *  CREATE TABLE user(
		id VARCHAR(256) PRIMARY KEY,
		email VARCHAR(256) NOT NULL, 
		pwd VARCHAR(256) NOT NULL,
		nick_name VARCHAR(256) UNIQUE,
		user_ban VARCHAR(5) DEFAULT 'N',
		file_name VARCHAR(256),
		save_dir VARCHAR(256)
		);
 * 
 * */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
	private String id;
	private String email;
	private String pwd;
	private String nickName;
	private String terms;
	private String userBan;
	private String fileName;
	private String saveDir;
	private List<AuthVO> authList;
}
