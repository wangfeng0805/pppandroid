package com.ylfcf.ppp.entity;

public class UserCardInfo implements java.io.Serializable {

	/**
	 * �û����п���Ϣ
	 */
	private static final long serialVersionUID = 271828231621552318L;

	private String id;
	private String user_id;
	private String bank_card;// ���п���
	private String bank_name;//���е�����
	private String province_code;//ʡ
	private String city_code;//��
	private String branch_name;//����
	private String local_bank_id;//��������id
	private String bank_num;//���к�
	private String bank_code;//����
	private String type;//�û����ͣ�vip�û�������ͨ�û�
	private String is_binding;// �Ƿ��
	private String real_name;
	private String bind_id;

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getBind_id() {
		return bind_id;
	}

	public void setBind_id(String bind_id) {
		this.bind_id = bind_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getProvince_code() {
		return province_code;
	}

	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	public String getLocal_bank_id() {
		return local_bank_id;
	}

	public void setLocal_bank_id(String local_bank_id) {
		this.local_bank_id = local_bank_id;
	}

	public String getBank_num() {
		return bank_num;
	}

	public void setBank_num(String bank_num) {
		this.bank_num = bank_num;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBank_card() {
		return bank_card;
	}

	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}

	public String getIs_binding() {
		return is_binding;
	}

	public void setIs_binding(String is_binding) {
		this.is_binding = is_binding;
	}

}
