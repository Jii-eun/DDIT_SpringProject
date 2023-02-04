package kr.or.ddit.vo;

import lombok.Data;

@Data
public class BookVO {

	//BOOK_ID, TITLE, CATEGORY, PRICE, INSERT_DATE
	private int bookId;
	private String title;
	private String category;
	private int price;
	private String insertDate;
	
}
