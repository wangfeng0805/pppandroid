package com.ylfcf.ppp.entity;

/**
 * �û���������˻�
 * @author Administrator
 *
 */
public class UserRMBAccountInfo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6633931533467501783L;
	private String user_id;
	private String total_money; //�ܽ��
	private String use_money;//�˻����
	private String frozen_money;//�����ܶ�
	private String collection_money;//�����ܶ�
	private String recharge_money;//��ֵ���
	private String repayment_money;//������
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTotal_money() {
		return total_money;
	}
	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}
	public String getUse_money() {
		return use_money;
	}
	public void setUse_money(String use_money) {
		this.use_money = use_money;
	}
	public String getFrozen_money() {
		return frozen_money;
	}
	public void setFrozen_money(String frozen_money) {
		this.frozen_money = frozen_money;
	}
	public String getCollection_money() {
		return collection_money;
	}
	public void setCollection_money(String collection_money) {
		this.collection_money = collection_money;
	}
	public String getRecharge_money() {
		return recharge_money;
	}
	public void setRecharge_money(String recharge_money) {
		this.recharge_money = recharge_money;
	}
	public String getRepayment_money() {
		return repayment_money;
	}
	public void setRepayment_money(String repayment_money) {
		this.repayment_money = repayment_money;
	}
	
}
