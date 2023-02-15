package kr.or.ddit.book.web;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.book.ServiceResult;
import kr.or.ddit.book.service.IBookService;
import kr.or.ddit.vo.BookVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/book")
public class BookInsertController {
	
	@Inject
	IBookService bookService;
	
	@RequestMapping(value="/form.do", method = RequestMethod.GET)
	public String bookForm() {
		return "book/form";
	}
	
	@RequestMapping(value="/form.do", method = RequestMethod.POST)	//Model = 데이터 전달자?
	public String bookInsert(
			HttpServletRequest request
			, @RequestParam Map<String, Object> map 
			, BookVO bookVO	//bookVO - 자바빈즈를 통해 데이터를 받아봄
			, Model model) {
		String goPage = "";
//		bookService.insertBook(map);
//		
//			//map에서 꺼내면 object라서 문자열로 변환!
//		if(map.get("book_id").toString() != null) {	//
//			//goPage = "redirect:/book/detail.do?bookId="+bookId;
//			goPage = "redirect:/book/detail.do?bookId=" + map.get("book_id").toString();
//		} else {	//실패시
//			model.addAttribute("data", map);
//			goPage = "book/form";
//		}
		
		//////////////////////////////////////////
		//0209 
		//파일데이터 없는 insert End
		
		//파일데이터 사용하는 insert
		//book패키지에서 enum생성 (이름 : ServiceResult)
		Map<String, String> errors = new HashMap<String, String>();
		if(StringUtils.isBlank(bookVO.getTitle())) {
			errors.put("title", "책 제목을 입력하세요.");
		}
		if(StringUtils.isBlank(bookVO.getCategory())) {
			errors.put("category", "책 카테고리를 입력해주세요.");
		}
		
		if(errors.size() > 0) {	//에러가 있다는 것이므로 사이즈가 0보다 크다.
			model.addAttribute("errors", errors);
			model.addAttribute("bookVO", bookVO);
			goPage = "book/form";
			//model = 데이터 전달자
		}else {					//에러가 없다(정상적인 입력)
			ServiceResult result = bookService.insertBookByFile(bookVO, request);
			if(result.equals(ServiceResult.OK)) {
				goPage = "redirect:/book/list.do";
			}else {	//실패
				model.addAttribute("message", "서버에러, 다시 시도해주세요!");
				goPage = "book/form";
			}
		}
							//메소드에 대한 결과값으로 넘어온 값이 ServiceResult를 통해 OK, FAILED 이렇게 받아짐
		//파일데이터 사용하는 insert End
		
		
		return goPage;
	}
	
}






























