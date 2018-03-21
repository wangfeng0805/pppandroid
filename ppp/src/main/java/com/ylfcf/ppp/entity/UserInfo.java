package com.ylfcf.ppp.entity;

/**
 * @author Mr.liu
 * �������û���Ϣ��
 */
public class UserInfo implements java.io.Serializable {

	private static final long serialVersionUID = -6672267238135167211L;

	private String id;
	/*
	 * �û���
	 */
	private String user_name;
	/*
	 * �ֻ�����
	 */
	private String phone;
	/*
	 * �û���ʵ����
	 */
	private String real_name;
	/*
	 * ����
	 */
	private String password;
	/*
	 * �û����� 
	 * ö�����ͣ�'�ڲ����','�ڲ�Ͷ��','�ⲿ���','�ⲿͶ��','�����˻�'
	 */
	private String type;
	private String co_mobile;
	private String co_phone;	
	private String reg_ip;
	private String init_pwd;//�Ƿ��޸Ĺ�����
	private String sales_phone;
	private String user_status;
	/*
	 * Ԫ���
	 */
	private String coin;
	/*
	 * ����
	 */
	private String email;
	/*
	 * ���֤����
	 */
	private String id_number;
	/*
	 * �Ա� 
	 * ö�����ͣ�'��','Ů'
	 * Ĭ��Ϊ��
	 */
	private String sex;
	/*
	 * �㸶���ɵ�id
	 */
	private String hf_user_id;
	/*
	 * ע��ʱ�� 
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String reg_time;
	/*
	 * �û���Դ
	 * ö�����ͣ�'�����','�����ƹ�','A5����','Ψһ��ý','Ԫ�����','Ԫ��������','��׿APP','������','���л���','ƻ��APP','ȯ����','Ԫ����΢�ŷ����','˾��Ǯ'
	 * Ĭ��'Ԫ��������'
	 */
	private String user_from;
	/*
	 * ��APP�˱�ʾ��Դ��ĳ������ƽ̨
	 * ����'Ӧ�ñ�'��
	 */
	private String user_from_sub;
	/*
	 * ΢��Openid
	 */
	private String open_id;
	/*
	 * ���Ƽ��˵��ƹ���
	 */
	private String extension_code;
	/*
	 * �ƹ���
	 */
	private String promoted_code;
	/*
	 * �Ƽ���id
	 */
	private String extension_user_id;
	/*
	 * �ʱ�
	 */
	private String post_code;
	/*
	 * ��ַ
	 */
	private String address;
	/*
	 * ��¼ʱ��
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String login_time;
	/*
	 * ��¼��ʱ���ݣ��������û��֣��������
	 */
	private String tmp_data;
	/*
	 * ��������״̬
	 * ö�����ͣ�'�ر�','����'
	 * Ĭ�ϣ�'�ر�'
	 */
	private String deal_enabled;
	/*
	 * ��������
	 */
	private String deal_pwd;
	/*
	 * �ֻ���������أ�ʡ��
	 */
	private String phone_province;
	/*
	 * ʵ����֤����
	 */
	private String verify_times;
	/*
	 * ��������������
	 */
	private String deal_pwd_times;
	
	private String yyzz_code;//Ӫҵִ��
	private String jgxy_code;//�������ú�
	private String khxk_code;//������ɺ�
	private String bank_name;//��������
	private String bank_card;//���п�
	private String yyzz_img;//Ӫҵִ��ͼƬ
	private String jgxy_img;//��������ͼƬ
	private String khxk_img;//�������ͼƬ
	
	public String getYyzz_code() {
		return yyzz_code;
	}

	public void setYyzz_code(String yyzz_code) {
		this.yyzz_code = yyzz_code;
	}

	public String getJgxy_code() {
		return jgxy_code;
	}

	public void setJgxy_code(String jgxy_code) {
		this.jgxy_code = jgxy_code;
	}

