package com.jaehun.main.controller;

import java.util.Scanner;

import com.jaehun.QnA.controller.QnAController;
import com.jaehun.board.controller.BoardController;


public class MainController {
	
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
	
		//  환영인사
		System.out.println("====================================");
		System.out.println("안녕하세요, 최재훈의 게시판입니다.");
		System.out.println("====================================");
		
		// main메뉴 무한루프
		mainloop : while(true) {
			// 위치 출력
			System.out.println("====================================");
			System.out.println("현재 위치 : main");
			System.out.println("====================================");
			// 메뉴 출력
			System.out.println("====================================");
			System.out.println("1. 게시판   2. 질의응답   0. 나가기");
			System.out.println("====================================");
			
			// 메뉴 입력 메시지
			System.out.print("접속하실 메뉴를 선택해주세요 (1~2), 0 : ");
			int menu = sc.nextInt();
			
			// 메뉴 처리
			switch (menu) {
			case 1:
				System.out.println("게시판입니다=======================");
				// BoardController를  실행메소드로 불러오기.
				execute(new BoardController());
				
				break;
				
			case 2:
				System.out.println("질의응답입니다=======================");
				execute(new QnAController());
				
				break;
	
			case 0:
				System.out.println("종료합니다.");
				break mainloop;
				
			default : 
				System.out.println("잘못된 메뉴를 입력하셨습니다. 다시 입력해주세요.");
				break;
			} //end of switch
		} // end of while(true) 
	} // end of main()
	
	//CONTROLLER 실행 메소드
	private static void execute(Controller controller) {
		controller.execute();
	}
	

	

}


