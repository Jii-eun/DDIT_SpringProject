<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	<!-- c:xx 쓸 수 있음 -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>	<!-- form:xx 쓸 수 있음 -->
<link rel="stylesheet" href="/resources/css/bootstrap.min.css" />
<link rel="stylesheet" href="/resources/css/sweetalert2.min.css" />
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/resources/js/sweetalert2.min.js"></script>
<!-- ========================= ↓ daum API -->
<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>

<script type="text/javascript">
	$(function(){
			
		$(".nav-link").on("click", function(){		//nav-item은 li , nav-link는 a태그
			//직원탭에서 수정버튼 누르고 관리자탭 누르면 readonly로 잠기게 만들어줌!! ↓ 밑에 다섯줄
			$(".spn1").css("display", "block");
			$(".spn2").css("display", "none");
			//읽기전용
			$(".form-control").attr("readonly", "true");
			//검색버튼 비활성
			$("#btnPostno").attr("disabled", true);
			$("#btnEmpMjNum").attr("disabled", true);
			
			
			//toggleClass() 메서드는 해당 클래스를 토글함(현재 없는 속성값이면 추가, 있는 속성값이면 삭제)
// 			$(this).toggleClass("active");
// 			$(this).attr("class", "nav-link active");	
			//=>이걸쓰면 두번눌렀을 때 class속성 전체가 사라짐... 만약 값에 "active"만 넣으면 nav-link도 사라져버림
			
			//toggleClass() 메서드는 해당 클래스를 토글함(현재 없는 속성값이면 추가, 있는 속성값이면 삭제)
			$(".link").toggleClass("active");
			//removeAttr() : 선택한 요소에서 하나 이상의 특성을 제거함
			$(".link").removeAttr("aria-current");	//두개 링크에서 모두 aria-current 속성을 제거
			//attr() : 선택한 요소의 속성을 추가함
			$(this).attr("aria-current", "page");	//내가 선택한 a링크에만 해당 속성 추가
			//this : link 클래스는 2개. 그 중에서 클릭한 바로 그 요소
			let id = $(this).data("id");	//employee 또는 manager
						//data-id 를 찾는 것..........요소에 data-name을 쓴 후 data("name") 이렇게 해도 쓸 수 있음
			//페이지가 실제로 바뀌는 건 이부분↓
			if(id == "employee") {	//직원 탭의 경우
				$("#employee").css("display", "block");
				$("#manager").css("display", "none");
			} else {	//직원 탭의 경우 직원 div를 보여주고, 관리자 div는 hidden처리함
				$("#employee").css("display", "none");
				$("#manager").css("display", "block");
			}
		})
	
		let Toast = Swal.mixin({
			toast:true,
			position : 'top-end',		//최상위의 오른쪽 끝
			showConfirmButton:false,
			timer:3000
		});
		
		Toast.fire({
			icon: 'success',
			title:'새로고침'
		});
		
		//수정버튼 클릭 => spn1:none / spn2:block
		$(".edit").on("click", function(){
			$(".spn1").css("display", "none");
			$(".spn2").css("display", "block");
			//읽기 전용 해제
			$(".form-control").removeAttr("readonly");
			//검색버튼 사용
			$("#btnPostno").removeAttr("disabled");
			$("#btnEmpMjNum").removeAttr("disabled");
		});

		//취소버튼 클릭 => spn1:block / spn2:none
		$(".cancel").on("click", function(){
			$(".spn1").css("display", "block");
			$(".spn2").css("display", "none");
			//읽기전용
			$(".form-control").attr("readonly", "true");
			//검색버튼 비활성
			$("#btnPostno").attr("disabled", true);
			$("#btnEmpMjNum").attr("disabled", true);
		});
		
		//삭제버튼 클릭
		$(".delete").on("click", function(){
			//파라미터를 javascript 변수에 저장하고자 한다면..↓
			let empNumJs = "${param.empNum}"; 	//이렇게 한다. 그렇지만 안쓴다.(DB를 거친게 아니라 보안에 취약할 수 있음)
			let empNum = $("#employeeEmpNum").val();  	
			//세션스토리지에  변수값을 넣고자 한다면..
			sessionStorage.setItem("empNum", empNum);  
			
			console.log("empNumJs : " + empNumJs + ", empNum : " + empNum);
			
			//true(1) / false(0)
			let cfm = confirm("삭제하시겠습니까?");
			console.log("cfm : " + cfm);
			
			if(cfm > 0){	//삭제 수행
				//json 오브젝트
				let data = {"empNum":empNum}
			
				//아작났어유 피씨다타써
				//contentType : 보내는 타입
				//dataType : 응답 데이터 타입
				
				$.ajax({
					url:"/emp/deletePost",
					contentType:"application/json;charset=utf-8",
					data:JSON.stringify(data),
					dataType:"json",
					type:"post",
					success:function(map){
						//controller에서 map.put("result", 1+""); / ResponseBody
						console.log("result :" + map);
						//controller에서 넘기면        result : {"result":"1"} 이렇게 넘어간다.
						let str = map.result;		//1 또는 0
						
						//str이 0보다 크면 성공, 아니면 실패
						//성공시 /emp/list로 이동 / 실패시 실패 메시지를 보여줌
						if(str > 0) {
							Toast.fire({
								icon: 'success',
								title:'삭제에 성공했습니다.'
								//https://sweetalert2.github.io/#icons
							});
							location.href="/emp/list";
						} else {
							Toast.fire({
								icon: 'warning',
								title:'삭제에 실패했습니다.'
								//https://sweetalert2.github.io/#icons
							});
						}
					}
				});
				
			} else {	//삭제 취소
				Toast.fire({
					icon: 'error',
					title:'삭제가 취소되었습니다.'
					//https://sweetalert2.github.io/#icons
				});
			}
			
		});
		
		
	});
	
