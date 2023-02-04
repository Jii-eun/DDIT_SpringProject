<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Page Heading -->
<h1 class="h3 mb-2 text-gray-800">Tables</h1>
<p class="mb-4">
	DataTables is a third party plugin that is used to generate the demo
	table below. For more information about DataTables, please visit the <a
		target="_blank" href="https://datatables.net">official DataTables
		documentation</a>.
</p>

<!-- DataTales Example -->
<div class="card shadow mb-4">
	<div class="card-header py-3">
		<h6 class="m-0 font-weight-bold text-primary">DataTables Example</h6>
	</div>
	<div class="card-body">
		<div class="table-responsive">
			<div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap4">
				<div class="row" style="width:100%;">
					<!-- action="lprod/list" method="get" 이건 생략 가능-->
					<!-- 요청URI : /lprod/list?keyword=개똥이&currentPage=2$size=10 -->
					<form action="/lprod/list" name="frm" id="frm" method="get"
						style="width:100%;">
						
						<div class="col-sm-12 col-md-3" style="float:left;">
							<div class="dataTables_length" id="dataTable_length">
								<label>Show 
									<select name="size"
											aria-controls="dataTable"
											class="custom-select custom-select-sm form-control form-control-sm">
										<option value="10" <c:if test="${param.size==10 }">selected</c:if>>10</option>
										<option value="25" <c:if test="${param.size==25 }">selected</c:if>>25</option>
										<option value="50" <c:if test="${param.size==50 }">selected</c:if>>50</option>
										<option value="100" <c:if test="${param.size==100 }">selected</c:if>>100</option>
									</select> entries
								</label>
							</div>
						</div>
						<div class="col-sm-12 col-md-4" style="float:left;">
							<label> Current Page :
								<input class="form-control form-control-sm" type="text" name="currentPage" value="${param.currentPage }" />
							</label>
						</div>
						<div class="col-sm-12 col-md-5" style="float:right;">
							<div id="dataTable_filter" class="dataTables_filter">
								
								<label>Search:
									<input type="search"
											class="form-control form-control-sm" 
											placeholder="검색어를 입력해주세요"
											aria-controls="dataTable" 
											name ="keyword"
											value="${param.keyword}"
											placeholder="검색어를 입력해주세요"/>
								</label>			
								<button type="submit" class="btn btn-primary">검색</button>
							</div>
						</div>
					</form>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<table class="table table-bordered dataTable" id="dataTable"
							width="100%" cellspacing="0" role="grid"
							aria-describedby="dataTable_info" style="width: 100%;">
							<thead>
								<tr role="row">
									<th class="sorting sorting_asc" tabindex="0"
										aria-controls="dataTable" rowspan="1" colspan="1"
										aria-sort="ascending"
										aria-label="순번"
										style="width: 86.3906px;">순번</th>
									<th class="sorting" tabindex="0" aria-controls="dataTable"
										rowspan="1" colspan="1"
										aria-label="상품분류 명"
										style="width: 129.422px;">상품분류 아이디</th>
									<th class="sorting" tabindex="0" aria-controls="dataTable"
										rowspan="1" colspan="1"
										aria-label="상품분류 구분"
										style="width: 65.9688px;">상품분류 구분</th>
									<th class="sorting" tabindex="0" aria-controls="dataTable"
										rowspan="1" colspan="1"
										aria-label="상품분류 아이디"
										style="width: 30.1719px;">상품분류 명</th>
								</tr>
							</thead>
							<!--                     <tfoot> -->
							<!--                         <tr><th rowspan="1" colspan="1">Name</th><th rowspan="1" colspan="1">Position</th><th rowspan="1" colspan="1">Office</th><th rowspan="1" colspan="1">Age</th><th rowspan="1" colspan="1">Start date</th><th rowspan="1" colspan="1">Salary</th></tr> -->
							<!--                     </tfoot> -->
							<tbody>
							<!-- 
								1) 페이징 이전
								List<LprodVO> lprodVOList
								mav.addObject("data", lprodVOList);
								
								2) 페이징 이후
								mav.addObject("data", new ArticlePage<LprodVO>(total, currentPage, size, lprodVOList));
								
								ArticlePage에서 List<LprodVO> lprodVOList를 끄집어 내야함 => 멤버변수 content를 사용
								data는 ArticalPage이고
								data.content는 ArticlePage의 content멤버변수
								
								viv로 기억 (포이치 비브)
								
								stat.count : 1부터 시작
								stat.index : 0부터 시작
							 -->
							 									<!-- data => 페이징처리 => data.content -->
								 <c:forEach var="lprodVO" items="${data.content }" varStatus="stat">
									<tr 
										<c:if test="${stat.count%2 != 0}">
											class="odd"
										</c:if>	
										<c:if test="${stat.count%2 == 0}">
											class="even"
										</c:if>	
									>
										<td class="sorting_1">${lprodVO.rnum }</td>
										<td><a href="/lprod/detail?lprodGu=${lprodVO.lprodGu}">${lprodVO.lprodNm}</a></td>
										<td>${lprodVO.lprodGu}</td>
										<td>${lprodVO.lprodId}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 col-md-5">
						<div class="dataTables_info" id="dataTable_info" role="status"
							aria-live="polite">Showing 1 to 10 of 57 entries</div>
					</div>
					<!-- EL태그 정리
						 == : eq(equal)
						 != : ne(not equal)
						 <  : lt(less than)
						 >  : gt(greater than)
						 <= : le(less equal)
						 >= : ge(greater equal)
					 -->
					
					<div class="col-sm-12 col-md-7">
						<div class="dataTables_paginate paging_simple_numbers"
							id="dataTable_paginate">
							<ul class="pagination">
								<li class="paginate_button page-item previous 
											<c:if test='${data.startPage lt 6}'>disabled</c:if>" 
									id="dataTable_previous">
									<a href="/lprod/list?currentPage=${data.startPage-5}&keyword=${param.keyword}&size=${param.size}"
										aria-controls="dataTable" data-dt-idx="0" tabindex="0"
										class="page-link">Previous</a>
								</li>
								
								<!--  -->
								<c:forEach var="pNo" begin="${data.startPage}" end="${data.endPage}">
									<li class="paginate_button page-item
										<c:if test='${param.currentPage == pNo }'>active</c:if>
									">
										<a href="/lprod/list?currentPage=${pNo }&keyword=${param.keyword}&size=${param.size}"
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
									<a	href="/lprod/list?currentPage=${data.startPage+5}&keyword=${param.keyword}&size=${param.size}" aria-controls="dataTable" 
										data-dt-idx="7" tabindex="0"
										class="page-link">Next</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div>
					<!-- 
						  요청URI	: /lprod/create
						  방식 	: GET
						  
						  * 모든 요청은 controller에서 처리된다.
						    view ===> controller
					 -->
					<a href="/lprod/create" class="btn btn-primary btn-icon-split">
	                    <span class="icon text-white-50">
	                        <i class="fas fa-flag"></i>
	                    </span>
	                    <span class="text">상품분류 추가</span>
	                </a>
              	</div>
			</div>
		</div>
	</div>
</div>














