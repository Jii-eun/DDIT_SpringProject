package kr.or.ddit.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.LprodVO;
import lombok.extern.slf4j.Slf4j;

/*
	Controller 응답
	1. void
	2. String
	3. 자바빈즈 클래스
	4. 컬렉션 List 타입
	5. 컬렉션 Map 타입		==> 여기까진 기본적인 crud...............
	-----------------------------
		6. ResponseEntity<void>		
	7. ResponseEntity<String>
	8. ResponseEntity<자바빈즈 클래스>
	9. ResponseEntity<컬렉션 List 타입>
	10. ResponseEntity<컬렉션 Map 타입>
 */

//최종에서 ResponseEntity를 쓰지는 않음
//OK?? OK가 가 아니면 수정해야함 OK가 나오록 해야한다.... 그래서 안 쓴다....? ㅜ
//보통 String, javabeans, list 그 중 대부분은 String
//비동기통신을 할 때에는 ResponseBody만 써주면 됨. Model은 안 씀

/*
 230203

@GetMapping("lprod/list")
public void 메소드명()
	void ==> return 생략
	==>  lprod폴더 list.jsp리턴

public String 메소드명()
	

자바빈즈는 @responsebody를 꼭 써줘야한다. 
@ResponseBody


list, map은 알아서 json으로 간다.
 */


@Slf4j
@Controller	//컨트롤러라고 알려준다.
public class PracticeController {

	//요청URI : /practice/ajaxHome
	@GetMapping("/practice/ajaxHome")
	public String ajaxHome() {
		//forwarding
		return "practice/ajaxHome";
	}
	
	/*	경로 패턴 매핑
		요청 경로를 동적으로 표현이 가능한 경로 패턴을 지정할 수 있음
		- URI 경로 상의 변하는 값을 경로 변수(PathVariable)로 취급함
		- 경로 변수에 해당하는 값을 파라미터 변수(매개변수-파라미터를 받아 값을 저장하는 변수)에 설정할 수 있음
	 */
	
	//요청URI : /board/100 => 100은 boardNo(게시판 기본키)
	//요청방식 : get (데이터 변화가 없으면 get,  있으면 post)
			//리턴타입 = ResponseEntity<제네릭> 
	@PostMapping("/board/{boardNo}")		//↓경로상의 변수
	public ResponseEntity<String> read(@PathVariable("boardNo") int boardNo){
																//boardNo = 파라미터변수, 매개변수		
		log.info("boardNo : " + boardNo);
		
		//String을 응답할것임
		ResponseEntity<String> entity = 
				new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		
		return entity;
		
		//url 뒤에 parameter가 뜨는건 get 방식이라서.
		//post방식일 때는 안 뜬다.
	}
	
	//요청URI : /board/goHome0301
	//JSON데이터를 받을 땐.. ＠RequestBody
	//JSON데이터로 보낼 땐.. ＠ResponseBody 
	@ResponseBody	//엄청 중요 (비동기방식 처리 시 사용) ==>  ajaxHome.jsp에서 보낸 데이터를 받으니까 ResposeBody
	@GetMapping("/board/goHome0301")
	public LprodVO home0301() {
		log.info("home0301에 왔다.");
		
		LprodVO lprodVO = new LprodVO();
		lprodVO.setLprodId(1);
		lprodVO.setLprodGu("P101");
		lprodVO.setLprodNm("컴퓨터 제품");
		//forwarding도 아니고 redirect도 아님
		//자바빈 객체가 JSON으로 보냄
		return lprodVO;
	}
	
	//요청URI : /board/goHome04
	//요청파라미터(json) : {"lprodId" : lprodId, "lprodGu":lprodGu, "lprodNm":lprodNm}
	//방식 : post
	//리턴타입 : json
	@ResponseBody
	@PostMapping("/board/goHome04")
	public List<LprodVO> Home04(@RequestBody LprodVO lprodVO) {
		log.info("lprodVO : " +lprodVO);
		
		List<LprodVO> lprodVOList = new ArrayList<LprodVO>();
		
		LprodVO vo = new LprodVO();
		vo.setLprodId(2);
		vo.setLprodGu("P102");
		vo.setLprodNm("전자제품");
		lprodVOList.add(vo);
		/////////////////////
		vo = new LprodVO();		//초기화 -> 리스트에는 객체가 담기기 때문에 초기화를 안해주면 
		vo.setLprodId(3);
		vo.setLprodGu("P103");
		vo.setLprodNm("피혁잡화");
		lprodVOList.add(vo);
		/////////////////////
		vo = new LprodVO();
		vo.setLprodId(4);
		vo.setLprodGu("P104");
		vo.setLprodNm("문구류");
		lprodVOList.add(vo);
		/////////////////////
		log.info("lprodVOList : " + lprodVOList);
		
		return lprodVOList;
	}
	
