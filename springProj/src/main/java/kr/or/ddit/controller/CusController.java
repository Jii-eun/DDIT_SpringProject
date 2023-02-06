package kr.or.ddit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.service.CusService;
import kr.or.ddit.vo.CusVO;
import lombok.extern.slf4j.Slf4j;

//스프링에 이 클래스가 컨트롤러인 것을 알려줌. + 스프링은 이 클래스를 자바빈으로 등록해서 관리해준다
//속성이 1개일 때 생략 가능	==> 15번째 줄 GetMapping()괄호 안에 "value =" 생략함
@Slf4j
@RequestMapping(value="/cus")
@Controller
public class CusController {		
	
	@Autowired	// CusService를--주입--> CusController 
	CusService cusService;
	
	//void로 응답
	//요청URI : /cus/create
	@GetMapping("/create")		//create.jsp에 있는 form태그의 modelAttribute="cusVO"에 적힌 cusVO와 연결(form태그 때문에)
	public void create(CusVO cusVO, Model model) {
		log.info("create()에 왔다");
		
		//기본키 데이터 생성
		String cusNum = this.cusService.getNextCusNum();
		//cusVO{cusNum=CUS002,cusNm=null,cusAddr=null, ...}
		cusVO.setCusNum(cusNum);
		cusVO.setCusNm("개똥이");
		
		//취미 미리 등록 ==> controller에서 셋팅한 것만 jsp에서 미리 선택된걸로 만듦
		List<String> hobbyList = new ArrayList<String>();
//		hobbyList.add("Music");
		hobbyList.add("Movie");
//		hobbyList.add("Sports");
		cusVO.setHobbyList(hobbyList);
		
		//성별 등록	(남성 : male, 여성  : female, 기타 : others)
		cusVO.setGender("female");	//만약 여러개를 셋팅하면 마지막에 셋팅한 값을 넘어간다.
		
		//국적(한 개 선택) => select박스
		Map<String, String> nationalityMap = new HashMap<String, String>();
		//map은 interface?		=========> hashMap을 이용해서 class로 구현 ????
		nationalityMap.put("Korea", "Korea");
		nationalityMap.put("English", "English");
		nationalityMap.put("Germany", "Germany");

		//vo에 담을 수 없어서 model을 이용
		model.addAttribute("nationalityMap", nationalityMap);
		//생성된 고객번호를 model에 넣음
		model.addAttribute("cusNum", cusNum);
		
		//forwarding
//		return "cus/create";
	}
	
	//String으로 응답*******
	/*
	 	요청URI : /cus/createPost
	 	요청파라미터 : {cusNum=12345, cusNm=개똥이, postno=33233, cusAddr=주소
	 			,addrDet=상세주소, cusPhe=010-123-1223}
	 	요청방식 : post
	 	
	 	validate => VO타입으로 해야함(map으로 안됨)
	 */
	//String, int, Map.. => 골뱅이 RequestParam
	//VO => ModelAttribute
	//골뱅이 Valid는 CusVO의 validation 체크를 해주는 어노테이션
	// 													====> validation 이란......?
	//문제발생 시 Erros erros객체에 오류 정보를 담아서 꼭!!  	★★★★ forwarding★★★★해주면 됨. redirect는 소용없음
	@PostMapping("/createPost")
	public String createPost(@Valid @ModelAttribute CusVO cusVO
			, String cusNum, String cusNm, String postno, String cusAddr
			, String addrDet	//요청파라미터를 매개변수로 받을 수 있다.
			, @RequestParam("cusPhe") String phone	//매개변수명을 바꿔서 받을 수도 있다.	
			, @DateTimeFormat(pattern = "yyyy-MM-dd") Date cusBir	//문자로 받아온게 날짜타입으로 변환돼서 들어온다.
			, String gender, String nationality
			, Errors errors) {
		
		//요청파라미터를 VO형태 외에 스트링 형태로도 받을 수 있다.
		log.info("cusVO : " + cusVO);
		log.info("cusNum : " + cusNum + ", cusNm : " + cusNm + ", postno : " + postno
				+ ", addrDet : " + addrDet + ", phone : " + phone + ", cusBir : " + cusBir);
		
		//List<String>
		String hobby = "";
		List<String> hobbyList = cusVO.getHobbyList();
		for(String str : hobbyList) {
			hobby += str + ",";
		}
		
		cusVO.setHobby(hobby);
		
		//errors.hasErrors() : 문제 발생 시 true
		if(errors.hasErrors()) {
			return "cus/create";
		}
		
		//고객번호는 CUS001, CUS002...
		//자동으로 다음 고객번호를 생성시켜보자
//		cusNum = cusService.getNextCusNum();
//		cusVO.setCusNum(cusNum);
//		log.info("cusNum : " + cusNum);
		//==> CUS 테이블에 insert
		int result = this.cusService.createPost(cusVO);
		log.info("result : " + result);
		
		
		//입력 성공 : 상세보기로 redirect => 기본 데이터를 파라미터로 보냄
													//vo는 타입을 정해놨으니까 사용 가능
		return "redirect:/cus/detail?cusNum=" + cusVO.getCusNum();
	}
	
	/* 컨트롤러 메서드 매개변수
	 - Model
	 - RedirectAttributes
	 - 자바빈즈 클래스
	 - MultipartFile
	 - BindingResult
	 - java.util.Locale
	 - java.security.Principal
	 */
	//요청URI : /cus/detail?cusNum=CUS004
	@GetMapping("/detail")		
	public String detail(Model model, RedirectAttributes ras,
			@ModelAttribute CusVO cusVO ) {	
			//ModelAttribute detail.jsp에 있는 form 태그 에서 modelAttribute를 사용하기 때문에 넣어줌 가독성을 위해? 생략가능하다고도 하긴 했음 쓰면 좋다.
			//CusVO = 자바빈즈
		
		//상세보기
		cusVO = this.cusService.detail(cusVO);
		
		//국적(한 개 선택) => select박스
		Map<String, String> nationalityMap = new HashMap<String, String>();
		//map은 interface?		=========> hashMap을 이용해서 class로 구현 ????
		nationalityMap.put("Korea", "Korea");
		nationalityMap.put("English", "English");
		nationalityMap.put("Germany", "Germany");
		
		model.addAttribute("nationalityMap", nationalityMap); 
		//=> detail.jsp에 값이 전해짐 <form:select path="nationality" items="${nationalityMap }" />
		
		//forwarding
		return "cus/detail";
	}
}



































