package kr.or.ddit.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import kr.or.ddit.service.BookService;
import kr.or.ddit.util.ArticlePage;
import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BookVO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;


/*	Controller 어노테이션
	스프링 프레임워크에게 "이 클래스는 웹 브라우저(클라이언트)의 요청(request)
	받아들이는 컨트롤러야"라고 알려주는 것임
  	스프링은 servlet-context.xml의 context:component-scan의 설정에 의해
  	이 클래스를 자바빈 객체로 미리 등록(메모리에 바인딩)
 */
@Slf4j		//이걸 추가해야 lombok을 쓸 수 있음
@Controller
public class BookController {
	
	//서비스를 호출하기 위해 의존성 주입(Dependency Injection-DI)
	@Autowired
	BookService bookService; 
	
	//요청 URI : /create
	//방법 : get
	//요청 - (매핑) - 메소드
	//↓↓밑의 메소드가 있어야 RequestMethod 자동완성
	//↓ 서블릿에서 주소 설정하는 느낌
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public ModelAndView create() {
		/*	ModelAndView
			1) Model : Controller가 반환할 데이터(String, int, List, Map, VO..)를 담당
			2) View : 화면을 담당(뷰(View : jsp)의 경로).jsp파일의 위치
		 */
		ModelAndView mav = new ModelAndView();
		
		/*
			<beans:property name="prefix" value="/WEB-INF/views/" />
			<beans:property name="suffix" value=".jsp" />
			
			prefix + 뷰경로 + .jsp 가 조립됨
			/WEB-INF/views/ + book/create + .jsp
			
		*/
		//forwarding
		mav.setViewName("book/create");
		return mav;
	}	
	
	//위는 get방식이라 아래에 post방식으로 다시 메소드 만들기
	/*
		요청URI : /create?title=개똥이글로리&category=소설&price=10000
		요청URL : /create
		요청파라미터 : {title=개똥이글로리, category=소설, price=10000}	←포스트방식일 때는 이렇게
		요청방식 : post
	 
	 	bookVO{bookId:null, title:개똥이글로리, category:소설, price:10000, insertDate:null
	 				content:null, cont:내용, uploadfile:파일객체,  attachVOList : null}
	 				
	 	private int bookId;
		private String title;
		private String category;
		private int price;
		private Date insertDate;
		private String content;
		private String cont;
		private MultipartFile[] uploadfiles;
		private List<AttachVO> attachVOList;
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
	//public ModelAndView createPost(HtHttpServletRequest request) { 
	//예전에 했떤 방법(request 파라미터를 받아서 ..)
	public ModelAndView createPost(BookVO bookVO, ModelAndView mav) {
		log.info("bookVO : " + bookVO.toString());
		
		//<selectKey resultType="int" order="BEFORE" keyProperty="bookId">
		//1증가된 기본키 값을 받음
		int bookId = bookService.createPost(bookVO);
		
		log.info("bookId : " + bookId);
		
		if(bookId < 1) {	//등록 실패
			// '/create'로 요청을 다시 함 => uri주소가 바뀜
			mav.setViewName("redirect:/create");
		} else {	//등록 성공 : 상세보기 페이지로 이동
			mav.setViewName("redirect:/detail?bookId=" + bookId);
		}
		
		return mav;
	}
	
	//책 상세보기
	//요청URI : /detail?bookId=2
	//요청URL : /detail
	//요청파라미터(HTTP파라미터=QueryString) : bookId=2
	//bookVO => bookVO{bookId:2, title:개똥이글로리, category:소설, price:10000, 
	//					insertDate:null, content:null}
	@RequestMapping(value="/detail", method =RequestMethod.GET)
	public ModelAndView detail(BookVO bookVO, ModelAndView mav) {
		log.info("bookVO : " + bookVO);
		
		BookVO data = bookService.detail(bookVO);
		// (후) bookVO{bookId:1, title:개똥이글로리, category:소설, price:10000, 
		//	insertDate:null, content:null}
		log.info("data : " + data);
		
		//ModelAndView mav = new ModelAndView();
		//model : 데이터를 jsp로 넘겨줌
		mav.addObject("data", data);
		mav.addObject("bookId", data.getBookId());	//기본키 값  //=>여기에서 에러가 나는 것
		
		/*
		 	<beans:property name="prefix" value="/WEB-INF/views/" />
		 	<beans:property name="suffix" value=".jsp"	/>
		 */
		//forwarding = detail.jsp를 해석/ 컴파일 => html로 크롬에게 리턴. 데이터 전달 가능
		//WEB-INF/views/ + book/detail + .jsp
		mav.setViewName("book/detail");	//이 경로 찾아가는거 = servlet-context.xml에서 조립되어 찾아가짐
						
