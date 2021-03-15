package board.controller;

import java.util.List;

// Logback 사용하더라도 slf4j 패키지의 의존성을 활용하여 logger를 사용함
// 다른 로깅 시스템을 사용하더라도 쉽게 변경이 가능
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import board.dto.BoardDto;
import board.service.BoardService;

//logback 사용하기
//기존 자바의 로깅 시스템인 log4j 라는 프로젝트가 종료된 후 출범된 로그를 위한 프로젝트

//로그란? 각종 시스템이 작동하는 내역을 남기는 것을 로그라고 함




@Controller
public class BoardController {
//	getLogger() 메서드의 매개변수는 로거의 이름임
//	getLogger("name"); 으로 생성하면 name 이라는 이름을 가진 로거 객체가 생성됨
//	일반적으로 클래스 객체를 사용함/ 패키지 이름
	private Logger log = LoggerFactory.getLogger(this.getClass());
//	@Autowired : 객체 자동 생성 어노테이션
	@Autowired
	private BoardService boardService;

//	게시판의 전체 목록을 불러오는 페이지
//	@RequestMapping : 사용자가 접속하는 주소와 Controller의 메서드와 연동시키는 어노테이션
	@RequestMapping("/board/openBoardList.do")
	public ModelAndView openBoardList() throws Exception {
//		사용자 화면과 데이터 베이스 정보를 가지고 있는 클래스
		ModelAndView mv = new ModelAndView("/board/boardList");
		
		log.debug("openBoardList");
		List<BoardDto> list = boardService.selectBoardList();
		mv.addObject("data", list);
		
		return mv;
	}
	
//	@RequestParam : 사용자가 입력한 파라미터값 받아오기, jsp의 getParameter()와 같음
	@RequestMapping("/board/openBoardDetail.do")
	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception {
		ModelAndView mv = new ModelAndView("/board/boardDetail");
		
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);
		
		return mv;
	}
	
//	게시글 작성 페이지 호출
//	스프링부트에서는 사용자 주소 입력을 무조건 controller가 담당하기 때문에 해당 주소와 연동되는 메서드가 반드시 필요함
	@RequestMapping("/board/openBoardWrite.do")
	public String openBoardWrite() throws Exception{
//		해당 메서드는 단순 웹페이지 출력 부분이므로 데이터를 클라이언트에 전송할 필요가 없으므로 웹페이지 이름만 입력하면 스프링부트가 자동으로 해당 html파일을 찾아서 화면에 출력함
		return "/board/boardWrite";
	}
//	새 게시글 작성하기
//	insertBoard 메서드의 매개변수로 BoardDto 클래스 타입의 객체 board를 사용함으로 입력데이터를 담당하는 boardWrite.html에서 input태그의 name속성을 전부 BoardDto클래스의 멤버 변수명으로 지정해야함
	@RequestMapping("/board/insertBoard.do")
	public String insertBoard(BoardDto board) throws Exception{
//		해당 메서드가 실행 시 boardService의 insertBoard 메서드를 실행하여 데이터 베이스에 접근
//		insert, update, delete 문은 반환값이 없어도 상관 없기 때문에 sql 쿼리 결과를 받아오는 부분이 없어도됨
		boardService.insertBoard(board);
//		redirect 를 통해서 지정한 주소로 화면 전환
		return "redirect:/board/openBoardList.do";
	}
	@RequestMapping("/board/updateBoard.do")
	public String updateBoard(BoardDto board) throws Exception{
		boardService.updateBoard(board);
//		Service를 이용하여 데이터베이스 접근해서 수정
		return "redirect:/board/openBoardList.do";
	}
	@RequestMapping("/board/deleteBoard.do")
	public String deleteBoard(int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board/openBoardList.do";
//		Service를 이용하여 데이터베이스 접근해서 삭제
	}
}











