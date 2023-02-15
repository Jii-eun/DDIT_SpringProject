package kr.or.ddit.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;	//setter, getter 안 만들어도 알아서 받아줌

@Data
public class BookVO {

	//BOOK_ID, TITLE, CATEGORY, PRICE, INSERT_DATE
	private int bookId;
	private String title;
	private String category;
	private int price;
	private String insertDate;
	
	private int[] delBookId;		//파일 추가, 삭제 기록을 남기기 위해(삭제된 파일 id는 남아있음)
	private MultipartFile[] bookFile;	
				// = bookVO에서 파일을 받을 자리, bookVO를 매개변수로 사용하면 bookFile에 들어있는 파일을 사용할 수 있다는 것
				//
	private List<BookFileVO> bookFileList;
	
	/*
	 	서버에는 vo라는 객체가 있음
	 	브라우저에서 파일을 보냄 => BookVO안에 있는 bookFile이라는 변수가 됨 (배열형태로 세개가 들어감)
	 	이 파일이 들어가는 순간 setbookFile이라는 함수가 돌아가게 된다(setter)
	 	setter가 돌아가면서 List에 들어갈 수 있게 setter가 작동
	 */
	public void setBookFile(MultipartFile[] bookFile) {
		this.bookFile = bookFile;
		if(bookFile != null) {
			List<BookFileVO> bookList = new ArrayList<BookFileVO>();
			for (MultipartFile item : bookFile) {
				if(StringUtils.isBlank(item.getOriginalFilename())) {
					continue;
				}
				BookFileVO bookFileVO = new BookFileVO(item);	
				//bookFileVO 생성자를 통해서 파일하나하나가 bookFileVO에 바인딩되고, 마지막에 list에 추가됨
				bookList.add(bookFileVO);
			}
			this.bookFileList = bookList;
		}
	}
	
}
