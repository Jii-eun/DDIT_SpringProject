package kr.or.ddit.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.w3c.dom.ls.LSInput;

import kr.or.ddit.vo.MemAuthVO;
import kr.or.ddit.vo.MemVO;

//User : 스프링 시큐리티가 제공하고 있는 사용자 정보 클래스
public class CustomUser extends User{

	private MemVO memVO;
	
	public CustomUser(String username, String password
			, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
	}

	//기본생성자
	//memVO : select한 사용자 및 권한 정보가 들어있음
	//memVO : 타입의 객체 memVO 스프링 시큐리티에 제공해주고 있는 UerDatils 타입으로 변환
	//스프링 시큐리티에 회원정보를 보내줄테니 이가 니가 고려를 해라.
	public CustomUser(MemVO memVO) {
			//1) username, 2) password, 3) authorities
		//memVO.getMemAuthVOList() : 
		//	2	ROLE_ADMIN
		//	2	ROLE_MEMBER
		
		//memVO.getMemAuthVOList().stream(): <=일렬로 만들어짐
		//	2	ROLE_ADMIN	2	ROLE_MEMBER ==>LIST로 만들어서 COLLECTOON 처리
		super(memVO.getUserId(), memVO.getUserPw()
				, memVO.getMemAuthVOList().stream()
				.map(auth->new SimpleGrantedAuthority(auth.getAuth()))
				.collect(Collectors.toList())
				);
		this.memVO = memVO;
	}
	
	public static List<SimpleGrantedAuthority> getCollect(MemVO memVO){
		List<SimpleGrantedAuthority> authorities
			= new ArrayList<SimpleGrantedAuthority>();
		
		/*
			2	ROLE_ADMIN
			2	ROlE_MEMBER
		 */
		List<MemAuthVO> memAuthVOList= memVO.getMemAuthVOList();
		
		//memAuthVo : 2 ROLE_ADMIN
		for (MemAuthVO memAuthVO : memAuthVOList) {
			//memAuthVO.getAuth() : ROLE_ADMIN
			SimpleGrantedAuthority authority 
				= new SimpleGrantedAuthority(memAuthVO.getAuth());
			authorities.add(authority);
		}
		
		return authorities;
	}
	
	public MemVO getMemVO() {
		return memVO;
	}
	
	public void setMemVO(MemVO memVO) {
		this.memVO = memVO;
	}
}






























