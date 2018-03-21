package com.ylfcf.ppp.entity;

/**
 * ������Ѷ����Ķ���
 * @author Mr.liu
 *
 */
public class ArticleInfo implements java.io.Serializable{
	private static final long serialVersionUID = -8159007845784039125L;
	
	private String id;
	/*
	 * ���·���
	 * ö�����ͣ�'��ҵ����','��վ����','������Ѷ','������Ѷ','��������'
	 * Ĭ�ϣ�'��ҵ����'
	 */
	private String type;
	/*
	 * ���±���
	 */
	private String title;
	/*
	 * ���¼��
	 */
	private String summary;
	/*
	 * ��������
	 */
	private String content;
	/*
	 * ���·����URL
	 */
	private String pic_url;
	/*
	 * SEO����
	 */
	private String seo_title;
	/*
	 * SEO�ؼ���
	 */
	private String seo_keywords;
	/*
	 * SEO����
	 */
	private String seo_description;
	/*
	 * ����
	 */
	private String admin_id;
	/*
	 * ����ֵ
	 */
	private String sort;
	/*
	 * �Ƿ��ö�
	 */
	private String is_order;
	/*
	 * ����״̬
	 * ö�����ͣ�'����','�ر�','ɾ��'
	 * Ĭ�ϣ�'����'
	 */
	private String status;
	/*
	 * �Ƿ���Ҫ
	 * ö�����ͣ�'��','��'
	 * Ĭ�ϣ�'��'
	 */
	private String is_important;
	/*
	 * ����ʱ��
	 */
	private String add_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getSeo_title() {
		return seo_title;
	}
	public void setSeo_title(String seo_title) {
		this.seo_title = seo_title;
	}
	public String getSeo_keywords() {
		return seo_keywords;
	}
	public void setSeo_keywords(String seo_keywords) {
		this.seo_keywords = seo_keywords;
	}
	public String getSeo_description() {
		return seo_description;
	}
	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getIs_order() {
		return is_order;
	}
	public void setIs_order(String is_order) {
		this.is_order = is_order;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIs_important() {
		return is_important;
	}
	public void setIs_important(String is_important) {
		this.is_important = is_important;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	
}
