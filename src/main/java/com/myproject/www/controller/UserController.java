 package com.myproject.www.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.www.domain.UserVO;
import com.myproject.www.handler.FileHandler;
import com.myproject.www.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/user/*")
@Slf4j
@Controller
public class UserController {
	/* 초기화
	 * 
	 * > usv, bcEncoder) 필드를 final로 선언한 이유는 생성자 주입
	 *   (Constructor Injection) 을 사용하기 위함
	 * 
	 * > final 필드는 한 번 할당되면 이후에 재할당이 불가능하므로 컨트롤러가 
	 *   생성된 뒤에 의존 객체 (UserService, BCryptPasswordEncoder) 가
	 *   절대로 변경될 수 없다는 것을 컴파일 시점에 보장
	 *   
	 * > final 로 선언하는 것은 이 컨트롤러가 UserService 없이 절대로 
	 *   존재할 수 없다는 의미를 코드로 명확히 표현
	 * 
	 * > @RequiredArgsConstructor 를 이용한 생성자 주입은 필드 주입(@Autowired 필드)에 
	 *   비해 불변성·안정성이 높은 방식
	 *   
	 * > @RequiredArgsConstructor 를 통해 아래와 같은 코드가 자동 생성
	 *   
	 *   public UserController(UserService usv, BCryptPasswordEncoder bcEncoder){
    		this.usv = usv;
    		this.bcEncoder = bcEncoder;
    	 }
   * 	 
	 * */
	// 초기화
	private final UserService usv;
	private final BCryptPasswordEncoder bcEncoder;
	 // @Component 로 선언되어있는데 직접 new 로 만드는 경우 DI 호출, AOP 등이 적용 X 
	 // 생성자 주입
	private final FileHandler fh;
	
	// @GetMapping - 로그인 페이지
	@GetMapping
	public void login() {}
	
	
	// @PostMapping("/login") - 로그인 처리
	@PostMapping("/login")
	public String login(HttpServletRequest req, RedirectAttributes re){
		log.info("LoginFailureHandler 의 에러 메시지 : {}"
				, req.getAttribute("errMsg").toString());
		
		re.addAttribute("id", req.getAttribute("id"));
		re.addAttribute("errMsg", req.getAttribute("errMsg"));
		
		return "redirect:/user/login";
	}
	
	
	// @PostMapping("/register") - 회원 가입 처리
	@PostMapping("/register")
	public String register(UserVO uvo) {
		// 비밀번호 암호화
		uvo.setPwd(bcEncoder.encode(uvo.getPwd()));
		// 프로필 이미지 기본 이미지로 변경 
		uvo.setFileName("basicImg.png");
		
		int isOk = usv.membershipReg(uvo);
		
		return isOk > 0 ? "redirect:/user/login" : "redirect:/user/join";
	}
	
	// @GetMapping("/mypage") - 회원 정보 페이지
	@GetMapping("/mypage")
	public void mypage(@RequestParam("id") String id, Model m){
		// username 으로 회원 정보 불러오기 
		UserVO uvo = usv.getUser(id);
		
//		if(uvo.getFileName() == null){
//      uvo.setFileName("basicImg.png");
//    }
		
		m.addAttribute("uvo", uvo);
	}
	
	
	// 회원 정보 수정
	@PostMapping("/update")
	public String update(@ModelAttribute UserVO uvo, RedirectAttributes re
			, @RequestParam(value="file", required=false) MultipartFile file
			,HttpServletRequest req, HttpServletResponse res) throws IOException {
		// 초기화
     // 비밀번호 변경 검사 변수
    boolean pwdChanged = uvo.getPwd() != null && !uvo.getPwd().isEmpty();
     // 파일 업로드 검사 변수
    boolean hasFile = file != null && !file.isEmpty();
    
    
    // 1) 비밀번호 변경 검사, 비밀번호 변경 시 비밀번호 암호화
    if (pwdChanged) {
      uvo.setPwd(bcEncoder.encode(uvo.getPwd()));
    }

    // 2) 파일 업로드 검사, 파일 업로드 시 uvo.setFileName(파일네임)
    if (hasFile) {
      // 파일이 이미지인지 확인
    	String ct = file.getContentType();
       
    	// 파일이 이미지가 아니라면 alert 창 띄움
      if (ct == null || !ct.startsWith("image/")) {
          re.addFlashAttribute("imgErrMsg", -1);
          return "redirect:/user/mypage?id=" + uvo.getId();
      }
      // 파일이 이미지라면 profileImg 폴더에 이미지 저장 후 uvo.setFileName(파일이름);      
      fh.upProfile(file, uvo);
    }

    // 3) 서비스 호출
     // 모든 필드 업데이트
    if (pwdChanged && hasFile) {
      usv.update(uvo);            
    
    } // 파일만 제외
    	else if (pwdChanged) {
    		usv.subFileUpdate(uvo);     
    
    } // 비밀번호만 제외
    	else if (hasFile) {
        usv.subPwdUpdate(uvo); 
        
    } // 둘 다 제외
    	else {
        usv.subPwdFileUpdate(uvo);  
    }
		
		/* 4) 로그아웃 처리, logout(req, res);
		 * 
		 * > 기본 Spring Security 설정에서는 POST /user/logout 요청만 로그아웃 처리 
		 *   (LogoutFilter)를 수행하고 GET /user/logout은 필터가 무시
		 * 
		 * > return "redirect:/user/logout"; 은 브라우저에 GET /user/logout 요청을 보냄
		 * 
		 * > 해결책으로 logout() 메서드 생성 
		 * */ 
    logout(req, res);
				
		return "redirect:/";
	}
	
	
	/* public String delete()
	 * 
	 * > Principal principal 은 현재 인증된 사용자의 username (id) 을 꺼내기 위한 선언
	 * 
	 * > HttpServletRequest req, HttpServletResponse res 는 logout(req, res) 를 위한 선언
	 * 
	 * > RedirectAttributes re 는 alert 창 (회원 탈퇴 완료) 을 띄우기 위한 선언으로 
	 *   리다이렉트 시 한번만 유지되는 (Flash) 속성을 저장
	 *   
	 * > Model에서 값을 가져오는 데 사용되는 주요 어노테이션은 @ModelAttribute 로 
	 *  이 어노테이션은 폼 데이터를 객체에 매핑하거나 컨트롤러에서 뷰로 데이터를 전달
	 *  
	 * > View 의 데이터를 Controller 로 바인딩할 때 쓰는 Annotaion 
	 *  #1_ @RequestParam 폼의 개별 <input name =""> 값을 메서드 파라미터로 받을 때 사용
	 *  		(e.g., /user/checkid?id=${id})
	 *  
	 *  #2_ @PathVariable URL 템플릿에 담긴 값을 파라미터로 받을 때 사용 
	 *      (e.g., /checkid/{id} ) 
	 *  
	 *  #3_ @ModelAttribute 여러 폼 필드를 하나의 커맨드 객체(VO/DTO) 에 한꺼번에 
	 *      바인딩할 때 사용
	 *      
	 *  # 즉, 단일 파라미터라면 @RequestParam, RESTful URL 변수라면 @PathVariable
	 *    , 폼 전체를 VO/DTO에 바인딩하려면 @ModelAttribute
	 *    
	 *  ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――  
	 *  > <button type="button"> 을 <a> 태그로 감싸면 파라미터가 제대로 전송이 되지 않음
	 * */
	@GetMapping("/delete")
	public String delete(@RequestParam("id") String id, RedirectAttributes re
		, HttpServletRequest req, HttpServletResponse res) {
		String delMsg = "fail";
		
		int isOk = usv.userDelete(id);
		if(isOk > 0) {
			logout(req, res);
			delMsg = "ok";
		}
		
		re.addFlashAttribute("delMsg", delMsg);
		
		return "redirect:/";
	}
	
	
	/* JS 에서의 비동기 요청 처리
	 * 
	 * > 해당 요청은 회원 가입 시 아이디, 닉네임 중복 검사를 위한 Mapping
	 * 
	 * > Spring 이 JS 의 AJAX (비동기) 요청을 처리할 때는 뷰 이름을 반환하는 게 아니라
	 *   데이터 (JSON, XML, 텍스트 등) 자체를 HTTP 응답 본문에 실어서 보낼 필요가 있기 때문에
	 *   Spring 에게 View 로 반환 하지말고 있는 그대로 클라이언트로 보내라 라고 명시 해주어야 함
	 *   
	 * > Spring 에게 클라이언트로 데이터 (JSON, XML, 텍스트 등) 자체를 HTTP 응답 본문에 실어서 보내
	 *   라는 역할을 해 주는 어노테이션/반환타입이 바로 다음 두 가지
	 *   
	 *   #1_ @ResponseBody 
	 *    - @ResponseBody가 붙은 메서드는 반환값을 뷰 이름으로 해석하지 않고 HTTP 
	 *      응답 본문(body)에 직접 쓰게 됨
	 *      
	 *   #2_ ResponseEntity<T>
	 *     - ResponseEntity 는 HTTP 상태 코드, 응답 헤더, 응답 본문을 모두 직접 
	 *       설정할 수 있는 타입
	 *       
	 *     - return type 에 ResponseEntity<…>를 선언하면 Spring 이 자동으로 @ResponseBody 
	 *       효과도 적용
	 * 
	 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
	 * > @Controller 대신 @RestController를 쓰면 해당 클래스의 모든 핸들러 메서드에 
	 *   자동으로 @ResponseBody 가 적용
	 *   
	 * > 그렇지 않으면 스프링이 반환값을 뷰 이름으로 해석해서 404나 뷰 템플릿 로딩 오류 발생
	 * 
	 * > JS 의 비동기 요청을 처리할 때, JS 에서 쿼리스트링 형식으로 변수를 전달한다면 
	 *   @PathVariable 를 객체의 body 에 담아 전달한다면 @RequestBody 를 파라미터에 적어
	 *   주어야 함
	 *   
	 *   #JS 에서 쿼리 스트링으로 보낸 비동기 요청의 매핑 
	 *   @ResponseBody
			 @DeleteMapping("/{cno}")
			 public String delete(@PathVariable("cno") long cno) {
				 int isOk = csv.delete(cno);
				 return isOk > 0 ? "1" : "0";
			 } 
			 
			 #JS 에서 객체의 body 에 담아 보낸 비동기 요청의 매핑 
			 @ResponseBody
			 @PutMapping("/update")
			 public ResponseEntity<String> update(@RequestBody CommentVO cvo) {
		     // 확인, log.info("cvo : {}", cvo);
				 int isOk = csv.update(cvo);
				 
				 // ResponseEntity<String> 로 반환하는 경우 아래와 같이 자료형을 명시하여
				    작성 해주어야 함
				 return isOk > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) : 
				 new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
	 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
	 * 			
	 * */
	@ResponseBody
	@GetMapping("/checkid")
	public String checkid(@RequestParam("id") String id) {
		int isOk = usv.checkId(id);
		
		return (isOk == 0) ? "pass" : "fail";
	}
	
	@ResponseBody
	@GetMapping("/checknick")
	public String check(@RequestParam("nick") String nick) {
		int isOk = usv.checkNick(nick);
		
		return (isOk == 0) ? "pass" : "fail";
	}
	
	
	// logout() - mypage 에서 회원 정보 수정 후 로그아웃을 위한 메서드 구현 
	private void logout(HttpServletRequest req, HttpServletResponse res) {
		// 내가 로그인한 시큐리티의 authentication 객체를 직접 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 로그아웃 작업 수행
		new SecurityContextLogoutHandler().logout(req, res, authentication);
	}
}
