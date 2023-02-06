<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	<!-- c:xx 쓸 수 있음 -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>	<!-- form:xx 쓸 수 있음 -->
<link rel="stylesheet" href="/resources/css/bootstrap.min.css" />
<!-- ↓ daum API -->
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>
<script type="text/javascript">
	$(function(){
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
	 요청파라미터 : {cusNum=12345, cusNm=개똥이, postno=33233, cusAddr=주소
	 			,addrDet=상세주소, cusPhe=010-123-1223}
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
<!-- 			<input type="text" class="form-control" -->
<!-- 				id="cusNum" name="cusNum" placeholder="고객번호를 입력하세요" /> -->
			<!-- path : cusVO객체의 cusNum 멤버변수 -->
<%-- 			<form:input> 에는 type="text"랑 id="cusNum"이랑 name="cusNum"이 내장 --%>
			<form:input class="form-control"
					path="cusNum" placeholder="고객번호를 입력하세요" />
			<!-- path 는 cusVO객체의 cusNum 멤버변수 -->
			
			<form:errors path="cusNum" style="color:red;" />
		</div>
		
		<div class="mb-3">
			<label for="cusNm" class="form-label">
				고객명
			</label> 
<!-- 			<input type="text" class="form-control" -->
<!-- 				id="cusNm" name="cusNm" placeholder="고객명를 입력하세요"> -->
				<!-- ↑위 대신 ↓아래로 고침 -->
			<form:input path="cusNm" class="form-control"
						placeholder="고객명을 입력하세요"/>
						<!-- 로그인했을때 로그인 정보를 미리 넣을 수 있음!! 편하다 (주소, 연락처, 이름 정도) -->
			<form:errors path="cusNm" style="color:red;" />
		</div>
		
		<div class="mb-3">
			<div> 
				<label for="postno" class="form-label">우편번호</label>
			</div>
			<div> 
				<input type="text" class="form-control"
					id="postno" name="postno" placeholder="우편번호를 입력하세요"
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
				id="cusAddr" name="cusAddr" placeholder="주소를 입력하세요">
		</div>
		<div class="mb-3">
			<label for="addrDet" class="form-label">
				상세 주소
			</label> 
			<input type="text" class="form-control"
				id="addrDet" name="addrDet" placeholder="상세 주소를 입력하세요">
		</div>
		
		<div class="mb-3">
			<label for="cusPhe" class="form-label">
				연락처
			</label> 
			<input type="text" class="form-control"
				id="cusPhe" name="cusPhe" placeholder="연락처를 입력하세요">
			<form:errors path="cusPhe" style="color:red;"/>
		</div>
		<!-- jsp에 입력한 내용이 CusVO로 넘어갈 때 String으로 넘어간다.
			 그런데 VO에서 cusBir에 설정한 타입은 Date. 오류가 날 수 있다. => CusVO에서 처리
		 -->
		
		<div class="mb-3">
			<label for="cusBir" class="form-label">
				생일
			</label>
			<input type="date" class="form-control"
					id="cusBir" name="cusBir" placeholder="생일을 입력하세요">
			<form:errors path="cusBir" style="color:red;"/>
		</div>
		
		<div class="mb-3">
			<label for="exampleFormControlTextarea1" class="form-label">
				취미
			</label>			<!--  path : cusVO의 변수와 같게 -->
			<form:checkbox path="hobbyList" value="Music" label="Music"/>
			<form:checkbox path="hobbyList" value="Movie" label="Movie"/>
			<form:checkbox path="hobbyList" value="Sports" label="Sports"/>
		</div>
		
		<div class="mb-3">
			<!-- 성별 : (남성 : male, 여성  : female, 기타 : others) -->
			<label for="" class="form-label">성별</label>
			<form:radiobutton path="gender" value="male" label="male"/>
			<!-- cusVO.setGender("female"); -->
			<form:radiobutton path="gender" value="female" label="female"/>
			<form:radiobutton path="gender" value="others" label="others"/>
		</div>
		
		<div class="mb-3">
			<!-- 국적(한 개 선택) => select박스 -->
			<label for="exampleFormControlTextarea1" class="form-label">국적</label>
			<form:select path="nationality" items="${nationalityMap }" />
													<!-- model로 가져옴 -->
		</div>

		<div class="mb-3">	<!-- 1:多관계 insert -->
			<!-- 고객 : 소유 자동차(List<CarVO>carVOList) = 1 : N -->
			<div class="card" style="width: 100%;">
			  <div class="card-header">
			    소유자동차
			  </div>
			  <ul class="list-group list-group-flush">
			    <li class="list-group-item">
<!-- 			    	<div>자동차번호 : <input type="text" class="form-control" style="width : 15; "/></div> -->
<!-- 			    	제조 번호 : <input type="text" class="form-control" style="width : 15%; flaot:left;" /> -->
<!-- 			    	연식 : <input type="text" class="form-control" style="width : 15%; flaot:left;" /> -->
<!-- 			    	주행거리 : <input type="text" class="form-control" style="width : 15%; flaot:left;" /> -->
						
						<input type="hidden" name="carVOList[0].cusNum" value="${cusNum }" />
	 					<input type="text" class="form-control" name="carVOList[0].carNum"
			               style="width:25%;float:left;" placeholder="자동차번호" required/>
			            <input type="text" class="form-control" name="carVOList[0].mnfNum"
			               style="width:25%;float:left;" placeholder="제조 번호" required/>
			            <input type="date" class="form-control" name="carVOList[0].dt"
			               style="width:25%;float:left;" placeholder="연식" required/>
			            <input type="text" class="form-control" name="carVOList[0].dist"
             			   style="width:25%;float:left;" placeholder="주행거리" required/>
             			   <!-- CarVO에 있는 변수들이니까 -->
	 					
			    </li>
			    <li class="list-group-item">
						<input type="hidden" name="carVOList[1].cusNum" value="${cusNum }" />
	 					<input type="text" class="form-control" name="carVOList[1].carNum"
			               style="width:25%;float:left;" placeholder="자동차번호" />
			            <input type="text" class="form-control"name="carVOList[1].mnfNum"
			               style="width:25%;float:left;" placeholder="제조 번호" />
			            <input type="date" class="form-control" name="carVOList[1].dt"
			               style="width:25%;float:left;" placeholder="연식" />
			            <input type="text" class="form-control"  name="carVOList[1].dist"
             			   style="width:25%;float:left;" placeholder="주행거리" />
	 					
			    </li>
			  </ul>
			</div>
		</div>
	
		
<!-- 		<div class="mb-3"> -->
<!-- 			<label for="exampleFormControlTextarea1" class="form-label"> -->
<!-- 				Example textarea -->
<!-- 			</label> -->
<!-- 			<textarea class="form-control" id="exampleFormControlTextarea1" -->
<!-- 				rows="3"></textarea> -->
<!-- 		</div> -->
	
		<div class="mb-3">
			<button type="submit" class="btn btn-primary btn-lg">
				등록
			</button>
		</div>
	</div>
</form:form>























