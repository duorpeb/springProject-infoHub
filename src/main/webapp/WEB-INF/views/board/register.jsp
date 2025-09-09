<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

<link rel="stylesheet" href="../../../resources/dist/css/register.css" />
<jsp:include page="../layout/header.jsp"></jsp:include>

	<div class="board-container">
    <h1>게시글 작성</h1>
    <form id="postForm" action="/board/insert" method="post" enctype="multipart/form-data">
   		<!-- CSRF Token -->
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			
      <!-- 1. 카테고리 선택 -->
      <div class="form-group">
        <label for="category">카테고리</label>
        <select id="category" name="category" required>
          <option value="">-- 선택하세요 --</option>
          <option value="news">뉴스</option>
          <option value="sports">스포츠</option>
          <option value="currentEvent">시사</option>
          <option value="it">IT</option>
          <option value="entertainment">연예</option>
          <option value="fashion">패션</option>
          <option value="economy">경제</option>
        </select>
      </div>
      
      <!-- 2. 제목 입력 -->
      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" name="title" 
        	placeholder="제목을 입력하세요" required />
      </div>
      
      <!-- 3. 작성자 (읽기 전용) -->
      <sec:authorize access="isAuthenticated()">
				<sec:authentication property="principal.uvo.nickName" var="nick"/>
	      <div class="form-group">
	        <label for="writer">작성자</label>
	        <input type="text" id="author" name="writer" value="${nick }" readonly />
	      </div>
      
      </sec:authorize>
      
      <!-- 4. 내용 입력 -->
      <div class="form-group">
        <label for="content">내용</label>
        <textarea id="content" name="content" rows="20" placeholder="내용을 입력하세요" required></textarea>
      </div>
      
      
      <!-- 5. 파일 첨부 -->
      <div class="form-group">
        <label for="file">첨부파일</label>
        
        <input 
					type="file" 
					id="file"
					class="form-control" 
					name="files"  
					multiple
					style = "display : none;"
				>
			
				<button type="button" id="trigger" class="btn btn-primary">파일 선택</button>		
      </div>
      
      <!-- 6. 버튼 -->
      <div class="form-actions">
				<button 
					type="submit" 
					id="regBtn" 
					class="btn btn-primary"
				>
					등록
				</button>
        <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
      </div>
      
      <!-- 첨부 파일 표시 -->
			<div 
				class="mb-3"
				id="fileZone"
				>
			</div>
    </form>
	</div>
<jsp:include page="../layout/footer.jsp"></jsp:include>

<script type= "text/javascript" src="/resources/js/boardRegisterFile.js"></script>

