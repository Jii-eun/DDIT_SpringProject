package kr.or.ddit.book.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.PaginationInfoVO;

public interface IBookService {
	public List<Map<String, Object>> selectBookList(Map<String, Object> map);

	public Map<String, Object> selectBook(Map<String, Object> map);

	public void insertBook(Map<String, Object> map);
	
	public int deleteBook(Map<String, Object> map);

	public int updateBook(Map<String, Object> map);

	public int selectBookCount(PaginationInfoVO<BookVO> pagingVO);

	public List<BookVO> selectBookList(PaginationInfoVO<BookVO> pagingVO);
}
