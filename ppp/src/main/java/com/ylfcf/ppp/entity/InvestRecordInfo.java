package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * Ͷ�ʼ�¼
 */
public class InvestRecordInfo implements java.io.Serializable{
	
	private static final long serialVersionUID = -3732782069493226186L;
	
	private String id;
	private String borrow_id;
	private String user_id;
	private String extension_user_id;
	private String first_borrow_time;//��Ͷ���ڣ�Ԫ��ӯ�����ֶΣ�
	private String interest_rate;//�껯����
	private String borrow_user_id;//���û�id
	private String invest_user_id;//Ͷ�ʷ��û�id
	private String start_time;
	private String end_time;
	private String money;//������Ͷ����
	private String fact_money;//Ͷ��ʱ֧���Ľ�����ȥԪ��ҵȣ�
	private String invest_money;//��Ͷ��Ԫ��ӯ�����ֶΣ�
	private String invest_status;//Ͷ��״̬����Ͷ��Ͷ��Ԫ��ӯ�����ֶΣ�
	private String invest_times;//��Ͷ������Ԫ��ӯ�����ֶΣ�
	private String invest_time;//Ͷ��ʱ��
	private String return_status;//���״̬��Ԫ��ӯ�����ֶΣ�
	private String repeat_time;//��Ͷʱ�䣨Ԫ��ӯ�����ֶΣ�
	private String interest_start_time;//��Ϣ��
	private String interest_end_time;//��������
	private String invest_start_time;//Ͷ�����濪ʼʱ��
	private String invest_end_time;//Ͷ���������ʱ��
	private String show_money;//��ʾ���
	private String interest_period;//Ͷ������
	private String interest_days;//Ͷ�����ޣ���ʱ��꣩
	private String borrow_type;
	private String now_invest_money;//
	private String return_total_money;//
	private String total_money;
	private String recent_date;
	private String expire_date;
	private String end_date;
	private String borrow_period;//
	private String has_returned_return_money;
	private String return_time;
	private String return_money_status;//-2:û����ؼ�¼  -3:�����   -4:��ȡ��   -5:�����
	private String return_money_msg;//��Ӧreturn_money_status��״̬
	private String sum_interest;
	private String type;//�û�Ͷ��
	private String status;//Ͷ��״̬���ɹ���ʧ�ܣ��ѷſ��
	private String add_time;//Ͷ�����ʱ�� ����Ͷ��ʱ��
	private String sub_time;
	private String repay_time;
	private String add_ip;
	private String user_name;
	private String borrow_name;
	private String invest_from;
	private String invest_from_sub;
	private String experience_code;
	private String invest_order_id;//Ͷ�ʶ�����
	private String interest;//����
	private String interest_add;//��Ϣ
	private String is_appoint_user;//�Ƿ���ԤԼ�û�
	private String tgy_benefit;
	private String android_interest_rate;//��׿�˼�Ϣ
	private String coupon_interest_add;//��Ϣȯ��Ϣ
	private String invest_interest;//Ͷ������
	private String next_time;//�ȶ�ӯ���ڼ�����
	private String totalLend;//�ۼƳ���
	private String totalDelay;//�ۼ�����
	private String wdy_real_interest;//ʵ������
	private String wdy_pro_interest;//Ԥ������
	private String interest_free_period;//ÿ���������������
	private String interest_period_month;//Ͷ�����ޣ���λ�£�
	private String is_generated_records;//�ȶ��Ĳ鿴�����¼��ť�Ƿ���Ե�� 1ʱ���Ե����0ʱ���ɵ��
	private String period;//1Ϊ��Ͷ
	private String red_bag_money;//ʹ�ú�����
	private String red_bag_interest;//�������
	private List<WDYChildRecordInfo> wdyChildRecordList;//�ȶ�ӯͶ�ʼ�¼

	public String getRed_bag_money() {
		return red_bag_money;
	}

	public void setRed_bag_money(String red_bag_money) {
		this.red_bag_money = red_bag_money;
	}

	public String getRed_bag_interest() {
		return red_bag_interest;
	}

	public void setRed_bag_interest(String red_bag_interest) {
		this.red_bag_interest = red_bag_interest;
	}

