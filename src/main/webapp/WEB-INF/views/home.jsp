<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- <%@ page session="false" %> --%>

<jsp:include page="./layout/header.jsp"></jsp:include>
<link rel="stylesheet" href="/resources/dist/css/home.css">
  <!-- News & Weather API -->
  <div id="apiSection">
    <div class="news">
    	<div class="explainationNewsDiv">
    		<h3 style="margin-bottom: 22px;">📰 실시간 뉴스 기사 </h3> 
    	</div>
    	<!-- 뉴스 출력 -->
    	<ul class="newsUl">
    		<c:forEach items="${news.items}" var="item" >
    			<li class="newsLi list-group-item list-group-item-warning list-group-item-action ">
    				<!-- 제목 -->
    				<div class="newsTit">
	    				<a href="${item.originallink} ">${item.title}</a>
    				</div>
  
  					<!-- 짧은 내용  -->
    				<div class="newsDescription">${item.description}</div>
    			</li>
    		</c:forEach>
    	</ul>
    </div>
  </div>
  
  <div id="apiSection" style="display: block">
  	<div style="margin: 13px 16px; height: 50px; display: flex; ">
	  	<span>
	  		<img id="weatherIconImg" src="/resources/img/weather-icon.png" >
	  	</span> 
	  	<h3 style ="margin-left: 13px; line-height: 42px;">이번주 날씨</h3>
	  	<h6 
	  		class="locationInWeather"
	  		style ="margin-left: 13px; line-height: 42px;"
	  		></h6>
  	</div>
  	
	  <div class="weatherPrintDiv">
	  	<c:forEach items="${week}" var="week" begin="0" end="3">
		    <div class ="dayInWeatherPrintDiv">
		    	<div class = "dayCntIndayPrintDiv">
		    		${week } 일
		    	</div>
		    	
		    	<div class ="weatherInfoIndayPrintDiv">
		    		<div class="weather-img-div"></div>
		    		
		    		<div class="weather-txt-div"></div>
		    	</div>
		    	
		    </div>
	  	</c:forEach>
	  </div>
	</div>
	
	
  <!-- Category Navigation -->
  <nav id="categoryNav">
    <ul>
      <li class="news-li"><a href="/board/list?category=news&pageNo=1&qty=10">뉴스</a></li>
      <li class="sports-li"><a href="/board/list?category=sports&pageNo=1&qty=10">스포츠</a></li>
      <li class="current-li"><a href="/board/list?category=currentEvent&pageNo=1&qty=10">시사</a></li>
      <li class="it-li"><a href="/board/list?category=it&pageNo=1&qty=10">IT</a></li>
      <li class="enter-li"><a href="/board/list?category=entertainment&pageNo=1&qty=10">연예</a></li>
      <li class="fashion-li"><a href="/board/list?category=fashion&pageNo=1&qty=10">패션</a></li>
      <li class="economy-li"><a href="/board/list?category=economy&pageNo=1&qty=10">경제</a></li>
      <li><a id="boardReg" href="/board/register">게시물 작성</a></li>
    </ul>
  </nav>

  <!-- 게시글 출력 영역, SNS 스크롤 방식 게시글 -->
  <div id="boardContainer">
    <!-- Container Div -->
    <div id="boardDiv">
    	<c:forEach items="${list }" var="bdto">
    		<c:set value = "${bdto.bvo.bno }" var="bno" />
    		<!-- 하나의 게시글  -->
    		<div class="post">
    			<!-- 포스트 헤더 -->
    			<div class="post-header">
    				<!-- 프로필 이미지  -->
    				<div class="avatar">
    					<img 
    						src="/spring_myproject_up/profileImg/${bdto.bdFileName }"
    						style="width : 60px; height : 60px;">
    				</div>
    			
	    			<!-- User 정보 -->
	    			<div class="user-info">
		    			<div class="username">${bdto.bvo.writer }</div>    			
	    			</div>
    			
	    			<!-- 작성일 -->
	    			<div class="meta">${bdto.bvo.regDate }</div>
    			</div>
    			
    			<!-- 제목 -->
    			<div class="title"><a href="/board/detail?bno=${bno }">${bdto.bvo.title }</a></div>
    			
    			<!-- 내용 -->
    			<div class="content ellipsis--line2">${bdto.bvo.content }</div>
    			
    			<!-- 좋아요 및 게시글 개수 -->
    			<div class="actions">
    				<div class="like">좋아요 ${bdto.bvo.recommend }</div>
    				<div class="comment">댓글 ${bdto.bvo.cmtCnt }</div>
    			</div>
    		</div>
    		
    	</c:forEach>
    </div>

    <!-- 추천 많은 순 / 댓글 많은 순 -->
    <div id="recommAndCmtDiv">
      <div id="recommandBoard">
        <h3>좋아요 많은 순</h3>
        <ol class="recommendOl">
        	<!-- 확인  -->
        	<%-- <li>${likeList }</li> --%>
        	
 					<c:forEach items="${likeList}" var="likes">
        		<li style="margin-bottom: 13px;">
	        		<div class="ellipsis--lineTwo">
		        		<a class="homeTitA" href="/board/detail?bno=${likes.bno }">${likes.title }</a>	        		
	        		</div>
        		</li>
        	</c:forEach>
        </ol>
      </div>
      
      <div id="cmtBoard">
        <h3>댓글 많은 순</h3>
        <ol class="cmtOl">
          <c:forEach items="${cmtList}" var="cmts">
        		<li style="margin-bottom: 13px;">
        			<div class="ellipsis--lineTwo" >
		        		<a class="homeTitA" href="/board/detail?bno=${cmts.bno }">${cmts.title }</a>
        			</div>        		
        		</li>
        	</c:forEach>
        </ol>
      </div>
      
      <div id="rctBoard">
       	<h3>조회수 많은 순</h3>      	
      	
        <ol class="rctOl ellipsis--lineTwo">
					<c:forEach items="${viewsList}" var="views">
        		<li style="margin-bottom: 13px;">
        			<div class="ellipsis--lineTwo">
        				<a class="homeTitA" href="/board/detail?bno=${views.bno }">${views.title }</a>
        			</div>
        		</li>
        	</c:forEach>
        </ol>
      </div>
    </div>
		
		<!-- scroll 시 추가 -->
		    
  </div>
  
	<!-- 바닥 감지용 센티넬 -->
	<div id="infinite-scroll-end" style="height: 1px"></div>
  
<script>
	const delMsg = `<c:out value="${delMsg}" />`;
	console.log(delMsg);
	
	const imgDir = `${pageContext.request.contextPath}`;
	
	if(delMsg == "ok"){ alert('회원 탈퇴가 성공적으로 완료되었습니다.'); }	
</script>

<script type="text/javascript" src="/resources/js/home.js"></script>
<script type="text/javascript" src="/resources/js/WeatherOpenAPIAppendixDoc.js"></script>
<script type="text/javascript" src="/resources/js/geolocationAndWeatherAPI.js"></script>

<jsp:include page="./layout/footer.jsp"></jsp:include>


