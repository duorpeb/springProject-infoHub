<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="../../../resources/dist/css/login.css" rel="stylesheet">
<jsp:include page="../layout/header.jsp"></jsp:include>

	<div class="loginCon">
		<form action="/user/login" method="post">
			<!-- CSRF TOKEN 
					> SecurityConfig 에서 csrf.disable() 를 하는게 아니라면 다음 변수를 JS 에
					  전달해주어야 함 
			-->
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token }" />
		
	    <div class="pBox">
	      <div class="id">
	        <input 
	        	type = "text" 
	        	name = "id"
	        	class = "idbox" 
	        	placeholder="아이디"
	      	/>
	      </div>
	      <!--  -->
	      <div class="password">
	        <input type = "password" name="pwd" class = "pwbox" placeholder="비밀번호">
	      </div>
	      
	      
	      <div class="loginbut">
	        <button type = "submit" class = "submit">로그인</button>
	      </div>
	      
	      <!-- 로그인 실패 시 에러 메시지 출력을 위한 코드로 에러 메시지는 param 을
	           붙이지 않으면 에러가 발생 -->
	      <c:if test="${param.errMsg ne null }">
	      	
        			<p 
        				id="loginErrMsg"
        				style="color: red; width : 310px; text-align : center;"
        			>
        				${param.errMsg }
        			</p>
	      </c:if>
	      
	      <a href="/user/join" id="a_loginJoinMsg">
		      <p id="loginJoinMsg" style="width : 310px; text-align : center;">회원가입을 하지 않으셨나요?</p>	      
	      </a>
	    </div>
	  </form>	
	</div>
<jsp:include page="../layout/footer.jsp"></jsp:include>