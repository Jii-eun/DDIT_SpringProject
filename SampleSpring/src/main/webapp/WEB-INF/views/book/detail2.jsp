<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>도서관리시스템</title>
</head>
<body>
	<h3>책 상세페이지</h3>
	<table>
		<!-- list.jsp에서는 map을 이용했기 때문에 컬럼명과 동일하게 대문자 + 스네이크표기법사용
			 list2.jsp에서는 vo를 이용했기 때문에 각 vo의 매개변수처럼 소문자 + 카넬 표기법 사용
		 -->
		<tr>		
			<td>제목</td>
			<td>${book.title} </td>			
		</tr>
		<tr>
			<td>카테고리</td>
			<td>${book.category } </td>			
		</tr>
		<tr>
			<td>가격</td>
			<td>${book.price } </td>			
		</tr>
		<tr>
			<td>입력일</td>
			<td>${book.insertDate } </td>			
		</tr>
		<tr>
			<td>
				<c:forEach items="${book.bookFileList }" var="bookFile">
<%-- 					<a href="/resources/upload/2/${bookFile.fileName }" download> --%>
						<!-- a태그에 download속성을 걸면 다운로드를 구현할 수 있음
							정확한 서버 업로드명을 넣어주면 바로 다운로드 가능
						 -->
						${bookFile.fileName }
<!-- 					</a>  -->
					(${bookFile.fileFancysize })
				</c:forEach>
			</td>
		</tr>
	</table>
	<a href="/book/update.do?bookId=${book.bookId}">수정</a>
	<a href="/book/list2.do">목록</a>
	
	<form method="post" action="/book/delete.do" id="delForm">
		<input type="hidden" name="bookId" value="${book.bookId}" />
		<input type="submit" value="삭제 " id="delBtn" />
	</form>
</body>
</html>

















