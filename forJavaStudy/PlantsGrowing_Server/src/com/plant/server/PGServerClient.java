package com.plant.server;

import java.io.*;
import java.net.Socket;

import com.plant.biz.PlantsGrowingBiz;
import com.plant.vo.PlantsGrowingStatus;
import com.plant.vo.PlantsGrowingVo;

// 서버 클라이언트
public class PGServerClient {
	
	Socket dataSocket;
	PrintWriter printWriter;
	ObjectInputStream objectInputStream;
	PlantsGrowingBiz plantsGrowingBiz = new PlantsGrowingBiz();
	
	//생성자를 통해 socket 만들어주고 receive 실행
	public PGServerClient(Socket dataSocket) {
		this.dataSocket = dataSocket;
		System.out.println(dataSocket);
		receiveData();
	}
	
	
	 //클라이언트로부터 확성기를 전달받는 메서드
	 //runnable 객체를 생성하여 항상 input을 받고 메세지를 작성하여 output하는 (벡터 내 모든 클라이언트의)소켓으로 보내준다.
	 //runnable 객체를 Main의 threadpool에서 관리해준다.
	public void receiveData() {
		
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				
				try {
					while(true) {
						printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream())));
						objectInputStream = new ObjectInputStream(dataSocket.getInputStream());
						System.out.println(objectInputStream.toString());
						PlantsGrowingVo pgVo = (PlantsGrowingVo) objectInputStream.readObject();
						
						System.out.println("데이터 저장 전"+pgVo.toString());
						
						if(plantsGrowingBiz.check_id(pgVo) == true) {
							if(pgVo.getpStatus() == PlantsGrowingStatus.FIRSTACCES) {
								plantsGrowingBiz.load_info(pgVo);
								pgVo.setpStatus(PlantsGrowingStatus.KEEPCONNECTED);
							}else {
								plantsGrowingBiz.update_info(pgVo);
							}
							
						}else {
							plantsGrowingBiz.insert_plant_name(pgVo);
							pgVo.setpStatus(PlantsGrowingStatus.KEEPCONNECTED);
						}
						System.out.println("데이터 저장 후"+pgVo.toString());
						send(pgVo);
						//plantsGrowingBiz.update_plant_name(pgVo);
					}
				} catch (Exception e) {
					try {
						
						System.out.println("[데이터 수신 오류]"
								+ dataSocket.getRemoteSocketAddress() + " : "
								+ Thread.currentThread().getName());
						
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			
		};
		PlantsGrowingServer.threadPool.submit(thread);
	}
	
	// 클라이언트에게 확성기를 전달하는 메서드
	// runnable 객체를 생성하여 output을 받고 메세지를 작성하여 클라이언트에게 input시킬 준비를한다.
	// runnable 객체를 Main의 threadpool에서 관리해준다.
	public void send(PlantsGrowingVo pgVo) {
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				try {
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(dataSocket.getOutputStream());
					objectOutputStream.writeObject(pgVo); // 데이터 직렬화
		            objectOutputStream.flush(); // 직렬화된 데이터 전달
					
				} catch (Exception e) {
					try {
						System.out.println("[확성기 송신 오류]" 
								+ dataSocket.getRemoteSocketAddress() + " : " 
								+ Thread.currentThread().getName());
						
						PlantsGrowingServer.clients.remove(PGServerClient.this);
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
				
			}
		};
		PlantsGrowingServer.threadPool.submit(thread);
	}
	
	
	
}
