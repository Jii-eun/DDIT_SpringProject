<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>
<title>도서관리시스템</title>
</head>
<body>
	<h3>책 등록</h3>
	<hr/>
	<form action="" method="post" >
		<table> 
			<tr>
				<td>제목</td>
				<td>
					<input type="text" name="title" id="title"/>
				</td>
			</tr>
			<tr>
				<td>카테고리</td>
				<td>
					<input type="text" name="category" id="category"/>
				</td>
			</tr>
			<tr>
				<td>가격</td>
				<td>
					<input type="text" name="price" id="price"/>
				</td>
			</tr>
		</table>
		<input type="submit" value="등록" />
		<!-- action이 없는 상태에서 submit을 하면 이 페이지의 주소(form.do)로 실행됨 -->
	</form>
	<input type="button" value="목록" onclick="location.href='/book/list.do'"/>
	
</body>
</html>





































