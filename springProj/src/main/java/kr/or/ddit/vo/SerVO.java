package kr.or.ddit.vo;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

//서비스
@Data
public class SerVO {
	
	@NotBlank
	private String serNum;		//서비스 번호
	@NotBlank
	private int serCs;			//수리 비
	@Size(max=3)
	private int serTm;			//수리 시간(3자리수를 넘기지 않도록 제한)
	@Future		//미래 날짜인지 검사(23/02/03(x), 2023-02-03(o).. =>형식을 제한)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date rep_comp_dt;	//수리 완료 예정일  
	@NotBlank
	private String cusNum;		//고객 번호
	@NotBlank
	private String empNum;		//직원 번호
	@NotBlank
	private String carNum;		//자동차 번호
	
	
	
}



















