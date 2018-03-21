package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * ��Ʒ��Ϣ/ �����Ϣ
 */
public class ProductInfo implements java.io.Serializable {

	private static final long serialVersionUID = 8370003969027655415L;

	private String id;
	/*
	 * ��Ʒ����
	 * ö�����ͣ�'��ӯ','��ӯ','Ԫ��̩��','Ԫ��̩��','Ԫ��̩һ','��ӯ'
	 */
	private String borrow_type;
	/*
	 * �������
	 */
	private String borrow_name;
	/*
	 * ��Ʒ������Ԫ��ӯ�����ֶΣ�
	 */
	private String borrow_period;
	/*
	 * �����ڣ�����Ϊ��λ��Ԫ��ӯ�����ֶΣ�
	 */
	private String frozen_period;
	/*
	 * ���Ͷ�ʽ�Ԫ��ӯ�����ֶΣ�
	 */
	private String invest_highest;
	/*
	 * ��ƷLogo��Ԫ��ӯ�����ֶΣ�
	 */
	private String logo;
	/*
	 * �������ϣ����룩 ��Ԫ��ӯ�����ֶΣ�
	 */
	private String materials;
	/*
	 * �������ϣ�û���룩��Ԫ��ӯ�����ֶΣ�
	 */
	private String materials_nomark;
	/*
	 * �����������ƣ�Ԫ��ӯ�����ֶΣ�
	 */
	private String imgs_name;
	/*
	 *  ��Ŀ��id 
	 *  һ����Ŀ���Էֳɺö��ڵı�
	 */
	private String project_id;
	/*
	 * ���û�id
	 */
	private String user_id;
	/*
	 * ����ܽ��
	 */
	private String total_money;
	/*
	 * ʵ��Ͷ�ʽ��
	 */
	private String invest_money;
	/*
	 * ����Ͷ��
	 */
	private String single_invest_money;
	/*
	 * �껯����
	 */
	private String interest_rate;
	/*
	 * ��Ŀ�ʼʱ��
	 */
	private String start_time;
	private String end_time;
	/*
	 * ����ʱ��
	 */
	private String full_time;
	/*
	 * ��Ϣ����,����Ϊ��λ
	 */
	private String interest_period;
	/*
	 * ���ļ����
	 * ����Ϊ��λ
	 */
	private String collect_period;
	/*
	 * ��Ϣ����ʱ��
	 */
	private String interest_end_time;
	/*
	 * ���ڻ�����ʽ
	 * '���ڻ�����Ϣ','���¸�Ϣ���ڻ�����Ϣ'
	 */
	private String repay_way;
	/*
	 * ����ʱ��
	 */
	private String flow_time;
	/*
	 * ���״̬
	 * ö�����ͣ�'�����','����ͨ��','����ܾ�','����ͨ��','����ܾ�','����','����','������','�������'
	 */
	private String borrow_status;
	/*
	 * 'δ����','������','�ѷſ�','�ѻ���'
	 */
	private String money_status;
	/*
	 * ������¸���ʱ��
	 */
	private String update_time;
	private String now_time;
	private String will_start_time;//���--- �¸���Ŀ�ʼʱ��
	/*
	 * ������ʱ��
	 */
	private String add_time;
	private String plan_publish_time;//˽�������Ʒ�� ԤԼ����ʱ��
	private String danbaohan;//������
	private String add_ip;
	private String invest_use;// Ͷ����;
	private String invest_period;//Ͷ�����ޣ�update��
	private String invest_horizon;// Ͷ������
	private String is_show;// Ͷ������
	private String invest_lowest;// ��Ϳ�Ͷ����Ǯ
	private String is_TYJ;// �Ƿ���Ԫ��� value:����/�ر�
	private String is_coin;// �Ƿ���Ԫ��� value:����/�ر�
	private String active_title;
	private String bite;// 3.35%
	private String is_wap;// �Ƿ��ƶ���ר���
	private String up_money;// �������
	private String introduce;// ��Ʒ����
	private String where_ids;// Ͷ��ȥ���id
	private String pc_interest_rate;// pc�˼�Ϣ����
	private String wap_interest_rate;// ΢��wap���Ϣ������
	private String android_interest_rate;// android���Ϣ������
	private String ios_interest_rate;// ios���Ϣ������
	private String min_rate;//
	private String max_rate;
	private String min_max_rate;
	private String list;// Ԥ�������ʵ�list����ͬ��Ͷ�ʽ���Ӧ��ͬ������
	private List<YJHBiteInfo> yjhBiteList;// Ԫ�ƻ������ʼ���
	private String invest_day;//ÿ��Ͷ���գ��ȶ�Ӯ��Ʒ�¼Ӳ�����
	private String interest_period_month;//Ͷ�����ޣ���λ�£�
	private String return_period;
	private String interest_free_period;//�����ڷѵ�����
	private String invest_status;//Ͷ��״̬
	
