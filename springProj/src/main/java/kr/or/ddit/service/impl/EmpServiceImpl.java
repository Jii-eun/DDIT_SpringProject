package kr.or.ddit.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.mapper.EmpMapper;
import kr.or.ddit.service.EmpService;
import kr.or.ddit.vo.EmpVO;
import kr.or.ddit.vo.MemVO;

//스프링이 자바빈으로 등록해줌
@Service						//상속받으려면 interface!
public class EmpServiceImpl implements EmpService{
	//DI(의존성 주입)
	@Autowired
	EmpMapper empMapper;

	//다음 직원번호를 가져옴
	@Override //메서드 재정의  ====> super type. 메소드 시그니처 만들어줌
	public String getEmpNum() {
		//매퍼인터페이스 호출 필요
		return this.empMapper.getEmpNum();
	}
	
	//신규 직원 등록
	@Override 
	public int createPost(EmpVO empVO) {
		return this.empMapper.createPost(empVO);
	}
	
	//모든 직원 정보 가져오기
	@Override
	public List<EmpVO> getEmpAll(){
		return this.empMapper.getEmpAll();
	}
	
	//직원 상세 보기(관리자가 있으면 관리자 정보도 포함)
	@Override
	public List<EmpVO> detail(EmpVO empVO){
		return this.empMapper.detail(empVO);
	}
	
	//직원 삭제
	@Override
	public int deletePost(EmpVO empVO) {
		return this.empMapper.deletePost(empVO);
	}
	
	//직원 목록
	@Override
	public List<EmpVO> list(Map<String, String> map){
		return this.empMapper.list(map);
	}
	
	//직원 상세 보기 1명
	@Override
	public EmpVO detailOne(EmpVO empVO) {
		return this.empMapper.detailOne(empVO);
	}
	
	//목록의 행 수를 구함
	@Override
	public int getTotal(Map<String, String> map) {
		return this.empMapper.getTotal(map);
	}
	
	///////////////////////////////////////////////////////////////
	//회원 로그인
	@Override
	public MemVO memLogin(MemVO memVO) {
		return this.empMapper.memLogin(memVO);
	}
	
	
	
}

























