package kr.or.ddit.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoginSuccessHandler extends 
	SavedRequestAwareAuthenticationSuccessHandler {
				//springfamework - security에서 제공
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth)
			throws ServletException, IOException {
		
		// ~했을 때, 로그인을 성공했을 때
		log.info("onAuthenticationSuccess");
		
		//user = 최종 목적지 (springframework.security에서 제공하는 것 추가)
User costomUser = (User)auth.getPrincipal();
		
		log.info("username : " + costomUser.getUsername());
		
		super.onAuthenticationSuccess(request, response, auth);
	}
	
	
}

































