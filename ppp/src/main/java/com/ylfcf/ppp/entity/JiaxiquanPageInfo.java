package com.ylfcf.ppp.entity;

import java.util.List;

public class JiaxiquanPageInfo implements java.io.Serializable{
	/**
	 * ��Ϣȯ��ҳ
	 */
	private static final long serialVersionUID = 7256979508090800632L;
	private String list;//��Ϣȯ�б�
	private String total;
	private List<JiaxiquanInfo> infoList;
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<JiaxiquanInfo> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<JiaxiquanInfo> infoList) {
		this.infoList = infoList;
	}
	
}
