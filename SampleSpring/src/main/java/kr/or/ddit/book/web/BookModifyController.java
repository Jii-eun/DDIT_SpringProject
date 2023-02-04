package kr.or.ddit.book.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.book.service.IBookService;

@Controller
@RequestMapping("/book")
public class BookModifyController {
	
	//삭제를 처리하기 위해 service를 불러야함
	//inject, autowired 똑같음
	//일일히 싱글톤 안만들어도 된다는 것이 장점
	@Autowired
	private IBookService bookService;
	
	//삭제
	@RequestMapping(value="/delete.do", method = RequestMethod.POST)
	public String deleteBook(@RequestParam Map<String, Object> map) {
		String goPage = "";
		
		int status_code = bookService.deleteBook(map);
		if(status_code > 0) {	//삭제 성공
			goPage = "redirect:/book/list.do";
		} else {				//삭제 실패
			goPage = "redirect:/book/detail.do?bookId=" + map.get("bookId").toString();
		}
		
		return goPage;
	}
	
	
	//수정 폼 부르기
	@RequestMapping(value="/update.do", method = RequestMethod.GET)
	public String modifyBook(@RequestParam Map<String, Object> map, Model model) {
		//한 줄의 bookdata를 리턴
		Map<String, Object> book = bookService.selectBook(map);
		model.addAttribute("book", book);
		
		//수정 폼을 요청하는 것
		return "book/update";
	}

	//수정							//넘긴 값은 다 map에 들어있음
	@RequestMapping(value="/update.do", method = RequestMethod.POST)
	public String updateBook(@RequestParam Map<String, Object> map, Model model) {
		String goPage = "";
		int status_code = bookService.updateBook(map);
		if(status_code > 0) {	//삭제 성공
			goPage = "redirect:/book/detail.do?bookId=" + map.get("bookId").toString();
		} else {				//삭제 실패
			//수정 실패시 값을 그대로 들고갈 수 있게 model을 이용해서 값이 담긴 map을 보내줌
			model.addAttribute("book", map);
			goPage = "book/update";	 //방식: 포워드 입력했던 값을 그대로 띄울 수 있게 하도록
		}
		
		//수정하면서 페이징+검색
		
		//수정 폼을 요청하는 것
		return goPage;
	}
	
	
}





		
		













		
		
		
		
		
		