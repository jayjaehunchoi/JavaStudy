package application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnect {

	private Connection con;
	private Statement st; // 특정 데이터 베이스에 sql 문장 실행
	private ResultSet rs; // 실행 결과를 받아옴
	
	public DBConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "1234");
			st = con.createStatement();
		}catch(Exception e) {
			System.out.println("데이터 베이스 연결 오류");
		}
	}
	
	public Connection connect() {
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "1234");
		}catch(ClassNotFoundException ce) {
			System.out.println("드라이버 로딩 실패");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public boolean check(String adminID, String adminPassword) {
		try {
			String SQL = "SELECT * FROM testmem WHERE id = '" + adminID + "' and pwd = '" + adminPassword + "'";
			rs = st.executeQuery(SQL);
			if(rs.next()) {
				return true;
			}
					
		} catch (Exception e) {
			System.out.println("데이터 베이스 검색 오류 : "  + e.getMessage());
		}
		return false;
	}
}
