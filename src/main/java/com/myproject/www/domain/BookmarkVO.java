package com.myproject.www.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* bookmark TABLE
 * 
 *  CREATE TABLE bookmark(
		bookmark_no BIGINT AUTO_INCREMENT PRIMARY KEY,
		bno BIGINT NOT NULL,
		id VARCHAR(256) NOT NULL,
		reg_date DATETIME DEFAULT NOW(),
		FOREIGN KEY(bno) REFERENCES board(bno) ON DELETE CASCADE,
		FOREIGN KEY(id) REFERENCES user(id) ON DELETE CASCADE
		);
 * 
 * */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkVO {
	private long bookmarkNo;
	private long bno;
	private String id;
	private String regDate;
}
