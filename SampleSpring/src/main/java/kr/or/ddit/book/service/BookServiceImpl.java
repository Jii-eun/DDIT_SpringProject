package kr.or.ddit.book.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import kr.or.ddit.book.ServiceResult;
import kr.or.ddit.book.dao.IBookDAO;
import kr.or.ddit.vo.BookFileVO;
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

	@Override
	public ServiceResult insertBookByFile(BookVO bookVO, HttpServletRequest request) {
		ServiceResult result = null;
		
		//↓ 얘는 dao에만 있음
		int status = bookDAO.insertBookByFile(bookVO);
		if(status > 0) {
			List<BookFileVO> bookFileList = bookVO.getBookFileList();
			try {				//파일을 셋팅하기 위함(row data)
				//processBookFile에서 파일 업로드를 하고있음
				processBookFile(bookFileList, bookVO.getBookId(), request);	
												//뒤에 두개는 경로 생성용
				//↑ 공통모듈로 가져다 쓸 것
			} catch (IOException e) {
				e.printStackTrace();
			}
			result = ServiceResult.OK;
		} else {
			result = ServiceResult.FAILED;
		}
		
		return result;
	}

	//컨트롤러에서 서비스로 넘길 때는 bookVO와 request 두개가 필요
	//어떤게시글에 어떤 파일이 있는지 검증할 때 bookId를 이용하는 편...
	// 
	private void processBookFile(List<BookFileVO> bookFileList, int bookId, HttpServletRequest request) throws IOException {
		if(bookFileList != null && bookFileList.size() > 0) {
			for (BookFileVO bookFileVO : bookFileList) {	//리스트에 들어있는 파일 꺼내서 하나씩 담음
				String saveName = UUID.randomUUID().toString();
				
				//확장자가 된다. (.jpg, .png, ...)
															//.을 기준으로 앞과 뒤가 잘림 앞은 0번째 파일명, 뒤는 1번째 확장자
				String endFilename = bookFileVO.getFileName().split("\\.")[1];
//				String saveLocate = request.getSession().getServletContext().getRealPath(path);	//아래랑 같음
				String saveLocate = request.getRealPath("/resources/upload");
				
				File file = new File(saveLocate);
				if(!file.exists()) {
					file.mkdirs();
				}
															//↓uuid를 사용하지않고 원본파일명을 넣는것 
//				saveLocate = saveLocate + "/" + bookId + "/" + bookFileVO.getFileName();	//아래와 같음
				saveLocate = saveLocate + "/" + bookId + "/" + saveName + "." + endFilename;
				File saveFile = new File(saveLocate);
				bookFileVO.setBookId(bookId);
				bookFileVO.setFileSavepath(saveLocate);
				bookDAO.insertBookFile(bookFileVO);		//==> DB에 작성
				InputStream is = bookFileVO.getItem().getInputStream();
				FileUtils.copyInputStreamToFile(is, saveFile); //(inputstream, 파일 경로)
				is.close();
			}
		}
	}

	@Override
	public BookVO selectBook2(int bookId) {
		return bookDAO.selectBook2(bookId);
	}


}






























