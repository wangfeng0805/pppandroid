package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * ǩ������صĶ���
 * @author Mr.liu
 *
 */
public class SignResultInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7742061267347887361L;
		
	private String message;
	private String list;
	private String total_day;
	private List<String> signDateList;//�Ѿ�ǩ��������
	
	public List<String> getSignDateList() {
		return signDateList;
	}

	public void setSignDateList(List<String> signDateList) {
		this.signDateList = signDateList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getTotal_day() {
		return total_day;
	}

	public void setTotal_day(String total_day) {
		this.total_day = total_day;
	}

}
