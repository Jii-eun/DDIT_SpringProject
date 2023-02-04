package kr.or.ddit.util;

import java.util.List;

import kr.or.ddit.vo.LprodVO;

//페이징 관련 정보 + 게시글 데이터
public class ArticlePage<T> {		//<T> = 제네릭. 어떤 VO가 와도 OK

	private int total;				//전체글 수
	private int currentPage;		//현재 페이지 번호
	private int totalPages;			//전체 페이지 수
	private int startPage;			//시작 페이지 번호
	private int endPage;			//종료 페이지 번호
	private int blockSize;			//블록 크기
	private List<T> content;	//글 데이터

	//생성자(Constructor) :페이징 정보를 생성 및 초기화 ---------> 컨트롤러에서 사용
	//size : 한 화면에 보여질 행의 수
	public ArticlePage(int total, int currentPage, int size, List<T> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		
		/* 
		 	* 현재 페이지를 통해 현재 보이는 블록의 시작 페이지, 끝 페이지 번호를 알 수 있음
		*/
		
		if(total==0) {	//전체 글 수가 0이면
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {		//전체 글 수가 1 이상일 때
			//1) ------전체 페이지 구하기 시작------
			//전체 페이지 수 = 전체글 수 / 한 화면에 보여질 목록의 행 수
			totalPages = total / size;
			
			//전체 글 수 % 한 화면에 보여질 목록의 행 수 => 0이 아니면(나머지가 있다면)? 페이지를 1 증가
			if(total % size > 0) {
				totalPages++;
			}
			//1) ------전체 페이지 구하기 끝------
			
			//2) ------시작 페이지 구하기 시작------
			//시작페이지 = 현재페이지 / 페이지크기 * 페이지 크기 + 1		(페이지크기 = 블록 수)
			//1set : 	[1][2][3][4][5]  다음
			//2set : 이전 [6][7][8][9][10] 다음
			//3set : 이전[11][12][13]
			startPage = currentPage / 5 * 5 + 1;	//몫을 int로만 계산하기 (소수점 절사)
													//ex)  현재페이지 3/5 = 0 > 0*5 = 0 > 0+1 = 1
			//현재 페이지 % 페이지크기 => 0일 때 보정
			if(currentPage % 5 == 0) {
				//페이지크기를 빼줌
				startPage -= 5;
			}
			//2) ------시작 페이지 구하기 끝------
			
			//3) ------종료 페이지 구하기 시작------
			//종료페이지번호 = 시작페이지번호 + (페이지크기 -1)
			endPage = startPage + (5 - 1);

			//종료페이지번호 > 전체페이지수 일 때  endPage가 totalPage를 넘어설때 (클 때)
			if(endPage > totalPages) {
				endPage = totalPages;
			}
			//3) ------종료 페이지 구하기 끝------
		}	
	}	//end ArticlePage 생성자

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	
	//데이터가 없나유? 전체 글의 수는 0이유
	public boolean hasNoArticles() {
		return total == 0;
	}
	
	//데이터가 있습니까?
	public boolean hasArticles() {
		return total > 0;
	}
	
	
}































