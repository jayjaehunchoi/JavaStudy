package com.plant.dao.implement;

import java.sql.*;

import com.plant.dao.*;
import com.plant.vo.*;

import static common.JdbcTemplate.*;


public class MemberDaoImple implements MemberDao {
	Connection conn;
	
	// ID 로그인 시 ID Password 가 맞는지 확인
	// sql문 작성하여 Statement로 보내주고, ResultSet으로 결과 받아줌
	public boolean check(MemberVo memberVo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(members_check_login);
			pstmt.setString(1, memberVo.getId());
			pstmt.setString(2, memberVo.getPassword());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String find_id = rs.getString(1);
				String find_pw = rs.getString(2);
				
				if(memberVo.getId().equals(find_id) && memberVo.getPassword().equals(find_pw)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return false;
		
	}
	

	// ID 중복확인
	// sql문 작성하여 Statement로 보내주고, ResultSet으로 결과 받아줌
	public boolean checkDuplicateId(MemberVo memberVo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(members_selectId);
			pstmt.setString(1, memberVo.getId());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return false;
	
	}

	// 아이디 생성시 DB업데이트
	// userinfo id, pw만 있는 생성자 버전 사용해서 업뎃하면 됨.
	public void createId(MemberVo memberVo)  {
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(members_insert);
			pstmt.setString(1, memberVo.getId());
			pstmt.setString(2, memberVo.getPassword());
			pstmt.setString(3, memberVo.getNickName());
			pstmt.executeUpdate();
			
			System.out.println("[계정생성 완료] id = " + memberVo.getId() +" pw = " + memberVo.getPassword() + "현재 ip = " + memberVo.getNickName());
			commit(conn);
		} catch(SQLException e) {
			System.out.println("계정 생성 오류");
			e.printStackTrace();
			rollBack(conn);
		} finally {
			close(pstmt);
			
		}
		
	}
	
	
	
}
//Connection conn = null;
//Statement st = null;
//ResultSet rs = null;
//PreparedStatement psmt = null;