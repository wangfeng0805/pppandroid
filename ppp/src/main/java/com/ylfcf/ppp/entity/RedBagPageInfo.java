package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * �����ҳ����
 */
public class RedBagPageInfo implements java.io.Serializable {

	private static final long serialVersionUID = -6813690178210486563L;
	private String list;
	private String total;
	private List<RedBagInfo> redbagList;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public List<RedBagInfo> getRedbagList() {
		return redbagList;
	}

	public void setRedbagList(List<RedBagInfo> redbagList) {
		this.redbagList = redbagList;
	}

}
