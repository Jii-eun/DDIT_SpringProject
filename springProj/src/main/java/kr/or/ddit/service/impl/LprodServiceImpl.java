package kr.or.ddit.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import kr.or.ddit.dao.BookDao;
import kr.or.ddit.dao.LprodDao;
import kr.or.ddit.service.BookService;
import kr.or.ddit.service.LprodService;
import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.LprodVO;
import lombok.extern.slf4j.Slf4j;

//서비스 클래스 : 비즈니스 로직
//스프링 MVC 구조에서 Controller와 DAO를 연결하는 역할
/*
	스프링 프레임워크(디자인패턴 + 라이브러리 )는 직접 클래스를 생성하는 것을 지양하고
	인터페이스를 통해 접근하는 것을 권장하고 있기 때문(확장성)
	* 스프링은 인터페이스를 좋아함 
		=> 그래서 서비스 레이어는 인터페이스(BookService)와 클래스(BookServiceImpl)를 함께 사용함
	
	Impl : implement의 약자
 */
//Service 어노테이션 : 스프링에게 이 클래스는 서비스 클래스라고 알려줌
//					스프링이 자바빈으로 등록해줌
@Slf4j //log찍으려면 필요
@Service		//↓자식						//↓부모
public class LprodServiceImpl implements LprodService {
	
	@Autowired
	LprodDao lprodDao;
	
	@Override
	public List<LprodVO> list(Map<String, String> map) {
		return this.lprodDao.list(map);
	}

	//상품분류 자동 생성
	@Override
	public int getLprodId() {
		return this.lprodDao.getLprodId();
	}
	
	//상품분류코드 자동생성
	@Override
	public String getLprodGu() {
		return this.lprodDao.getLprodGu();
	}
	
	//신규 등록
	@Override
	public int createPost(LprodVO lprodVO) {
		//LPROD테이블에 insert
		int result =  this.lprodDao.createPost(lprodVO);
		
		//ATTACH테이블에 insert(다중insert) 
		//<update id="createPostAttach" parameterType="java.util.List">
		List<AttachVO> attachVOList = lprodVO.getAttachVOList();
		result = result + this.lprodDao.createPostAttach(attachVOList);
		log.info("result : " + result);
		
		return result;
	}
	
	//상세조회
	@Override
	public LprodVO detail(LprodVO lprodVO) {
		return this.lprodDao.detail(lprodVO);
	}
	
	//상품분류 정보변경
	//<update id="updatePost" parameterType="lprodVO">
	@Override
	public int updatePost(LprodVO lprodVO) {
		return this.lprodDao.updatePost(lprodVO);
	}
	
	//상품분류 삭제
	//lprodVO{"lprodId":"10","lprodGu":"P404","lprodNm":"간식류변경"}
	@Override
	public int deletePost(LprodVO lprodVO) {
		return this.lprodDao.deletePost(lprodVO);
	}
	
	//첨부파일 등록
	@Override
	public int uploadFormAction(AttachVO attachVO) {
		return this.lprodDao.uploadFormAction(attachVO);
	}
		
	//전체 행의 수(total)		//여기에서 먼저 작성한 후에 interface에 supertype을 만들어줄 수도 있다.
	@Override
	public int getTotal(String keyword) {
		return this.lprodDao.getTotal(keyword);
	}
	
	
}
