	//요청URI : /board/goHome05/					뒤에 {id} 이런게붙으면 @PathVariable 사용
	//방식 : post
	//컬렉션 Map 타입을 JSON으로 응답
	@ResponseBody
	@PostMapping("/board/goHome05")
	public Map<String, LprodVO> Home05(Model model) {
		log.info("goHome05");
		
		Map<String, LprodVO> map = new HashMap<String, LprodVO>();
		
		LprodVO vo = new LprodVO();
		vo.setLprodId(2);
		vo.setLprodGu("P102");
		vo.setLprodNm("전자제품");
		map.put("key1", vo);
		/////////////////////
		vo = new LprodVO();		//초기화 -> 리스트에는 객체가 담기기 때문에 초기화를 안해주면 
		vo.setLprodId(3);
		vo.setLprodGu("P103");
		vo.setLprodNm("피혁잡화");
		map.put("key2", vo);
		/////////////////////
		vo = new LprodVO();
		vo.setLprodId(4);
		vo.setLprodGu("P104");
		vo.setLprodNm("문구류");
		map.put("key3", vo);
		/////////////////////
		
//		model.addAttribute(attributeName, attributeValue);
		
		//map으로는 잘 안씀 방법이 있다 정도만
		log.info("map : " + map);
		return map;
	}
	
	//SpringFramework를 이용한 파일 다운로드
	/*	MIME(Multipurpose Internet Mail Extensions)
		문자열을 전송할 때는 7비트 아스키파일로 전송하여 사용하지만
		사진, 음악, 동영상, 문서 파일을 보낼 땐 8비트 데이터(바이너리 데이터)를 사용함.
		이것을 전송하기 위해서는 바이너리 데이터를 텍스트로 변환하는 인코딩작업이 필요함.
		
		MIME은 이런 인코딩 방식의 일종.  인코딩 + data type(contents type)
		
		image/jpeg
	 */
	//요청URI : /board/goHome1102
	@ResponseBody
	@GetMapping("/board/goHome1102")
	public ResponseEntity<byte[]> home1102() throws IOException{
		log.info("home1102에 왔다");
		
		//입력 스트림(00110111011000..)
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		HttpHeaders headers = new HttpHeaders();
		
		String fileName = "C:\\eclipse_202006\\workspace\\springProj\\src\\main\\webapp\\resources\\upload\\2023\\01\\31\\0495dcde-f94d-4af1-83a2-d611198669b3_24.png";
		
		try {
			in = new FileInputStream(fileName);
													
			//APPLICATION_OCTET_STREAM : //OCTET = 8비트, STREAM(순수한 바이너리 데이터)
			//	표준으로 정의되어있지 않은 파일인 경우 지정하는 타입
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			//Content-Disposition : 웹브라우저에서 다운로드 창의 띄움
			//	이 뒤에는 파일 이름과 확장자를 지정해야 함
			//	스트림 파일을 통해 write해줌
			headers.add("Content-Disposition",  "attachment;filename=\"" +
					new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + 
					"\"");
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in),
					headers, HttpStatus.CREATED);
		} catch (FileNotFoundException e) {
			log.info(e.getMessage());
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		
		return entity;
	}
	
	/*
		요청 URI : /board/uploadAjax
		요청파라미터 : 
			<form>
				<input type="file" name="uploadFile" />
				.... x3
				<input type="text" name="lprodId" value="1" /> 		☆
				<input type="text" name="lprodGu" value="P101" />	☆
				<input type="text" name="lprodNm" value="컴퓨터제품" />	☆
			</form>
		방식 : post
		요청파라미터 : {lprodId=1, lprodGu={101, lprodNm=컴퓨터제품, uploadFile=파일객체}
	 */
	@ResponseBody
	@PostMapping("/board/uploadAjax")
	public ResponseEntity<String> uploadAjax(String lprodId, String lprodGu, String lprodNm
			, MultipartFile[] uploadFile
			, @RequestParam Map<String, Object> map
			, @ModelAttribute LprodVO lprodVO){
		log.info("lprodId : " + lprodId + ", lprodGu : " + lprodGu
				+ ", lprodNm : " + lprodNm);
		
		log.info("map : " + map);
		log.info("lprodVO : " +lprodVO);
		
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}
	
	
	
	
}
































