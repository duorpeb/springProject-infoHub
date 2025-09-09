package com.myproject.www.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* file TABLE
 * 
 *  CREATE TABLE  file(
		bno BIGINT NOT NULL,
		uuid VARCHAR(256) PRIMARY KEY,
		save_dir VARCHAR(256) NOT NULL,
		file_name VARCHAR(256) NOT NULL,
		file_type TINYINT(1) DEFAULT 0,
		file_size BIGINT,
		reg_date DATETIME DEFAULT NOW()
		);
 * 
 * */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileVO {
	private long bno;
	private String uuid;
	private String saveDir;
	private String fileName;
	private int fileType;
	private long fileSize;
	private String regDate;
}
