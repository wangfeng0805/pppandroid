package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * ������˾
 * @author Mr.liu
 *
 */
public class AssociatedCompanyInfo implements java.io.Serializable{

	private static final long serialVersionUID = 5614920176603886256L;
	
	private String id;
	/*
	 * ��˾����
	 */
	private String company_name;
	/*
	 * ��˾����
	 */
	private String introduce;
	/*
	 * ���ϣ����룩
	 */
	private String materials_mark;
	/*
	 * ���ϣ������룩
	 */
	private String materials_nomark;
	/*
	 * ��������
	 */
	private String materials_name;
	/*
	 * ���״̬ 
	 * ö�����ͣ�'���δͨ��','���ͨ��','����','�����'
	 * Ĭ�ϣ�'�����'
	 */
	private String status;
	/*
	 * �����
	 */
	private String add_user_id;
	/*
	 * ���ʱ��
	 * ��ʽ��yyyy-MM-dd HH:mm:ss
	 */
	private String add_time;
	/*
	 * �����ip
	 */
	private String add_ip;
	/*
	 * ������û�id
	 */
	private String audit_user_id;
	/*
	 * ���δͨ��ԭ��
	 */
	private String reason;
	/*
	 * �����ͼƬ
	 */
	private String markPics;
	private List<String> markPicsList;//������ϵ�list
	/*
	 * δ�����ͼƬ
	 */
	private String noMarkPics;
	private List<String> nomarkPicsList;//û�д�����ϵ�list
	/*
	 * ��������
	 */
	private String materials_names;
	private List<String> materialsNamesList;//���ϵ�����list
	
	public List<String> getMarkPicsList() {
		return markPicsList;
	}
	public void setMarkPicsList(List<String> markPicsList) {
		this.markPicsList = markPicsList;
	}
	public List<String> getNomarkPicsList() {
		return nomarkPicsList;
	}
	public void setNomarkPicsList(List<String> nomarkPicsList) {
		this.nomarkPicsList = nomarkPicsList;
	}
	public List<String> getMaterialsNamesList() {
		return materialsNamesList;
	}
	public void setMaterialsNamesList(List<String> materialsNamesList) {
		this.materialsNamesList = materialsNamesList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getMaterials_mark() {
		return materials_mark;
	}
	public void setMaterials_mark(String materials_mark) {
		this.materials_mark = materials_mark;
	}
	public String getMaterials_nomark() {
		return materials_nomark;
	}
	public void setMaterials_nomark(String materials_nomark) {
		this.materials_nomark = materials_nomark;
	}
	public String getMaterials_name() {
		return materials_name;
	}
	public void setMaterials_name(String materials_name) {
		this.materials_name = materials_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAdd_user_id() {
		return add_user_id;
	}
	public void setAdd_user_id(String add_user_id) {
		this.add_user_id = add_user_id;
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
	public String getAudit_user_id() {
		return audit_user_id;
	}
	public void setAudit_user_id(String audit_user_id) {
		this.audit_user_id = audit_user_id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMarkPics() {
		return markPics;
	}
	public void setMarkPics(String markPics) {
		this.markPics = markPics;
	}
	public String getNoMarkPics() {
		return noMarkPics;
	}
	public void setNoMarkPics(String noMarkPics) {
		this.noMarkPics = noMarkPics;
	}
	public String getMaterials_names() {
		return materials_names;
	}
	public void setMaterials_names(String materials_names) {
		this.materials_names = materials_names;
	}
}
