package com.plant.dao.implement;

import java.sql.*;

import com.plant.dao.*;
import com.plant.vo.*;

import static common.JdbcTemplate.*;


public class MemberDaoImple implements MemberDao {
	Connection conn;
	
	// ID �α��� �� ID Password �� �´��� Ȯ��
	// sql�� �ۼ��Ͽ� Statement�� �����ְ�, ResultSet���� ��� �޾���
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
	

	// ID �ߺ�Ȯ��
	// sql�� �ۼ��Ͽ� Statement�� �����ְ�, ResultSet���� ��� �޾���
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

	// ���̵� ������ DB������Ʈ
	// userinfo id, pw�� �ִ� ������ ���� ����ؼ� �����ϸ� ��.
	public void createId(MemberVo memberVo)  {
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(members_insert);
			pstmt.setString(1, memberVo.getId());
			pstmt.setString(2, memberVo.getPassword());
			pstmt.setString(3, memberVo.getNickName());
			pstmt.executeUpdate();
			
			System.out.println("[�������� �Ϸ�] id = " + memberVo.getId() +" pw = " + memberVo.getPassword() + "���� ip = " + memberVo.getNickName());
			commit(conn);
		} catch(SQLException e) {
			System.out.println("���� ���� ����");
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