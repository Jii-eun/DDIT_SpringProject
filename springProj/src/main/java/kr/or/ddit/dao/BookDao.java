package kr.or.ddit.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BookVO;
import lombok.extern.slf4j.Slf4j;

/*
	매퍼XML(book_SQL.xml)을 실행시키는  DAO(Data Access Object) 클래스
	Repository 어노테이션 : 데이터에 접근하는 클래스
	=> 스프링이 데이터를 관리하는 클래스라고 인지하여 자바빈 객체로 등록하여 관리함 
 */
@Slf4j		//이걸 넣으면 log.info 쓸 수 있음
@Repository	//골뱅이가 있기 때문에 클래스가 얘를 메모리에 올려놔준다.
public class BookDao {
	
	/*	DI(Dependency Injection) : 의존성 주입
	 	new 키워드를 통해 개발자가 직접 생성하지 않고,
	 	스프링에 미리 만들어놓은 (톰켓서버 실행 시 스프링이 미리 객체를 인스턴스화 해놓음)
	 	sqlSessionTemplate 타입 객체를 BookDao 객체에 주입함 
		
		IoC(Inversion of Control) : 제어의 역전
	 */
	//sqlSessionTemplate : 모더나 / BookDao : 개똥이 => 모더나를 개똥이 팔에 주입
	/*
	 	root-context.xml <bean id="sqlSessionTemplate" ...
	 	데이터베이스에 개별적으로 쿼리를 실행시키는 객체
	 	이 객체를 통해 query를 실행함
	 */
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
//	private String name;
	
	//도서 테이블(BOOK)에 입력
	//<insert id="createPost" parameterType="bookVO">
	public int createPost(BookVO bookVO) {
		// .insert("namespace값.id값", 파라미터)
		//book_SQL.xml 파일의 namespace가 book이고, id가 insert인
		//태그를 찾아서 그 안에 들어있는 sql을 실행함
		/*
			(전) bookVO{bookId:0, title:개똥이글로리, category:소설, price:10000, 
		 			insertDate:null, content:null}
			(후) bookVO{bookId:1, title:개똥이글로리, category:소설, price:10000, 
		 			insertDate:null, content:null}
		 */
		log.info("bookVO : " + bookVO.toString());
		//this ; BookDao, 생략 가능
		//insert, update, delete는 반영된 건수가 return 됨
		//insert 성공 : 1이상, 실패면 0
		int result = this.sqlSessionTemplate.insert("book.createPost", bookVO);
					//this는 클래스인 BookDaofmf, sqlSessionTemplate은 autowired 어노테이션에서 선언한 SqlSessionTemplate
					//위의 this는 생략 가능하다.
		int bookId = 0;
//		this.name = "개똥이"; 	//this가 붙은 name과 위에서 선언한 name은 다르다
		
		if(result > 0) {	//insert 성공
			bookId = bookVO.getBookId();		//0 ? 1(1 증가된 값이 들어있음) ?
		} else {			//insert 실패
			bookId = 0;
		}
		
		return bookId;
	}
	
	//책 상세보기
	public BookVO detail(BookVO bookVO) {
		//쿼리를 실행해주는 객체(힌트 : root_context.xml)
		//selectOne() 메소드 : 1행을 가져올 때 사용 
		//selectList() 메소드 : 결과 집합 목록 반환(다중행)
		//결과 행 수가 0이면? null을 반환
		//결과 행 수가 2이상이면? TooManyResultException 발생
		//결과로 나온 행 각 한개 = resultSet
		//.selectOne("namespace명.id명", 파라미터)
		return this.sqlSessionTemplate.selectOne("book.detail", bookVO);
	}
	
	//책 수정하기
	//parameterType="bookVO" 
	public int updatePost(BookVO bookVO) {
		//.update("namespace.id", 파라미터)
		//반환타입 int : update구문이 반영된 행의 수
		return this.sqlSessionTemplate.update("book.updatePost", bookVO);
	}
	
	//책 삭제하기
	//<delete id="deletePost" parameterType="bookVO">
	public int deletePost(BookVO bookVO) {
		//.delete("namespace.id", 파라미터)
		//반환타입 int : delete구문이 반영된 행의 수
		return this.sqlSessionTemplate.delete("book.deletePost", bookVO);
	}
	
	//책 목록
	//리턴타입 : List<BookVO> 
							//페이징 처리와 검색 키워드를 같이 받기 위해 String에서 map으로 변경
	public List<BookVO> list(Map<String, String> map){
		//	.selectOne() : 1행(상세보기) 
		//	.selectList("namespace.id", 파라미터) : 여러행(목록)
		return this.sqlSessionTemplate.selectList("book.list", map);
	}
	
	//attach 테이블에 insert all
	public int createPostAttch(List<AttachVO> attahVOList) {
		return this.sqlSessionTemplate.update("book.createPostAttach", attahVOList);
		//insert ALL은 UPDATE로
	}
	
	//전체 행의 수
	public int getTotal(String keyword) {
		return this.sqlSessionTemplate.selectOne("book.getTotal", keyword);
	}
		
}






























