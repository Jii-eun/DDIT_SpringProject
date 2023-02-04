package kr.or.ddit.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.or.ddit.service.BookService;
import kr.or.ddit.service.LprodService;
import kr.or.ddit.util.ArticlePage;
import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BookVO;
import kr.or.ddit.vo.LprodVO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import oracle.jdbc.proxy.annotation.Post;

import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;
/*	Controller 어노테이션
	스프링 프레임워크에게 "이 클래스는 웹 브라우저(클라이언트)의 요청(request)
	받아들이는 컨트롤러야"라고 알려주는 것임
  	스프링은 servlet-context.xml의 context:component-scan의 설정에 의해
  	이 클래스를 자바빈 객체로 미리 등록(메모리에 바인딩)
 */

@RequestMapping("/lprod") // ==> ~~~/lprod/list 로만 매핑
@Slf4j // 이걸 추가해야 lombok을 쓸 수 있음
@Controller
public class LprodController {

	@Autowired
	LprodService lprodService; // 이미 만들어져있는 자바빈 객체를 클래스에 주입한다 (autowired
	
	//요청URI : /lprod/list 또는 /lprod/list?currentPage=2
	//1) 요청파라미터 : currentPage=2
	//2) 요청파라미터 : 파라미터가 없을 수도 있음 
	//	** required : 파라미터가 필수인지 체크
	//	** defaultValue : 없을 때 기본값 (defaultValue="1" : 1페이지로 간주함)
	//	** currentPage = 2 파라미터의 타입은 String.. but, int 타입의 매개변수로 자동 형변환 가능
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView mav, 
			@RequestParam(value = "currentPage", required = false, defaultValue = "1" ) int currentPage,
			@RequestParam(value="keyword", required = false) String keyword,
			@RequestParam(value="size", required = false, defaultValue = "10") int size) {
						//value = 요청파라미터의 name
		//검색 조건
		Map<String, String> map = new HashMap<String, String>();
		
		
		//1) 전체 행의 수 구하기(total)
		int total = this.lprodService.getTotal(keyword);
		//3) 한 페이지에 보여질 행의 수(size) ====> jsp에서 size를 param으로 가져오기 때문에 이제 필요가 없음!(사용자가 직접 조정 가능)
//		int size = 10;
		
		//keyword도 map에 넣기
		map.put("keyword", keyword);
		
		//map{"size":"10", "currentPage":"1", "keyword", "?}
		map.put("size", size+"");
		map.put("currentPage", currentPage+"");
		log.info("map : " + map);
		
		List<LprodVO> lprodVOList = this.lprodService.list(map);
		
		// 페이징 처리 전..data
		// mav.addObject("data", lprodList);
		// 페이징 처리 후..
		mav.addObject("data"
				, new ArticlePage<LprodVO>(total, currentPage, size, lprodVOList));
		// jsp경로
		// tiles-config.xml의
		// */*은 lprod/list와 {1}/{2}.jsp에 의해 lprod/list.jsp가 forwarding됨
		mav.setViewName("lprod/list");

		return mav;
	}

	/*
	 * 요청URI : /lprod/create 방식 : GET
	 */

	// @RequestMapping(value="/create", method=RequestMethod.GET) 과 같음
	@GetMapping("/create")
	public String create(Model model) {
		// 상품분류 아이디 자동생성 //기능 => 서비스 호출
		int lprodId = this.lprodService.getLprodId();

		// lprodId 데이터를 model에 담아서 create.jsp로 보내줌
		model.addAttribute("lprodId", lprodId);

		// 상품대분류 등록 jsp를 forwarding
		// 1. 타일즈의 index.jsp 우선 적용
		// 2. <definition name="*/*" extends="tiles-layout">
		// /WEB-INF/views/lprod/create.jsp => tiles의 body로 include됨
		// forwarding
		return "lprod/create";
	}

	/*
	 * url : "/lprod/getLprodGu", type : "post",
	 */
	// 골뱅이 RequestMapping(value="getLprodGu", method=RequestMethod.POST)
	// 비동시 방식 요청 처리 시 @Responsebody를 사용함 => JSON데이터로 변환하여 리턴해줌
				//pom.xml에 Jackson Databind 2.13.3 버전 추가했음
	@ResponseBody
	@PostMapping("/getLprodGu")
	public String getLprodGu(Model model) {
//		log.info("getLprodGu에 왔다");
		
		//비즈니스(기능, service) 로직
		String lprodGu = this.lprodService.getLprodGu();
		log.info("lprodGu : " + lprodGu);
		
		return lprodGu;
	}
	
	/*
	create.jsp에서 해당사항을 요청
	요청URI : /lprod/createPost
	요청파마리터 : {"lprodId":"10", "lprodGu":"P404", "lprodNm":"간식류"}
	요청방식 : post
	model.addAttribute("LprodId", LprodVO.getl);
	=====> 아래의 메소드에 매핑되어 처리됨
	*/
	@PostMapping("/createPost")
	public String createPost(@ModelAttribute LprodVO lprodVO,
			Model model) {
		
		String uploadFolder 
			= "C:\\eclipse_202006\\workspace\\springProj\\src\\main\\webapp\\resources\\upload";
		
		//----------make folder 시작----------
							//File(어디에, 무엇을)
		File uploadPath = new File(uploadFolder, getFolder() );		
		log.info("uploadPath" + uploadPath);
		//----------make folder 끝----------
		
		//만약 연/월/일 해당 폴더가 없다면 생성
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();	//다중폴더 생성
		}
		
		//넘어온 정보
		//lprodVO : LprodVO[lprodId:10, lprodGu:P404, lprodNm:간식류]
		//uploadFile : 있음(사진을 첨부했으니), attachVOList = null; 인 상태
		log.info("lprodVO : " + lprodVO);
		
		//<input class="form-control" type="file" id="input_imgs" name="uploadFile">
		MultipartFile[] multipartFiles = lprodVO.getUploadFile();
		log.info("multipartFiles : " + multipartFiles);
		
		List<AttachVO> voList = new ArrayList<AttachVO>();
		int seq = 1;
		
		//uploadFile 정보를 통해서 attachVOList에 값들을 setting해줘야 함
		//배열로부터 하나씩 파일을 꺼내오자
		for(MultipartFile mf : multipartFiles) {
			AttachVO vo = new AttachVO();
			//실제 파일명
			String uploadFileName = mf.getOriginalFilename();
			
			log.info("---------------------------------");
			log.info("fileName : " + uploadFileName);
			log.info("fileSize : " + mf.getSize());
			log.info("contentType : " + mf.getContentType()); //MIME 타입
			
			//------- 같은 날 같은 이미지를 업로드 시 파일 중복 방지 시작-------
			//java.util.UUId => 랜덤값 생성
			UUID uuid = UUID.randomUUID();		//임의의 값을 생성
			//원래의 파일 이름과 구분하기 위해 _를 붙임
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			//------- 같은 날 같은 이미지를 업로드 시 파일 중복 방지 끝-------
			
			//파일 객체 설계(복사할 대상 경로, 파일명)
			File saveFile = new File(uploadPath, uploadFileName);
			
			try {
				//파일 복사가 일어남 (클라이언트에 있는 파일을 서버에 복사하겠다.)
				mf.transferTo(saveFile);
				
				//-----------썸네일 처리 시작-----------
				//파일이 이미지인지 체킹
				if(checkImageType(saveFile)) { //이미지라면 true => 실행
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					//썸네일 생성(원본파일, 대상, 가로크기, 세로크기)
					Thumbnailator.createThumbnail(mf.getInputStream(),
							thumbnail, 100, 100);
					
					thumbnail.close();
				}
				//-----------썸네일 처리 끝-----------
			} catch (IllegalStateException e) {
				log.error(e.getMessage());
				return "0";
			} catch (IOException e) {
				log.error(e.getMessage());
				return "0";
			}
			
			vo.setSeq(seq++);	//현재 seq의 값을 먼저 넣어서 처리 후 1을 더해줌
			// ↓ /2023/01/30/sadflkjfle_개똥이.jpg
			String filename = "/" + getFolder().replace("\\", "/") 
					+ "/" + uploadFileName;
			vo.setFilename(filename);
			Long l = mf.getSize();			//롱은 Long/// long은 다름?					
			vo.setFilesize(l.intValue());
			// ↓ /2023/01/30/sadflkjfle_개똥이.jpg
			String thumbFileName =  "/" + getFolder().replace("\\", "/") 
					+ "/s_" + uploadFileName;
			vo.setThumbnail(thumbFileName);
			//전사적 아이디(P301)
			vo.setEtpId(lprodVO.getLprodGu());
			voList.add(vo);
		}
		//lprodVO : lprodVO[lprodId:10, lprodGu:P413, lprodNm:간식류,
		//					uploadFile:있음, attachVOList:있음]
		lprodVO.setAttachVOList(voList);
		
		
		int result = this.lprodService.createPost(lprodVO);
		log.info("result : " + result);
		
		if(result>0) { 	//입력 성공
			// /lprod/detail
			return "redirect:/lprod/detail?lprodGu=" + lprodVO.getLprodGu();
		} else {		//입력 실패 => forwarding 처리
			// /lprod/create => create.jsp에 데이터가 입력되어있게
			model.addAttribute("data", lprodVO);
			model.addAttribute("lprodId", lprodVO.getLprodId());
			//forwarding
			return "lprod/create";
		}
	}
	
	//요청URI : /lprod/detail?lprodGu=P404
	//요청URL : /lprod/detail
	//요청파라미터 : lprodGu=P404
	//요청방식 : GET
						//@ModelAttribute 없어도 상관 없음
	@GetMapping("/detail")
	public String detail(@ModelAttribute LprodVO lprodVO,
			Model model) {
		//lprodVO : LprodVO[lprodId:0, lprodGu:P404, lprodNm:null]
							//int의 기본값은 0, String의 기본값은 null
		log.info("lprodVO : " + lprodVO);
		
		//lprodGu의 값이 P404인 상품분류 정보 1행을 가져오자
		LprodVO lprodVOresult = this.lprodService.detail(lprodVO);
		
		//lprodVO(후) : LprodVO[lprodId:10, lprodGu:P404, lprodNm:간식류]
		model.addAttribute("data", lprodVOresult);
	
		//forwarding
		//detail.jsp는 create.jsp를 기본폼으로 하고, data를 받아서 정보를 화면에 출력
		return "lprod/detail";
	}
	
	//상품분류 정보 변경
	//요청URL : /lprod/updatePost
	//요청파라미터 : {"lprodId":"10", "lprodGu":"P404", "lprodNm":"간식류변경"}
	//요청방식 : post
	@PostMapping("/updatePost")
	public String updatePost(@ModelAttribute LprodVO lprodVO,
			Model model) {
		log.info("lprodVO : " + lprodVO);
		
		//정보 변경
		int result = this.lprodService.updatePost(lprodVO);
		log.info("result : " + result);
		
		if(result>0) {	//정보 변경 성공
			//detail로 되돌아오기
			return "redirect:/lprod/detail?lprodGu="+lprodVO.getLprodGu();
		} else {		//정보 변경 실패
			//list로 되돌아오기 => 이 때는 redirect, forward 다 가능
//			return "redirect:/lprod/list";
			
			model.addAttribute("data", lprodVO);
			
			return "lprod/detail";
		}
		
	}
	
	//요청 URL : /lprod/uploadForm
	//요청방식 : get
	@GetMapping("/uploadForm")
	public String uploadForm() {
		//forwarding
		return "lprod/uploadForm";
	}
	
	/*
	 요청URL : /lprod/uploadFormAction
	 요청파라미터 : uploadFile이라는 이름의 파일객체
	 요청방식 : post 
	 */
	@PostMapping("/uploadFormAction")
	public String uploadFormAction(MultipartFile uploadFile) {
		//MultipartFile : 스프링에서 제공해주는 타입
		/*
		  String, getOriginalFileName() : 업로드 되는 파일의 실제 파일명
		  boolean, isEmpty() : 파일이 없다면 true
		  long, getSize() : 업로드되는 파일의 크기
		  transferTo(File file    ) : 파일을 저장
		 */
		//파일이 저장되는 경로
		String uploadFolder = "c:\\upload";
		
		//--------------make folder 시작---------------
//		//2023-01-27 형식(format) 지정
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		
//		//날짜 객체 생성(java.util 패키지)
//		Date date = new Date();
//		//2023-01-27
//		String str = sdf.format(date);
		////↑ getFolder()함수로 만듦
		
		//단순 날짜 문자를 File 객체의 폴더 타입으로 바꾸기
		//c:\\upload\\2023\\01\\27
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("upload Path : " + uploadPath);
		
		//만약 연/월/일 해당 폴더가 없다면 생성
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		//--------------make folder 끝----------------
		
		//파일명
		String uploadfileName = uploadFile.getOriginalFilename();
	
		log.info("--------------------");
		log.info("이미지 명 : " + uploadFile.getOriginalFilename());
		log.info("파일 크기 : " + uploadFile.getSize());
		
		//----------------파일명 중복 방지 시작----------------
		//java.util.UUID => 랜덤값을 생성
		UUID uuid = UUID.randomUUID();
		//ERADFDSDFA_개똥이.jpg	원래 이름 앞에 uuid로 랜덤으로 0 파0일명을 추가
		uploadfileName = uuid.toString() + "_" + uploadfileName;
		//----------------파일명 중복 방지 끝----------------
		
		//계획을 세움
		//java.io.File		File(어디에, 무엇을)
		File saveFile = new File(uploadPath, uploadfileName);

		try {
			//계획 실행. 파일 복사됨(클라이언트의 파일을 서버의 공간으로 복사)
			uploadFile.transferTo(saveFile);
			
			AttachVO attachVO = new AttachVO();
			//1) filename : /2023/01/27/323251417b09_nullPointer.jpg
			String filename = "/" + getFolder().replace("\\", "/") + "/" + 
								uploadfileName;
			attachVO.setFilename(filename);
			
			//2) filesize 
			Long l = uploadFile.getSize();
			attachVO.setFilesize(l.intValue());
			
			//3) thumbnail
			String thumbnailName = "/" + getFolder().replace("\\", "/") + "/s_" + 
							uploadfileName;
			attachVO.setThumbnail(thumbnailName);
			
			log.info("attachVO : " + attachVO);
			
			//이미지인지 체킹
			try {
				String contentType = Files.probeContentType(saveFile.toPath());
				log.info("contentType : "+contentType);
				if(contentType.startsWith("image")) {
					FileOutputStream thumbnail = new FileOutputStream(
							new File(uploadPath,"s_" +uploadfileName)
					);
					//섬네일 생성
					Thumbnailator.createThumbnail(uploadFile.getInputStream(),thumbnail,100,100);
					thumbnail.close();
				}
				//ATTACH 테이블에 insert
				int result = this.lprodService.uploadFormAction(attachVO);
				log.info("result : " + result);
				
	         }catch (IOException e) {
	        	 	e.printStackTrace();
	         }

		} catch (IllegalStateException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return "redirect:/lprod/uploadForm";
	}

	
	//요청URL : /lprod/uploadFormMultiAction
 	//요청파라미터 : uploadFile이라는 이름의 파일객체
 	//요청방식 : post
	@PostMapping("/uploadFormMultiAction")
	public String uploadFormMultiAction(MultipartFile[] uploadFile) {
		//파일이 저장되는 경로
		String uploadFolder = "c:\\upload";
		
		//--------------make folder 시작---------------
		//단순 날짜 문자를 File 객체의 폴더 타입으로 바꾸기
		//c:\\upload\\2023\\01\\27
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("upload Path : " + uploadPath);
		
		//만약 연/월/일 해당 폴더가 없다면 생성
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		//--------------make folder 끝----------------

		
		for(MultipartFile multipartFile : uploadFile) {
			//파일명
			String uploadfileName = multipartFile.getOriginalFilename();
					
			log.info("--------------------");
			log.info("이미지 명 : " + uploadfileName);
			log.info("파일 크기 : " + multipartFile .getSize());
			log.info("컨텐츠(MIME)타입 : " + multipartFile.getContentType());
			
			//----------------파일명 중복 방지 시작----------------
			//java.util.UUID => 랜덤값을 생성
			UUID uuid = UUID.randomUUID();
			// ex) ERADFDSDFA_개똥이.jpg	원래 이름 앞에 uuid로 랜덤으로 0 파0일명을 추가
			uploadfileName = uuid.toString() + "_" + uploadfileName;
			//----------------파일명 중복 방지 끝----------------
			
			//계획을 세움
			//c:\\upload\\ERAASER_개똥이.jpg
			File saveFile = new File(uploadPath, uploadfileName);
			
			try {
				//계획 실행. 파일 복사됨(클라이언트 파일을 서버의 공간으로 복사)
				multipartFile.transferTo(saveFile);
				
				// 이미지인지 체킹
				try {
		            String contentType = Files.probeContentType(saveFile.toPath());
		            log.info("contentType : "+contentType);
		            if(contentType.startsWith("image")) {
		               FileOutputStream thumbnail = new FileOutputStream(
		                     new File(uploadPath,"s_" + uploadfileName)
		               );
		               //섬네일 생성
		               Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail,100,100);
		               thumbnail.close();
			        }
				} catch (IOException e) {
					e.printStackTrace();
				}	
			} catch (IllegalStateException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		
		//redirect
		return "redirect:/lprod/uploadForm";
	}
	
	
	//연/월/일 폴더 생성
	public static String getFolder() {
		//2023-01-27 형식(format) 지정
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//날짜 객체 생성(java.util 패키지)
		Date date = new Date();
		//2023-01-27	=> 2023File.separator01File.separator27
							//윈도우에서 File.separator는 '\\'임
		String str = sdf.format(date);
		//단순 날짜 문자를 File 객체의 폴더 타입으로 바꾸기
		//c:\\upload\\2023\\01\\27
		return str.replace("-", File.separator);
	}
	
	//* 썸네일 처리 전 이미지 파일을 판단하는 메소드
	//용량이 큰 파일을 썸네일 처리를 하지 않으면
	//모바일과 같은 환경에서 많은 데이터를 소비해야 하므로
	//이미지의 경우 특별한 경우가 아니면 썸네일을 제작해야 함
	//썸네일은 이미지만 가능함
	public static boolean checkImageType(File file) {
		/* ex)
		 .jpeg / .jpg(JPEG이미지)의 MIME 타입 : image/jpeg
		 */
		//MIME 타입을 통해 이미지 여부 확인
		try {
			//file.toPath() : 파일 객체를 path객체로 변환
			String contentType = Files.probeContentType(file.toPath());
			log.info("contentType : " + contentType);
			//MIME 타입 정보가 imamge로 시작하는지 여부를 return
			return contentType.startsWith("image"); //image로 시작되면 참/ 아니면 거짓
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		//이 파일이 이미지가 아닐 경우
		return false; //catch로 빠지는거 방지
	}
	
	//파일 다운로드
	
	
}






















