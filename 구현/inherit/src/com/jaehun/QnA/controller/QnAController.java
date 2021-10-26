package com.jaehun.QnA.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.jaehun.main.controller.Controller;

public class QnAController implements Controller {
	Scanner sc = new Scanner(System.in);
	HashMap<String, String> qnAMap = new HashMap<String, String>();
	ArrayList<String> qList = new ArrayList<String>();
	ArrayList<String> qListCompleted = new ArrayList<String>();
	
	@Override
	public void execute() {
		
		while(true) {
			try {
				
				System.out.println("====================================");
				System.out.println("현재 위치 : main > 질의응답");
				System.out.println("====================================");
				
				System.out.println("====================================");
				System.out.println("1. 질문하기   2. 답변하기  3. 답변보기 0. 이전메뉴 ");
				System.out.println("====================================");
				
				System.out.print("접속하실 메뉴를 선택해주세요 (1~3), 0 : ");
				int menu = sc.nextInt();
				
				switch (menu) {
				case 1:
					System.out.println("====================================");
					System.out.print("질문하기 입니다. 질문을 입력하세요.");
					
					sc.nextLine();
					String q = sc.nextLine();
					
					qList.add(q);
					qnAMap.put(q, "");
					
					System.out.print("질문이 완료되었습니다!");
					break;
					
				case 2:
					System.out.println("====================================");
					if(qList.size() == 0) {
						System.out.println("질문이 없습니다.");
						break;
					}
					
					System.out.println("질문에 답변하기입니다. 답변 할 질문의 번호를 선택하세요.");				
					
					for(int i = 1 ; i <= qList.size(); i++) {
						System.out.print(i + ". " + qList.get(i-1) +" / ");
					}
					
					System.out.println();
					int n = sc.nextInt();
					System.out.print(" 답변을 입력하세요. : ");
					sc.nextLine();
					String a = sc.nextLine();
					
					qnAMap.replace(qList.get(n-1), a);
					System.out.println(" 답변이 완료되었습니다. ");
					
					qListCompleted.add(qList.get(n-1));
					qList.remove(n-1);
					
					for(int i = 1 ; i <= qListCompleted.size(); i++) {
						System.out.print("답변 완료 : " + i + ". " +qListCompleted.get(i-1)+ " / ");
					}

					break;
					
				case 3:
					System.out.println("====================================");
					if(qListCompleted.size() == 0) {
						System.out.println("등록된 답변이 없습니다.");
						break;
					}
					
					System.out.println("답변보기입니다. 보고싶은 질문의 번호를 선택하세요.");
					
					for(int i = 1 ; i <= qListCompleted.size(); i++) {
						System.out.print(i + ". " +qListCompleted.get(i-1) + " / ");
					}
					
					System.out.println();
					int ansNum = sc.nextInt();
					
					System.out.println(qnAMap.get(qListCompleted.get(ansNum-1)));
					
					break;
				
					
				case 0:
					return;
					
				default:
					break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("<<<<<<<<게시판 처리 중 오류가 발생되었습니다. 다시 시도해주세요.>>>>>>>");
			}
			
		}
		
	}

}
