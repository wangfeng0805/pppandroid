package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * �ƹ������ҳ���� ���µĽӿڣ�
 * @author Mr.liu
 *
 */
public class ExtensionNewPageInfo implements java.io.Serializable{

	private static final long serialVersionUID = 3268698894885514135L;
	
	private String list;
	private String total;
	private String extension_user_count;//�Ƽ��ĺ��ѵĸ���
	private String reward_total;//�ܹ�׬ȡ�˶���
	private String myself_total;//���ʦ�Լ�Ͷ����׬ȡ��Ӷ��
	private String one_total;//һ������
	private String second_total;//��������
	private String other_total;//��ʷ����
	private List<ExtensionNewInfo> extensionList;

	public String getMyself_total() {
		return myself_total;
	}

	public void setMyself_total(String myself_total) {
		this.myself_total = myself_total;
	}

	public String getOne_total() {
		return one_total;
	}

	public void setOne_total(String one_total) {
		this.one_total = one_total;
	}

	public String getSecond_total() {
		return second_total;
	}

	public void setSecond_total(String second_total) {
		this.second_total = second_total;
	}

	public String getOther_total() {
		return other_total;
	}

	public void setOther_total(String other_total) {
		this.other_total = other_total;
	}

	public String getList() {
		return list;
	}


	public void setList(String list) {
		this.list = list;
	}


	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}


	public String getExtension_user_count() {
		return extension_user_count;
	}


	public void setExtension_user_count(String extension_user_count) {
		this.extension_user_count = extension_user_count;
	}


	public String getReward_total() {
		return reward_total;
	}


	public void setReward_total(String reward_total) {
		this.reward_total = reward_total;
	}


	public List<ExtensionNewInfo> getExtensionList() {
		return extensionList;
	}

	public void setExtensionList(List<ExtensionNewInfo> extensionList) {
		this.extensionList = extensionList;
	}

}
