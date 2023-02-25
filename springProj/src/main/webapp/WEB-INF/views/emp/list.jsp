<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/resources/css/bootstrap.min.css" />
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-3.6.0.js"></script>

<!-- data, param, empVO로 받는거  -->

<%-- ${data } --%>
<div class="card shadow mb-4">
	<div class="card-header py-3">
		<h6 class="m-0 font-weight-bold text-primary">직원 목록</h6>
	</div>
	<div class="card-body">
		<div class="table-responsive">
			<div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap4">
				<!-- 검색영역 시작 (action을 생략하면 현재의 URI, method를 생략하면 기본이 get-->
				<form name="frm" id="frm" action="/emp/list" method="get">
					<div class="row">
						<div class="col-sm-12 col-md-6">
							<div class="dataTables_length" id="dataTable_length">
								<label>Show 
									<select name="dataTable_length"
										aria-controls="dataTable"
										name="show" id="show"
										class="custom-select custom-select-sm form-control form-control-sm">
										<option value="10" <c:if test="${param.show==10 }">selected</c:if> >10</option>
										<option value="25" <c:if test="${param.show==25 }">selected</c:if> >25</option>
										<option value="50" <c:if test="${param.show==50 }">selected</c:if> >50</option>
										<option value="100" <c:if test="${param.show==100 }">selected</c:if> >100</option>
									</select> entries
								</label>
							</div>
						</div>
						<div class="col-sm-12 col-md-6">
							<div id="dataTable_filter" class="dataTables_filter">
								<label>Search:<input type="search" name="keyword"
									class="form-control form-control-sm" placeholder="검색어를 입력해주세요"
									aria-controls="dataTable" value="${param.keyword }"> <!-- 검색어 남음 -->
								</label> <label>
									<button type="submit"
										class="btn btn-primary btn-icon-split btn-sm">
										<span class="icon text-white-50"> <i
											class="fas fa-flag"></i>
										</span> <span class="text">검색</span>
									</button>
								</label>
							</div>
						</div>
					</div>
				</form>
				<!-- 검색영역 끝 -->
				<div class="row">
					<div class="col-sm-12">
						<table class="table table-bordered dataTable" id="dataTable"
							width="100%" cellspacing="0" role="grid"
							aria-describedby="dataTable_info" style="width: 100%;">
							<thead>
								<tr role="row">
									<th class="sorting" tabindex="0" aria-controls="dataTable"
										rowspan="1" colspan="1"
										aria-label="Name: activate to sort column ascending"
										style="width: 57px;">번호</th>
									<th class="sorting" tabindex="0" aria-controls="dataTable"
										rowspan="1" colspan="1"
										aria-label="Position: activate to sort column ascending"
										style="width: 61px;">직원번호</th>
									<th class="sorting" tabindex="0" aria-controls="dataTable"
										rowspan="1" colspan="1"
										aria-label="Office: activate to sort column ascending"
										style="width: 49px;">직원명</th>
									<th class="sorting" tabindex="0" aria-controls="dataTable"
										rowspan="1" colspan="1"
										aria-label="Age: activate to sort column ascending"
										style="width: 31px;">급여</th>
									<th class="sorting sorting_desc" tabindex="0"
										aria-controls="dataTable" rowspan="1" colspan="1"
										aria-label="Start date: activate to sort column ascending"
										style="width: 68px;" aria-sort="descending">매니저</th>
								</tr>
							</thead>

							
							<tbody>
								<!-- data : List<EmpVO> empVOList 
									 empVO : EmpVO
								stat.index : 0부터 시작
								stat.count : 1부터 시작
								
								↓ 페이징 처리 후 
								data=> data.content로 바꿔줌
								data : ArticlePage
								data.content : ArticlePage의 content 멤버변수(List<EmpVO>)
							
								-->
								<c:forEach var="empVO" items="${data.content}" varStatus="stat">
									<tr class="
<%-- 										<c:if test='${stat.count % 2 == 0}'>even</c:if> --%>
										<c:if test='${empVO.rnum % 2==0 } % 2 == 0}'>even</c:if>
										<c:if test='${empVO.rnum % 2 != 0}'>odd</c:if>
									">
<%-- 										<td class="sorting_1">${stat.count }</td> --%>
										<td class="sorting_1">${empVO.rnum }</td>
										<td class="">
											<a href="/emp/detail?empNum=${empVO.empNum}">${empVO.empNum }</a>
										</td>
										<td class="">${empVO.empNm }</td>
										<td class="">${empVO.empPay }</td>
										<td class="detailOne" data-emp-mjnum="${empVO.empMjNum}" data-bs-toggle="modal" data-bs-target="#exampleModal">
											${empVO.empMjNm }
											
<%-- 											<a id="detailOne" data-bs-toggle="modal" href="#exampleModal">${empVO.empMjNm }</a>	 --%>
											<!-- bootstrap 주소넣기 
												 td에 넣어도 모달창 뜨고, a태그에 넣어도 모달창 뜸
											-->
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				
				<div class="row">
				<!-- 페이지 개수 처리 (entries) -->
					<div class="col-sm-12 col-md-5">
	                  <div class="dataTables_info" id="dataTable_info" role="status"
	                     aria-live="polite">
	                     <c:if test="${param.show==null}">
	                        <c:set var="show" value="1" />
	                     </c:if>
	                     <c:if test="${param.show!=null}">
	                        <c:set var="show" value="${param.show}" />
	                     </c:if>
	                     <!-- scope(공유영역) : page(기본), request, session, application -->
	                     <!-- 종료행 : currentPage * show -->
	                     <c:set var="endRow" value="${data.currentPage * show}" />                     
	                     <!-- 시작행 : 종료행 - (size-1) -->
	                     <c:set var="startRow" value="${endRow - (show-1)}" />
	                     <!-- 전체행수 : total -->
	                     <c:if test="${endRow > data.total}">
	                              <c:set var="endRow" value="${data.total}"/>
	                           </c:if>
	                     Showing ${startRow} to ${endRow} of ${data.total} entries
	                  </div>
	               </div>
<!-- 					<div class="col-sm-12 col-md-5"> -->
<!-- 						<div class="dataTables_info" id="dataTable_info" role="status" -->
<!-- 							aria-live="polite">Showing 1 to 10 of 57 entries</div> -->
<!-- 					</div> -->
					<div class="col-sm-12 col-md-7">
						<div class="dataTables_paginate paging_simple_numbers"
							id="dataTable_paginate">
							<ul class="pagination">
								<li class="paginate_button page-item previous 
									<c:if test='${data.startPage lt 6}'>disabled</c:if>"
									id="dataTable_previous">
									<a href="/emp/list?currentPage=${data.startPage-5}&keyword=${param.keyword}&show=${param.show}"
									aria-controls="dataTable" data-dt-idx="0" tabindex="0"
									class="page-link">Previous</a>
								</li>
								
								<c:forEach var="pNo" begin="${data.startPage }" end="${data.endPage }">	
								<li class="paginate_button page-item 
									<c:if test='${data.currentPage == pNo }'>active</c:if>
								">
									<a href="/emp/list?currentPage=${pNo}&keyword=${param.keyword}&show=${param.show}"
										aria-controls="dataTable" data-dt-idx="1" tabindex="0"
										class="page-link">${pNo}</a>
								</li>
								</c:forEach>
								
								<li class="paginate_button page-item next 
									<c:if test='${data.endPage ge data.totalPages }'>
									disabled
									</c:if> " 
									id="dataTable_next">
									<a href="/emp/list?currentPage=${data.startPage+5}&keyword=${param.keyword}&show=${param.show}" aria-controls="dataTable" data-dt-idx="7" tabindex="0"
									class="page-link">Next</a>
								</li>
								
							</ul>
						</div>
					</div>
				</div>
			
			<!-- 쌤 -->
<!-- 			<div class="col-sm-12 col-md-7"> -->
<!-- 				<div class="dataTables_paginate paging_simple_numbers" -->
<!-- 					id="dataTable_paginate"> -->
<!-- 					<ul class="pagination"> -->
<%-- 						<li class="paginate_button page-item previous  --%>
<%-- 							<c:if test='${data.startPage lt 6}'>disabled</c:if> --%>
<%-- 						" id="dataTable_previous"> --%>
<%-- 							<a href="/emp/list?currentPage=${data.startPage-5}&show=${param.show}&keyword=${param.keyword}" --%>
<!-- 							aria-controls="dataTable" data-dt-idx="0" tabindex="0" -->
<!-- 							class="page-link">Previous</a></li> -->
<%-- 						<c:forEach var="pNo" begin="${data.startPage}" end="${data.endPage}"> --%>
<%-- 						<li class="paginate_button page-item --%>
<%-- 								<c:if test='${data.currentPage==pNo}'>active</c:if> --%>
<%-- 								"> --%>
<%-- 						<a href="/emp/list?currentPage=${pNo}&show=${param.show}&keyword=${param.keyword}" --%>
<!-- 							aria-controls="dataTable" data-dt-idx="1" tabindex="0" -->
<%-- 							class="page-link">${pNo}</a></li> --%>
<%-- 						</c:forEach> --%>
<%-- 						<li class="paginate_button page-item next --%>
<%-- 							<c:if test='${data.endPage ge data.totalPages}'>disabled</c:if> --%>
<%-- 						" id="dataTable_next"><a --%>
<%-- 							href="/emp/list?currentPage=${data.startPage+5}&show=${param.show}&keyword=${param.keyword}" aria-controls="dataTable" data-dt-idx="7" tabindex="0" --%>
<!-- 							class="page-link">Next</a></li> -->
<!-- 					</ul> -->
<!-- 				</div> -->
<!-- 			</div> -->


			</div>
		</div>
	</div>
</div>


<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">Modal title</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <!-- 내용 시작 -->
        <div class="mb-3 row">
          <label for="staticEmail" class="col-sm-3 col-form-label">직원번호</label>
          <div class="col-sm-9">
            <input type="text" readonly class="form-control-plaintext" id="empNum" 
            		value="">
          </div>
        </div>
        <div class="mb-3 row">
          <label for="staticEmail" class="col-sm-3 col-form-label">직원명</label>
          <div class="col-sm-9">
            <input type="text" readonly class="form-control-plaintext" id="empNm" 
            		value="">
          </div>
        </div>
        <div class="mb-3 row">
          <label for="staticEmail" class="col-sm-3 col-form-label">연락처</label>
          <div class="col-sm-9">
            <input type="text" readonly class="form-control-plaintext" id="empPhe" 
            		value="">
          </div>
        </div>
        <div class="mb-3 row">
          <label for="staticEmail" class="col-sm-3 col-form-label">주소</label>
          <div class="col-sm-9">
            <input type="text" readonly class="form-control-plaintext" id="empAddr" 
            		value="">
          </div>
        </div>
        <div class="mb-3 row">
          <label for="staticEmail" class="col-sm-3 col-form-label">매니저</label>
          <div class="col-sm-9">
            <input type="text" readonly class="form-control-plaintext" id="empMjNm" 
            		value="">
          </div>
        </div>
        <!-- 내용 끝 -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
        <button type="button" class="btn btn-primary">저장</button>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
	$(function(){
		//$("#detailOne").on("click", function(){
		$(document).on("click", ".detailOne", function(){		//detailOne는 여러개니까 class
			console.log("ajax로 매니저 정보 가져와보자");
			console.log("요청URI : /emp/detailOne");
			console.log("요청파라미터(json) : {'empNum' : 'EMP006'}");
			console.log("요청방식 : post");
			
			//이름 이용하려고 했는데 이름을 못 가져온다
			//this : class="detailOne"인 것 여러개 중에 클릭한 바로 그 요소
// 			let empMjNm = document.getElementById("#detailOne").innerText;	
							//아이디를 클래스로 바꿔줘얗마..아니 선택한 td행이 뭔지도 정확히 알아야함 그래서 안됐나봐
			let empMjNm = $(this).innerHTML;
			console.log("empMjNm : " + empMjNm);
// 			let data =  {"empMjNm":empMjNm};

			//번호 이용
			//this : class="detailOne"인 것 여러개 중에 클릭한 바로 그 요소
			let empMjNum = $(this).data("emp-mjnum");	//data-xxx 이 형태는 대소문자 구분을 못하나봐
														//data-empMjNum X data-emp-mjnum OK ('-' 들어가는건 됨)
			console.log("empMjNum : ", empMjNum);
			//json object
			let data =  {"empMjNum":empMjNum};
			
			//아작나써유 피씨다타써
			$.ajax({
				url : "/emp/detailOne",
				contentType : "application/json;charset:utf-8",
				data : JSON.stringify(data),	//마샬링 작업, 일렬화(serialization)
				dataType : "json",
				type:"post",
				beforeSend : function(xhr) {   // 데이터 전송 전  헤더에 csrf값 설정
	                xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
				},
				success:function(res){ //res == object 로 들어옴
					//res : {"empNum":"EMP004","empAddr":"02830 서울 성북구 아리랑로 3 기업은행",
					//		 "empPhe":"010-1597-1597","empNm":"철수","empPay":30000000,
					//		 "empMjNum":null,"empMjNm":null,"serVOList":null}
					console.log("res : " + JSON.stringify(res));	//데이터를 가져와서 찍히는건 여기
					
					$("#empNum").val(res.empNum);
					$("#empNm" ).val(res.empNm);
					$("#empPhe").val(res.empPhe);
					$("#empAddr").val(res.empAddr);
					$("#empPay" ).val(res.empPay);
					$("#empMjNm	").val(res.empMjNm);
				}
			});
		});
		/*
		json 
		{
		o1 : {}
		key : [ {key:value}, {}, ]
		}
		*/
		
		//show가 바뀜
		$("#show").on("change", function(){
			//currentPage=1&keyword=개똥이&show=10
			let currentPage = "${param.currentPage}";
			let keyword = "${param.keyword}";
			
			console.log("currentPage : " + currentPage + ", keyword : " + keyword);
			
			let show = $(this).val();
			let show2 = $("#show option:selected").val();
			let show3 = $("select[name='show']").val();
			let show4 = $("show option").index($("#show option:selected"));
			
			console.log("show : " + show)
			
			location.href="/emp/list?show="+show+"&currentPage=1"	//
							+"&keyword="+keyword;
		});
	});
</script>
































