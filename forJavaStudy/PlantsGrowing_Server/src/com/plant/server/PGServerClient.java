package com.plant.server;

import java.io.*;
import java.net.Socket;

import com.plant.biz.PlantsGrowingBiz;
import com.plant.vo.PlantsGrowingStatus;
import com.plant.vo.PlantsGrowingVo;

// ���� Ŭ���̾�Ʈ
public class PGServerClient {
	
	Socket dataSocket;
	PrintWriter printWriter;
	ObjectInputStream objectInputStream;
	PlantsGrowingBiz plantsGrowingBiz = new PlantsGrowingBiz();
	
	//�����ڸ� ���� socket ������ְ� receive ����
	public PGServerClient(Socket dataSocket) {
		this.dataSocket = dataSocket;
		System.out.println(dataSocket);
		receiveData();
	}
	
	
	 //Ŭ���̾�Ʈ�κ��� Ȯ���⸦ ���޹޴� �޼���
	 //runnable ��ü�� �����Ͽ� �׻� input�� �ް� �޼����� �ۼ��Ͽ� output�ϴ� (���� �� ��� Ŭ���̾�Ʈ��)�������� �����ش�.
	 //runnable ��ü�� Main�� threadpool���� �������ش�.
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
						
						System.out.println("������ ���� ��"+pgVo.toString());
						
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
						System.out.println("������ ���� ��"+pgVo.toString());
						send(pgVo);
						//plantsGrowingBiz.update_plant_name(pgVo);
					}
				} catch (Exception e) {
					try {
						
						System.out.println("[������ ���� ����]"
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
	
	// Ŭ���̾�Ʈ���� Ȯ���⸦ �����ϴ� �޼���
	// runnable ��ü�� �����Ͽ� output�� �ް� �޼����� �ۼ��Ͽ� Ŭ���̾�Ʈ���� input��ų �غ��Ѵ�.
	// runnable ��ü�� Main�� threadpool���� �������ش�.
	public void send(PlantsGrowingVo pgVo) {
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				try {
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(dataSocket.getOutputStream());
					objectOutputStream.writeObject(pgVo); // ������ ����ȭ
		            objectOutputStream.flush(); // ����ȭ�� ������ ����
					
				} catch (Exception e) {
					try {
						System.out.println("[Ȯ���� �۽� ����]" 
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
