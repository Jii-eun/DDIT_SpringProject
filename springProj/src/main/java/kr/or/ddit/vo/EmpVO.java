package kr.or.ddit.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

//직원
@Data  
public class EmpVO {
	
	@NotBlank
	private String empNum;	//직원 번호
	private String empAddr;	//주소
	@NotBlank
	private String empPhe;	//연락처
	@NotBlank
	private String empNm;	//직원 명
	@NotNull
	private int empPay;		//급여
	private String empMjNum;	//매니저
	//매니저명(EMP_MH_NM)		
	private String empMjNm;	//매니저명(list를 받아올때 값 처리를 위해 추가)
	//행번호
	private int rnum;
	
	//직원(EMP) : 서비스(SER) = 1 : N
	private List<SerVO> serVOList;

}



//고객(CUS), 직원(EMP), 자동차(CAR) == MAIN ENTITY

//