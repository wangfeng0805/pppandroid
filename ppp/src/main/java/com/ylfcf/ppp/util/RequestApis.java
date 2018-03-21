package com.ylfcf.ppp.util;

import android.content.Context;

import com.ylfcf.ppp.async.AsyncUserBankCard;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.async.AsyncYXBInvestRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.inter.Inter.OnIsYXBInvestorListener;
import com.ylfcf.ppp.inter.Inter.OnUserBankCardInter;

/**
 * �ӿ������API
 * @author Mr.liu
 *
 */
public class RequestApis {
	/**
	 * �����û��Ƿ��Ѿ�ʵ����֤�ӿ�
	 * @param userId �û�iD
	 * @param verifyListener �Ƿ�ʵ����֤�Ľӿڻص�
	 */
	public static void requestIsVerify(Context context,String userId,final OnIsVerifyListener verifyListener){
		AsyncUserSelectOne task = new AsyncUserSelectOne(context, userId, "", "", "",new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo == null){
					baseInfo = new BaseInfo();
					baseInfo.setMsg("�������粻����");
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null && !"".equals(userInfo.getReal_name()) && !"".equals(userInfo.getId_number())){
						//�Ѿ�ʵ����֤
						verifyListener.isVerify(true, userInfo);
					}else{
						verifyListener.isVerify(false, baseInfo.getMsg());
					}
					if(userInfo != null && !"".equals(userInfo.getDeal_pwd())){
						//�Ѿ�������������
						verifyListener.isSetWithdrawPwd(true, userInfo);
					}else{
						verifyListener.isSetWithdrawPwd(false, baseInfo.getMsg());
					}
				}else{
					verifyListener.isVerify(false, baseInfo.getMsg());
					verifyListener.isSetWithdrawPwd(false, baseInfo.getMsg());
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �ж��Ƿ�ΪVIP�û�
	 * @param context
	 * @param userId
	 * @param vipListener
	 */
	public static void requestIsVip(Context context,String userId,final OnIsVipUserListener vipListener){
		AsyncUserSelectOne task = new AsyncUserSelectOne(context, userId, "", "","", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo == null){
					baseInfo = new BaseInfo();
					baseInfo.setMsg("�������粻����");
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null && userInfo.getType().contains("vip")){
						//�Ѿ�ʵ����֤
						vipListener.isVip(true);
					}else{
						vipListener.isVip(false);
					}
				}else{
					vipListener.isVip(false);
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �����û��Ƿ��Ѿ��󿨽ӿ�
	 * @param context
	 * @param userId �û�ID
	 * @param type  ö�����ͣ�����
	 * @param bindindListener �ӿ�����ɹ���Ļص��ӿ�
	 */
	public static void requestIsBinding(Context context,String userId,String type,final OnIsBindingListener bindindListener){
		AsyncUserBankCard task = new AsyncUserBankCard(context, userId, type, new OnUserBankCardInter() {
			@Override
			public void back(BaseInfo info) {
				if(info == null){
					info = new BaseInfo();
					info.setMsg("�������粻����");
				}
				int resultCode = SettingsManager.getResultCode(info);
				if (resultCode == 0) {
					UserCardInfo bankCardInfo = info.getUserCardInfo();
					if (bankCardInfo != null && "��".equals(bankCardInfo.getIs_binding())) {
						//�Ѱ�
						bindindListener.isBinding(true, bankCardInfo);
					} else {
						//δ��
						bindindListener.isBinding(false, info.getMsg());
					}
				} else {
					//����ʧ�ܣ�������ԭ��ȵ�
					bindindListener.isBinding(false, info.getMsg());
				} 
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �ж��Ƿ�Ͷ�ʹ�Ԫ�ű�
	 * @param userId
	 * @param page
	 * @param pageSize
	 */
	public static void requestIsYXBInvestor(Context context,String userId,int page,int pageSize,final OnIsYXBInvestorListener listener){
		AsyncYXBInvestRecord yxbInvestTask = new AsyncYXBInvestRecord(context, "", userId, "", String.valueOf(page), String.valueOf(pageSize), 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								listener.isYXBInvestor(true);
							}else{
								listener.isYXBInvestor(false);
							}
						}else{
							listener.isYXBInvestor(false);
						}
					}
				});
		yxbInvestTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
