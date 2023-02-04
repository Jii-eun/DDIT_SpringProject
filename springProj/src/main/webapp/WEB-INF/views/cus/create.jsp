<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	<!-- c:xx 쓸 수 있음 -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>	<!-- form:xx 쓸 수 있음 -->
<link rel="stylesheet" href="/resources/css/bootstrap.min.css" />
<!-- ↓ daum API -->
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>
<script type="text/javascript">
	$(function(){
		//다음 우편번호 검색
// 		function openHomeSearch(){	 //핸들러함수 안됐음
// 			new daum.Postcode({
// 				//다음 창에서 검색이 완료되면 콜백함수에 의해 결과 데이터가 data 객체로 들어옴
// 				oncomplete:function(data){
// 					$("#postno").val(data.zonecode);
// 					$("#cusAddr").val(data.address);
// 					$("#addrDet").val(data.buildingName);
// 				}
// 			}).open();
// 		}
		$("#btnPostno").on("click", function(){
			new daum.Postcode({
				//다음 창에서 검색이 완료되면 콜백함수에 의해 결과 데이터가 data 객체로 들어옴
				oncomplete:function(data){
					$("#postno").val(data.zonecode);
					$("#cusAddr").val(data.address);
					$("#addrDet").val(data.buildingName);
				}
			}).open();	
		});
	});
</script>
<!-- ctrl + shift + F  : 인덴트 자동 처리-->
<nav class="navbar bg-body-tertiary">
	<div class="container-fluid">
		<a class="navbar-brand" href="#">고객관리</a>
	</div>
</nav>
<!-- 폼페이지
	 요청URI : /cus/createPost
	 요청파라미터 : {cusNum=12345, cusNm=개똥이, postno=33233...}
	 요청방식 : post	
 -->
 		<!-- cusVO와 synchronize가 된다. -->
<form:form modelAttribute="cusVO" action="/cus/createPost" method="post">
	<!-- 폼데이터 -->
	<div class="bd-example">
		<div class="mb-3">
			<label for="cusNum" class="form-label">
				고객번호
			</label> 
			<input type="text" class="form-control"
				id="cusNum" name="cusNum" placeholder="고객번호를 입력해주세요" />
			<!-- path : cusVO객체의 cusNum 멤버변수 -->
			<form:errors path="cusNum" style="color:red;" />
		</div>
		<div class="mb-3">
			<label for="cusNm" class="form-label">
				고객명
			</label> 
			<input type="text" class="form-control"
				id="cusNm" name="cusNm" placeholder="고객명를 입력해주세요">
			<form:errors path="cusNm" style="color:red;" />
		</div>
		<div class="mb-3">
			<div> 
				<label for="postno" class="form-label">우편번호</label>
			</div>
			<div> 
				<input type="text" class="form-control"
					id="postno" name="postno" placeholder="우편번호를 입력해주세요"
					style="width:80%;float:left;" />
				<input class="btn btn-info" type="button" value="우편번호 검색"
					style="width:20%;float:right;" id="btnPostno" />
			</div>
		</div>
		<div class="mb-3">
			<label for="cusAddr" class="form-label">
				주소
			</label> 
			<input type="text" class="form-control"
				id="cusAddr" name="cusAddr" placeholder="주소를 입력해주세요">
		</div>
		<div class="mb-3">
			<label for="addrDet" class="form-label">
				상세 주소
			</label> 
			<input type="text" class="form-control"
				id="addrDet" name="addrDet" placeholder="상세 주소를 입력해주세요">
		</div>
		<div class="mb-3">
			<label for="cusPhe" class="form-label">
				연락처
			</label> 
			<input type="text" class="form-control"
				id="cusPhe" name="cusPhe" placeholder="연락처를 입력해주세요">
			<form:errors path="cusPhe" style="color:red;"/>
		</div>
	<!-- 	<div class="mb-3"> -->
	<!-- 		<label for="exampleFormControlTextarea1" class="form-label"> -->
	<!-- 			Example textarea -->
	<!-- 		</label> -->
	<!-- 		<textarea class="form-control" id="exampleFormControlTextarea1" -->
	<!-- 			rows="3"></textarea> -->
	<!-- 	</div> -->
	
		<div class="mb-3">
			<button type="submit" class="btn btn-primary btn-lg">
				등록
			</button>
		</div>
	</div>
</form:form>























