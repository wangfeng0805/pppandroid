package com.ylfcf.ppp.entity;

public class RechargeResultInfo implements java.io.Serializable{
	/**
	 * ��ֵƾ֤
	 */
	private static final long serialVersionUID = 868246136238107956L;
	private String order_sn;//��ֵƾ֤id
	private String resp_msg;//���׽��
	
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getResp_msg() {
		return resp_msg;
	}
	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}
	
}