		return mav;
	}
	
	//요청URI : /update?bookId=2
	//요청URL : /update
	//요청파라미터 : bookId=2
		//Model = data, View = jsp	//vo가 쓰이는 곳에 ModelAttribute가 따라다닌다.(생략도 가능)
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public ModelAndView update(@ModelAttribute BookVO bookVO, ModelAndView mav) {
		//bookVO => bookVO{bookId:2, title:개똥이글로리, category:소설, price:10000, 
		//					insertDate:null, content:null}
		log.info("bookVO : " + bookVO);
		
		//책 수정 화면 = 책 입력 화면 + 책 상세 데이터
		//책 입력 화면 양식을 그대로 따라가고, 빈 칸을 데이터로 채워주면 된다.
		
		BookVO data = bookService.detail(bookVO);
		// (후) bookVO{bookId:1, title:개똥이글로리, category:소설, price:10000, 
		//	insertDate:null, content:null}
		log.info("data : " + data);
		
		//ModelAndView mav = new ModelAndView();
		//model : 데이터를 jsp로 넘겨줌
		mav.addObject("data", data);
		
		//View : jsp의 경로
		//	/WEB-INF/views/ + book/update + .jsp
		//forwarding
		mav.setViewName("book/update");
		
		return mav;
	}
	
	/*
	 	요청URI : /update
	 	요청파라미터 : {bookId:2, title:개똥이글로리2, category:소설2, price:12000 }
	 	요청방식 : post
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public ModelAndView updatePost(BookVO bookVO, ModelAndView mav) {
		//bookVO{bookId:2, title:개똥이글로리2, category:소설2, price:12000}
		log.info("bookVO(updatePost) : " + bookVO);
		
		//result는 update문에 반영된 행의 수
		int result = this.bookService.updatePost(bookVO);
		
		if(result > 0) {	//업데이트 성공 -> 책 상세페이지(/detail)로 이동
			mav.setViewName("redirect:/detail?bookId=" + bookVO.getBookId());
		} else {	//업데이트 실패 -> 업데이트 뷰페이지(/update)로 이동
			mav.setViewName("redirect:/update?bookId=" + bookVO.getBookId());
		}
		
		return mav;
	}
	
	//요청URI : /delete
	//요청파라미터 : {"bookId":"2"}
	//<input type="hidden" name="bookId", value="2" />
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	public ModelAndView deletePost(@ModelAttribute BookVO bookVO,
			ModelAndView mav) {
		//bookVO : BookVO [bookId=2, title=null, category=null, 
		//				price=0, insertDate=null, content=null]
		log.info("bookVO : " + bookVO);
		
		//삭제SQL 실행 후 반영된 행의 수
		int result = this.bookService.deletePost(bookVO);
		log.info("result : " + result);
		if(result > 0) {	//삭제 성공
			//목록으로 요청 이동(상세페이지가 없으므로..)
			mav.setViewName("redirect:/list");
		} else {	//삭제 실행
			//상세페이지로 이동
			mav.setViewName("redirect:/detail?bookId=" + bookVO.getBookId());
		}
				
		return mav;
	}
	
	/* 요청URI 경우의 수 */
	//1)요청URI : /list
	//	요청파라미터 : {}
	//2)요청URI : /list?keyword=개똥이
	//	요청파라미터 : {"keyword":"개똥이"}
	//3)요청URI : /list?currentPage=1
	//	요청파라미터 : {"keyword" : "1"}
	//4)요청URI : /list?currentPage=1&keyword=개똥이
	//	요청파라미터 : {"currentPage" : "1", "keyword":"개똥이"}
	//방식 : get
	//스프링에서 요청파라미터를 매개변수로 받을 수 있음
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(ModelAndView mav,
			@RequestParam(value="keyword", required = false) String keyword,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
			//입력한 값을 받아올 매개변수가 필요
		log.info("keyword : " + keyword);
		
		//① total => 전체 행의 수 구하기
		int total = this.bookService.getTotal(keyword);
		log.info("total : " + total);
		
		//③ size => 한 페이지에 보여질 행의 수
		int size = 10;
		
		//검색조건 + 페이징 처리하는 쿼리문에 필요한 parameter 담기
		//Map<String, String> map = {}
		Map<String, String> map = new HashMap<String, String>();
		
		//쿼리문에서 사용할 수 있게 map에 값을 담아서 넘겨야함
		//쿼리문에서는 size랑 keyword만 필요함 => 이걸로 DB 내용을 가져온다
		//아래에 mav에 넣는 값들은 jsp에서 처리할 때 필요한 것 같음
		map.put("keyword", keyword);
		map.put("size", size+"");	
		map.put("currentPage", currentPage+"");
		
		
		List<BookVO> bookVOList = this.bookService.list(map);
		
		log.info("bookVOList : " + bookVOList);
		
		//데이터
		//1)페이징 처리 전
//		mav.addObject("data", bookVOList);
		
		//02.01
		//2) 페이징 처리 후					//↓해당 변수들 채우기
		/*
			① total = 전체 행의 수 구하기
			② list 메소드 파라미터로 currentPage를 받아옴(@RequestParam 이용)
				currentPage는 jsp에서 주소를 만들 때  /lprod/list?currentPage=${pNo }
				이렇게 써서 만들어준것!!
			③ size = 한 페이지에서 볼 행의 개수
			④ content = bookVOList
		 */
		mav.addObject("data", new ArticlePage<BookVO>(total, currentPage, size, bookVOList));
		
		//jsp(뷰) : book폴더에 있는 list.jsp를 forwarding(jsp를 해석, 컴파일하여 html로 리턴)
		mav.setViewName("book/list");
		
		return mav;
	}
	
}

























