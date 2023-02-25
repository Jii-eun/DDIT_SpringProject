<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<link rel = "stylesheet" href="/resources/css/bootstrap.min.css" />
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>
<script type="text/javascript" src="resources/ckeditor/ckeditor.js"></script>
<title>도서관리시스템</title>
</head>
<body>
<!-- 사진 넘어온거 체크용 -->
<%-- ${data } --%>

<!-- mav.addObject("data", data); -->
<%-- ${data } --%>
<hr/>
<!-- mav.addObject("bookId", data.getBookId()); //기본키 값 -->
<%--  ${bookId } --%>

<!--
	JSTL(JSP Standard Tag Library) : 개발자가 자주 사용하는 패턴을 모아놓은 집합
	=> BookController에서 보내준 데이터를 뷰에 표현하도록 도와줌
	JSTL은 maven에서 설정되어 있음 => pom.xml => jstl
-->
	<h1>책 상세</h1>
	<p>제목 : ${data.title }</p>
	<p>카테고리 : ${data.category }</p>
	<p>가격 : <fmt:formatNumber type="number" maxFractionDigits="3"
				value ="${data.price }" /></p>
	<p>입력일 : <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss"
				value="${data.insertDate }" /></p>
	<p>내용 : 
		<textarea name="cont" rows="5" cols="30" readonly>${data.content }</textarea>
	</p>
	
		<div class="accordion" id="accordionExample">
			<div class="accordion-item">
			  <h2 class="accordion-header" id="headingOne">
			    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
			      	첨부파일
			    </button>
			  </h2>
			  
			  <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
			    <div class="accordion-body">
		
					<!-- data.attachVOList : List<AttachVO> -->
					<!-- c:forEach는 viv => var, items, varStatus -->
					<!-- 
						모달을 띄우는 방법
						1. button으로 띄우기
							<button type="button" class="btn btn-default"
									data-toggle="modal" data-target="#modal-default">
							Default Modal 띄우기
							</button>											
						2. a 태그로 띄우기
							<a data-toggle="modal" href="#modal-default">
						3. 기타 요소로 띄우기
							<p data-toggle="modal" data-target="#modal-default"></p>
					 -->
					<c:forEach var="attachVO" items="${data.attachVOList }" varStatus="stat">
						<div class="card" style="width: 200px; display: inline-block;">
							<img src="/resources/upload${attachVO.thumbnail }" class="card-img-top" 
									title="${attachVO.thumbnail }" alt="${attachVO.thumbnail }">
							<div class="card-body">
								<p class="card-title">${attachVO.filename }</p>
						    	<p class="card-text">파일크기:${attachVO.filesize}</p>
						    	<a class="btn btn-outline-primary" 
						    		data-filename="${attachVO.filename }"
						    		data-toggle="modal" href="#modal-default">
						    		보기
						    	</a>
							</div>
						</div>
					</c:forEach>
		
			    </div>
		    </div>
		  </div>
		</div>

	<p><a href="/update?bookId=${data.bookId }">수정폼</a></p>
	<p><a href="/list">목록</a></p>
	<p>
	<!-- 
		method
		1) GET : 데이터를 변경하지 않을 때.	목록/상세보기
		2) POST : 데이터를 변경할 때.		입력/수정/삭제
	 -->	
	 	<!-- 폼페이지 -->
		<form action="/delete" method="post">
			<!-- 폼데이터 -->
			<input type="hidden" name="bookId", value="${bookId }" />
			<button type="submit">삭제</button>
			<!-- ↑ input type도 되고, button type도 된다.0 -->
		</form>	
	</p>
	
	
<script type="text/javascript">
	CKEDITOR.replace("cont");
// 	CKEDITOR.instances["content"].setReadOnly(true);
// 	CKEDITOR.instances["content"].setReadOnly(flase); => false면 readonly가 풀림
</script>
</body>
</html>









