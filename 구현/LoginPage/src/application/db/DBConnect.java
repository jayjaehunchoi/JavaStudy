package application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnect {

	private Connection con;
	private Statement st; // Ư�� ������ ���̽��� sql ���� ����
	private ResultSet rs; // ���� ����� �޾ƿ�
	
	public DBConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "1234");
			st = con.createStatement();
		}catch(Exception e) {
			System.out.println("������ ���̽� ���� ����");
		}
	}
	
	public Connection connect() {
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "1234");
		}catch(ClassNotFoundException ce) {
			System.out.println("����̹� �ε� ����");
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
			System.out.println("������ ���̽� �˻� ���� : "  + e.getMessage());
		}
		return false;
	}
}
