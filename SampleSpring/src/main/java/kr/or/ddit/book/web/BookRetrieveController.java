package kr.or.ddit.book.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.book.service.IBookService;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.PaginationInfoVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/book")
public class BookRetrieveController {
	
	@Autowired
	IBookService bookService;
	
	@RequestMapping(value="/list.do")
	public String list(@RequestParam Map<String, Object> map, Model model) {
		List<Map<String, Object>> list =  bookService.selectBookList(map);
		model.addAttribute("list", list);
		//model쓰는 이유 검색 모르겠어 왜 쓰는지
		return "book/list";
	}
	
	//만든 순서
	/*
	 	만든 순서
	 	controller - service - dao 다시 되돌아오면서 instance를 주입함.
	 	원래는 주입도 한번에 한다고는 함....
	 */
	
	@RequestMapping(value="/detail.do", method = RequestMethod.GET)
	public String detail(@RequestParam Map<String, Object> map, Model model) {
		Map<String, Object> detailMap = bookService.selectBook(map);
		model.addAttribute("book", detailMap);
		
		log.info("map : " + map);
		
		return "book/detail";
	}
	
	
	//ReuqestParam : 데이터를 받는 방법 => 받는 변수와 사용하는 변수가 다를 때 requestParam의 name이라는 속성을 사용해서 받을 수 있다.
	//				required라는 속성 : 매개변수로 받는 파라미터가 필수인지 아닌지.
	@RequestMapping(value="/list2.do")
	public String noticeListView(
			@RequestParam(name="page", required=false, defaultValue="1") int currentPage,
			@RequestParam(required=false, defaultValue="title") String searchType,
			@RequestParam(required=false) String searchWord,
			HttpServletRequest req,
			Model model){
		PaginationInfoVO<BookVO> pagingVO = new PaginationInfoVO<BookVO>();
		
		// 검색 기능 추가시 활용
		if(StringUtils.isNotBlank(searchWord)){
			if("title".equals(searchType)){
				pagingVO.setSearchType("title");
			}else {
				pagingVO.setSearchType("category");
			}
			pagingVO.setSearchWord(searchWord);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchWord", searchWord);
			//검색이 완료가 되고 목록페이지를 보여줄 때 그 키워드가 남아있게 하려고(뭐로 검색했나 확인할 수 있게)
		}
		
		pagingVO.setCurrentPage(currentPage);
		int totalRecord = bookService.selectBookCount(pagingVO);
		pagingVO.setTotalRecord(totalRecord);
		
		List<BookVO> dataList = bookService.selectBookList(pagingVO);
		pagingVO.setDataList(dataList);
		
		model.addAttribute("pagingVO", pagingVO);
		return "book/list2";
	}

}



































