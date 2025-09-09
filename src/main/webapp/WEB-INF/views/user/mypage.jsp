<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="/resources/dist/css/profile-edit.css" rel="stylesheet"/>


<jsp:include page="../layout/header.jsp"></jsp:include>

<div class="profile-edit-container">
	<input type="hidden" id="imgErrMsg" name="imgErrMsg" value="${imgErrMsg }">
 	<%-- <input type="hidden" name=> --%>
	
  <h1>회원 정보 수정</h1>
  
  <form 
  	class="profile-edit-form" 
  	action="/user/update" 
  	method="post" 
  	enctype="multipart/form-data"
  >
 	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
  
    
    <!-- 프로필 사진 -->
    <div class="form-group">
      <label for="fileName">프로필 사진</label>
      <div class="profile-image-preview">
        <img
        	id="previewImg" 
        	src="/spring_myproject_up/profileImg/${uvo.fileName }"
        	alt="프로필 사진">
      </div>
      <!-- accept="image/*" 
      
      	> accept="image/*" 으로 설정하면 기본적으로 이미지 파일만 보이도록 필터링할 뿐
      		완벽히 차단하는 긱능이 아니므로 서버 사이드에서 업로드된 파일의 실제 
      		Content-Type 과 확장자를 서버에서 다시 검사해야 보안을 확보할 수 있음
      -->
      <input 
      	type="file" 
      	id="fileInput"
      	name="file" 
      	accept="image/*" />
    </div>
    
    
    <!-- 이름 -->
    <div class="form-group">
      <label for="id">ID</label>
      <input 
      	type="text"
      	name="id" 
      	value="${uvo.id }" 
      	readonly 
      />
    </div>
    
    
    <!-- 이메일 -->
    <div class="form-group">
      <label for="email">이메일</label>
      <input type="email" name="email" value="${uvo.email }"/>
    </div>
    
    <!-- 닉네임 -->
     <div class="form-group">
      <label for="nickName">닉네임</label>
      <input type="text" name="nickName" value="${uvo.nickName }" readonly/>
    </div>
    
    <!-- 비밀번호 변경 -->
    <div class="form-group">
      <label for="pwd">새 비밀번호</label>
      <input 
      	type="text" 
      	name="pwd"
       	placeholder="변경할 경우에만 새 비밀번호 입력하세요" />
    </div>
    
    <!-- 버튼 그룹 -->
    <div class="form-actions">
      <button type="submit" class="btn btn-primary">수정 완료</button>
      
      <script type="text/javascript"> 
      	const userId = "${uvo.id}" 
      	
     		document.getElementById('userDelBtn').addEventListener('click',() => {
     			location.href = `/user/delete?id=${userId}`;
     		})
      </script>
      
      <button 
      	type="button" 
      	id="userDelBtn"
      	class="btn btn-primary"
      >
      회원 탈퇴
      </button>        
     
      
      <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
    </div>
  </form>
</div>  
  
<script type="text/javascript" src="/resources/js/imgPreview.js"></script>
<script type="text/javascript" src="/resources/js/imgCheck.js"></script>

<jsp:include page="../layout/footer.jsp"></jsp:include>


