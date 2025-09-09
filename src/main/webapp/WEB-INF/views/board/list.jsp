<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 커스텀 태그 -->
<%@ taglib uri="http://myproject.com/jsp/customFunc" prefix="cf" %>

<link href="../../../resources/dist/css/list.css" rel="stylesheet">

<jsp:include page="../layout/header.jsp"></jsp:include>

<head>
	<style>
		.selAndSear, .searchDiv{
			display : flex;
		}
		
		.selAndSear{
			margin-bottom : 20px;
		}
		
		.selCont{
			height : 40px;
			
			/* background-color : #d3d3d3; */
		}
		
		.me-2{
			width : 600px;
		}
	</style>
</head>

<div class="container-md">
	<h3>${cf:getCategoryTit(param.category)}의 전체 게시글</h3>
	<p style = "margin-bottom : 20px;" >전체글개수 : ${ph.totalCnt }</p>
	
	<!-- Search -->
	<form action="/board/list">
		<input type="hidden" name="pageNo" value="1">
		<input type="hidden" name="qty" value="${ph.pgvo.qty }">
		<input type="hidden" name="category" value="${category}">
	
		<div class = "selAndSear">
			<select class= "selCont" name = "type">
				<option ${ph.pgvo.type eq null ? 'selected' : '' }>---검색어 선택---</option>
				<option value ="t" ${ph.pgvo.type eq 't' ? 'selected' : '' }>제목</option>
				<option value ="w" ${ph.pgvo.type eq 'w' ? 'selected' : '' }>작성자</option>
				<option value ="c" ${ph.pgvo.type eq 'c' ? 'selected' : '' }>내용</option>
				<option value ="tw" ${ph.pgvo.type eq 'tw' ? 'selected' : '' }>제목 + 작성자</option>
				<option value ="tc" ${ph.pgvo.type eq 'tc' ? 'selected' : '' }>제목 + 내용</option>
				<option value ="wc" ${ph.pgvo.type eq 'wc' ? 'selected' : '' }>작성자 + 내용</option>
				<option value ="twc" ${ph.pgvo.type eq 'twc' ? 'selected' : '' }>전체</option>
			</select>
		
			<!-- <nav class="navbar bg-body-tertiary"> -->
			  <div class="container-fluid searchDiv">
			      <input class="form-control me-2" name="keyword" value="${ph.pgvo.keyword }" type="search" placeholder="Search" aria-label="Search"/>
			      <button class="btn btn-outline-success" type="submit">검색</button>
			  </div>
			<!-- </nav> -->
		</div>
	</form>

	
	
	<table class="table">
	  <thead>
	    <tr>
	      <th scope="col">게시글 번호</th>
	      <th scope="col">제목</th>
	      <th scope="col">작성자</th>
	      <!-- <th scope="col">content</th> -->
	      <th scope="col">작성일시</th>
	      <th scope="col">조회수</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<c:forEach items="${ph.list }" var="list">
		    <tr>
		      <th scope="row">${list.bvo.bno }</th>
		      <td>
						<a href="/board/detail?bno=${list.bvo.bno}">${list.bvo.title }</a>
						<span>[ ${list.bvo.cmtCnt} ]</span>
					</td>
		      <td>${list.bvo.writer }</td>
		      <%-- <td>${bvo.content }</td> --%>
		      <td>${list.bvo.regDate }</td>
		      <td>${list.bvo.views }</td>
		    </tr>
	  	</c:forEach>
	  </tbody>
	</table>
	
	<!-- Pagination -->
	<nav aria-label="Page navigation example">
	  <ul class="pagination justify-content-center">
	  <!-- prev btn of Pagination -->
	    <li class="page-item ${ph.prev eq false ? 'disabled' : ''}"> 
	      <a 
	      	class="page-link" 
	      	href="/board/list?pageNo=${ph.startPage-1 }&qty=${ph.pgvo.qty}&type=${ph.pgvo.type}&keyword=${ph.pgvo.keyword}" 
	      	aria-label="Previous">
	        <span aria-hidden="true">&laquo;</span>
	      </a>
	    </li>
	    
	  <!-- Page Number btn of Pagination -->
    <!-- <li class="page-item "><a class="page-link" href="#">1</a></li> -->
	  <c:forEach begin="${ph.startPage}" end="${ph.endPage}" var="i">
			<li 
				class="page-item ${ph.pgvo.pageNo == i ? 'active' : ''}">
				<a 
					class = "page-link"
					href="/board/list?pageNo=${i }&qty=${ph.pgvo.qty}&type=${ph.pgvo.type}&keyword=${ph.pgvo.keyword}"> 
					${i }
				</a>
			</li>
		</c:forEach>
		
		<!-- next btn of Pagination  -->
	    <li class="page-item ${ph.next eq false ? 'disabled' : ''}">
	      <a 
	      	class="page-link" 
	      	href="/board/list?pageNo=${ph.endPage+1 }&qty=${ph.pgvo.qty}&type=${ph.pgvo.type}&keyword=${ph.pgvo.keyword}"
	      	aria-label="Next">
	        <span aria-hidden="true">&raquo;</span>
	      </a>
	    </li>
	  </ul>
	</nav>
</div>
<jsp:include page="../layout/footer.jsp"></jsp:include>