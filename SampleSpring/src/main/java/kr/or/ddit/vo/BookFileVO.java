package kr.or.ddit.vo;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BookFileVO {
	private MultipartFile item;
	private int bookId;
	private int fileNo;
	private String fileName;
	private long fileSize;
	private String fileFancysize;
	private String fileMime;
	private String fileSavepath;
	private int fileDowncount;
	
	public BookFileVO() {}
	
	public BookFileVO(MultipartFile item) {
		//파일이 넘어오면 자동으로 바인딩되게(setting) 직접 셋팅할 필요 없음
		this.item = item;
		fileName = item.getOriginalFilename();
		fileSize = item.getSize();
		fileMime = item.getContentType();
		fileFancysize = FileUtils.byteCountToDisplaySize(fileSize);
	}
	
	
	
}
