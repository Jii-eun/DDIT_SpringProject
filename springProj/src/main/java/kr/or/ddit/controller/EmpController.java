package kr.or.ddit.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.service.EmpService;
import kr.or.ddit.util.ArticlePage;
import kr.or.ddit.vo.EmpVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/emp")
@Controller
public class EmpController {		
	
	@Autowired	// CusService를--주입--> CusController 
	EmpService empService;
	
	//요청URI : /emp/create
	//요청방식 : get
	@GetMapping("/create")		//vo를 쓰는순간 @ModelAttribute를 써줘라
	public String create(@ModelAttribute EmpVO empVO
			, Model model) {
//		empVO.setEmpNm("개똥이");
		
		//다음 직원번호를 가져옴
		//서비스의 메서드 호출
		String empNum = this.empService.getEmpNum();
		empVO.setEmpNum(empNum);
		//${empNum}
		model.addAttribute("empNum", empNum);
		
		//forwarding
		return "emp/create";
	}
	
	//신규 직원번호 가져오기
	//요청URI : /emp/getEmpNum
	//요청방법 : post
	@ResponseBody
	@PostMapping("/getEmpNum")
	public String getEmpNum() {
		String empNum = this.empService.getEmpNum();
		
		log.info("empNum : " + empNum);
		return empNum;                                         
	}
	
	//신규 지원 등록하기
	/*
	  →
	   @ModelAttribute EmpVO empVO
	 	@ModelAttribute("empVO") aaa => 같은 기능
		
		요청URI : /emp/createPost
	 	요청파라미터 : {empNum=EMP001, zipCode=12345, address=대전..., empMjNum= 
	 				zipcode=12345, detAddress=주소상세}
	 	요청방식 : post
	 */
	@PostMapping("/createPost")
	public String createPost(@Valid @ModelAttribute EmpVO empVO
			, String zipCode, String address, String detAddress	//주소합치기위해서
			, @RequestParam(required = false, defaultValue = "new") String mode
			, Errors errors) {
		log.info("empVO : " +empVO);
		log.info("zipcode : " +zipCode + ", address : " + address + 
				", detAddress : " + detAddress + " /vo getter 체크 " + empVO.getEmpNum());
		
		//mode
		// - create에서 오면 new
		// - detail에서 오면 update
		log.info("mode : " + mode);
		//update를 실행한다면..
		String oldEmpNum = "";
		if(mode.equals("update")) {
			oldEmpNum = empVO.getEmpNum();	//기본키 데이터 백업
				//createPost 메서드 수행하는 동안 값 바뀔수도 있어서 백업
		}
		
		if(errors.hasErrors()) {
			//empVO에서 걸어놓은 조건을 만족하지 못하는 데이터 == 에러 ==> if문에 걸러진다.
			return "emp/create";
		}
		
		//우편번호 + 주소 + 상세주소 => empVO의 empAddr 멤버변수에 setting하기
		String empAddr = zipCode + " " + address + " " + detAddress;
		empVO.setEmpAddr(empAddr);
		
		int result = this.empService.createPost(empVO);
		//가기 전    > empVO : {empNum=EMP001, zipCode=12345, address=대전.., empMjNum=??}
		//다녀온 후 > empVO : {empNum=EMP003, zipCode=12345, address=대전.., empMjNum=??}
		
		//forwarding (원래 주소(반환값) : "emp/detail")
		// ==> redirect => 상세페이지로
		//update
		if(mode.equals("update")) {
			return "redirect:/emp/detail?empNum=" + oldEmpNum;
		} else {
			//insert
			return "redirect:/emp/detail?empNum=" + empVO.getEmpNum();
		}
	}
	
	/*
	 * 	모든 직원 정보 가져오기
		요청URI : /emp/getEmpAll
		요청방식 : post
		dataType : json
	 */
	@ResponseBody //json으로 return해줘야해서 씀
	@PostMapping("/getEmpAll")
	public List<EmpVO> getEmpAll(){
		//모든 직원 정보 가져오기
		List<EmpVO> empVOList = this.empService.getEmpAll();
		
		log.info("empVOList : " + empVOList);
		
		return empVOList;
	}
	
	//직원 상세보기
	/*
	 요청URI : /emp/detail?empNum=EMP007
	 요청파라미터 : empNum=EMP007
	 요청방식 : get
	 
	@RequestParam("empNum") String empNum
	*/
	@GetMapping("/detail")				//@ModelAttribute는 jsp에서 form:form을 쓸 때 맞춰서 써준다.
	public String detail(String empNum, @ModelAttribute EmpVO empVO
			, Model model) {
		log.info("empNum :" + empNum);
		log.info("empVO :" + empVO);
		
		//상세화면은 등록화면과 동일 + empVO데이터를 채우면 됨
		List<EmpVO> empVOList = this.empService.detail(empVO);
		log.info("empVOList : " + empVOList);
		
		model.addAttribute("data", empVOList);
		
		//emp폴더의 detail.jsp를 forwarding
		return "emp/detail";
	}
	
