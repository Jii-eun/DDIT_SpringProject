package kr.or.ddit.book.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.or.ddit.book.ServiceResult;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.PaginationInfoVO;

public interface IBookService {
	public List<Map<String, Object>> selectBookList(Map<String, Object> map);

	public Map<String, Object> selectBook(Map<String, Object> map);

	public void insertBook(Map<String, Object> map);
	
	public int deleteBook(Map<String, Object> map);

	public int updateBook(Map<String, Object> map);

	//페이징, 검색 이용하여 목록 가져오기
	public int selectBookCount(PaginationInfoVO<BookVO> pagingVO);
	public List<BookVO> selectBookList(PaginationInfoVO<BookVO> pagingVO);
	
	//페이징, 검색 이용하여 목록 가져오기 End
	public ServiceResult insertBookByFile(BookVO bookVO, HttpServletRequest request);

	//
	public BookVO selectBook2(int bookId);
}
