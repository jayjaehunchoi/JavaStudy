package com.plant.vo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Platform;

public class test {
	PrintWriter printWriter;
	ObjectInputStream objectInputStream;
	static Socket socket;
	private final String ip = "127.0.0.1";
	private final int port = 9878; 
	static PlantsGrowingVo pgVo = new PlantsGrowingVo("최재훈"); // 이름 수정 필요합니다. 첫 화면에서 객체 생성하고 그 객체로 유지될 수 있게 부탁드려요
	
	
	
	public void serverOn() {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(ip,port);
					System.out.println("[소켓 연결]");
					receive();
				} catch (Exception e) {
					if(!socket.isClosed()) {
						try {
							socket.close();
							System.out.println("[서버접속 실패]");
							Platform.exit();
						} catch (IOException e1) {
						
							e1.printStackTrace();
						}
					}
				}
			}
		};
		thread.start();
	}
	public void receive() {
		while(true) {
			try {
				printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				
				pgVo = (PlantsGrowingVo) objectInputStream.readObject();
				System.out.println(pgVo.toString());
			}catch (Exception e) {
				break;
			}
		}
		
	}
	
	public void sending(PlantsGrowingVo pgVo) {
		Thread thread = new Thread() {
			public void run() {
				try {
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				
					objectOutputStream.writeObject(pgVo); // 데이터 직렬화
		            objectOutputStream.flush(); // 직렬화된 데이터 전달

					
				}catch (Exception e) {
					
					if(socket != null && !socket.isClosed()) {
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}	
			}
		};
		thread.start();
	}
	
	public static void main(String[] args) throws InterruptedException {
		test t =new test();
		int n = 4;
		
		Scanner sc = new Scanner(System.in);
		
		while(n-- >0) {
			String d = sc.next();
			pgVo.setPlants_name(d);
			System.out.println("데이터 저장 전"+pgVo.toString());
			t.serverOn();
			Thread.sleep(100);
			t.sending(pgVo);
		}
		
	}
		
		

}
