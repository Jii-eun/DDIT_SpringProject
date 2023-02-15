package kr.or.ddit.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.service.LprodService;

@RequestMapping(value="/chart")
@Controller
public class ChartController {
	//DI
	@Autowired
	LprodService lprodService;
	
	//요청URI : /chart/chartMain 
	//요청방식   : get
	@GetMapping("/chartMain")
	public String chartMain() {
		return "chart/chartMain";
	}
	
	//요청URI : /chart/chart01			
	@GetMapping("/chart01")
	public String chart01() {
		//forwarding
		return "chart/chart01";	//chart폴더의 char01페이지를 forwading해준다
	}
	
	//요청URI : /chart/chart01Multi			
	@GetMapping("/chart01Multi")
	public String chart01Multi() {
		//forwarding
		return "chart/chart01Multi";	
	}
	
	//요청URI : /chart/chart02
	@ResponseBody	//응답 데이터타입 : json일 때
	@GetMapping("/chart02")
	public JSONObject chart02() {
		return this.lprodService.cartMoney();
		//chart02.jsp없이 JSONObject파일만 return하니까 {~~~} 타입형태로 값만 떴음!
	}
	
	//요청URI : /chart/memberMoney
	//응답 데이터타입 : json
	@ResponseBody
	@GetMapping("/memberMoney")
	public JSONObject memberMoney() {
		return this.lprodService.memberMoney();
	}
	
	
}



























