package com.jaehun.main.controller;

import java.util.Scanner;

import com.jaehun.QnA.controller.QnAController;
import com.jaehun.board.controller.BoardController;


public class MainController {
	
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
	
		//  ȯ���λ�
		System.out.println("====================================");
		System.out.println("�ȳ��ϼ���, �������� �Խ����Դϴ�.");
		System.out.println("====================================");
		
		// main�޴� ���ѷ���
		mainloop : while(true) {
			// ��ġ ���
			System.out.println("====================================");
			System.out.println("���� ��ġ : main");
			System.out.println("====================================");
			// �޴� ���
			System.out.println("====================================");
			System.out.println("1. �Խ���   2. ��������   0. ������");
			System.out.println("====================================");
			
			// �޴� �Է� �޽���
			System.out.print("�����Ͻ� �޴��� �������ּ��� (1~2), 0 : ");
			int menu = sc.nextInt();
			
			// �޴� ó��
			switch (menu) {
			case 1:
				System.out.println("�Խ����Դϴ�=======================");
				// BoardController��  ����޼ҵ�� �ҷ�����.
				execute(new BoardController());
				
				break;
				
			case 2:
				System.out.println("���������Դϴ�=======================");
				execute(new QnAController());
				
				break;
	
			case 0:
				System.out.println("�����մϴ�.");
				break mainloop;
				
			default : 
				System.out.println("�߸��� �޴��� �Է��ϼ̽��ϴ�. �ٽ� �Է����ּ���.");
				break;
			} //end of switch
		} // end of while(true) 
	} // end of main()
	
	//CONTROLLER ���� �޼ҵ�
	private static void execute(Controller controller) {
		controller.execute();
	}
	

	

}


