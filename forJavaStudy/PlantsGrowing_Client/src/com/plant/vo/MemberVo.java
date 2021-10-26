package com.plant.vo;

import java.io.Serializable;

public class MemberVo implements Serializable{
	
	private static final long serialVersionUID = 1234567890L;
	
	//@OnetoOne
	private String id;
	private String password;
	private String nickName;
	
	
	//�ʱ⿡ ������ ���� ���־����
	public MemberVo(String id, String password, String nickName) {
		super();
		this.id = id;
		this.password = password;
		this.nickName = nickName;
		
	}
	// id �� �������� setter x
	public String getId() {
		return id;
	}
	
	// ���� ���� ���� ip�� �� ���ӽ� Ȯ��, password�� ���� ������ ���� ����
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
}
