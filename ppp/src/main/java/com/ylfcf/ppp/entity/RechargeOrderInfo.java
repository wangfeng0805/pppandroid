package com.ylfcf.ppp.entity;

/**
 * ��ֵ����
 * @author Mr.liu
 *
 */
public class RechargeOrderInfo implements java.io.Serializable{
	private static final long serialVersionUID = -7571510130426096730L;
	
	private String resp_msg;//������Ϣ
	private String order_sn;//������

	public RechargeOrderInfo(){}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	
}
