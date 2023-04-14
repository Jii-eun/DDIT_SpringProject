<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>
<title>도서관리시스템</title>
</head>
<body>
	<!-- 검색 시작 -->
	<p>
		<!-- action 속성 및 그 값이 생략 시, 현재 URI(/list)를 재요청 
			 method는 GET(form 태그의 기본 HTTP 메소드는 GET임)
		-->
<!-- 		<form action="/list" method="get">  ==> 안적은 것과 같음-->
		<form >
			<!-- param: keyword=톰소여  ▶ value="${param.keyword}"-->
			<!-- redirect를 안하면 param에 담긴 값이 계속 넘어간다.
				insert, update, delete할 때 redirect를 쓰는 이유 -->
			<!-- form에서 submit하면 입력해서 넘긴 값이 param으로 자동으로 들어간다. -->
														
			<inp
			
			
			ut type="text" name="keyword" value="${param.keyword }"
				placeholder="검색어를 입력하세요."/>
			<!-- /list?keyword=개똥이 -->
			<input type="submit" value="검색"	/>
		</form>		
	</p>

	<!-- 검색 끝 -->
	<p>
		<table border="1">
			<thead>
				<th>번호</th>
				<th>제목</th>
				<th>카테고리</th>
				<th>가격</th>
			</thead>
	      <!-- 
	         forEach태그 : 배열(String[], int[][]) Collection(List,Set) 또는
	                  Map(HashTable, HashMap, SortMap)에 저장되어있는 값들을
	                  순차적으로 처리할 때 사용함. 자바의 for, do-while을 대신하여 사용
	         data : mav.addObject("data",bookVOList)
	         data : List<BookVO>
	         var : 변수(variable)
	         items : 아이템(배열, Collection, Map)
	         varStatus : 루프 정보를 담은 객체
	            - index : 루프(반복) 실행 시 현재 인덱스(0부터 시작)
	            - count : 실행 횟수(1부터 시작, 보통 행번호를 출력)
	       -->
			<tbody>						<!-- data => 페이징처리 후 => data.content : 멤버변수에서 정보를 가져와야함-->
										<!-- 페이징 처리 후에는 data로 들어오는 값이 ArticlePage이기 때문에 값을 꺼낼 때 멤버변수를 써야한다. -->
				<c:forEach var="bookVO" items="${data.content}" varStatus="stat">
					<tr>
						<td>${bookVO.rnum}</td>
						<td><a href="/detail?bookId=${bookVO.bookId }">${bookVO.title}</a></td>
						<td>${bookVO.category}</td>
						<td><fmt:formatNumber type="number" maxFractionDigits="3"
	               			value="${bookVO.price }" /></td>
	         	   </tr>
				</c:forEach>
			</tbody>
		</table>
	</p>
	<p>
		<a href="/create">책 생성</a>
	</p>
	
	<!-- 페이징 -->
	<div class="col-sm-12 col-md-7">
		<div class="dataTables_paginate paging_simple_numbers"
			id="dataTable_paginate">
			<ul class="pagination">
				<li class="paginate_button page-item previous 
							<c:if test='${data.startPage lt 6}'>disabled</c:if>" 
					id="dataTable_previous">
					<a href="/list?currentPage=${data.startPage-5}"
						aria-controls="dataTable" data-dt-idx="0" tabindex="0"
						class="page-link">Previous</a>
				</li>
				
				<!--  -->
				<c:forEach var="pNo" begin="${data.startPage}" end="${data.endPage}">
					<li class="paginate_button page-item
						<c:if test='${param.currentPage == pNo }'>active</c:if>
					">
						<!-- keyword=22 : 파라미터(param) -->
						<a href="/list?keyword=${param.keyword}&currentPage=${pNo }"
							aria-controls="dataTable" data-dt-idx="1" tabindex="0"
							class="page-link">${pNo}</a>
					</li>
				</c:forEach>	
										<!--  ge 는 >= 와 같음 -->
				<li class="paginate_button page-item next
							<c:if test='${data.endPage ge data.totalPages }'>
								disabled
							</c:if> " 
					id="dataTable_next">
					<a	href="/list?currentPage=${data.startPage+5}" aria-controls="dataTable" 
						data-dt-idx="7" tabindex="0"
						class="page-link">Next</a>
				</li>
			</ul>
		</div>
	</div>
	
</body>
</html>






























