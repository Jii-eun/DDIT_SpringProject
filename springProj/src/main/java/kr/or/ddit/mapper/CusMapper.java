package kr.or.ddit.mapper;

import java.util.List;

import kr.or.ddit.vo.CarVO;
import kr.or.ddit.vo.CusVO;

/*  root-context.xml에
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	이걸 추가했기 때문에 Mapper에는 annotation을 추가하지 않아도 된다.

 */

public interface CusMapper {
	//고객테이블(CUS)의 기본키 데이터 생성
	//<select id="getNextCusNum" resultType="">
	public String getNextCusNum();
	
	//고객(CUS) 등록
	//<insert id="createPost" parameterType="cusVO">
	public int createPost(CusVO cusVO);
	
	//소유자동차(CAR) 등록, 다중 insert 시 update 태그를 사용함 
	//<update id="createPostCar" parameterType="java.util.List">
	public int createPostCar(List<CarVO> carVOList);
	
	//고객 상세보기
	//<select id="detail" parameterType="cusVO" resultType="cusVO">
	public CusVO detail(CusVO cusVO);
}