	public String getKhxk_code() {
		return khxk_code;
	}

	public void setKhxk_code(String khxk_code) {
		this.khxk_code = khxk_code;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_card() {
		return bank_card;
	}

	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}

	public String getYyzz_img() {
		return yyzz_img;
	}

	public void setYyzz_img(String yyzz_img) {
		this.yyzz_img = yyzz_img;
	}

	public String getJgxy_img() {
		return jgxy_img;
	}

	public void setJgxy_img(String jgxy_img) {
		this.jgxy_img = jgxy_img;
	}

	public String getKhxk_img() {
		return khxk_img;
	}

	public void setKhxk_img(String khxk_img) {
		this.khxk_img = khxk_img;
	}

	public String getCo_mobile() {
		return co_mobile;
	}

	public void setCo_mobile(String co_mobile) {
		this.co_mobile = co_mobile;
	}

	public String getCo_phone() {
		return co_phone;
	}

	public void setCo_phone(String co_phone) {
		this.co_phone = co_phone;
	}

	public String getReg_ip() {
		return reg_ip;
	}

	public void setReg_ip(String reg_ip) {
		this.reg_ip = reg_ip;
	}

	public String getInit_pwd() {
		return init_pwd;
	}

	public void setInit_pwd(String init_pwd) {
		this.init_pwd = init_pwd;
	}

	public String getSales_phone() {
		return sales_phone;
	}

	public void setSales_phone(String sales_phone) {
		this.sales_phone = sales_phone;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	/**
	 * ��ȡ��������״̬
	 * @return '�ر�','����'
	 */
	public String getDeal_enabled() {
		return deal_enabled;
	}

	/**
	 * ���ý�������״̬
	 * @param deal_enabled
	 */
	public void setDeal_enabled(String deal_enabled) {
		this.deal_enabled = deal_enabled;
	}

	/**
	 * ��ȡ��������
	 * @return
	 */
	public String getDeal_pwd() {
		return deal_pwd;
	}

	/**
	 * ���ý�������
	 * @param deal_pwd
	 */
	public void setDeal_pwd(String deal_pwd) {
		this.deal_pwd = deal_pwd;
	}

	/**
	 * ��ȡԪ���
	 * @return
	 */
	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getExtension_code() {
		return extension_code;
	}

	public void setExtension_code(String extension_code) {
		this.extension_code = extension_code;
	}

	public String getPromoted_code() {
		return promoted_code;
	}

	public void setPromoted_code(String promoted_code) {
		this.promoted_code = promoted_code;
	}

	public String getExtension_user_id() {
		return extension_user_id;
	}

	public void setExtension_user_id(String extension_user_id) {
		this.extension_user_id = extension_user_id;
	}

	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public String getTmp_data() {
		return tmp_data;
	}

	public void setTmp_data(String tmp_data) {
		this.tmp_data = tmp_data;
	}

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public String getUser_from() {
		return user_from;
	}

	public void setUser_from(String user_from) {
		this.user_from = user_from;
	}

	public String getUser_from_sub() {
		return user_from_sub;
	}

	public void setUser_from_sub(String user_from_sub) {
		this.user_from_sub = user_from_sub;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡ�û���
	 * @return
	 */
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHf_user_id() {
		return hf_user_id;
	}

	public void setHf_user_id(String hf_user_id) {
		this.hf_user_id = hf_user_id;
	}

	public String getPhone_province() {
		return phone_province;
	}

	public void setPhone_province(String phone_province) {
		this.phone_province = phone_province;
	}

	public String getVerify_times() {
		return verify_times;
	}

	public void setVerify_times(String verify_times) {
		this.verify_times = verify_times;
	}

	public String getDeal_pwd_times() {
		return deal_pwd_times;
	}

	public void setDeal_pwd_times(String deal_pwd_times) {
		this.deal_pwd_times = deal_pwd_times;
	}
}
