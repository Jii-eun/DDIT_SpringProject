package kr.or.ddit.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/board")
@Controller
public class BoardController {
	
	// 요청URI : /board/list :모두가 접근 가능
	@PreAuthorize("isAuthenticated()")	//로그인을 해야 접근 가능
	@GetMapping("/list")
	public String list() {
		//forwarding
		//board폴더의 list.jsp를 포워딩
		return "board/list";
	} 
	
	
	//요청URI : /board/register : 로그인한 회원만 접근 가능
	@PreAuthorize("hasRole('ROLE_MEMBER')")	//security-context.xml에서 걸었던 접근제한을 걸어주는 역할
	@GetMapping("/register")
	public String register() {
		//forwardin\
		return "board/register";
	}
}