	/*
	  	요청URI : /emp/deletePost
	  	요청파라미터 : let data = {"empNum":empNum};
	  	요청방식 : post
	  	응답데이터타입 : json
	  	응답데이터 : {"result" : "1"}
	 */
	@ResponseBody		//==>데이터를 json형태로 받아야 할때
	@PostMapping("/deletePost")			//↓ json데이터를 받을때는 여기에 @RequestBody
	public Map<String, String> deletePost(@RequestBody EmpVO empVO) {
		//empVO : {empNum=EMP001, zipCode=null, address=null, ..., empMjNum=null}
		log.info("empVO : " + empVO);
		
		int result = this.empService.deletePost(empVO);
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", result+"");	//result가 int라서 ""를 더해준 뒤 String으로 바꿈 
		log.info("map : " + map);
				
		return map;
		
	}
	
	/*	
 		요청URI : /emp/list
 		요청방식 : get
 		요청파라미터 : show=10&keyword=개똥이
 			  ==> show=10&keyword=개똥이&currentPage=1
 		
 		 map은 RequestParam
 		 vo는 ModelAttribute
 		 json은 RequestBody
 		 
 		 ?show=10&keyword=김철수 처럼 currentPage가 없을 수 있음 -> 처리 필요 -> (required=false)
 		 
 		 /emp/list ==> 이 주소로 넘어오면  show가 없으니까... => 보정처리 ◆◆◆◆◆
 		
	 */
	@GetMapping("/list")
	public String list(Model model
//			, @RequestParam(value="keyword", required = false) String Keyword
			//예전엔 하나씩 받아왔는데..이번엔 Map으로
			, @RequestParam Map<String, String> map	 //검색조건과 keyword가 들어옴
			, @RequestParam(value="currentPage",required = false, defaultValue = "1") int currentPage	
												// ↑데이터타입을 int로 넣어주면 string타입이 자동으로 변환된다.
												//스트링이라 @requestparam을 생략 가능하긴하지만 써주는게 맞음
			, @RequestParam(value="show", required = false, defaultValue = "10") int size
														){
		//넘어온 파라미터 확인
		log.info("map : " + map);
		log.info("currentPage : " + currentPage);
		log.info("size : " + size);
		//map은 jsp에서 넘어오는 변수를 갖고있음. 거기에 추가로 필요한 값을 setting
		//map{show=10, keyword=개똥이, currentPage=1}
		map.put("currentPage", currentPage+"");
		
		//	◆◆◆◆◆ show 보정처리
		//1) /emp/list : show가 null	 <========== if문 전자 처리
		//2) /emp/list?show= : show의 값이 없음 <========== if문 후자 처리
		if(map.get("show") ==null || map.get("show").length() <1) {
			map.put("show", "10");
			//parameter 처리를 할 때 기본값이 주어진 것은 size인데
			//여기서는 size를 넘긴게 아니라 show를 map에 담아 넘겨야하기때문에 null값일대와 ""일때의 처리를 다 직접 해줘야한다.
			//만약 size 그대로 넘겼다면 따로 처리를 안해도 기본값 10으로 들어간다.
		}
		
		//map으로 파라미터를 받아서 매퍼 xml에서 검색 조건으로 사용
		List<EmpVO> empVOList = this.empService.list(map);	//=content
		log.info("empVOList :" + empVOList );
		
		//total
		int total = this.empService.getTotal(map);
		log.info("total : " + total);
		
		//size <- map.get("show")
//		int size = Integer.parseInt(map.get("show")); //==> parameter로 받아옴			
		
		//empVOList 객체를 페이징 처리해보자
		//new ArticlePage<EmpVO>(total, currentPage, size, content)
		ArticlePage<EmpVO> article = new ArticlePage<EmpVO>(total, currentPage, size, empVOList);
		model.addAttribute("data", article);
		
		//forwarding
		return "emp/list";
	}
	
	/**
	 *  직원 1명의 정보를 리턴
	 	요청URI : /emp/detailOne
	 	요청파라미터 : let data = {"empMjNum":empMjNum}	//띄어쓰기 하면 안된다고함
	 	요청방식 : post
	 	응답데이터타입 : json
	 */
	@ResponseBody	//json으로 응답하기 위해서 ResponseBody를 쓴다.
	@PostMapping("/detailOne")
	public EmpVO detailOne(@RequestBody EmpVO empVO) {
		log.info("empVO : " + empVO);
		
		empVO = this.empService.detailOne(empVO);
		
		return empVO;
	}
	
	
}















































