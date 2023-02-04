package kr.or.ddit.vo;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

//자동차
@Data
public class CarVO {
	@NotBlank
	private String carNum;	//자동차 번호
	@NotBlank
	private String mnfNum;	//제조 번호
	private int dist;		//주행거리
	private Date dt;		//연식	//util 삽입
	@NotBlank
	private String cusNum;	//고객 번호
	
	//자동차(CAR) : 서비스(SER) = 1 : N
	private List<SerVO> serVOList;
	
}

//의존관계 주입 => pom.xml에서
