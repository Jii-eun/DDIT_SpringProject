package kr.or.ddit.vo;

import java.util.Date;

import lombok.Data;

//POJO에 위반.. (POJO : 특정 기술에 종속되지 않는 순수한 자바 객체)
@Data
public class AttachVO {
	private int seq;
	private String filename;
	private int filesize;
	private String thumbnail;
	private Date regdate;
	//전사적 아이디
	private String etpId;
}
