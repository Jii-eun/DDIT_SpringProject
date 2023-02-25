<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	<!-- c:xx 쓸 수 있음 -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>	<!-- form:xx 쓸 수 있음 -->
<link rel="stylesheet" href="/resources/css/bootstrap.min.css" />
<!-- ========================= ↓ daum API -->
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>

<script type="text/javascript">
	$(function(){
		$("#btnPostno").on("click", function(){
			new daum.Postcode({
				//다음 창에서 검색이 완료되면 콜백함수에 의해 결과 데이터가 data 객체로 들어옴
				oncomplete:function(data){
					$("#zipCode").val(data.zonecode);
					$("#address").val(data.address);
					$("#detAddress").val(data.buildingName);
				}
			}).open();	
		});
		
		//직원번호 자동 등록
		//ajax => 아작났어유..피씨다타써
		// 피 : processType : falsee는 파일업로드 시 사용(let=formData = new formData())
		// 씨 : contentType : "application/json;charset:utf-8"(보내는 타입)
		// 다 : data : JSON.stringify(data);
		// 타 : type : "post"
		// 써 : success:function(){}
		$.ajax({
			url : "/emp/getEmpNum", 
			type : "post",
			beforeSend : function(xhr) {   // 데이터 전송 전  헤더에 csrf값 설정
                xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
			},
			success : function(result){
				console.log("result : " + result);
				$("#empNumAjax").val(result);
			}
		})
		
		//매니저 선택하기
		$("#btnEmpMjNum").on("click", function(){
			$.ajax({
				url : "/emp/getEmpAll",
				type : "post",
				beforeSend : function(xhr) {   // 데이터 전송 전  헤더에 csrf값 설정
	                xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
				},
				success:function(result){
					//result : List<EmpVO> empVOList
					let str = "";

					$.each(result, function(index, empVO){
						console.log("empVO.empNum : " + empVO.empNum);
						console.log("empVO.empNm : " + empVO.empNm );
						
						//<th scope="row"> : 해당 셀이 행(row)을 위한 헤더 셀임을 명시함.
						str += "<tr class='trSelect'><th scope='col'>" + (index +1) + "</th>";
						str += "<td>" + empVO.empNum + "</td><td>" + empVO.empNm + "</td></tr>";
					});
					
					console.log("str : " + str);
					//.html() :  새로고침/ .append() : 누적
// 					$("#trAdd").html("");	//초기화
// 					$("#trAdd").append(str);
					//아래처럼 그냥 html로 한번에 넣어도 된다.
					$("#trAdd").html(str);	//초기화
					
				}
			});
		});
		
		//동적으로 생성된 요소의 이벤트 처리
// 		$(".trSelect").on("click", function(){})  // => 안 된 다	동적으로 생성돼서
		$(document).on("click", ".trSelect", function(){
			//this : tr이 여러개인데 그 중 클릭한 바로 그 tr
			//선택한 회원 색깔 노란색으로 바뀌게 하기
			$(this).children("td").css("background-color", "yellow");
		
		
			//td들을 가져옴. 그 안에 데이터를 가져와보자
// 			let objArr = $(this).children("td");
// 			$.each(objArr, function(index, obj){
// 				let data = obj.text;
// 				console.log(data);
// 			}) //=========> log에 undefined 뜸

			//td들을 가져옴. 그 안에 데이터를 가져와보자2 쌤	
											//map()안에 있는 함수 = 콜백함수 -> 함수 처리한 것을 map에 넣는다.
			let resultStr = $(this).children().map(function(){	
							//$(this) == <tr> //$(this).children() == <td>
							// .map ==> 하나하나를 끄집어내서 map처럼 쓰겠다.
				return $(this).text();}).get().join();
											//join()랑 join(",") 둘다 ,를 붙여준다.
											//join안에 아무것도 안넣어도 쉼표 넣어줌 맨뒤에는 알아서 빼줌!
			console.log("resultStr : " + resultStr);
			
			//resultStr : 5, EMP005, 이정재 => split(",")을 해서 배열로 만들고
			//[1]은 매니저번호로 입력, [2]는 매니저명으로 입력
			//arr[0] : 5 / arr[1] : EMP005 / arr[2] : 이정재  
			var resultArr = [];
			resultArr = resultStr.split(',');
			console.log("resultArr[1] : " + resultArr[1] );
			
			//val 과 text 차이***************
// 			$("input[name=empNum]").val(resultArr[1]); //==> readonly여도 이렇게 해놓으면 값이 바뀐다
			$("input[name=empMjNum]").val(resultArr[1]);
			$("#empMjNm").val(resultArr[2]);
			
		});
	
		
	});
	
</script>

