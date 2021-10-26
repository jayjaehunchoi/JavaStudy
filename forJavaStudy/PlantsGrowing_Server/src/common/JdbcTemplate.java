package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTemplate {
	
	public static Connection getConnection() {
		
		Connection conn = null;
		String user = "scott";
		String pw = "TIGER";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",user, pw);
			conn.setAutoCommit(false);
			 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("jdbc Driver 탐색 오류");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("오라클 커넥션 오류");
		}
		return conn;
	}
	
	public static boolean isConnected(Connection conn) {
		
		boolean check = true;
		try {
			if(conn.isClosed() || conn == null) {
				check =  false;
			}else {
				check =  true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return check;
	}
	
	public static void close(Connection conn) {
		
		try {
			if(isConnected(conn)) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(!pstmt.isClosed() || pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(Statement stmt) {
		try {
			if(!stmt.isClosed() || stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(ResultSet rs) {
		try {
			if(!rs.isClosed() || rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void commit(Connection conn) {
		try {
			if(isConnected(conn)) {
				conn.commit();
			}
			System.out.println("[커밋 성공]");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollBack(Connection conn) {
		try {
			if(isConnected(conn)) {
				conn.rollback();
			}
			System.out.println("[롤백 성공]");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
