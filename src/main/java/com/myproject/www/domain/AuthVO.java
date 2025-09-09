package com.myproject.www.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* auth TABLE
 * 
 * auth_no BIGINT AUTO_INCREMENT PRIMARY KEY,
	id VARCHAR(256) NOT NULL,
	email VARCHAR(256) NOT NULL,
	auth VARCHAR(30) NOT NULL,
	FOREIGN KEY(id) REFERENCES user(id) ON DELETE CASCADE
 * */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthVO {
	private long authNo;
	private String id;
	private String auth;
}
