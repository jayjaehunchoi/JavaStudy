package com.plant.vo;

import java.io.Serializable;

public class MemberVo implements Serializable{
	
	private static final long serialVersionUID = 1234567890L;
	
	//@OnetoOne
	private String id;
	private String password;
	private String nickName;
	
	
	//초기에 무조건 세팅 돼있어야함
	public MemberVo(String id, String password, String nickName) {
		super();
		this.id = id;
		this.password = password;
		this.nickName = nickName;
		
	}
	// id 는 변동없음 setter x
	public String getId() {
		return id;
	}
	
	// 수정 가능 정보 ip는 매 접속시 확인, password는 수정 페이지 추후 제작
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
