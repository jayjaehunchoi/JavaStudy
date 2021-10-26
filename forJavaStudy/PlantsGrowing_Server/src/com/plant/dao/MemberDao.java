package com.plant.dao;

public interface MemberDao {
	public static final String members_check_login = "select * from member where member_id= ? and member_pw = ?";
	public static final String members_selectId = "select * from member where member_id= ?";
	public static final String members_insert ="insert into member values(?,?,?)";
	
	
	
}
