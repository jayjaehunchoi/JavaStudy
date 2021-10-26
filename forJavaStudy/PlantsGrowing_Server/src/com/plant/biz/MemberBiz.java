package com.plant.biz;

import static common.JdbcTemplate.*;

import java.sql.Connection;

import com.plant.dao.implement.MemberDaoImple;
import com.plant.vo.MemberVo;

public class MemberBiz {
	
	// check = true 이면 로그인 정보 일치
	public boolean check_Login_Info(MemberVo memberVo) {
		Connection conn = getConnection();
		boolean check = new MemberDaoImple().check(memberVo);
		close(conn);
		return check;
	}
	
	// check = true 이면  아이디 중복 존재
	public boolean check_Duplicate_Id(MemberVo memberVo) {
		Connection conn = getConnection();
		boolean check = new MemberDaoImple().checkDuplicateId(memberVo);
		close(conn);
		return check;
	}
	
	public void create_id(MemberVo memberVo) {
		Connection conn  = getConnection();
		new MemberDaoImple().createId(memberVo);
		close(conn);
	}

}
