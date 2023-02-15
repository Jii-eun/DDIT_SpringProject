package kr.or.ddit.mapper;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.EmpVO;
import kr.or.ddit.vo.MemVO;

public interface EmpMapper {

	//다음 직원번호를 가져옴
	//<select id="getEmpNum" resultType="String">
	public String getEmpNum();
	
	//직원(EMP) 등록
	//<insert id="createPost" parameterType="empVO">
	public int createPost(EmpVO empVO);
	
	//모든 직원 정보 가져오기
	//<select id="getEmpAll" resultType="empVO">
	public List<EmpVO> getEmpAll();
	
	//직원 상세 보기(관리자가 있으면 관리자 정보도 포함)
	//<select id="detail" parameterType="empVO" resultType="empVO">
	public List<EmpVO> detail(EmpVO empVO);
	
	//직원 삭제
	//<delete id="deletePost" parameterType="empVO">
	public int deletePost(EmpVO empVO);
	
	//직원 목록
	//<select id="list" resultType="empVO">
	public List<EmpVO> list(Map<String, String> map);
	
	//직원 상세보기 1명
	public EmpVO detailOne(EmpVO empVO);
	
	//목록의 행 수를 구함
	public int getTotal(Map<String, String> map);
	
	//////////////////////////////////////////////////

	//회원 로그인
	//<select id="memLogin" parameterType="memVO" resultMap="memMap">
	public MemVO memLogin(MemVO memVO);
}




































