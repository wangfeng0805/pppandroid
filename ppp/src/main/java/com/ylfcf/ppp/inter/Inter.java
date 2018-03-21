package com.ylfcf.ppp.inter;

import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.ProductPageInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;

/**
 * �ص��ӿ���
 * @author Administrator
 *
 */
public class Inter {
	
	public interface OnApiQueryBack{
		void back(AppInfo object);
	}
	
	/**
	 * ͨ�õĻص��ӿ�
	 * @author Administrator
	 *
	 */
	public interface OnCommonInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * ע��
	 * @author Administrator
	 *
	 */
	public interface OnRegisteInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * ��¼
	 * @author Administrator
	 *
	 */
	public interface OnLoginInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * �û���������˻�
	 * @author Administrator
	 *
	 */
	public interface OnUserRMBAccountInter{
		void back(UserRMBAccountInfo info);
	}
	
	/**
	 * �û���Ԫ����˻�
	 * @author Administrator
	 *
	 */
	public interface OnUserYUANAccountInter{
		void back(BaseInfo info);
	}
	
	/**
	 * Ͷ��
	 * @author Administrator
	 *
	 */
	public interface OnBorrowInvestInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * Ͷ�ʼ�¼�б�
	 * @author Administrator
	 *
	 */
	public interface OnInvestRecordListInter{
		void back(InvestRecordPageInfo pageInfo);
	}
	
	/**
	 * �����û���Ϣ
	 * @author Administrator
	 *
	 */
	public interface OnUpdateUserInfoInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * �����ֻ������ȡ�û���Ϣ
	 * @author Administrator
	 *
	 */
	public interface OnGetUserInfoByPhone{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * ��Ŀ����
	 * @author Administrator
	 */
	public interface OnProjectDetails{
		void back(ProjectInfo projectInfo);
	}
	
	/**
	 * �û����п���Ϣ
	 * @author Administrator
	 *
	 */
	public interface OnUserBankCardInter{
		void back(BaseInfo info);
	}
	
	/**
	 * �û��Ƿ��Ѿ�ʵ����֤
	 * @author Mr.liu
	 */
	public interface OnIsVerifyListener{
		/*
		 * �û��Ƿ��Ѿ�ʵ����֤
		 */
		void isVerify(boolean flag ,Object object);
		/*
		 * �û��Ƿ��Ѿ����ý�������
		 */
		void isSetWithdrawPwd(boolean flag,Object object);
	}
	
	/**
	 * �û��Ƿ��Ѱ�
	 * @author Mr.liu
	 */
	public interface OnIsBindingListener{
		void isBinding(boolean flag, Object object);
	}
	
	/**
	 * �Ƿ�ΪVIP�û�
	 * @author Mr.liu
	 *
	 */
	public interface OnIsVipUserListener{
		void isVip(boolean isvip);
	}
	
	/**
	 * �û��Ƿ�Ͷ�ʹ�Ԫ�ű�
	 * @author Mr.liu
	 *
	 */
	public interface OnIsYXBInvestorListener{
		void isYXBInvestor(boolean flag);
	}
}
