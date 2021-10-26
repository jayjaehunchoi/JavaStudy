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
	private Statement st; // 특정 데이터 베이스에 sql 문장 실행
	private ResultSet rs; // 실행 결과를 받아옴
	private Connection conn; // 서버와 연결하는 객체
	
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
	
	// 로그인 기능 (DB의 회원가입 정보를 가져올 수 있음)
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
		
	// 회원 가입 기능 
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
			 System.out.println("여기니?"); 
		 }
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("회원가입 완료");
		alert.setContentText("축하합니다 회원가입 완료되었습니다");
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
			alert.setHeaderText("비밀번호 확인");
			alert.setContentText("비밀번호와 비밀번호 확인이 맞지 않습니다.");
			alert.showAndWait();
		}
	}
	
	// 멤버 정보 sql testmem 테이블에 넣기
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
			System.out.println("오류");
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
				System.out.println("오류");
				e.printStackTrace();
			}
		}
	}
	
	// 아이디 중복 확인 버튼
	public void checkID(ActionEvent ae) throws IOException {
		String id = tfd_create_ID.getText();

		if(id.equals(checkIdList(id))) {
			Alert checkIdErr = new Alert(AlertType.ERROR);
			checkIdErr.setTitle("아이디 확인");
			checkIdErr.setContentText("중복된 아이디입니다.");
			checkIdErr.showAndWait();
		}
		else {
			Alert checkIdErr = new Alert(AlertType.CONFIRMATION);
			checkIdErr.setTitle("아이디 확인");
			checkIdErr.setContentText("회원가능이 가능합니다.");
			checkIdErr.showAndWait();
		}
		

	}
	
	
	// database 에서 동일한 아이디가 있는지 확인하는 과정
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
	
	// 페이지 닫기 (다음 Stage로 넘어갈때 close하는 코드 간소화)
	public void closeStage() {
		Stage stage = (Stage) btn.getScene().getWindow();
		Platform.runLater(() -> {stage.close();});
	}

}

