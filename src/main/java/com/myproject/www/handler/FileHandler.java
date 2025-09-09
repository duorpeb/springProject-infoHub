package com.myproject.www.handler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.www.domain.FileVO;
import com.myproject.www.domain.UserVO;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Component
public class FileHandler {
	// 초기화
	 // 프로필 이미지 저장 경로 
	private final String PF_DIR = "D:\\Jstl_Servlet_Spring\\spring_myproject_up\\profileImg";
	 // 게시물 이미지 저장 경로
	private final String UP_DIR = "D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload";
	
	
	// 프로필 이미지 저장 메서드 
	public String upProfile(MultipartFile file, UserVO uvo) throws IOException {
		// 프로필 이미지를 지정하지 않은 경우 null 을 리턴
		if(file == null || file.isEmpty()) { return null; }		
		
		// 이미지 파일 유효성 검사
		if(!isImageFile(file)) {
			throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
		}
		
		// 파일 저장 로직
		 // 업로드 경로 설정 - PF_DIR
		
		 /* 고유한 파일 이름 설정 (UUID 이용)
		  * 
		  * > getName 은 객체이름 getOriginalFilename 이 파일의 실제 이름울 불러오는 메소드 
		  * 
		  * */
		 // uuid 생성
		String uuidStr = UUID.randomUUID().toString();
     // 파일 실제이름 불러오기
		String fileName = file.getOriginalFilename(); 
		 // profileImg 폴더에 저장할 이미지 파일 이름 설정
		String fullName = uuidStr + "_" + fileName;
		File saveFile = new File(PF_DIR, fullName);
		 // uvo.setFileName 에 해당 이름으로 저장
		uvo.setFileName(fullName);
		 // Disk 에 저장 
		file.transferTo(saveFile);
		
		 // Thumbnail 저장 
		File thumbNail = new File(PF_DIR, uuidStr + "_th_" + fileName);
		Thumbnails.of(saveFile).size(150, 150).toFile(thumbNail);			
		
		return fullName;
	}
	
	// 게시물 이미지 저장 메서드
	 // Multipartfile[] 을 Param 으로 받아 List<FileVO> 로 return 하고 폴더에 저장하는 메서드 
	public List<FileVO> upFile(MultipartFile[] files){
		List<FileVO> flist = new ArrayList<FileVO>();
		
		/* #1 fileVO 생성 및 파일 저장
		 * 
		 * > 그림파일인 경우에만 썸네일을 저장 
		 * 
		 * > 일반적으로 파일 저장 시 날짜별로 폴더화하여 업로드된 파일을 관리 
		 * */  
		// #1.1 - 오늘 날짜를 리턴 (e.g., 2025-06-05) 
		LocalDate date = LocalDate.now();
		String currTime = date.toString().replace("-", File.separator);
		// 확인, File.separator 는 구분자마다 다름 
		log.info("currTime : ", currTime);
		
		/* #1.2 폴더 생성
		 *  
		 * > 다음의 경로로 폴더 생성, D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\2025\\06\\05
		 * 
		 * > mkdir 은 폴더 1개만 생성 (2025만 생성), mkdirs 는 하위 폴더까지 생성 
		 * */ 
		File folders = new File(UP_DIR, currTime);
		
		// 기존 파일이 없는 경우에만 폴더를 생성 
		if(!folders.exists()) { folders.mkdirs(); }
		
		// #1.3 files 를 가지고 FileVO 를 생성 
		for(MultipartFile f : files) {
			FileVO fvo = new FileVO();
			
			// 파일 생성을 위해 uuid(PK), saveDir, fileName, fileType, bno, fileSize, regDate 중 
			 // uuid, saveDir, fileName, fileType, fileSize 만 초기화 
			fvo.setSaveDir(currTime);
			fvo.setFileSize(f.getSize());
			 // getName 은 객체이름 getOriginalFilename 이 파일의 실제 이름울 불러오는 메소드 
//			log.info("getName : {}", f.getName());
			log.info("getOriginalFilename : {}", f.getOriginalFilename());
			String fileName = f.getOriginalFilename();
			fvo.setFileName(fileName);
			
			UUID uuid = UUID.randomUUID();
			String uuidStr = uuid.toString();
			fvo.setUuid(uuidStr); // fin #1.3
			
			// #1.4 Disk 에 저장 
			String fullFName = uuidStr + "_" + fileName; 
			File storeFile = new File(folders, fullFName);
			
			try {
				f.transferTo(storeFile);
				// Save Thumbnail 
				 // tika Lib 로 img 파일인지 확인 
				if(isImgFile(storeFile)) {
					fvo.setFileType(1);
					File thumbNail = new File(folders, uuidStr + "_th_" + fileName);
					Thumbnails.of(storeFile).size(100, 100).toFile(thumbNail);
				}
			} catch (Exception e) {
				log.info("file store Err");
				e.printStackTrace();
			} // fin #1.4
			
			flist.add(fvo);
		}
		
		return flist;
	}
//	public List<FileVO> upFile(MultipartFile[] file){
		/* FileVO 초기화
		 * 
		 * > 파일 생성을 위해 uuid(PK), saveDir, fileName, fileType, bno, fileSize, regDate 중 
		 *   uuid, saveDir, fileName, fileType, fileSize 만 초기화
		 * 
		 * */  
//		FileVO fvo = new FileVO();
//		 // UUID 생성
//		UUID uuid = UUID.randomUUID();
//		String uuidStr = uuid.toString();
//		 // uuid
//		fvo.setUuid(uuidStr);
//		 // save_dir
//		fvo.setSaveDir(PF_DIR);
//		 // file_name
//		fvo.setFileName(file.getOriginalFilename()); 
//		 // file_type
		
//	}
	
	// isImgFile(File storeFile) - img 파일인지 확인하는 메서드
	private boolean isImgFile(File file) throws IOException {
		String mimeType = new Tika().detect(file);
		return mimeType.startsWith("image") ? true : false;
	}
	
	// isImageFile(MultipartFile file) - MultipartFile 인 경우 img 파일인지 확인하는 메서드
	private boolean isImageFile(MultipartFile file) {
	    try {
	        String mime = new Tika().detect(file.getInputStream());
	        return mime != null && mime.startsWith("image/");
	    } catch (IOException e) {
	        return false;
	    }
  	}

}
