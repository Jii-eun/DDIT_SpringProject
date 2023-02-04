<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/ckeditor/ckeditor.js"></script>
<title>책 수정하기</title>
</head>
<body>
 <h1>책 수정</h1>
 <!-- 폼페이지
 mav.addObject("data",data);// data:bookVO
  -->
 <!-- 
 요청URI : /create?title=개똥이글로리&category=소설&price=10000
 요청파라미터 : title=개똥이글로리&category=소설&price=10000
 요청 방식 : post
 
  -->
 <form action="/update" method="post">
    <!-- 폼 데이터 -->
    <input type="hidden" name="bookId" value="${data.bookId}"/>
    <p>제목:<input type="text" name="title" value="${data.title}" required/></p>
    <p>카테고리:<input type="text" name="category" value="${data.category}" required/></p>
    <p>가격:<input type="text" name="price" value="${data.price}" required/></p>
    <p>내용:<textarea name="cont" rows="5" cols="30" >${data.content}</textarea></p>
    <p>
       <input type="submit" value="저장"/>
       <input type="button" value="목록"/>
    </p>
 </form>
 
 <script type="text/javascript">
 CKEDITOR.replace('cont');
 </script>
 
</body>
</html>