<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>
<title>도서관리시스템</title>
</head>
<body>
	<form action="" method="post" id="searchForm">
		<input type="hidden" name="page" id="page"/> 
		<select name="searchType">
			<option value="title" <c:if test="${searchType == 'title' }"><c:out value="selected"/></c:if>>제목</option>
			<option value="category" <c:if test="${searchType == 'category' }"><c:out value="selected"/></c:if>>카테고리</option>
		</select>
		<input type="text" name="searchWord" value="${searchWord }">
		<input type="submit" value="검색">
	</form>
	<hr/>
	
	<table border="1">
		<tr>
			<th>번호</th>
			<th>책제목</th>
			<th>카테고리</th>
			<th>가격</th>
		</tr>					<!-- retrieveController에서 만든 dataList -->
		<c:set value="${pagingVO.dataList }" var="list"></c:set>
		<c:choose>
			<c:when test="${empty list }">
				<tr>
					<td colspan="4">조회하신 게시글이 존재하지 않습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${list }" var="book">
					<tr>
						<td>${book.bookId }</td>
						<td>
							<a href="/book/detail2.do?bookId=${book.bookId}">
								${book.title }
							</a>
						</td>
						<td>${book.category }</td>
						<td>${book.price }</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<hr/>
	<div id="pagingArea">
		${pagingVO.pagingHTML }
	</div>
	<a href="/book/form.do">등록</a>
</body>
<script type="text/javascript">
$(function(){
	var searchForm = $("#searchForm");
	var pagingArea = $("#pagingArea");
	
	pagingArea.on("click", "a", function(event){
		event.preventDefault();
		var pageNo = $(this).data("page");
		searchForm.find("#page").val(pageNo);
		searchForm.submit();
	});
});
</script>
</html>