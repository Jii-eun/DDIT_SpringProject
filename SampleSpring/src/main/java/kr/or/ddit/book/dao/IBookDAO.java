package kr.or.ddit.book.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import kr.or.ddit.vo.BookFileVO;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.PaginationInfoVO;

public interface IBookDAO {
	public List<Map<String, Object>> selectBookList(Map<String, Object> map);
	
	public Map<String, Object> selectBook(Map<String, Object> map);
	
	public void insertBook(Map<String, Object> map);
	
	public int deleteBook(Map<String, Object> map);

	public int updateBook(Map<String, Object> map);

	public int selectBookCount(PaginationInfoVO<BookVO> pagingVO);

	public List<BookVO> selectBookList(PaginationInfoVO<BookVO> pagingVO);
	
	public int insertBookByFile(BookVO bookVO);

	public void insertBookFile(BookFileVO bookFileVO);

	public BookVO selectBook2(int bookId);
	
}
