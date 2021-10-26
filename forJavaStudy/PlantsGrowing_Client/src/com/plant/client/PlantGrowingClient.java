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
	static PlantsGrowingVo pgVo = new PlantsGrowingVo("������"); // �̸� ���� �ʿ��մϴ�. ù ȭ�鿡�� ��ü �����ϰ� �� ��ü�� ������ �� �ְ� ��Ź�����
	
	// ù �����̶�� �̸� ���� �� , �ƴ϶�� �÷�Ʈ�׷������� ������ �� �ٷ� ������ ������ ���ּ���.
	public void serverOn() {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(ip,port);
					System.out.println("[���� ����]");
					receiveData();
				} catch (Exception e) {
					if(!socket.isClosed()) {
						try {
							socket.close();
							System.out.println("[�������� ����]");
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
	
	// �����κ��� plantsGrowing ��ü�� �޾ƿɴϴ�.
	// PlantsGrowingVo Ȯ���غ��ð� �𸣴� �κ� ������ ���� �ּ���
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
	
	
	// ��ü�� ������ �����ϴ�.
	// ���� ��Ʈ�ѷ��ܿ��� pgVo �� ������ set�ϰ�
	// set �� ������ ������ ������ db�� �����ϰ� db ���� ���� �����´ٰ� �����Ͻø� �˴ϴ�.
	public void sending(PlantsGrowingVo pgVo) {
		Thread thread = new Thread() {
			public void run() {
				try {
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				
					objectOutputStream.writeObject(pgVo); // ������ ����ȭ
		            objectOutputStream.flush(); // ����ȭ�� ������ ����

					
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