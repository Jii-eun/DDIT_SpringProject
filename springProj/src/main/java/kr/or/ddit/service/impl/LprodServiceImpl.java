package kr.or.ddit.service.impl;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.dao.LprodDao;
import kr.or.ddit.service.LprodService;
import kr.or.ddit.vo.AttachVO;
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
	
	//상품별 판매금액의 합계가 천만원을 넘은 데이터 ============> simpleData.json파일이랑 비교해서 보면 이해가 쉽다.
	@Override
	public JSONObject cartMoney(){
		//LprodDao에서 list로 받아온 값을 밑에서 JSONObject로 바꿔준다.
		//json => object 파싱
		//object => json 스크립트에서는 stringify
		List<Map<String, Object>> list = this.lprodDao.cartMoney();

		System.out.println("list : " + list.get(0).toString());

		// sampleData.json 파일 참고
		// 0. 리턴할 json객체--------------------------
		JSONObject data = new JSONObject(); // {}

		// 1.cols 배열에 넣기	(==컬럼명)
		// JSON 컬럼 객체---------------------------------------------
		JSONObject col1 = new JSONObject();	//첫번재 컬럼
		JSONObject col2 = new JSONObject();	//두번째 컬럼
		// JSON 배열 객체
		JSONArray title = new JSONArray();
		col1.put("label", "상품명");		//json 파일에 적었던 key:value
		col1.put("type", "string");
		col2.put("label", "금액");
		col2.put("type", "number");
		// 타이틀행에 컬럼 추가
		title.add(col1);		//배열의 title이 들어간 것
		title.add(col2);

		// json객체에 타이틀행 추가
		data.put("cols", title);	//data = 최종목표 	//cols라는 이름으로 valeu인 title을 넣음
		// {"cols":[{"label":"상품명","type":"string"},{"label":"금액","type":"number"}]}

		// 2.rows 배열에 넣기	(==컬럼 데이터)
		JSONArray body = new JSONArray(); // rows
		for (Map<String, Object> map : list) {
			JSONObject prodName = new JSONObject();
			prodName.put("v", map.get("PROD_NAME")); // 상품명

			JSONObject money = new JSONObject();
			money.put("v", map.get("MONEY")); // 금액

			JSONArray row = new JSONArray();
			row.add(prodName);
			row.add(money);

			JSONObject cell = new JSONObject();
			cell.put("c", row);
			body.add(cell); // 레코드 1개 추가
		}

		data.put("rows", body);

		return data;
	}
	
	//회원별 구매횟수 구하기
	@Override
	public JSONObject memberMoney(){
		// LprodDao에서 list로 받아온 값을 밑에서 JSONObject로 바꿔준다.
		// json => object 파싱
		// object => json 스크립트에서는 stringify
		List<Map<String, Object>> list = this.lprodDao.memberMoney();

		System.out.println("list : " + list.get(0).toString());

		// sampleData.json 파일 참고
		// 0. 리턴할 json객체--------------------------
		JSONObject data = new JSONObject(); // {}

		// 1.cols 배열에 넣기 (==컬럼명)
		// JSON 컬럼 객체---------------------------------------------
		JSONObject col1 = new JSONObject(); // 첫번재 컬럼
		JSONObject col2 = new JSONObject(); // 두번째 컬럼
		// JSON 배열 객체
		JSONArray title = new JSONArray();
		col1.put("label", "회원"); // json 파일에 적었던 key:value
		col1.put("type", "string");
		col2.put("label", "구매횟수");
		col2.put("type", "number");
		// 타이틀행에 컬럼 추가
		title.add(col1); // 배열의 title이 들어간 것
		title.add(col2);

		// json객체에 타이틀행 추가
		data.put("cols", title); // data = 최종목표 //cols라는 이름으로 valeu인 title을 넣음
		// {"cols":[{"label":"상품명","type":"string"},{"label":"금액","type":"number"}]}

		// 2.rows 배열에 넣기 (==컬럼 데이터)
		JSONArray body = new JSONArray(); // rows
		for (Map<String, Object> map : list) {
			JSONObject memId = new JSONObject();
			memId.put("v", map.get("MEMID")); // 회원

			JSONObject cartCNT = new JSONObject();
			cartCNT.put("v", map.get("CARTCNT")); // 횟수

			JSONArray row = new JSONArray();
			row.add(memId);
			row.add(cartCNT);

			JSONObject cell = new JSONObject();
			cell.put("c", row);
			body.add(cell); // 레코드 1개 추가
		}

		data.put("rows", body);

		return data;
	}
	
	
}
