</script>

<!-- 요청URI : /emp/createPost
	  요청파라미터 : {empNum=EMP001, zipCode=12345, address=대전..., empMjNum= 
				zipcode=12345, detAddress=주소상세}
	  요청방식 : post 
-->
<!-- data : List<EmpVO> empVOList
	 stat.index : 0부터
	 stat.count : 1부터
	 
	empVO : EmpVO(empNum=EMP007, empAddr=대전, empPne=010-, empNm=뷔, empPay=0, empMjNum=EMP006, serVOList=null)
	param : empNum=EMP007
	** param은 주소에서 가져오는 것 (url uri,,,,헷갈,,,)
	
	scope : page(jsp), request(동일 요청), session(동일 웹브라우저), application(웹브라우저)
-->
	
	<!-- c:forEach는 v i v -->
<c:forEach var="empVO" items="${data }" varStatus="stat">
<%-- 	${empVO.empNum}, ${param.empNum} <br/>	 --%>
	<!-- ==>empVO에서 가져온 직원번호와 param에서 가져온 직원번호가 다르면 해당직원, 같으면 관리자 -->
	<c:choose>
		<c:when test="${empVO.empNum==param.empNum }"><!-- 직원 -->
			<c:set var="employee" value="${empVO }" scope="page"/>
		</c:when>
		<c:otherwise>	<!-- 관리자 -->
			<c:set var="manager" value="${empVO }"/>
		</c:otherwise>
	</c:choose>
	
</c:forEach>

<nav class="navbar bg-body-tertiary">
	<div class="container-fluid">
		<a class="navbar-brand" href="#">직원관리</a>
	</div>
</nav>

<!-- 요청URI : /emp/createPost
	 요청파라미터 : {empNum=EMP001, zipCode=12345, address=대전..., empMjNum= }
	 요청방식 : post	 
 -->
	
<!-- 활성화된 페이지는 class="active", aria-current="page"가 있어야 하고
	  비 활성화된 페이지는 위의 속성이 없어져야 함
	-->
<ul class="nav nav-tabs">
	<li class="nav-item">
		<a class="nav-link link active" data-id="employee" aria-current="page" href="#">직원</a>
	</li>
	<li class="nav-item">
		<a class="nav-link link" data-id="manager" href="#">관리자</a>
	</li>
</ul><br/>

