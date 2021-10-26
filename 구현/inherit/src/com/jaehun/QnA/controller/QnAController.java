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
				System.out.println("���� ��ġ : main > ��������");
				System.out.println("====================================");
				
				System.out.println("====================================");
				System.out.println("1. �����ϱ�   2. �亯�ϱ�  3. �亯���� 0. �����޴� ");
				System.out.println("====================================");
				
				System.out.print("�����Ͻ� �޴��� �������ּ��� (1~3), 0 : ");
				int menu = sc.nextInt();
				
				switch (menu) {
				case 1:
					System.out.println("====================================");
					System.out.print("�����ϱ� �Դϴ�. ������ �Է��ϼ���.");
					
					sc.nextLine();
					String q = sc.nextLine();
					
					qList.add(q);
					qnAMap.put(q, "");
					
					System.out.print("������ �Ϸ�Ǿ����ϴ�!");
					break;
					
				case 2:
					System.out.println("====================================");
					if(qList.size() == 0) {
						System.out.println("������ �����ϴ�.");
						break;
					}
					
					System.out.println("������ �亯�ϱ��Դϴ�. �亯 �� ������ ��ȣ�� �����ϼ���.");				
					
					for(int i = 1 ; i <= qList.size(); i++) {
						System.out.print(i + ". " + qList.get(i-1) +" / ");
					}
					
					System.out.println();
					int n = sc.nextInt();
					System.out.print(" �亯�� �Է��ϼ���. : ");
					sc.nextLine();
					String a = sc.nextLine();
					
					qnAMap.replace(qList.get(n-1), a);
					System.out.println(" �亯�� �Ϸ�Ǿ����ϴ�. ");
					
					qListCompleted.add(qList.get(n-1));
					qList.remove(n-1);
					
					for(int i = 1 ; i <= qListCompleted.size(); i++) {
						System.out.print("�亯 �Ϸ� : " + i + ". " +qListCompleted.get(i-1)+ " / ");
					}

					break;
					
				case 3:
					System.out.println("====================================");
					if(qListCompleted.size() == 0) {
						System.out.println("��ϵ� �亯�� �����ϴ�.");
						break;
					}
					
					System.out.println("�亯�����Դϴ�. ������� ������ ��ȣ�� �����ϼ���.");
					
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
				System.out.println("<<<<<<<<�Խ��� ó�� �� ������ �߻��Ǿ����ϴ�. �ٽ� �õ����ּ���.>>>>>>>");
			}
			
		}
		
	}

}
