package com.ylfcf.ppp.entity;

/**
 * �����
 * @author Mr.liu
 */
public class TYJInfo implements java.io.Serializable{
	private static final long serialVersionUID = 2905062963483464627L;
	
	/*
	 * ������루MD 16λ��
	 */
	private String experience_code;
	/*
	 * ���ʶ
	 */
	private String active_title;
	/*
	 * �û�id
	 */
	private String user_id;
	/*
	 * �������Ч�ڿ�ʼʱ��
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String start_time;
	/*
	 * �������Ч�ڽ���ʱ��
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String end_time;
	/*
	 * �������
	 */
	private String account;
	/*
	 * ʵ��ʹ��ʱ��
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String use_time;
	/*
	 * ��Ϣ����
	 */
	private String time_limit;
	/*
	 * ������
	 */
	private String interest;
	/*
	 * ���id
	 */
	private String borrow_id;
	/*
	 * �������
	 */
	private String borrow_name;
	/*
	 * Ͷ��id
	 */
	private String tender_id;
	/*
	 * ��ע
	 */
	private String remark;
	/*
	 * ��ȡʱ��
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String add_time;
	/*
	 * �Ҹ�ʱ��
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String repay_time;
	/*
	 * �ɷ���״̬
	 * ö�����ͣ�'�ر�','����','�����','�ѷ���','�ܾ�','�ɷ���'
	 */
	private String put_status;
	/*
	 * ��ȡ����
	 */
	private String test_num;
	/*
	 * ��Ҫ�����Ͷ�ʽ��
	 */
	private String need_invest_money;
	/*
	 * ״̬
	 * ö�����ͣ�'δʹ��','��ʹ��'
	 * Ĭ�ϣ�'δʹ��'
	 */
	private String status;
	public String getExperience_code() {
		return experience_code;
	}
	public void setExperience_code(String experience_code) {
		this.experience_code = experience_code;
	}
	public String getActive_title() {
		return active_title;
	}
	public void setActive_title(String active_title) {
		this.active_title = active_title;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUse_time() {
		return use_time;
	}
	public void setUse_time(String use_time) {
		this.use_time = use_time;
	}
	public String getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getTender_id() {
		return tender_id;
	}
	public void setTender_id(String tender_id) {
		this.tender_id = tender_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getRepay_time() {
		return repay_time;
	}
	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}
	public String getPut_status() {
		return put_status;
	}
	public void setPut_status(String put_status) {
		this.put_status = put_status;
	}
	public String getTest_num() {
		return test_num;
	}
	public void setTest_num(String test_num) {
		this.test_num = test_num;
	}
	public String getNeed_invest_money() {
		return need_invest_money;
	}
	public void setNeed_invest_money(String need_invest_money) {
		this.need_invest_money = need_invest_money;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
