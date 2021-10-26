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
	
	
	//�������̽����� ��ӹ��� �޼ҵ带 �������Ͽ� ���
	@Override
	public void execute() {
		
		while (true) {
			
			try {
				
			
				System.out.println("====================================");
				System.out.println("���� ��ġ : main > �Խ���");
				System.out.println("====================================");
				
				System.out.println("====================================");
				System.out.println("1. ����Ʈ   2. �ۺ���  3. �۾��� ");
				System.out.println("4. �ۼ���   5. �ۻ���  0. ���� �޴� ");
				System.out.println("====================================");
				
				System.out.print("�����Ͻ� �޴��� �������ּ��� (1~5), 0 : ");
				int menu = sc.nextInt();
				
				switch(menu) {
				
				case 1 : 
					System.out.println("�Խ��� ����Ʈ");
					if(listPerPage.size() == 0) {
						System.out.println("�Խõ� ���� �����ϴ�.");
						break;
					}
					
					for(int i = 1 ; i <= listPerPage.size(); i++) {
						System.out.println(i + ". " + listPerPage.get(i-1) );
					}
					
					break;
				
				
				case 2 : 
					System.out.println("������� ���� ��ȣ�� �Է����ּ���: ");
					
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
					System.out.print("���� ������ �ۼ����ּ���.");
					sc.nextLine();
					String title = sc.nextLine();
					System.out.print("���� ������ �ۼ����ּ���.");
					
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
					System.out.println(title + " �ۼ� �Ϸ�");
					
					break;
				
				case 4 : 
					System.out.print("�����Ͻ� ���� �������ּ��� : ");
					
					for(int i = 1 ; i <= listPerPage.size(); i++) {
						System.out.println(i + ". " + listPerPage.get(i-1) );
					}
					
					int titleEdit = sc.nextInt();
					String titleToEdit = listPerPage.get(titleEdit-1);
							
					
					System.out.print("���� ������ �ۼ����ּ���.");
					
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
					System.out.println(titleToEdit + "�����Ϸ�");
					break;
					
				case 5 : 
					System.out.print("�����Ͻ� ���� �������ּ��� : ");
					
					for(int i = 1 ; i <= listPerPage.size(); i++) {
						System.out.println(i + ". " + listPerPage.get(i-1) );
					}
					
					int titleDelete = sc.nextInt();
					String titleToDelete = listPerPage.get(titleDelete-1);
					
					String filePath = "C:\\Users\\JaehunChoi\\Desktop\\2021\\Developer\\project0731\\"+ titleToDelete + ".txt";
					File file = new File(filePath);
					if(file.exists()) {
						file.delete();
						System.out.println(titleToDelete + " ���� �Ϸ�");
					}
					listPerPage.remove(titleDelete-1);
					
					break;
				
				case 0 : 
					return;
				default : 
					System.out.println("�߸��� �Է��Դϴ�.");
					break;
				}
			}
			
			catch(Exception e){
				e.printStackTrace();
				System.out.println("<<<<<<<<�Խ��� ó�� �� ������ �߻��Ǿ����ϴ�. �ٽ� �õ����ּ���.>>>>>>>");
			}
			
		}
	}

}
