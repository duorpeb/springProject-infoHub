<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

<link rel="stylesheet" href="../../../resources/dist/css/register.css" />
<jsp:include page="../layout/header.jsp"></jsp:include>
	<c:set value = "${bdto.bvo }" var="bvo"></c:set>
	
	<div class="board-container">
    <h1>게시글 작성</h1>
    <form id="postForm" action="/board/update" method="post" enctype="multipart/form-data">
   		<!-- CSRF Token -->
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			
      <!-- 카테고리 선택 -->
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
      
      <!-- 제목 입력 -->
      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" name="title" value="${bvo.title }"
        	placeholder="제목을 입력하세요" required />
      </div>
      
      <!-- 작성자 (읽기 전용) -->
      <sec:authorize access="isAuthenticated()">
				<sec:authentication property="principal.uvo.nickName" var="nick"/>
	      <div class="form-group">
	        <label for="writer">작성자</label>
	        <input type="text" id="author" name="writer" value="${bvo.writer }" readonly />
	      </div>
      
      </sec:authorize>
      
      <!-- 내용 입력 -->
      <div class="form-group">
        <label for="content">내용</label>
        <textarea id="content" name="content" 
        rows="20" placeholder="내용을 입력하세요" required>${bvo.content }</textarea>
      </div>
      
      
      <!-- 파일 첨부 -->
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
      
      <!-- 버튼 -->
      <div class="form-actions">
				<button 
					type="submit" 
					id="regBtn" 
					class="btn btn-primary"
				>
					수정 완료
				</button>
        <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
      </div>
      
      <!-- 업로드된 파일 프린트 -->
		<div class="mb-3">
			<ul class="list-group list-group-flush">
				<!-- 파일 개수만큼 li 반복, 반복 타입이 1이면 그림을 표시 아니면 그냥 파일 모양으로 표시 -->
				<c:forEach items="${bdto.fvoList }" var="fvo">
					<li 
						class="list-group-item"
						data-uuid = "${fvo.uuid}"
						data-bno = "${bvo.bno}">
						<c:choose>
							<c:when test="${fvo.fileType > 0 }">
								<div>
									<img src="/upload/${fvo.saveDir }/${fvo.uuid}_${fvo.fileName}">
								</div>
							
							</c:when>
						
							<c:otherwise>
								<!-- 일반 파일은 다운로드 가능하게 끔 -->
								<a 
									download="${fvo.fileName }"
									href="/upload/${fvo.saveDir }/${fvo.uuid}_${fvo.fileName}">
									<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-file-arrow-down-fill" viewBox="0 0 16 16">
										<path d="M12 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2M8 5a.5.5 0 0 1 .5.5v3.793l1.146-1.147a.5.5 0 0 1 .708.708l-2 2a.5.5 0 0 1-.708 0l-2-2a.5.5 0 1 1 .708-.708L7.5 9.293V5.5A.5.5 0 0 1 8 5"/>
									</svg>
								</a>
							</c:otherwise>
						</c:choose>
						<div class="mb-3">
							<div class="fw-bold">${fvo.fileName }</div>
						</div>
						<span class="badge text-bg-primary">${fvo.regDate } / ${fvo.fileSize } Bytes</span>
						<span> 
							<button 
								type = "button"
								class = "fileDelBtn"
								style = "width : 25px; height : 25px; background: none; border: none;">
								❌
							</button>
						</span>
					</li> 
				</c:forEach>
			</ul>
		</div>

		<!-- File -->
		<div class="mb-3">
			<label for="file" class="form-label"></label>
			<input 
				type="file" 
				class="form-control" 
				name="files" id="file" 
				multiple
				style = "display : none;"
			>
			
			<button type="button" id="trigger" class="btn btn-primary">파일 선택</button>		
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
<script type= "text/javascript" src="/resources/js/boardModify.js"></script>

