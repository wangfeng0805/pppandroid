package com.ylfcf.ppp.entity;
/**
 * �����������ݿ�ʵ�����
 */
public class GesturePwdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 8684221940279570296L;
	
	private String userId;
	private String phone;
	private String status;//���û��Ƿ�����������   0��δ����  1������
	private String pwd;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
