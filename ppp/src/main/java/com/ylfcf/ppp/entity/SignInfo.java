package com.ylfcf.ppp.entity;
/**
 * ǩ������
 * @author Mr.liu
 *
 */
public class SignInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1640024797487991247L;
	private String year;
	private String month;
	private String day;
	private boolean isSigned;//�Ƿ���ǩ��
	private boolean isToday;//�Ƿ����
	
	public boolean isToday() {
		return isToday;
	}
	public void setToday(boolean isToday) {
		this.isToday = isToday;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public boolean isSigned() {
		return isSigned;
	}
	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}
	
	
}
