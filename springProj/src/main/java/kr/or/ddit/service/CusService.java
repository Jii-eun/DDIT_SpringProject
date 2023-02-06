package kr.or.ddit.service;

import kr.or.ddit.vo.CusVO;

public interface CusService {
	//메소드 시그니처
	public String getNextCusNum();

	public int createPost(CusVO cusVO);

	public CusVO detail(CusVO cusVO);

}
