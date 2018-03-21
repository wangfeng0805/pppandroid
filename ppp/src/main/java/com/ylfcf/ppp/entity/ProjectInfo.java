package com.ylfcf.ppp.entity;

import java.util.ArrayList;

/**
 * ��Ŀ��Ϣ
 * @author Administrator
 *
 */
public class ProjectInfo implements java.io.Serializable{

	/**
	 * ��Ŀ��Ϣ
	 */
	private static final long serialVersionUID = -8304373447735506476L;
	
	private String id;
	private String name;
	private String img;//��Ŀlogo
	private String summary;//��Ҫ
	private String loan_id;//��id
	private String recommend_id;//�Ƽ���id
	private String guarantee_id;//������id
	private String capital;//�ʽ���;
	private String capital_safe;//�ʽ�ȫ
	private String introduced;//���ʷ�����
	private String measures;//������ʩ
	private String repay_from;//������Դ
	private String invest_point;//Ͷ������
	private String danbaohan;//������ͼƬ
	private String materials;//�������ϣ����룩
	private String materials_nomark;//�������ϣ�����
	private ArrayList<ProjectCailiaoInfo> cailiaoMarkList;//����Ĳ��ϵļ���
	private ArrayList<ProjectCailiaoInfo> cailiaoNoMarkList;//û�д���Ĳ��ϵļ���
	private String imgs_name;//������϶�Ӧ��ͼƬ����(���ڽе����������ƣ�
	private String safe;//��ȫ����
	private String status;//��Ŀ��Ч�������ȵ�...
	private String add_time;
	private String update_time;
	private String type;//�ʲ��������͡�

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<ProjectCailiaoInfo> getCailiaoNoMarkList() {
		return cailiaoNoMarkList;
	}
	public void setCailiaoNoMarkList(ArrayList<ProjectCailiaoInfo> cailiaoNoMarkList) {
		this.cailiaoNoMarkList = cailiaoNoMarkList;
	}
	public ArrayList<ProjectCailiaoInfo> getCailiaoMarkList() {
		return cailiaoMarkList;
	}
	public void setCailiaoMarkList(ArrayList<ProjectCailiaoInfo> cailiaoMarkList) {
		this.cailiaoMarkList = cailiaoMarkList;
	}
	public String getLoan_id() {
		return loan_id;
	}
	public void setLoan_id(String loan_id) {
		this.loan_id = loan_id;
	}
	public String getRecommend_id() {
		return recommend_id;
	}
	public void setRecommend_id(String recommend_id) {
		this.recommend_id = recommend_id;
	}
	public String getGuarantee_id() {
		return guarantee_id;
	}
	public void setGuarantee_id(String guarantee_id) {
		this.guarantee_id = guarantee_id;
	}
	public String getMaterials_nomark() {
		return materials_nomark;
	}
	public void setMaterials_nomark(String materials_nomark) {
		this.materials_nomark = materials_nomark;
	}
	public String getCapital_safe() {
		return capital_safe;
	}
	public void setCapital_safe(String capital_safe) {
		this.capital_safe = capital_safe;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getIntroduced() {
		return introduced;
	}
	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}
	public String getMeasures() {
		return measures;
	}
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	public String getRepay_from() {
		return repay_from;
	}
	public void setRepay_from(String repay_from) {
		this.repay_from = repay_from;
	}
	public String getInvest_point() {
		return invest_point;
	}
	public void setInvest_point(String invest_point) {
		this.invest_point = invest_point;
	}
	public String getDanbaohan() {
		return danbaohan;
	}
	public void setDanbaohan(String danbaohan) {
		this.danbaohan = danbaohan;
	}
	public String getMaterials() {
		return materials;
	}
	public void setMaterials(String materials) {
		this.materials = materials;
	}
	public String getImgs_name() {
		return imgs_name;
	}
	public void setImgs_name(String imgs_name) {
		this.imgs_name = imgs_name;
	}
	public String getSafe() {
		return safe;
	}
	public void setSafe(String safe) {
		this.safe = safe;
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
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	
}