	public String getInvest_day() {
		return invest_day;
	}

	public void setInvest_day(String invest_day) {
		this.invest_day = invest_day;
	}

	public String getInterest_period_month() {
		return interest_period_month;
	}

	public void setInterest_period_month(String interest_period_month) {
		this.interest_period_month = interest_period_month;
	}

	public String getReturn_period() {
		return return_period;
	}

	public void setReturn_period(String return_period) {
		this.return_period = return_period;
	}

	public String getInterest_free_period() {
		return interest_free_period;
	}

	public void setInterest_free_period(String interest_free_period) {
		this.interest_free_period = interest_free_period;
	}

	public String getInvest_status() {
		return invest_status;
	}

	public void setInvest_status(String invest_status) {
		this.invest_status = invest_status;
	}

	public String getSingle_invest_money() {
		return single_invest_money;
	}

	public void setSingle_invest_money(String single_invest_money) {
		this.single_invest_money = single_invest_money;
	}

	public String getNow_time() {
		return now_time;
	}

	public void setNow_time(String now_time) {
		this.now_time = now_time;
	}

	public String getWill_start_time() {
		return will_start_time;
	}

	public void setWill_start_time(String will_start_time) {
		this.will_start_time = will_start_time;
	}

	public String getInvest_period() {
		return invest_period;
	}

