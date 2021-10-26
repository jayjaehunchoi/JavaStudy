package com.plant.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.plant.vo.PlantsGrowingVo;

import javafx.application.Platform;

public class PlantGrowingClient {
	PrintWriter printWriter;
	ObjectInputStream objectInputStream;
	static Socket socket;
	private final String ip = "127.0.0.1";
	private final int port = 9878; 
	static PlantsGrowingVo pgVo = new PlantsGrowingVo("최재훈"); // 이름 수정 필요합니다. 첫 화면에서 객체 생성하고 그 객체로 유지될 수 있게 부탁드려요
	
	// 첫 접속이라면 이름 설정 후 , 아니라면 플랜트그로잉으로 접속할 때 바로 서버가 켜지게 해주세요.
	public void serverOn() {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(ip,port);
					System.out.println("[소켓 연결]");
					receiveData();
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
	
	// 서버로부터 plantsGrowing 객체를 받아옵니다.
	// PlantsGrowingVo 확인해보시고 모르는 부분 있으면 질문 주세요
	public void receiveData() {
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
	
	
	// 객체를 서버에 보냅니다.
	// 먼저 컨트롤러단에서 pgVo 의 정보를 set하고
	// set 한 정보를 서버로 보내서 db에 연결하고 db 값을 도로 가져온다고 생각하시면 됩니다.
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
}