<nav class="navbar bg-body-tertiary">
	<div class="container-fluid">
		<a class="navbar-brand" href="#">직원관리</a>
	</div>
</nav>

<!-- 요청URI : /emp/createPost
	 요청파라미터 : {empNum=EMP001, zipCode=12345, address=대전..., empMjNum= }
	 요청방식 : post	 
 -->
<form:form modelAttribute="empVO" action="/emp/createPost" method="post"
		 class="row g-3">
	<!-- 직원번호 -->
	<div class="col-md-6" style="clear:both;">
	  <label for="empNum" class="form-label">직원번호</label>
<!-- 	  <input type="text" name="empNum" class="form-control" id="empNum" -->
<%-- 	  	value="${empNum}" placeholder="직원번호를 입력해주세요" /> --%>
<%-- 	  <form:input path="empNum" class="form-control" placeholder="직원번호를 입력해주세요" /> --%>
	  <input type="text" name="empNum" class="form-control" id="empNumAjax"
	  	value="${empVO.empNum}" placeholder="직원번호를 입력해주세요" readonly required/>
	  									<!-- disabled는 값이 안넘어가니까 readonly로 -->
	  <form:errors path="empNum" />
	</div>
	
	<!-- 직원명 -->
	<div class="col-md-6">
		<label for="empNm" class="form-label">직원명</label> 
		<input
			type="text" class="form-control" id="empNm" name="empNm"
			placeholder="직원명을 입력하세요">
		<form:errors path="empNm" />
	</div>
	
	<!-- 주소 -->
	<div class="col-md-4">
		<label for="zipCode" class="form-label">우편번호</label> 
			<input
				type="text" class="form-control" id="zipCode" name="zipCode"
				placeholder="우편번호를 검색하세요">
	</div>
	<div class="col-md-2">
		<label for="btnPostno" class="form-label" >　</label> 
		<button type="button" class="btn btn-primary" id="btnPostno">검색</button>
	</div>
	
	<div class="col-12">
		<label for="address" class="form-label">주소</label> 
		<input
			type="text" class="form-control" id="address" name="address"
			placeholder="주소를 검색하세요">
	</div>
	<div class="col-12">
		<label for="detAddress" class="form-label" >상세주소</label> 
		<input
			type="text" class="form-control" id="detAddress" name="detAddress"
			placeholder="상세주소를 입력하세요">
	</div>
	<!-- 연락처 -->
	<div class="col-md-6">
		<label for="empPhe" class="form-label">연락처</label> 
		<input
			type="text" class="form-control" id="empPhe" name="empPhe"
			placeholder="핸드폰번호를 입력하세요">
		<form:errors path="empPhe" />
	</div>
	<!-- 급여 -->
	<div class="col-md-6">
		<label for="empPay" class="form-label">급여</label> 
		<input
			type="text" class="form-control" id="empPay" name="empPay"
			placeholder="급여를 입력하세요">
		<form:errors path="empPay" />
	</div>
	<!-- 매니저 -->
	<div class="col-md-6">
			<label for="empNm" class="form-label">매니저명</label> 
			<input
				type="hidden" class="form-control" id="empMjNum" name="empMjNum" />
			<input
				type="text" class="form-control" id="empMjNm" name="empMjNm"
				placeholder="매니저명을 입력하세요">
		
	</div>
	<div class="col-md-2">
		<label for="btnPostno" class="form-label" >　</label> 
		<button type="button" class="btn btn-primary " data-toggle="modal"
				data-target="#exampleModal" id="btnEmpMjNum">검색</button>
	</div>


	<div class="col2">
		<button type="submit" class="btn btn-primary">등록</button>
	</div>
	

	
	
	
</form:form>

<!-- 모달 -->

<div class="modal fade" id="exampleModal" tabindex="-1"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h1 class="modal-title fs-5" id="exampleModalLabel">직원 검색</h1>
				<button type="button" class="btn-close" data-dismiss="modal"
					aria-label="Close"></button>
			</div>
			<div class="modal-body">
			<!--================ 직원목록 시작 -->
				<div class="bd-example">
					<table class="table table-hover">
						<thead>
							<tr>
								<th scope="col">#</th>
								<th scope="col">직원번호</th>
								<th scope="col">이름</th>
							</tr>
						</thead>
						<tbody id="trAdd">
						</tbody>
					</table>
				</div>
			<!--================ 직원목록 끝 -->

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary"
					data-dismiss="modal">닫기</button>
				<button type="button" class="btn btn-primary">저장</button>
			</div>
		</div>
	</div>
</div>



<!-- form태그 보내는 방법 세가지
1. ?
2. form 테그
3. ajax 
-->




