	public void setInvest_period(String invest_period) {
		this.invest_period = invest_period;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getPlan_publish_time() {
		return plan_publish_time;
	}

	public void setPlan_publish_time(String plan_publish_time) {
		this.plan_publish_time = plan_publish_time;
	}

	public String getDanbaohan() {
		return danbaohan;
	}

	public void setDanbaohan(String danbaohan) {
		this.danbaohan = danbaohan;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public List<YJHBiteInfo> getYjhBiteList() {
		return yjhBiteList;
	}

	public void setYjhBiteList(List<YJHBiteInfo> yjhBiteList) {
		this.yjhBiteList = yjhBiteList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBorrow_type() {
		return borrow_type;
	}

	public void setBorrow_type(String borrow_type) {
		this.borrow_type = borrow_type;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

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

	public String getInvest_money() {
		return invest_money;
	}

	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getFull_time() {
		return full_time;
	}

	public void setFull_time(String full_time) {
		this.full_time = full_time;
	}

	public String getInterest_period() {
		return interest_period;
	}

	public void setInterest_period(String interest_period) {
		this.interest_period = interest_period;
	}

	public String getCollect_period() {
		return collect_period;
	}

	public void setCollect_period(String collect_period) {
		this.collect_period = collect_period;
	}

	public String getInterest_end_time() {
		return interest_end_time;
	}

	public void setInterest_end_time(String interest_end_time) {
		this.interest_end_time = interest_end_time;
	}

	public String getRepay_way() {
		return repay_way;
	}

	public void setRepay_way(String repay_way) {
		this.repay_way = repay_way;
	}

	public String getFlow_time() {
		return flow_time;
	}

	public void setFlow_time(String flow_time) {
		this.flow_time = flow_time;
	}

	public String getBorrow_status() {
		return borrow_status;
	}

	public void setBorrow_status(String borrow_status) {
		this.borrow_status = borrow_status;
	}

	public String getMoney_status() {
		return money_status;
	}

	public void setMoney_status(String money_status) {
		this.money_status = money_status;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getAdd_ip() {
		return add_ip;
	}

	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}

	public String getInvest_use() {
		return invest_use;
	}

	public void setInvest_use(String invest_use) {
		this.invest_use = invest_use;
	}

	public String getInvest_horizon() {
		return invest_horizon;
	}

	public void setInvest_horizon(String invest_horizon) {
		this.invest_horizon = invest_horizon;
	}

	public String getIs_show() {
		return is_show;
	}

	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}

	public String getInvest_lowest() {
		return invest_lowest;
	}

	public void setInvest_lowest(String invest_lowest) {
		this.invest_lowest = invest_lowest;
	}

	public String getIs_TYJ() {
		return is_TYJ;
	}

	public void setIs_TYJ(String is_TYJ) {
		this.is_TYJ = is_TYJ;
	}

	public String getIs_coin() {
		return is_coin;
	}

	public void setIs_coin(String is_coin) {
		this.is_coin = is_coin;
	}

	public String getActive_title() {
		return active_title;
	}

	public void setActive_title(String active_title) {
		this.active_title = active_title;
	}

	public String getBite() {
		return bite;
	}

	public void setBite(String bite) {
		this.bite = bite;
	}

	public String getIs_wap() {
		return is_wap;
	}

	public void setIs_wap(String is_wap) {
		this.is_wap = is_wap;
	}

	public String getPc_interest_rate() {
		return pc_interest_rate;
	}

	public void setPc_interest_rate(String pc_interest_rate) {
		this.pc_interest_rate = pc_interest_rate;
	}

	public String getWap_interest_rate() {
		return wap_interest_rate;
	}

	public void setWap_interest_rate(String wap_interest_rate) {
		this.wap_interest_rate = wap_interest_rate;
	}

	public String getAndroid_interest_rate() {
		return android_interest_rate;
	}

	public void setAndroid_interest_rate(String android_interest_rate) {
		this.android_interest_rate = android_interest_rate;
	}

	public String getIos_interest_rate() {
		return ios_interest_rate;
	}

	public void setIos_interest_rate(String ios_interest_rate) {
		this.ios_interest_rate = ios_interest_rate;
	}

	public String getUp_money() {
		return up_money;
	}

	public void setUp_money(String up_money) {
		this.up_money = up_money;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getWhere_ids() {
		return where_ids;
	}

	public void setWhere_ids(String where_ids) {
		this.where_ids = where_ids;
	}

	public String getMin_rate() {
		return min_rate;
	}

	public void setMin_rate(String min_rate) {
		this.min_rate = min_rate;
	}

	public String getMax_rate() {
		return max_rate;
	}

	public void setMax_rate(String max_rate) {
		this.max_rate = max_rate;
	}

	public String getMin_max_rate() {
		return min_max_rate;
	}

	public void setMin_max_rate(String min_max_rate) {
		this.min_max_rate = min_max_rate;
	}

	public String getBorrow_period() {
		return borrow_period;
	}

	public void setBorrow_period(String borrow_period) {
		this.borrow_period = borrow_period;
	}

	public String getFrozen_period() {
		return frozen_period;
	}

	public void setFrozen_period(String frozen_period) {
		this.frozen_period = frozen_period;
	}

	public String getInvest_highest() {
		return invest_highest;
	}

	public void setInvest_highest(String invest_highest) {
		this.invest_highest = invest_highest;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getMaterials() {
		return materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public String getMaterials_nomark() {
		return materials_nomark;
	}

	public void setMaterials_nomark(String materials_nomark) {
		this.materials_nomark = materials_nomark;
	}

	public String getImgs_name() {
		return imgs_name;
	}

	public void setImgs_name(String imgs_name) {
		this.imgs_name = imgs_name;
	}
}
