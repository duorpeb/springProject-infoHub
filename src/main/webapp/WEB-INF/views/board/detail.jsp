<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

<link rel="stylesheet" href="../../../resources/dist/css/register.css" />
<link rel="stylesheet" href="/resources/dist/css/comment.css" />
<link rel="stylesheet" href="/resources/dist/css/like.css" />

<jsp:include page="../layout/header.jsp"></jsp:include>

<c:set value = "${bdto.bvo }" var="bvo"></c:set>
<c:set value = "${bdto.fvoList }" var="fvoList"></c:set>

<div class="board-container">
	<!-- 비로그인 유저도 댓글을 볼수 있게 하기 위한 변수 선언 -->
	<script type="text/javascript">
		let authNick;
	</script>

	<!-- 제목 -->
	<div class="post-detail">
	 <div class="post-header">
		 <h1 class="post-title">${bvo.title }</h1>
		 
		 <div class="post-meta">
		   <span class="author">${bvo.writer }</span>
		   <span class="date">${bvo.regDate }</span>
		   <span class="views">${bvo.views }</span>
		 </div>
	</div>
	
	<!-- 내용 -->
  <div class="post-content">${bvo.content }</div>
 
  
	<!-- 버튼 -->
	<div
		id="btnConDiv" 
		class="form-actions"
	>
		<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal.uvo.nickName" var="authNick"/>	
			<sec:authentication property="principal.uvo.id" var="uvoId"/>	
			<c:set var="userId" value="${uvoId}" />	
			
		
			<c:if test="${bvo.writer eq authNick }">
				<a href="/board/modify?bno=${bvo.bno }">
					<button type="button" class="btn btn-primary">수정</button>		
				</a>
				
				<a href="/board/delete?bno=${bvo.bno }">
					<button type="button" class="btn btn-primary">삭제</button>		
				</a>			
			</c:if>
		</sec:authorize>
		
		<button type="button" class="btn btn-secondary" onclick="history.back()">
			뒤로 가기
		</button>
		   
		<a href="/board/detail?bno=${prev }">
			<button type="button" class="btn btn-outline-secondary">이전글</button>
		</a>
		
		<a href="/board/detail?bno=${next }">
			<button type="button" class="btn btn-outline-secondary">다음글</button>
		</a>	
	</div>
	
	
	<!-- 좋아요 -->
	<div class="likeContainer">
		<div class="emptyLikeIconDiv">
			<svg class="emptyLikeIcon" xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-hand-thumbs-up" viewBox="0 0 16 16">
  			<path d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2 2 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a10 10 0 0 0-.443.05 9.4 9.4 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a9 9 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.2 2.2 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.9.9 0 0 1-.121.416c-.165.288-.503.56-1.066.56z"/>
			</svg>
			<div style="margin-top:20px;">좋아요</div>
		</div>
		
		<div class="filledLikeIconDiv invisible">
			<svg class="filledLikeIcon" xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-hand-thumbs-up-fill" viewBox="0 0 16 16">
			  <path d="M6.956 1.745C7.021.81 7.908.087 8.864.325l.261.066c.463.116.874.456 1.012.965.22.816.533 2.511.062 4.51a10 10 0 0 1 .443-.051c.713-.065 1.669-.072 2.516.21.518.173.994.681 1.2 1.273.184.532.16 1.162-.234 1.733q.086.18.138.363c.077.27.113.567.113.856s-.036.586-.113.856c-.039.135-.09.273-.16.404.169.387.107.819-.003 1.148a3.2 3.2 0 0 1-.488.901c.054.152.076.312.076.465 0 .305-.089.625-.253.912C13.1 15.522 12.437 16 11.5 16H8c-.605 0-1.07-.081-1.466-.218a4.8 4.8 0 0 1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 14.464 2 13.846 2 13V9c0-.85.685-1.432 1.357-1.615.849-.232 1.574-.787 2.132-1.41.56-.627.914-1.28 1.039-1.639.199-.575.356-1.539.428-2.59z"/>
			</svg>
			<div style="margin-top:20px;">좋아요</div>
		</div>
	</div>
  
     
	<!-- 비로그인 유저가 보는 댓글 작성창 -->
 	<sec:authorize access="isAnonymous()">
    <div class="comments" style="margin-top : 40px;">
    	<div class="comment-form">
          <div
            class="comment-avatar">
            <img src="/spring_myproject_up/profileImg/basicImg.png">
          </div>
          <span class="input-group-text" id="cmtWriter">비멤버</span>

          <textarea
            name="comment"
            class="comment-input"
            id="cmtContent"
            placeholder="댓글 작성 시 로그인이 필요합니다."
          ></textarea>
          <button type="button" id="nonLogincmtAddBtn" class="btn-comment" disabled>댓글 등록</button>
        </div>
    </div>
 	</sec:authorize>
	
	
	<!-- 로그인 유저가 보는 댓글 작성창 -->
  <div class="comments" style="margin-top : 40px;">
    <h3 class="comments-title">댓글 수 - ${bvo.cmtCnt}</h3>

		<!-- 권한 별 댓글창 출력 - 로그인 유저 -->
		<sec:authorize access="isAuthenticated()">
			<!-- principal 은 Auth  -->
			<sec:authentication property="principal" var="p" />
			<%-- <c:set var = "변수이름" value = "변수값" [scope = "scope 속성 중 하나"] /> --%>
				<div class="comment-form">
          <div
            class="comment-avatar">
            <img src="/spring_myproject_up/profileImg/${p.uvo.fileName}">
          </div>
          <span class="input-group-text" id="cmtWriter">${p.uvo.nickName}</span>
				</div>
        
        
        <div class="cmtContentDiv">
	        <textarea
	          name="comment"
	          class="comment-input responsive-textarea"
	          id="cmtContent"
	          placeholder="댓글을 입력하세요…"
	         ></textarea>
	         <button type="button" id="cmtAddBtn" class="btn-comment">댓글 등록</button>
        </div>
			
			<script type="text/javascript">
        console.log("========댓글 작성 창========");

        const profileName = `<c:out value="${p.uvo.fileName}" />`;
        authNick = `<c:out value="${p.uvo.nickName}" />`;
        
        console.log(authNick);
        console.log(profileName);
			</script>
		</sec:authorize>
	</div> <!-- 댓글 작성창 fin -->


	<!-- 댓글 프린트, 작성된 댓글 표시 -->
  <ul class="comment-list" id="cmtUl">
    <!-- 반복되는 댓글 아이템 -->
    <!--<li class="comment-item">
      <div class="comment-avatar">
        <img th:src="@{|/spring_myproject_up/profileImg/${cvo.fileName }|}">
      </div>
      <div class="comment-body">
        <div class="comment-meta">
          <span class="username">${cvo.writer}</span>
          <span class="time">2025-06-20 14:32</span>
        </div>
        <p class="comment-text">
          안녕하세요! 프로필 사진 예시가 잘 나옵니다 😊
        </p>
      </div>
    </li>-->
  </ul>
	
	
	<%-- 댓글 더보기 버튼 
		 > 한 페이지에 5개 댓글을 표시 
			 
		 > 5개를 초과하는 경우 더보기 버튼을 활성화 5개 이하이면 더보기 버튼 비활성화
			 
		 > 더보기 버튼을 누르면 5개씩 댓글을 가져옴 
			 
		 > select * from comment where bno = #{bno} order by cno desc limit #{pageStart}, #{qty}
	--%>
	<div class="mb-3">
		<button 
			type="button" 
			id="moreBtn"
			class="btn btn-outline-info" 
			data-page="1"
			style="visibility : hidden;">
			더보기
		</button>
	</div>


	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	      <!-- title - cno 와 작성자 -->
	      <h1 class="modal-title fs-5" id="exampleModalLabel"></h1>
	      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	    </div>
	    <div class="modal-body">
	      <input type="text" id ="cmtTextMod" class = "form-control">
	    </div>
	    <!-- 모달창 -->
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
	        <button type="button" id="cmtModBtn" class="btn btn-primary">변경</button>
	      </div>
	    </div>
	  </div>
	</div>


	<!-- 업로드된 파일 프린트 -->
	<div class="mb-3">
		<ul class="list-group list-group-flush">
			<!-- 파일 개수만큼 li 반복, 반복 타입이 1이면 그림을 표시 아니면 그냥 파일 모양으로 표시 -->
	<c:forEach items="${bdto.fvoList }" var="fvo">
		<li class="list-group-item">
			<c:choose>
				<c:when test="${fvo.fileType > 0 }">
					<div>
						<!-- 최대크기 지정 (500 * 500) 및 비율 유지 (width : auto; height : auto) -->
						<img 
							src="/upload/${fvo.saveDir }/${fvo.uuid}_${fvo.fileName}"
							style="max-width : 500px; max-height : 500px; width : auto; height : auto"
							>
					</div>
				
				</c:when>
			
				<c:otherwise>
					<!-- 일반 파일은 다운로드 가능하게 끔 -->
					<a 
						download="${fvo.fileName }"
						href="/upload/${fvo.saveDir }/${fvo.uuid}_${fvo.fileName}"
					>
						<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-file-arrow-down-fill" viewBox="0 0 16 16">
						  <path d="M12 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2M8 5a.5.5 0 0 1 .5.5v3.793l1.146-1.147a.5.5 0 0 1 .708.708l-2 2a.5.5 0 0 1-.708 0l-2-2a.5.5 0 1 1 .708-.708L7.5 9.293V5.5A.5.5 0 0 1 8 5"/>
						</svg>
					</a>
				</c:otherwise>
			</c:choose>
			
			<!-- 파일 이름 -->
			<div class="mb-3">
				<div class="fw-bold">${fvo.fileName }</div>
			</div>
			
			<span class="badge text-bg-primary">${fvo.regDate } / ${fvo.fileSize } Bytes</span>
		</li> 
	</c:forEach>
		</ul>
	</div>
	
	</div>
</div>
	
	<script type="text/javascript">
		const bno = `<c:out value="${bvo.bno}" />`;
		const userId = `<c:out value="${userId}" />`;

		console.log(bno);
		console.log(userId);
	</script>
	<script type="text/javascript" src="/resources/js/boardDetail.js"></script>
	
<jsp:include page="../layout/footer.jsp"></jsp:include>