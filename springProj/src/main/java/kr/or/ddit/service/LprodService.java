package kr.or.ddit.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.LprodVO;

//서비스 interface : 비즈니스 로직(비즈니스 레이어)
public interface LprodService {

	//메소드 시그니처
	
	//리스트
	public List<LprodVO> list(Map<String, String> map);

	//상품분류 자동 생성
	public int getLprodId();

	//상품분류코드 자동 생성
	public String getLprodGu();

	//상품분류 정보를 등록
	public int createPost(LprodVO lprodVO);

	//상세분류 상세 보기
	public LprodVO detail(LprodVO lprodVO);

	//상품분류 정보변경
	public int updatePost(LprodVO lprodVO);

	//상품분류정보  삭제
	public int deletePost(LprodVO lprodVO);

	//첨부파일 등록
	int uploadFormAction(AttachVO attachVO);

	//전체 행의 수(total)
	int getTotal(String keyword);

	//상품별 판매금액의 합계가 천만원이 넘는 데이터
	public JSONObject cartMoney();

	//회원별 구매횟수 구하기
	public JSONObject memberMoney();
	

}






















