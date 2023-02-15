package kr.or.ddit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.or.ddit.mapper.EmpMapper;
import kr.or.ddit.vo.MemVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j			//securitycontext에서 bean등록을 했기때문에 @service를 안해줘도된다.
public class CustomUserDetailsService implements UserDetailsService {

	//DI(의존성 주입)
	@Autowired
	EmpMapper empMapper;
	
	//요청파라미터 : <input type="text" name="username" ... />
	@Override									//DB상  USER_ID에 해당하는 값이 username에 들어옴
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		//파라미터 준비(publlic MemVO memLogin(MemVO memVO))
		MemVO memVO = new MemVO();
		//WHERE A.USER_ID = #{userId}
		memVO.setUserId(username);
		//<resultMap type ="memVO" id="memMap">	//갈때는 이 형태로, 오는건 list형태로
		memVO = empMapper.memLogin(memVO);
		
		log.warn("EmpMapper에 의해 쿼리를 실행할 것임 : " + memVO);
		
		//3항 연산자. memVO가 null이면 null을 리턴, null이 아니면 USER를 리턴
		return memVO==null ? null : new CustomUser(memVO);
	}

}






