<div class="bd-example" id="employee" >
	<form:form modelAttribute="empVO" action="/emp/createPost" method="post"
			 class="row g-3">
		<input type="hidden" name="mode" id="mode" value="update" />
		<!-- 직원번호 -->
		<div class="col-md-6" style="clear:both;">
		  <label for="empNum" class="form-label">직원번호</label>
	<!-- 	  <input type="text" name="empNum" class="form-control" id="empNum" -->
	<%-- 	  	value="${empNum}" placeholder="직원번호를 입력해주세요" /> --%>
	<%-- 	  <form:input path="empNum" class="form-control" placeholder="직원번호를 입력해주세요" /> --%>
		  <input type="text" name="empNum" class="form-control" id="employeeEmpNum"
		  	value="${employee.empNum}" placeholder="직원번호를 입력해주세요" readonly required/>
		  	<!-- c:set에서 empVO를 employee로 넣었음 -->	<!-- disabled는 값이 안넘어가니까 readonly로 -->
		  <form:errors path="empNum" />
		</div>
		
		<!-- 직원명 -->
		<div class="col-md-6">
			<label for="empNm" class="form-label">직원명</label> 
			<input
				type="text" class="form-control" id="empNm" name="empNm"
				placeholder="직원명을 입력하세요" value="${employee.empNm }" readonly/>
			<form:errors path="empNm" />
		</div>
		
		<!-- 주소 -->
		<div class="col-md-4">
		<label for="zipCode" class="form-label">우편번호</label> 
			<input
				type="text" class="form-control" id="zipCode" name="zipCode"
				placeholder="우편번호를 검색하세요" readonly>
		</div>
		<div class="col-md-2">
			<label for="btnPostno" class="form-label" >　</label> 
			<button type="button" class="btn btn-primary" id="btnPostno" >검색</button>
		</div>
		
		<div class="col-12">
			<label for="address" class="form-label">주소</label> 
			<input
				type="text" class="form-control" id="address" name="address"
				placeholder="주소를 검색하세요" readonly>
		</div>
		
		<div class="col-12">
			<label for="detAddress" class="form-label" >상세주소</label> 
			<input
				type="text" class="form-control" id="detAddress" name="detAddress"
				placeholder="상세주소를 입력하세요" value="${employee.empAddr }" readonly>
		</div>
		<!-- 연락처 -->
		<div class="col-md-6">
			<label for="empPhe" class="form-label">연락처</label> 
			<input
				type="text" class="form-control" id="empPhe" name="empPhe"
				placeholder="핸드폰번호를 입력하세요" value="${employee.empPhe }" readonly>
			<form:errors path="empPhe" />
		</div>
		<!-- 급여 -->
		<div class="col-md-6">
			<label for="empPay" class="form-label">급여</label> 
			<input
				type="text" class="form-control" id="empPay" name="empPay"
				placeholder="급여를 입력하세요" value="${employee.empPay }" readonly>
			<form:errors path="empPay" />
		</div>
		<!-- 매니저 -->
		<div class="col-md-10">
			<label for="empNm" class="form-label">매니저명</label> 
			<div class="col-md-6" display:inline-block>
					<input
						type="text" class="form-control" id="empMjNum" name="empMjNum"
						value="${employee.empMjNum}" readonly />
					<input
						type="text" class="form-control" id="empMjNm" name="empMjNm"
						placeholder="매니저명을 입력하세요" value="${manager.empNm}" readonly />
				
			</div>
			<div class="col-md-2" display:inline-block>
				<button type="button" class="btn btn-primary " data-toggle="modal"
						data-target="#exampleModal" id="btnEmpMjNum" disabled>검색</button>
			</div>
		</div>
	
	
		<div class="col2">
			<!-- 일반모드 시작 -->
			<span class="spn1">
				<button type="button" id="edit" class="btn btn-primary edit">수정</button>
				<button type="button" id="delete" class="btn btn-danger delete">삭제</button>
				<a href="/emp/list" class="btn btn-info list">목록</a>
			</span>
			<!-- 수정모드 시작 -->
			<span class="spn2" style="display:none;">
				<button type="submit" id="ok" class="btn btn-success ok">확인</button>
				<button type="button" id="cancel" class="btn btn-warning cancel">취소</button>
			</span>
<!-- 			<button type="button" id="" class="btn btn-info list">목록</button> -->
		</div>	
		
	</form:form>
</div>



<div class="bd-example" id="manager" style="display:none;">
<c:if test="${manager==null }"><!-- 이 직원은 매니저가 없음 -->
	<div class="text-center">
		<div class="error mx-auto" data-text="관리자가 없습니다."
			style="font-size:20px;">관리자가 없습니다.</div>
	</div>
