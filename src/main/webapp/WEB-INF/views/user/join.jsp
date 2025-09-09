<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="../../../resources/dist/css/join.css" rel="stylesheet">

<jsp:include page="../layout/header.jsp"></jsp:include>

<div class="signup-container">
    <h2>회원가입</h2>
    <form class="signup-form" action="/user/register" method="post">
 			<!-- CSRF TOKEN 
				> SecurityConfig 에서 csrf.disable() 를 하는게 아니라면 다음 변수를 JS 에
			    전달해주어야 함 
			-->
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token }" />
    
    	<!-- ID -->
    	<div class="form-group">
        <label for="id">아이디<span class="required">*</span>
        	<span>
	        	<button 
	        		type="button" 
	        		id="idCheckBtn" 
	        		class="btn btn-outline-primary"
	        		style = "height : 40px;"
	        	>
	        	아이디 중복검사
	        	</button>
        	</span>
        </label>
        <input type="text" id="id" name="id" placeholder="아이디" required />
      </div>
      
      <!-- 이메일 -->
      <div class="form-group">
        <label for="email">이메일<span class="required">*</span></label>
        <input type="email" id="email" name="email" placeholder="example@domain.com" required />
      </div>
      
      <!-- 비밀번호 -->
      <div class="form-group">
        <label for="pwd">비밀번호<span class="required">*</span></label>
        <input type="password" id="pwd" name="pwd" placeholder="8자 이상" minlength="8" required />
      </div>
<!--       비밀번호 확인
      <div class="form-group">
        <label for="confirm_password">비밀번호 확인<span class="required">*</span></label>
        <input type="password" id="confirm_password" name="confirm_password" placeholder="비밀번호 재입력" required />
      </div> -->
      
      <!-- 닉네임 -->
      <div class="form-group">
        <label for="nickName">닉네임<span class="required">*</span>
					<span>
			      <button 
			      	type="button" 
			      	id="nickCheckBtn" 
			      	class="btn btn-outline-primary"
			      	style = "height : 40px;"
			      >
			      	닉네임 중복검사
			      </button>
					</span>
        </label>
        <input type="text" id="nickName" name="nickName" placeholder="활동에 사용할 닉네임" required />
      </div>
      
      <!-- 약관 동의 -->
      <div class="form-group checkbox-group">
        <input type="checkbox" id="terms" name="terms" value="Y" required />
        <label for="terms">이용 약관 및 개인정보 처리방침에 동의합니다<span class="required">*</span></label>
      </div>
      
      <!-- 제출 버튼 -->
      <button type="submit" id="joinRegBtn" class="btn-submit" disabled="disabled">가입하기</button>
    </form>
  </div>
  
<script>
	const duplicMsg = `<c:out value = "${duplicMsg}"/>`
	
</script>
<script type="text/javascript" src="../../../resources/js/duplicatedNickCheck.js"></script>

<jsp:include page="../layout/footer.jsp"></jsp:include>


