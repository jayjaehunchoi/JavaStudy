package com.plant.server;

import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

// ����
public class PlantsGrowingServer extends Application {
	
	// threadPool�� �����ϰ� �Ǹ� �۾�ť���� �����带 ó���ϰ� �ٽ� ���ο� �����带 ó���Ͽ� ���ø����̼� ���� ������ ���� ��.
	public static ExecutorService threadPool;
	public static Vector<PGServerClient> clients = new Vector<>();
	
	private final String IP = "127.0.0.1";
	private final int port = 9878;
	
	ServerSocket dataServerSocket; // ������ ���۹��� ���� 9878
	Socket dataSocket;

	ObjectInputStream objectInputStream;
	PrintWriter printWriter;
	
	// ��������, socketServer�� ����ȣ��Ʈ�� �����ְ�, Ŭ���̾�Ʈ(����)�� �޾� ���Ϳ� �����Ѵ�.
	// �ش� ������ ������� ���� ������ Ǯ���� �������ش�.
	public void startServer() {
		
		try {
			dataServerSocket = new ServerSocket();
			dataServerSocket.bind(new InetSocketAddress(IP, port));	
		}catch(Exception e) {
			e.printStackTrace();
			if(!dataServerSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		// thread�� �����ϴ� Ŭ���̾�Ʈ ����
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						dataSocket = dataServerSocket.accept();
						
						clients.add(new PGServerClient(dataSocket));
						
						System.out.println("[Ŭ���̾�Ʈ ����]" + Thread.currentThread());
						
					}catch(Exception e) {
						if(!dataServerSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
			
		};
		// ���� ��� ������ ���� ,������ �ʴ� ������� ����� ���� ���� ����
		// �ܱ��� �񵿱� �½�ũ ó�� �� ���� ���ȴٰ� ��
		threadPool = Executors.newCachedThreadPool(); 
		threadPool.submit(thread); //���ܰ� �߻��ص� ��� ����
	}
	
	// �������� �޼���
	// ������ ������ ��, ���͸���Ʈ�� ���� Ŭ���̾�Ʈ�� �ִ��� Ȯ���ϰ�
	// Ŭ���̾�Ʈ�� �����ִٸ� ��ȸ�� ���� Ŭ���̾�Ʈ ������ ��� �ݾ��ش�.
	// ���� ������ ������Ǯ ���ᵵ �����Ѵ�.
	public void stopServer() {
		try {
			Iterator<PGServerClient> ir = clients.iterator();
			
			while(ir.hasNext()) {
				PGServerClient client = ir.next();
				client.dataSocket.close();
				ir.remove();
			}
			//������ ����ȭ ���� ��ü �ݱ�
			if(dataServerSocket != null && !dataServerSocket.isClosed()) {
				dataServerSocket.close();
			}

			//������ Ǯ ����
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	// ���� GUI
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("�������", 15));
		root.setCenter(textArea);
		
		Button toggleButton = new Button("�����ϱ�");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1,0,0,0));
		root.setBottom(toggleButton);

		toggleButton.setOnAction(event -> {
			if(toggleButton.getText().equals("�����ϱ�")) {
				startServer();
			Platform.runLater(() -> {
				String message = String.format("[��������] %3s / %3s\n", IP, port);
				textArea.appendText(message);
				toggleButton.setText("�����ϱ�");
			});
		} else {
			stopServer();
			Platform.runLater(() -> {
				String message = String.format("[��������] %3s / %3s \n", IP, port);
				textArea.appendText(message);
				toggleButton.setText("�����ϱ�");
			});
		}
			
		});
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[ Plant Growing ����  ]");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


