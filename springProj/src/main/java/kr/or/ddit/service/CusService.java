package kr.or.ddit.service;

import java.util.List;

import kr.or.ddit.vo.CarVO;
import kr.or.ddit.vo.CusVO;

public interface CusService {
	//메소드 시그니처
	public String getNextCusNum();

	public int createPost(CusVO cusVO);

	//소유자동차(CAR) 등록, 다중 insert 시 update 태그를 사용함 
	public int createPostCar(CusVO cusVO);
	
	//상세보기
	public CusVO detail(CusVO cusVO);

	public int createPostCar(List<CarVO> carVOList);

	public int createPostCar(CarVO carVOList);

}
