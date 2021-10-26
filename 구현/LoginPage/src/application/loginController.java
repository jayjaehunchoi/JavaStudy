package application;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.db.DBConnect;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class loginController {
	
	DBConnect connect = new DBConnect();
	private Statement st; // Ư�� ������ ���̽��� sql ���� ����
	private ResultSet rs; // ���� ����� �޾ƿ�
	private Connection conn; // ������ �����ϴ� ��ü
	
	@FXML
	private Button btn;
	
	@FXML
	private PasswordField enterPasswordField;
	
	@FXML
	private TextField userNameTextField;
	
	@FXML
	private TextField tfd_create_ID;
	@FXML
	private PasswordField tfd_createPw;
	@FXML
	private PasswordField tfd_createPw2;
	
	// �α��� ��� (DB�� ȸ������ ������ ������ �� ����)
	public void Login(ActionEvent ae) throws IOException {
		
		
		if(connect.check(userNameTextField.getText(), enterPasswordField.getText())) {
			
			closeStage();			
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("design1.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		}
	}
		
	// ȸ�� ���� ��� 
	public void createNewId(ActionEvent ae) throws IOException{
		
		closeStage();
		Stage primaryStage = new Stage();	
		Parent root = FXMLLoader.load(getClass().getResource("CreateId.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public void createDone(ActionEvent ae) throws IOException {
		
		if(tfd_createPw.getText().equals(tfd_createPw2.getText())) {
			
		 try {
			 members_insert(tfd_create_ID.getText(), tfd_createPw.getText());
		 }catch (Exception e) { 
			 System.out.println("�����?"); 
		 }
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ȸ������ �Ϸ�");
		alert.setContentText("�����մϴ� ȸ������ �Ϸ�Ǿ����ϴ�");
		alert.showAndWait();
			
		closeStage();
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("��й�ȣ Ȯ��");
			alert.setContentText("��й�ȣ�� ��й�ȣ Ȯ���� ���� �ʽ��ϴ�.");
			alert.showAndWait();
		}
	}
	
	// ��� ���� sql testmem ���̺� �ֱ�
	public void members_insert(String id, String password) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = connect.connect();
			String sql = "INSERT INTO testmem VALUES(?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			System.out.println("����");
			e.printStackTrace();
		}finally {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.close();
				}
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				System.out.println("����");
				e.printStackTrace();
			}
		}
	}
	
	// ���̵� �ߺ� Ȯ�� ��ư
	public void checkID(ActionEvent ae) throws IOException {
		String id = tfd_create_ID.getText();

		if(id.equals(checkIdList(id))) {
			Alert checkIdErr = new Alert(AlertType.ERROR);
			checkIdErr.setTitle("���̵� Ȯ��");
			checkIdErr.setContentText("�ߺ��� ���̵��Դϴ�.");
			checkIdErr.showAndWait();
		}
		else {
			Alert checkIdErr = new Alert(AlertType.CONFIRMATION);
			checkIdErr.setTitle("���̵� Ȯ��");
			checkIdErr.setContentText("ȸ�������� �����մϴ�.");
			checkIdErr.showAndWait();
		}
		

	}
	
	
	// database ���� ������ ���̵� �ִ��� Ȯ���ϴ� ����
	public String checkIdList(String id) {
		
		String idInList = "";
		
		try {
			conn = connect.connect();
			st = conn.createStatement();
			
			String SQL = "SELECT * FROM testmem WHERE id = '"+id+"'";
			rs = st.executeQuery(SQL);
			
			while(rs.next()) {
				idInList = rs.getString(1);
						
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.close();
				}
				if(st != null && !st.isClosed()) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idInList;
	}
	
	// ������ �ݱ� (���� Stage�� �Ѿ�� close�ϴ� �ڵ� ����ȭ)
	public void closeStage() {
		Stage stage = (Stage) btn.getScene().getWindow();
		Platform.runLater(() -> {stage.close();});
	}

}

