package com.ylfcf.ppp.util;

/**
 * ������
 * @author Mr.liu
 *
 */
public class Constants {
	/** �������ӷ����仯Action */
	public static final String ACTION_NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
	/** ��������WIFI״̬�����仯Action */
	public static final String ACTION_NET_WIFI_STATE_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
	
	/**
	 * ����������״̬
	 * @author Mr.liu
	 */
	public class TouchType {
		public static final int POINT_STATE_NORMAL = 0; // ����״̬

		public static final int POINT_STATE_SELECTED = 1; //����״̬

		public static final int POINT_STATE_WRONG = 2; // ����״̬
	}
	
	public class UserType {
		public static final String USER_NORMAL_PERSONAL = "normal_personal";//�����û�
		public static final String USER_VIP_PERSONAL = "vip_personal";//vip�����û�
		public static final String USER_COMPANY = "company";//��ҵ�û�
	}
	
	/**
	 * ��������
	 * @author Mr.liu
	 *
	 */
	public class WithdrawType {
		public static final String SHENHEZHONG = "�����";
		public static final String SUCCESS = "�ɹ�";
	}
	
	/**
	 * ��������
	 * @author Mr.liu
	 *
	 */
	public class ArticleType {
		public static final String NOTICE = "��վ����";
		public static final String NEWS = "��ҵ����"	;
		public static final String INFOR_NEW = "������Ѷ";
		public static final String INFOR_HOT = "������Ѷ";
		public static final String ABOUT_US = "��������";
	}
	
	/**
	 * ר������ͣ���ʲôר�⣩
	 * @author Administrator
	 *
	 */
	public class TopicType {
		public static final String CHONGZHISONG = "10";//��ֵ��
		public static final String ZHUCESONG = "20";//ע����
		public static final String JIAXI = "30";//��Ϣˬ����
		public static final String TOUZIFANLI = "40";//Ͷ�ʷ���
		public static final String XINGYUNZHUANPAN = "50";//���˴�ת��
		public static final String HOT_SUMMER = "60";//����һ��׬��ͣ
		public static final String YYY_JX = "70";//Ԫ��ӯ��Ϣ�
		public static final String RED_BAG = "80";//���ר��
		public static final String TUIGUANGYUAN = "90";//�ƹ�Ա��ר��
		public static final String FRIENDS_CIRCLE = "100";//��ǿ����Ȧ
		public static final String FLOAT_RATE = "110";//��������
		public static final String JXQ_RULE = "120";//��Ϣȯʹ�ù���
		public static final String GQQD = "130";//����ǩ��
		public static final String OCT_TZFX = "140";//10��Ͷ��ӭ�´����
		public static final String CXFT_FLDJ = "150";//������Ͷ����������
		public static final String DOUBLE_ELEVEN_2016 = "160";//˫ʮһ�
		public static final String TWOYEARS_FANXIAN = "170";//�����귵�ֻ��
		public static final String SECKILL = "180";//��ʱ���
		public static final String SRZX_APPOINT = "190";//˽������ԤԼר��
		public static final String YJY_APPOINT = "200";//Ԫ��ӯԤԼר��
	}
	
	/**
	 * ��BannerͼƬҪ��ת��Activity�ı�ţ���ӦBannerInfo���������article_id�ֶΣ�
	 * @author Mr.liu
	 *
	 */
	public class ActivityCode{
		public static final String YYY_DETAILS_ACTIVITY = "1";//Ԫ��ӯ����ҳ�档
		public static final String XSB_DETAILS_ACTIVITY = "2";//���ֱ�����ҳ�档
		public static final String DQLC_LIST_ACTIVITY = "3";//������Ʋ�Ʒ���б�ҳ�档
		public static final String VIP_LIST_ACTIVITY = "4";//VIP��Ʒ���б�ҳ�档
		public static final String DZP_TEMP_ACTIVITY = "5";//�����ת�̻ҳ�档
		public static final String SRZX_APPOINT_ACTIVITY = "6";//˽�������Ʒ��ԤԼҳ��
		public static final String FLJH_ACTIVITY = "7";//��Ա�����ƻ�
		public static final String XCFL_ACTIVITY = "8";//�´������	
		public static final String LXFX_ACTIVITY = "9";//������ �����
		public static final String SIGN_ACTIVITY = "10";//���·�ǩ���
		public static final String FLJH_ACTIVITY_02 = "11";//��Ա�����ƻ�2��
		public static final String YQHY_ACTIVITY = "12";//4�·ݻ������ѷ���
		public static final String QXJ5_ACTIVITY = "13";//5�·�ÿ��һ���ֽ�
	}
	
	public class PrizeName{
		public static final String PRIZE_NAME_JINKANGNI = "�����Ż�ȯ";
		public static final String PRIZE_NAME_VITA = "VITA�������ȯ";
		public static final String PRIZE_NAME_YOUHUA = "�ֻ���Ů��Ʒȯ";
	}
}
