package kr.or.ddit.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.service.CusService;
import kr.or.ddit.vo.CusVO;
import lombok.extern.slf4j.Slf4j;

//스프링에 이 클래스가 컨트롤러인 것을 알려줌. + 스프링은 이 클래스를 자바빈으로 등록해서 관리해준다
//속성이 1개일 때 생략 가능	==> 15번째 줄 GetMapping()괄호 안에 "value =" 생략함
@Slf4j
@RequestMapping(value="/cus")
@Controller
public class CusController {
	
	@Autowired
	CusService cusService;
	
	//void로 응답
	//요청URI : /cus/create
	@GetMapping("/create")
	public void create() {
		log.info("create()에 왔다");
		//forwarding
//		return "cus/create";
	}
	
	//String으로 응답*******
	/*
	 	요청URI : /cus/createPost
	 	요청파라미터 : {cusNum=12345, cusNm=개똥이, postno=33233...}
	 	요청방식 : post
	 	
	 	validate => VO타입으로 해야함(map으로 안됨)
	 */
	//String, int, Map.. => 골뱅이 RequestParam
	//VO => ModelAttribute
	//골뱅이 Valid는 CusVO의 validation 체크를 해주는 어노테이션
	// 													====> validation 이란......?
	//문제발생 시 Erros erros객체에 오류 정보를 담아서 꼭!!★★★★ forwarding★★★★해주면 됨. redirect는 소용없음
	@PostMapping("/createPost")
	public String createPost(@Valid @ModelAttribute CusVO cusVO,
			Errors errors) {
		
		//errors.hasErrors() : 문제 발생 시 true
		if(errors.hasErrors()) {
			return "cus/create";
		}
		
		//고객번호는 CUS001, CUS002...
		//자동으로 다음 고객번호를 생성시켜보자
		String cusNum = cusService.getNextCusNum();
		
		cusVO.setCusNum(cusNum);
		
		//입력 성공 : 상세보기로 redirect						//vo는 타입을 정해놨으니까 사용 가능
		return "redirect:/cus/detail?cusNum=" + cusVO.getCusNm();
	}
	
	
}



































