package kr.or.ddit.vo;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

//고객
/*	Bean Validation이 제공하는 제약 어노테이션
	- Not Null : 빈 값 체크
***	- NotBlank : null 체크, trim(공백제거) 후 길이가 0인지 체크    <= 많이 씀
	- Size : 글자 수 체크
	- Email : 이메일 주소 형식 체크
	- Past : 오늘보다 과거 날짜(ex. 생일)
	- Future : 미래 날짜 체크(ex. 예약일)
 */
@Data
public class CusVO {

	@NotBlank	//비어있으면 안되는 멤버변수에 해당 어노테이션을 달아준다.
	private String cusNum; 	//고객번호(=필수 =mandatory), null체크, trim 후 길이가 0인지 체크
	@NotBlank
	@Size(min = 2, max=10, message="2자~10자 이내로 입력해 주세요")	//이름 길이
	private String cusNm;	//고객명
	private String cusAddr;	//주소
	@NotBlank
	private String cusPhe;	//연락처
	private String postno;	//우편번호
	private String addrDet;	//상세 주소
	
	//getter, setter를 안 만든 것들은 lombok에서 자동으로 처리해줘서 쓸 수 있음
	/*
		2023-02-06 	(X) ← <input type="date"
		20230206	(X)
		2023/02/06	(O)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")	//input type="date"를 쓰려면 꼭 써라
	private Date cusBir;	//생일	//java.util.Dates
	private List<String> hobbyList;	//취미(여러개 선택) {"Music", "Movie", "Sports"}
	private String hobby;			//취미 => Music, Movie, sports
	private String gender;			//성별(한 개 선택)
	private String nationality;		//국적(한 개 선택) => select박스
	
	//고객(CUS) : 자동차(CAR) =  1 : N
	@Valid		//CarVO가 class이기 때문에 NotBlank를 쓸 수는 없고, 이 클래스도 blank여부를 체크하겠다는 의미에서 @Valid를 달아준다
	private List<CarVO> carVOList;
	//고객(CUS) : 서비스(SER) = 1 : N
	private List<SerVO> serVOList; 
	
	public String getCusNum() {
		return cusNum;
	}
	public void setCusNum(String cusNum) {
		this.cusNum = cusNum;
	}
	public String getCusNm() {
		return cusNm;
	}
	public void setCusNm(String cusNm) {
		this.cusNm = cusNm;
	}
	public String getCusAddr() {
		return cusAddr;
	}
	public void setCusAddr(String cusAddr) {
		this.cusAddr = cusAddr;
	}
	public String getCusPhe() {
		return cusPhe;
	}
	public void setCusPhe(String cusPhe) {
		this.cusPhe = cusPhe;
	}
	
	
	
	
	
	
}