</c:if>
<c:if test="${manager!=null }">
	<form:form modelAttribute="empVO" action="/emp/createPost" method="post" class="row g-3">
		<!-- 직원번호 -->
		<div class="col-md-6" style="clear:both;">
		  <label for="empNum" class="form-label">관리자번호</label>
	<!-- 	  <input type="text" name="empNum" class="form-control" id="empNum" -->
	<%-- 	  	value="${empNum}" placeholder="직원번호를 입력해주세요" /> --%>
	<%-- 	  <form:input path="empNum" class="form-control" placeholder="직원번호를 입력해주세요" /> --%>
		  <input type="text" name="empNum" class="form-control" id="empNumAjax"
		  	 value="${manager.empNum }" placeholder="직원번호를 입력해주세요" readonly required/>
		  									<!-- disabled는 값이 안넘어가니까 readonly로 -->
		  <form:errors path="empNum" />
		</div>
		
		<!-- 직원명 -->
		<div class="col-md-6">
			<label for="empNm" class="form-label">직원명</label> 
			<input
				type="text" class="form-control" id="empNm" name="empNm"
				placeholder="직원명을 입력하세요" value="${manager.empNm }" readonly>
			<form:errors path="empNm" />
		</div>
		
		<!-- 주소 -->	
		<div class="col-md-4">
		<label for="zipCode" class="form-label">우편번호</label> 
			<input
				type="text" class="form-control" id="zipCode" name="zipCode"
				placeholder="우편번호를 검색하세요" readonly>
		</div>
		<div class="col-md-2">
			<label for="btnPostno" class="form-label" >　</label> 
			<button type="button" class="btn btn-primary" id="btnPostno">검색</button>
		</div>
		
		<div class="col-12">
			<label for="address" class="form-label">주소</label> 
			<input
				type="text" class="form-control" id="address" name="address"
				placeholder="주소를 검색하세요" readonly>
		</div>
		<div class="col-12">
			<label for="detAddress" class="form-label" >상세주소</label> 
			<input
				type="text" class="form-control" id="detAddress" name="detAddress"
				placeholder="상세주소를 입력하세요" value="${manager.empAddr }" readonly>
		</div>
		<!-- 연락처 -->
		<div class="col-md-6">
			<label for="empPhe" class="form-label">연락처</label> 
			<input
				type="text" class="form-control" id="empPhe" name="empPhe"
				placeholder="핸드폰번호를 입력하세요" value="${manager.empPhe }" readonly>
			<form:errors path="empPhe" />
		</div>
		<!-- 급여 -->
		<div class="col-md-6">
			<label for="empPay" class="form-label">급여</label> 
			<input
				type="text" class="form-control" id="empPay" name="empPay"
				placeholder="급여를 입력하세요" value="${manager.empPay }" readonly>
			<form:errors path="empPay" />
		</div>
		<!-- 매니저 -->
		<div class="col-md-6">
				<label for="empNm" class="form-label">매니저명</label> 
				<input type="text" class="form-control" id="empMjNum" 
						name="empMjNum" value="${manager.empMjNum}" readonly/>
				<input
					type="text" class="form-control" id="empMjNm" name="empMjNm"
					placeholder="매니저명을 입력하세요" readonly/>
		</div>
		<div class="col-md-2">
			<label for="btnPostno" class="form-label" >　</label> 
			<button type="button" class="btn btn-primary " data-toggle="modal"
					data-target="#exampleModal" id="btnEmpMjNum" disabled>검색</button>
		</div>
	
	
		<div class="col2">
			<!-- 일반모드 시작 -->
			<span class="spn1">
				<button type="button" id="edit" class="btn btn-primary edit">수정</button>
				<button type="button" id="delete" class="btn btn-danger delete">삭제</button>
				<a href="/emp/list" class="btn btn-info list">목록</a>
			</span>
			<!-- 수정모드 시작 -->
			<span class="spn2" style="display:none;">
				<button type="submit" id="ok" class="btn btn-success ok">확인</button>
				<button type="button" id="cancel" class="btn btn-warning cancel">취소</button>
			</span>
		</div>
	</form:form>	
</c:if>
</div>

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




















