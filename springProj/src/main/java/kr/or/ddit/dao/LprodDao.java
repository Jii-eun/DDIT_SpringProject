package kr.or.ddit.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;

import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.LprodVO;
import lombok.extern.slf4j.Slf4j;

/*
	매퍼XML(book_SQL.xml)을 실행시키는  DAO(Data Access Object) 클래스
	Repository 어노테이션 : 데이터에 접근하는 클래스
	=> 스프링이 데이터를 관리하는 클래스라고 인지하여 자바빈 객체로 등록하여 관리함 
 */
@Slf4j		//이걸 넣으면 log.info 쓸 수 있음
@Repository	//골뱅이가 있기 때문에 클래스가 얘를 메모리에 올려놔준다.
public class LprodDao {
	//이미 만들어져있는 sqlSessionTemplate 객체를
	//LprodDao에 주입
	//DI(Dependency Injection) : 의존성 주입
	//							   개발자가 new를 하지 않고 스프링에게 요청하여 객체를 사용
	//IoC(Inversion of Control) : 제어의 역전
	
	//스프링의 삼각형 가운데에 PoJo 아래에는 Psa 왼쪽에는 DI  오른쪽에는 Aop
	
	@Autowired
	SqlSessionTemplate sqlSessiontemplate;
	
	public List<LprodVO> list(Map<String, String> map){
		return this.sqlSessiontemplate.selectList("lprod.list", map);
	}
	
	//SQL 쿼리 작성 => DAO
	//상품분류 자동생성
	//<select id="getLprodId" resultType="int">
	public int getLprodId() {
		//selectOne("namespace.id")
		return this.sqlSessiontemplate.selectOne("lprod.getLprodId");
	}
	
	//상품분류코드 자동생성
	//<select id="getLprodGu" resultType="String">
	public String getLprodGu() {
		//selectOne("namespace.id")
		return this.sqlSessiontemplate.selectOne("lprod.getLprodGu");
	}
	
	//상품분류 정보를 입력 
	//SQL.xml => <insert id="createPost" parameterType="lprodVO">
	public int createPost(LprodVO lprodVO) {
		return this.sqlSessiontemplate.insert("lprod.createPost", lprodVO);
	}
	
	//상품분류 상세보기 
	//<select id="detail" parameterType="lprodVO" resultType="lprodVO">
	public LprodVO detail(LprodVO lprodVO) {
		//매퍼xml을 호출 => sql을 실행하기 위해
		//.selectOne("namespace.id", parameter)
		return this.sqlSessiontemplate.selectOne("lprod.detail", lprodVO);
	}
	
	//상품분류 정보변경
	//<update id="updatePost" parameterType="lprodVO">
	public int updatePost(LprodVO lprodVO) {
		return this.sqlSessiontemplate.update("lprod.updatePost", lprodVO);
				//update 결과값 = int
	}
	
	//상품분류 삭제
	//lprodVO{"lprodId":"10","lprodGu":"P404","lprodNm":"간식류변경"}
	//<delete id="deletePost" parameterType="lprodVO">
	public int deletePost(LprodVO lprodVO) {
		return this.sqlSessiontemplate.delete("lprod.deletePost", lprodVO);
		//몇번이 삭제됐는지 넘어옴
	}
	
	//첨부파일 등록
	//<insert id="uploadFormAction" parameterType="attachVO">
	public int uploadFormAction(AttachVO attachVO) {
		return this.sqlSessiontemplate.insert("lprod.uploadFormAction", attachVO);
		//쿼리가 실행 된 후 몇 행이 처리됐는지 결과값이 return됨
	}
	
	//다중 insert 시 update 태그를 사용
	//<update id="createPostAttach" parameterType="java.util.List">
	public int createPostAttach(List<AttachVO> attachVOList) {
		return this.sqlSessiontemplate.update("lprod.createPostAttach", attachVOList);
	}
	
	//<!-- 전체 행의 수(total) -->
	//<select id="getTotal" resultType="int">
	public int getTotal(String keyword) {
		return this.sqlSessiontemplate.selectOne("lprod.getTotal", keyword);
	}
	
	//상품별 판매금액의 합계가 천만원을 넘은 데이터
	//<select id="cartMoney" resultType="hashMap">
	public List<Map<String, Object>> cartMoney(){
		return this.sqlSessiontemplate.selectList("lprod.cartMoney");
	}
	
	//회원별 구매횟수 구하기
	//<select id="memberMoney" resultType="hashMap">
	public List<Map<String, Object>> memberMoney(){
		return this.sqlSessiontemplate.selectList("lprod.memberMoney");
	}
	
}
































