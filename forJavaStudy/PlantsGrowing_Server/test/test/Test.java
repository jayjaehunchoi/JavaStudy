package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.plant.biz.PlantsGrowingBiz;
import com.plant.dao.implement.PlantsGrowingDaoImple;
import com.plant.vo.PlantsGrowingVo;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

public class Test {
	static Socket socket;
	static Socket socket2;
	public static void serverOn() {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket("127.0.0.1",9879);
					socket2 = new Socket("127.0.0.1",9878);
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
	public static void receive() {
		while(true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[1024];
				int length = in.read(buffer);
				while(length == -1) throw new IOException();
				String message = new String(buffer, 0 , length, "UTF-8");			
				System.out.println("[클라이언트 메시지 수신 성공] : " +  message);
			
			}catch (Exception e) {
				break;
			}
		}
		
	}
	
	public static void sending(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
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
		PlantsGrowingBiz p = new PlantsGrowingBiz();
		PlantsGrowingVo pgVo = new PlantsGrowingVo("123");
		pgVo.setPlants_name("super");
		p.load_info(pgVo);
		p.update_plant_name(pgVo);

	}
}
