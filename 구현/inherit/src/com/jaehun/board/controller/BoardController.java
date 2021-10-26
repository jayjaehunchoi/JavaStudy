package com.jaehun.board.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.jaehun.main.controller.Controller;

public class BoardController implements Controller {
	Scanner sc = new Scanner(System.in);
	ArrayList<String> listPerPage = new ArrayList<String>();
	HashMap<String, String> titleContent= new HashMap<String, String>(); 
	
	
	//인터페이스에서 상속받은 메소드를 재정의하여 사용
	@Override
	public void execute() {
		
		while (true) {
			
			try {
				
			
				System.out.println("====================================");
				System.out.println("현재 위치 : main > 게시판");
				System.out.println("====================================");
				
				System.out.println("====================================");
				System.out.println("1. 리스트   2. 글보기  3. 글쓰기 ");
				System.out.println("4. 글수정   5. 글삭제  0. 이전 메뉴 ");
				System.out.println("====================================");
				
				System.out.print("접속하실 메뉴를 선택해주세요 (1~5), 0 : ");
				int menu = sc.nextInt();
				
				switch(menu) {
				
				case 1 : 
					System.out.println("게시판 리스트");
					if(listPerPage.size() == 0) {
						System.out.println("게시된 글이 없습니다.");
						break;
					}
					
					for(int i = 1 ; i <= listPerPage.size(); i++) {
						System.out.println(i + ". " + listPerPage.get(i-1) );
					}
					
					break;
				
				
				case 2 : 
					System.out.println("보고싶은 글의 번호를 입력해주세요: ");
					
					for(int i = 1 ; i <= listPerPage.size(); i++) {
						System.out.println(i + ". " + listPerPage.get(i-1) );
					}
					
					int n = sc.nextInt();
					BufferedReader br = new BufferedReader(new FileReader(titleContent.get(listPerPage.get(n-1))));
					String lines;
					try {
						while((lines = br.readLine())!=null) {
							System.out.println(lines);
						}
					} 
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					

					break;
				
			
				case 3 : 
					System.out.print("글의 제목을 작성해주세요.");
					sc.nextLine();
					String title = sc.nextLine();
					System.out.print("글의 내용을 작성해주세요.");
					
					try {
						
						String filePath = "C:\\Users\\JaehunChoi\\Desktop\\2021\\Developer\\project0731\\"+ title + ".txt";
						FileWriter fileWriter = new FileWriter(filePath,true);
						for(int i = 0 ; i < 5 ; i++){
							
							String content = sc.nextLine();
							fileWriter.write(content + "\r\n");
						}
						fileWriter.close();
						titleContent.put(title, filePath);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					listPerPage.add(title);
					System.out.println(title + " 작성 완료");
					
					break;
				
				case 4 : 
					System.out.print("수정하실 글을 선택해주세요 : ");
					
					for(int i = 1 ; i <= listPerPage.size(); i++) {
						System.out.println(i + ". " + listPerPage.get(i-1) );
					}
					
					int titleEdit = sc.nextInt();
					String titleToEdit = listPerPage.get(titleEdit-1);
							
					
					System.out.print("글의 내용을 작성해주세요.");
					
					try {
						
						String filePath = "C:\\Users\\JaehunChoi\\Desktop\\2021\\Developer\\project0731\\"+ titleToEdit + ".txt";
						FileWriter fileWriter = new FileWriter(filePath,true);
						
						for(int i = 0 ; i < 5 ; i++){
							
							String content = sc.nextLine();
							fileWriter.write(content + "\r\n");
						}
						fileWriter.close();
						titleContent.replace(titleToEdit, filePath);
						
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(titleToEdit + "수정완료");
					break;
					
				case 5 : 
					System.out.print("삭제하실 글을 선택해주세요 : ");
					
					for(int i = 1 ; i <= listPerPage.size(); i++) {
						System.out.println(i + ". " + listPerPage.get(i-1) );
					}
					
					int titleDelete = sc.nextInt();
					String titleToDelete = listPerPage.get(titleDelete-1);
					
					String filePath = "C:\\Users\\JaehunChoi\\Desktop\\2021\\Developer\\project0731\\"+ titleToDelete + ".txt";
					File file = new File(filePath);
					if(file.exists()) {
						file.delete();
						System.out.println(titleToDelete + " 삭제 완료");
					}
					listPerPage.remove(titleDelete-1);
					
					break;
				
				case 0 : 
					return;
				default : 
					System.out.println("잘못된 입력입니다.");
					break;
				}
			}
			
			catch(Exception e){
				e.printStackTrace();
				System.out.println("<<<<<<<<게시판 처리 중 오류가 발생되었습니다. 다시 시도해주세요.>>>>>>>");
			}
			
		}
	}

}
