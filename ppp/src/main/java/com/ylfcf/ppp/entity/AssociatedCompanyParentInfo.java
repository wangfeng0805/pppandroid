package com.ylfcf.ppp.entity;

/**
 * ������˾����
 * @author Administrator
 *
 */
public class AssociatedCompanyParentInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5955591544753445093L;
	
	private String loan	;//	��
	private String recommend;//�Ƽ���
	private String guarantee;//������
	private AssociatedCompanyInfo loanInfo;
	private AssociatedCompanyInfo recommendInfo;
	private AssociatedCompanyInfo guaranteeInfo;
	public String getLoan() {
		return loan;
	}
	public void setLoan(String loan) {
		this.loan = loan;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getGuarantee() {
		return guarantee;
	}
	public void setGuarantee(String guarantee) {
		this.guarantee = guarantee;
	}
	public AssociatedCompanyInfo getLoanInfo() {
		return loanInfo;
	}
	public void setLoanInfo(AssociatedCompanyInfo loanInfo) {
		this.loanInfo = loanInfo;
	}
	public AssociatedCompanyInfo getRecommendInfo() {
		return recommendInfo;
	}
	public void setRecommendInfo(AssociatedCompanyInfo recommendInfo) {
		this.recommendInfo = recommendInfo;
	}
	public AssociatedCompanyInfo getGuaranteeInfo() {
		return guaranteeInfo;
	}
	public void setGuaranteeInfo(AssociatedCompanyInfo guaranteeInfo) {
		this.guaranteeInfo = guaranteeInfo;
	}
	
}
