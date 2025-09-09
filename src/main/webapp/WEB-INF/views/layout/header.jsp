<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Info hub</title>
<link href="/resources/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="/resources/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<!-- JS 의 댓글 작성 (비동기 작업) 시 CSRF 토큰을 함께 작성하기 위한 초기화 -->
	<sec:csrfMetaTags/>
	<meta name="_csrf" content="CSRF_TOEKN_VALUE">
	<meta name="_csrf_header" content="X-CSRF-TOKEN">
	
	<nav class="navbar navbar-expand-lg bg-body-tertiary">
		<div class="container-fluid">
		  <a class="navbar-brand" href="/">
		  	<img 
		  		src="../../../resources/img/logo.png"
		  		style="width : 120px; height : 120px;"
		  		>
		  </a>
		  
		  <button 
		  	class="navbar-toggler" 
		  	type="button" 
		  	data-bs-toggle="collapse" 
		  	data-bs-target="#navbarSupportedContent" 
		  	aria-controls="navbarSupportedContent" 
		  	aria-expanded="false" 
		  	aria-label="Toggle navigation"
		  >	
		    <span class="navbar-toggler-icon"></span>
		  </button>
		  
		  <div class="collapse navbar-collapse" id="navbarSupportedContent">
		    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
		     <!--
		      <li class="nav-item">
		        <a class="nav-link" href="/">Home</a>
		      </li> -->
		      
		      
		      <sec:authorize access="isAnonymous()">
		      <!-- 로그인 -->
			      <li class="nav-item">
			        <a class="nav-link" href="/user/login">로그인</a>
			      </li>		      
		      </sec:authorize>
		      
		      
		      <!-- 로그아웃 -->
		      <sec:authorize access="isAuthenticated()">
		      	<sec:authentication property="principal" var="p"/>
		      	
		      	<form action="/user/logout" method="post" id="lgForm">
			      	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			      	
			      	<li class="nav-item">
			        	<a 
			        		id="lgLink"
			        		class="nav-link"
			        		style="cursor : pointer; " 
			        	>
			        		로그아웃
			        	</a>
			      	</li>
		      	
		      	</form>
		      	
		      	<li class="nav-item">
		        	<a class="nav-link" href="/user/mypage?id=${p.username }">
		        		${p.username } 님의 MyPage
		        	</a>
		      	</li>
		      	
		      	<%-- ADMIN 권한이 있는지 확인하기 위한 구문
		      	
		      	> 1번 구문 
			    	${pri.authorities.stream().anyMatch(authVO -> autoVO.equals("ROLE_ADMIN")).get() }
			    	
			    	> 2번 구문
			    	${pri.uvo.authList.stream().anyMatch(authVO -> autoVO.auth.equals("ROLE_ADMIN")).get() }
			    	
			    	> 확인
			    	<div>${pri.uvo }</div>	 
		      	<div>${p }</div>	
		      	
		      	--%>
		      	
		      	<!-- ROLE_ADMIN 만 신고내역을 볼 수 있게 설정 -->
						<sec:authorize access="hasRole('ADMIN')">
							<li class="nav-item">
						    <a class="nav-link" href="/user/report">신고내역</a>
						  </li>
						</sec:authorize>
						
		      	
		      </sec:authorize>
		      
<!-- 		      <li class="nav-item dropdown">
		        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="true">
		          Dropdown
		        </a>
		        
		        
						<ul class="dropdown-menu">
		          <li><a class="dropdown-item" href="#">Action</a></li>
		          <li><a class="dropdown-item" href="#">Another action</a></li>
		          <li><hr class="dropdown-divider"></li>
		          <li><a class="dropdown-item" href="#">Something else here</a></li>
		        </ul> 
		      </li> -->
		      
<!-- 		      <li class="nav-item">
		        <a class="nav-link disabled" aria-disabled="true">Disabled</a>
		      </li> -->
		    </ul>
		  </div>
		</div>
	</nav>

	<script type="text/javascript" src="../../../resources/js/logout.js"></script>