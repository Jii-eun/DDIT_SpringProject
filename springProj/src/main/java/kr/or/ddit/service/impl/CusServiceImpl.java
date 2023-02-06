package kr.or.ddit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.mapper.CusMapper;
import kr.or.ddit.service.CusService;
import kr.or.ddit.vo.CarVO;
import kr.or.ddit.vo.CusVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CusServiceImpl implements CusService{
	@Autowired
	CusMapper cusMapper;
	
	//고객테이블(CUS)의 기본키 데이터 생성
	@Override
	public String getNextCusNum() {
		return this.cusMapper.getNextCusNum();
	}
	
	//고객(CUS) 등록
	@Override
	public int createPost(CusVO cusVO) {
		int result = 0;
		//고객(CUS) 등록(1행)
		result = this.cusMapper.createPost(cusVO);
		
		//소유자동차(CAR) 등록(N행)  ==> 다중insert문
		List<CarVO> carVOList = cusVO.getCarVOList();
		List<CarVO> carVOListNew = new ArrayList<CarVO>();
		
		//cusNum 최신화
		for(CarVO carVO : carVOList) {	
									//Nm으로 써서 parentKey어쩌구 에러났음
			carVO.setCusNum(cusVO.getCusNum());	//cusNum 최신화 후 다중 insert를 하자
			carVOListNew.add(carVO);
		}
		
		result += this.cusMapper.createPostCar(carVOListNew);
		log.info("result : " + result);
		
		return result;
	}

	@Override
	public CusVO detail(CusVO cusVO) {
		return null;
	}
	
}






























