package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * �ƹ������ҳ����
 * @author Mr.liu
 *
 */
public class ExtensionPageInfo implements java.io.Serializable{
	private static final long serialVersionUID = -889465986450598587L;
	private String list;//�ƹ�������б�
	private String user_list;//�ƹ���û��б�
	private String total;
	private List<ExtensionIncomeInfo> incomeInfoList;
	private List<ExtensionUserInfo> userInfoList;
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getUser_list() {
		return user_list;
	}
	public void setUser_list(String user_list) {
		this.user_list = user_list;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<ExtensionIncomeInfo> getIncomeInfoList() {
		return incomeInfoList;
	}
	public void setIncomeInfoList(List<ExtensionIncomeInfo> incomeInfoList) {
		this.incomeInfoList = incomeInfoList;
	}
	public List<ExtensionUserInfo> getUserInfoList() {
		return userInfoList;
	}
	public void setUserInfoList(List<ExtensionUserInfo> userInfoList) {
		this.userInfoList = userInfoList;
	}
	
}
