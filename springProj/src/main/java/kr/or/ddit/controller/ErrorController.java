package kr.or.ddit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/error")
@Controller
public class ErrorController {

	@GetMapping("/error400")
	public String error400() {
		//forwarding
		return "error/400";	//error폴더 안에 있는 400.jsp 로 이동
	}
	
	@GetMapping("/error404")
	public String error404() {
		//forwarding
		return "error/404";	//error폴더 안에 있는 404.jsp 로 이동
	}
	
	@GetMapping("/error500")
	public String error500() {
		//forwarding
		return "error/500";	//error폴더 안에 있는 500.jsp 로 이동
	}
	
	
}
