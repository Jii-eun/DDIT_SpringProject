package kr.or.ddit.book.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.PaginationInfoVO;

@Repository
public class BookDAOImpl implements IBookDAO{
	
	@Autowired	//=자동 주입해야하는데 서비스라고 지정된게 없어서 오류가 난다!!
	SqlSessionTemplate sqlSessionTemplate;
	
	/*
	 	@Autowired  =>  웹서버를 run하는 순간 붙은 애들이 메모리에 올라가서 객체가 되어 있음
	 					autowired라는 주사를 이용해서 객체를 이 곳에 주입시킴
	 	@Inject
	 	
	 	이 두가지 annotation을 사용하면 저렇게 선언한 걸 바로 객체로 사용할 수 있음
	 */
	
	@Override
	public List<Map<String, Object>> selectBookList(Map<String, Object> map) {
		return sqlSessionTemplate.selectList("Book.selectBookList", map);
	}

	@Override
	public Map<String, Object> selectBook(Map<String, Object> map) {
		return sqlSessionTemplate.selectOne("Book.selectBook", map);	//BOOK_ID가 들어있음
	}

	@Override
	public void insertBook(Map<String, Object> map) {	// 메소드가 void라 return X
		sqlSessionTemplate.insert("Book.insertBook", map);
	}

	@Override
	public int deleteBook(Map<String, Object> map) {
		return sqlSessionTemplate.delete("Book.deleteBook", map);
	}
	
	@Override
	public int updateBook(Map<String, Object> map) {
		return sqlSessionTemplate.update("Book.updateBook", map);
	}

	@Override
	public int selectBookCount(PaginationInfoVO<BookVO> pagingVO) {
		return sqlSessionTemplate.selectOne("Book.selectBookCount", pagingVO);
	}

	@Override
	public List<BookVO> selectBookList(PaginationInfoVO<BookVO> pagingVO) {
		return sqlSessionTemplate.selectList("Book.selectBookList2", pagingVO);
	}
	
}

