	public String getFact_money() {
		return fact_money;
	}
	public void setFact_money(String fact_money) {
		this.fact_money = fact_money;
	}
	public List<WDYChildRecordInfo> getWdyChildRecordList() {
		return wdyChildRecordList;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setWdyChildRecordList(List<WDYChildRecordInfo> wdyChildRecordList) {
		this.wdyChildRecordList = wdyChildRecordList;
	}
	public String getWdy_pro_interest() {
		return wdy_pro_interest;
	}
	public void setWdy_pro_interest(String wdy_pro_interest) {
		this.wdy_pro_interest = wdy_pro_interest;
	}
	public String getIs_generated_records() {
		return is_generated_records;
	}
	public void setIs_generated_records(String is_generated_records) {
		this.is_generated_records = is_generated_records;
	}
	public String getTotal_money() {
		return total_money;
	}
	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}
	public String getWdy_real_interest() {
		return wdy_real_interest;
	}
	public void setWdy_real_interest(String wdy_real_interest) {
		this.wdy_real_interest = wdy_real_interest;
	}
	public String getInterest_period_month() {
		return interest_period_month;
	}
	public void setInterest_period_month(String interest_period_month) {
		this.interest_period_month = interest_period_month;
	}
	public String getInterest_free_period() {
		return interest_free_period;
	}
	public void setInterest_free_period(String interest_free_period) {
		this.interest_free_period = interest_free_period;
	}
	public String getTotalLend() {
		return totalLend;
	}
	public void setTotalLend(String totalLend) {
		this.totalLend = totalLend;
	}
	public String getTotalDelay() {
		return totalDelay;
	}
	public void setTotalDelay(String totalDelay) {
		this.totalDelay = totalDelay;
	}
	public String getNext_time() {
		return next_time;
	}
	public void setNext_time(String next_time) {
		this.next_time = next_time;
	}
	public String getInterest_days() {
		return interest_days;
	}
	public void setInterest_days(String interest_days) {
		this.interest_days = interest_days;
	}
	public String getInvest_interest() {
		return invest_interest;
	}
	public void setInvest_interest(String invest_interest) {
		this.invest_interest = invest_interest;
	}
	public String getCoupon_interest_add() {
		return coupon_interest_add;
	}
	public void setCoupon_interest_add(String coupon_interest_add) {
		this.coupon_interest_add = coupon_interest_add;
	}
	public String getBorrow_type() {
		return borrow_type;
	}
	public void setBorrow_type(String borrow_type) {
		this.borrow_type = borrow_type;
	}
	public String getAndroid_interest_rate() {
		return android_interest_rate;
	}
	public void setAndroid_interest_rate(String android_interest_rate) {
		this.android_interest_rate = android_interest_rate;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getExtension_user_id() {
		return extension_user_id;
	}
	public void setExtension_user_id(String extension_user_id) {
		this.extension_user_id = extension_user_id;
	}
	public String getInvest_time() {
		return invest_time;
	}
	public void setInvest_time(String invest_time) {
		this.invest_time = invest_time;
	}
	public String getIs_appoint_user() {
		return is_appoint_user;
	}
	public void setIs_appoint_user(String is_appoint_user) {
		this.is_appoint_user = is_appoint_user;
	}
	public String getTgy_benefit() {
		return tgy_benefit;
	}
	public void setTgy_benefit(String tgy_benefit) {
		this.tgy_benefit = tgy_benefit;
	}
	public String getInterest_add() {
		return interest_add;
	}
	public void setInterest_add(String interest_add) {
		this.interest_add = interest_add;
	}
	public String getInvest_start_time() {
		return invest_start_time;
	}
	public void setInvest_start_time(String invest_start_time) {
		this.invest_start_time = invest_start_time;
	}
	public String getInvest_end_time() {
		return invest_end_time;
	}
	public void setInvest_end_time(String invest_end_time) {
		this.invest_end_time = invest_end_time;
	}
	public String getShow_money() {
		return show_money;
	}
	public void setShow_money(String show_money) {
		this.show_money = show_money;
	}
	public String getInterest_period() {
		return interest_period;
	}
	public void setInterest_period(String interest_period) {
		this.interest_period = interest_period;
	}
	public String getInterest_start_time() {
		return interest_start_time;
	}
	public void setInterest_start_time(String interest_start_time) {
		this.interest_start_time = interest_start_time;
	}
	public String getInterest_end_time() {
		return interest_end_time;
	}
	public void setInterest_end_time(String interest_end_time) {
		this.interest_end_time = interest_end_time;
	}
	public String getNow_invest_money() {
		return now_invest_money;
	}
	public void setNow_invest_money(String now_invest_money) {
		this.now_invest_money = now_invest_money;
	}
	public String getReturn_total_money() {
		return return_total_money;
	}
	public void setReturn_total_money(String return_total_money) {
		this.return_total_money = return_total_money;
	}
	public String getRecent_date() {
		return recent_date;
	}
	public void setRecent_date(String recent_date) {
		this.recent_date = recent_date;
	}
	public String getExpire_date() {
		return expire_date;
	}
	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getBorrow_period() {
		return borrow_period;
	}
	public void setBorrow_period(String borrow_period) {
		this.borrow_period = borrow_period;
	}
	public String getHas_returned_return_money() {
		return has_returned_return_money;
	}
	public void setHas_returned_return_money(String has_returned_return_money) {
		this.has_returned_return_money = has_returned_return_money;
	}
	public String getReturn_time() {
		return return_time;
	}
	public void setReturn_time(String return_time) {
		this.return_time = return_time;
	}
	public String getReturn_money_status() {
		return return_money_status;
	}
	public void setReturn_money_status(String return_money_status) {
		this.return_money_status = return_money_status;
	}
	public String getReturn_money_msg() {
		return return_money_msg;
	}
	public void setReturn_money_msg(String return_money_msg) {
		this.return_money_msg = return_money_msg;
	}
	public String getFirst_borrow_time() {
		return first_borrow_time;
	}
	public void setFirst_borrow_time(String first_borrow_time) {
		this.first_borrow_time = first_borrow_time;
	}
	public String getInvest_money() {
		return invest_money;
	}
	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}
	public String getInvest_status() {
		return invest_status;
	}
	public void setInvest_status(String invest_status) {
		this.invest_status = invest_status;
	}
	public String getInvest_times() {
		return invest_times;
	}
	public void setInvest_times(String invest_times) {
		this.invest_times = invest_times;
	}
	public String getReturn_status() {
		return return_status;
	}
	public void setReturn_status(String return_status) {
		this.return_status = return_status;
	}
	public String getRepeat_time() {
		return repeat_time;
	}
	public void setRepeat_time(String repeat_time) {
		this.repeat_time = repeat_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getInvest_order_id() {
		return invest_order_id;
	}
	public void setInvest_order_id(String invest_order_id) {
		this.invest_order_id = invest_order_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getInterest_rate() {
		return interest_rate;
	}
	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}
	public String getBorrow_user_id() {
		return borrow_user_id;
	}
	public void setBorrow_user_id(String borrow_user_id) {
		this.borrow_user_id = borrow_user_id;
	}
	public String getInvest_user_id() {
		return invest_user_id;
	}
	public void setInvest_user_id(String invest_user_id) {
		this.invest_user_id = invest_user_id;
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getSum_interest() {
		return sum_interest;
	}
	public void setSum_interest(String sum_interest) {
		this.sum_interest = sum_interest;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getSub_time() {
		return sub_time;
	}
	public void setSub_time(String sub_time) {
		this.sub_time = sub_time;
	}
	public String getRepay_time() {
		return repay_time;
	}
	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}
	public String getAdd_ip() {
		return add_ip;
	}
	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getInvest_from() {
		return invest_from;
	}
	public void setInvest_from(String invest_from) {
		this.invest_from = invest_from;
	}
	public String getInvest_from_sub() {
		return invest_from_sub;
	}
	public void setInvest_from_sub(String invest_from_sub) {
		this.invest_from_sub = invest_from_sub;
	}
	public String getExperience_code() {
		return experience_code;
	}
	public void setExperience_code(String experience_code) {
		this.experience_code = experience_code;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	
}
