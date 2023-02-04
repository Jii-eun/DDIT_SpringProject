package kr.or.ddit.book.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.or.ddit.book.dao.IBookDAO;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.PaginationInfoVO;

@Service
public class BookServiceImpl implements IBookService {
	
	@Inject	//이걸 이용하면 서버를 run할 때 만든 인스턴스들을 이곳에 주입시켜줌
	IBookDAO bookDAO;
	
	@Override
	public List<Map<String, Object>> selectBookList(Map<String, Object> map) {
		return bookDAO.selectBookList(map);
	}

	@Override
	public Map<String, Object> selectBook(Map<String, Object> map) {
		return bookDAO.selectBook(map);
	}

	@Override
	public void insertBook(Map<String, Object> map) {
		bookDAO.insertBook(map);
	}

	@Override
	public int deleteBook(Map<String, Object> map) {
		return bookDAO.deleteBook(map);
	}

	@Override
	public int updateBook(Map<String, Object> map) {
		return bookDAO.updateBook(map);
	}

	@Override
	public int selectBookCount(PaginationInfoVO<BookVO> pagingVO) {
		return bookDAO.selectBookCount(pagingVO);
	}

	@Override
	public List<BookVO> selectBookList(PaginationInfoVO<BookVO> pagingVO) {
		return bookDAO.selectBookList(pagingVO);
	}
}
