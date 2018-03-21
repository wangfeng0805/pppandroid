/**
  * All rights reserved.
 */
package com.ylfcf.ppp.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BankInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.util.Constants.UserType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Settings of this application.
 * ������
 * @author Waggoner.wang
 */

public class SettingsManager extends DefaultPreferences {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//����֧��ͨ������ʱ��
	private static final String bfPublishDate = "2016-06-29 15:00:00";
	//�����ת�̻��ֹʱ��
	private static final String dzpPrizeEndDate = "2016-09-21 23:59:59";
	//������Ƹ�����������ʱ��
	private static final String floatRateStartDate = "2016-09-14 23:59:59";
	
	//�����Ϣ���ʼ�ͽ���ʱ��
	private static final String guoqingJiaxiStartDate = "2016-09-28 00:00:00";
	private static final String guoqingJiaxiEndDate = "2016-10-10 23:59:59";
	
	//������ж��������ʼ�ͽ���ʱ��
	private static final String twoyearsTZFXStartDate = "2016-12-01 00:00:00";
	private static final String twoyearsTZFXEndDate = "2016-12-22 23:59:59";
	
	//��ʱ����Ŀ�ʼ�ͽ���ʱ��
	public static final String xsmbStartDate = "2016-12-23 00:00:00";
	public static final String xsmbEndDate = "2016-12-31 23:59:59";
	
	//��Ա�����ƻ����ʼ�ͽ���ʱ��
	public static final String fljhStartDate = "2016-12-23 00:00:00";
	public static final String fljhEndDate = "2017-02-05 23:59:59";
	
	//�´������Ŀ�ʼ�ͽ���ʱ��
	private static final String xchbStartDate = "2017-01-05 00:00:00";
	private static final String xchbEndDate = "2017-02-05 23:59:59";
	
	//����� ������
	private static final String lxfxStartDate = "2017-02-06 00:00:00";
	private static final String lxfxEndDate = "2017-02-23 23:59:59";
	
	//ǩ�����ʼ����ʱ��
	private static final String signStartDate = "2017-03-01 00:00:00";
	private static final String signEndDate = "2017-03-27 00:00:00";
	
	//������ѻ��ʼ����ʱ��
	private static final String yqhyStartDate = "2017-04-01 00:00:00";
	private static final String yqhyEndDate = "2017-05-31 00:00:00";

	public static final String yyyJIAXIStartTime = "2017-06-01 11:00:00";
	public static final String yyyJIAXIEndTime = "2017-07-01 00:00:00";

	//2017��7�·ݻ��ʼ����ʱ��
	public static final String activeJuly2017_StartTime = "2017-07-03 00:00:00";
	public static final String activeJuly2017_EndTime = "2017-07-31 00:00:00";

	//2017��ʮ��˫�ڻ
	public static final String activeOct2017_StartTime = "2017-10-01 00:00:00";
	public static final String activeOct2017_EndTime = "2017-10-08 23:59:59";

	//2017��˫11��Ϣ�
	public static final String activeNov2017_StartTime = "2017-10-10 00:00:00";
	public static final String activeNov2017_EndTime = "2017-11-12 23:59:59";

	public static final String eleContract_startTime = "2017-12-14 00:00:00";//����ǩ������ʱ��

	public static final String USER_FROM = "��׿APP";//�û���Դ������Դ��Ԫ������վ����΢�Ż���app�ȵȡ��˴�Ϊд��
	public static final String APP_FIRST	= "appfirst";//�ж�Ӧ���Ƿ����״δ򿪡�
	public static final String USER_PASSWORD	= "password";
	public static final String USER_ACCOUNT		= "useraccount";//���ڸ����û���˵���ֻ��� ������ҵ�û���˵����ҵ
	public static final String USER_COMP_PHONE = "comp_phone";//��ҵ�û�����ϵ�ֻ���
	public static final String USER_NAME	=	"username";
	public static final String USER_TYPE = "usertype";//�û����� personal:�����û�  company:��ҵ�û�
	public static final String USER_ID 		=	 "userid";
	public static final String USER_REG_TIME = "userregtime";//�û�ע��ʱ��
	public static final String XMPP_ACCOUNT		= "xmppaccount";
	public static final String XMPP_PASSWORD	= "xmpppassword";
	public static final String LOGIN_DATE		= "LoginDate";
	public static final String MY_PHONE_NUMBER	= "my_phone_number";
	public static final String masterSeed       = "uidasdf9031348ader";
	public static final String GET_MSG_SEND_KAIGUAN = "msg_send_kaiguan";//�Ƿ������Ϣ����
	public static final String DEAL_PWD_KAIGUAN = "msg_send_kaiguan";//�������뿪��
	public static final String DEAL_PWD = "deal_pwd";//��������
	public static final String MAINACTIVITY_PRODUCTLIST = "productlist";//��ת����ҳʱ�Ƿ�Ҫ������Ʒ�б�ҳ��
	public static final String MAINACTIVITY_ACCOUNT = "account";//��ת����ҳʱ�Ƿ�Ҫ�����ҵ��˻�ҳ��
	public static final String MAINACTIVITY_FIRSTPAGE = "firstpage";//��ҳ
	public static final String DOWNLOAD_APK_NUM = "download_apk";//apk���صĽ��̺�
	public static final String SHARED_BANNER_CACHE_TIME = "banner_cache_time";
	public static final String ACCOUNTCENTER_ISFLOAT = "account_center_float";
	
	public static final Map<String,BankInfo> bankMap = new LinkedHashMap<String,BankInfo>();//���֧�������б� keyΪbankcode  valueΪ���ж���
	public static final Map<String,Integer> bankLogosMap = new LinkedHashMap<String,Integer>();//���֧������logo keyΪbank_code  valueΪͼƬ��Դid
	
	public static String[] bankCodes = new String[]{"ICBC","ABC","BOC","CCB","PSBC","CITIC","CEB","PAB","BCOM","CIB",
		"CMBC","SPDB","SHB","CMB","HXB","CGB"};
	public static int[] bankLogos = new int[]{R.drawable.banklogo_icbc,R.drawable.banklogo_abc,R.drawable.banklogo_boc,R.drawable.banklogo_ccb,R.drawable.banklogo_psbc,
		R.drawable.banklogo_citic,R.drawable.banklogo_ceb,R.drawable.banklogo_pab,R.drawable.banklogo_bcom,R.drawable.banklogo_cib,R.drawable.banklogo_cmbc,
		R.drawable.banklogo_spdb,R.drawable.banklogo_shb,R.drawable.banklogo_cmb,R.drawable.banklogo_hxb,R.drawable.banklogo_cgb};
	/**
	 * ע�����֤��ģ��
	 */
	private static final String SMS_REGISTE_VERFIY_TEMP = "a:1:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";}";//�滻��ʱ��AUTHCODE�滻����λ���ֶ�����֤�뼴�ɡ�
	/**
	 * �һ�����  ��֤��ģ��
	 */
	private static final String SMS_FORGET_PWD_VERFIY_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * �һ���������
	 */
	private static final String SMS_GETBACK_DEAL_PWD_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"USER_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * �һ��������� -- ��ҵ
	 */
	private static final String SMS_GETBACK_DEAL_PWD_COMP_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"USER_NAME\";s:12:\"USERNAME\";}";
	
	/**
	 * �һ��������� --- Ĭ��ģ�� �û���Ϊ�յ����
	 */
	private static final String SMS_GETBACK_DEAL_PWD_TEMP_DEFAULT = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"USER_NAME\";s:4:\"USERNAME\";}";
	
	/**
	 * ������������
	 */
	private static final String SMS_SETTING_DEAL_PWD_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * ������������  ----  ��ҵ�û�
	 */
	private static final String SMS_SETTING_DEAL_PWD_COMP_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:12:\"USERNAME\";}";
	
	private static final String SMS_SETTING_DEAL_PWD_TEMP_DEFAULT = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:4:\"USERNAME\";}";
	
	/**
	 * ע��ɹ�
	 */
	private static final String SMS_REGISTER_SUCCESS = "a:1:{s:9:\"USER_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * ��������
	 */
	private static final String SMS_APPLY_CASH_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:NAMELENGTH:\"USERNAME\";}";
	
	/**
	 * �������� --  ��ҵ
	 */
	private static final String SMS_APPLY_CASH_COMP_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:12:\"USERNAME\";}";
	
	private static final String SMS_APPLY_CASH_TEMP_DEFAULT = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:4:\"USERNAME\";}";
	
	public static ExecutorService FULL_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(4);

	private SettingsManager() {
	}

	/**
	 * ��ȡ�û���Դ����manifest.xml�ļ���meta-data��������
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getUserFromSub(Context context){
		ApplicationInfo applicationInfo;
		String userFrom = "";
		try {
			applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			userFrom = applicationInfo.metaData.getString("USER_FROM_SUB");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			userFrom = "";
		}
		return userFrom;
	}
	
	/**
	 * ����ע��ʱ�����ж��Ƿ�Ϊ���û����������û���
	 * @param regTime ��ʽyyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static boolean checkIsNewUser(String regTime){
		Date regTimeD = null;
		Date bfPublishTimeD = null;
		try {
			regTimeD = sdf.parse(regTime);
			bfPublishTimeD = sdf.parse(bfPublishDate);
			if(bfPublishTimeD.compareTo(regTimeD) == -1){
				//ע��ʱ����ڱ�������ʱ��Ϊ���û�
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * ���·� ÿ��һ���ֽ�
	 * @param baseInfo
	 * @return
	 */
	public static boolean checkRobCashIsStart(BaseInfo baseInfo){
		int week = 0;
		int day = 0;
		try{
			Date sysDate = sdf.parse(baseInfo.getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(sysDate);
			day = cal.get(Calendar.DATE);
			week=cal.get(Calendar.DAY_OF_WEEK)-1;
			YLFLogger.d("�����Ǳ���"+day+"��--------------------"+"����������"+week);
			if(week == 1 && day != 8){
				//������һ
				int hourInt = cal.get(Calendar.HOUR_OF_DAY);
				if(hourInt >= 20){
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �ж϶�����Ʋ�Ʒ�������ʸ����Ƿ�����
	 * @return
	 */
	public static boolean checkFloatRate(ProductInfo info){
		Date borrowStartTime = null;
		Date dqlcFloatRateStartDate = null;
		try {
			dqlcFloatRateStartDate = sdf.parse(floatRateStartDate);
			borrowStartTime = sdf.parse(info.getStart_time());
			if(dqlcFloatRateStartDate.compareTo(borrowStartTime) == -1){
				//��ʾ���������Ѿ����ߡ�
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * �жϹ����Ϣ��Ƿ������
	 * @return
	 */
	public static boolean checkGuoqingJiaxiActivity(){
		Date nowTime = new Date();
		Date guoqingJiaxiStartTime = null;
		Date guoqingJiaxiEndTime = null;
		try {
			guoqingJiaxiStartTime = sdf.parse(guoqingJiaxiStartDate);
			guoqingJiaxiEndTime = sdf.parse(guoqingJiaxiEndDate);
			if(guoqingJiaxiStartTime.compareTo(nowTime) == -1 && guoqingJiaxiEndTime.compareTo(nowTime) == 1){
				//��ʾ��Ϣ����ڽ�����
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * �ж�������ж�������Ƿ������
	 * @return
	 */
	public static boolean checkTwoYearsTZFXActivity(){
		Date nowTime = new Date();
		Date xsmbStartTime = null;
		Date xsmbEndTime = null;
		try {
			xsmbStartTime = sdf.parse(twoyearsTZFXStartDate);
			xsmbEndTime = sdf.parse(twoyearsTZFXEndDate);
			if(xsmbStartTime.compareTo(nowTime) == -1 && xsmbEndTime.compareTo(nowTime) == 1){
				//��ʾͶ�ʷ��ֻ���ڽ�����
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * ����ϵͳʱ���ж�ĳ����Ƿ�ʼ
	 * @param sysTime ��ʽyyyy-MM-dd HH:mm:ss
	 * @param startTime ��ʽyyyy-MM-dd HH:mm:ss
	 * @param endTime ��ʽyyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static int checkActiveStatusBySysTime(String sysTime,String startTime,String endTime){
		Date sysDate = null;
		try{
			sysDate = sdf.parse(sysTime);
		}catch (Exception e){
			e.printStackTrace();
		}

		if(sysTime == null)
			return -1;
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(startTime);
			endDate = sdf.parse(endTime);
			if(startDate.compareTo(sysDate) == -1 && endDate.compareTo(sysDate) == 1){
				//��ʾ����ڽ�����
				return 0;
			}else if(endDate.compareTo(sysDate) == -1 ){
				//�����
				return -1;
			}else if(startDate.compareTo(sysDate) == 1){
				//��δ��ʼ
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * �´����
	 * @return
	 */
	public static int checkXCHBActivity(Date nowTime){
		Date xchbStartTime = null;
		Date xchbEndTime = null;
		try {
			xchbStartTime = sdf.parse(xchbStartDate);
			xchbEndTime = sdf.parse(xchbEndDate);
			if(xchbStartTime.compareTo(nowTime) == -1 && xchbEndTime.compareTo(nowTime) == 1){
				//��ʾͶ�ʷ��ֻ���ڽ�����
				return 0;
			}else if(xchbEndTime.compareTo(nowTime) == -1){
				//�����
				return -1;
			}else if(xchbStartTime.compareTo(nowTime) == 1){
				//��δ��ʼ
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * ����� �����ֻ�Ƿ�ʼ
	 * @return
	 */
	public static int checkLXFXActivity(){
		Date nowTime = new Date();
		Date lxfxStartTime = null;
		Date lxfxEndTime = null;
		try {
			lxfxStartTime = sdf.parse(lxfxStartDate);
			lxfxEndTime = sdf.parse(lxfxEndDate);
			if(lxfxStartTime.compareTo(nowTime) == -1 && lxfxEndTime.compareTo(nowTime) == 1){
				//��ʾͶ�ʷ��ֻ���ڽ�����
				return 0;
			}else if(lxfxEndTime.compareTo(nowTime) == -1 ){
				//�����
				return -1;
			}else if(lxfxStartTime.compareTo(nowTime) == 1){
				//��δ��ʼ
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * ǩ����Ƿ��Ѿ���ʼ
	 * @return
	 */
	public static int checkSignActivity(Date nowTime){
		Date signStartTime = null;
		Date signEndTime = null;
		try {
			signStartTime = sdf.parse(signStartDate);
			signEndTime = sdf.parse(signEndDate);
			if(signStartTime.compareTo(nowTime) == -1 && signEndTime.compareTo(nowTime) == 1){
				//��ʾǩ������ڽ�����
				return 0;
			}else if(signEndTime.compareTo(nowTime) == -1 ){
				//�����
				return -1;
			}else if(signStartTime.compareTo(nowTime) == 1){
				//��δ��ʼ
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * ������ѻ�Ƿ��Ѿ���ʼ
	 * @return
	 */
	public static int checkYQHYActivity(Date nowTime){
		Date yqhyStartTime = null;
		Date yqhyEndTime = null;
		try {
			yqhyStartTime = sdf.parse(yqhyStartDate);
			yqhyEndTime = sdf.parse(yqhyEndDate);
			if(yqhyStartTime.compareTo(nowTime) == -1 && yqhyEndTime.compareTo(nowTime) == 1){
				//��ʾǩ������ڽ�����
				return 0;
			}else if(yqhyEndTime.compareTo(nowTime) == -1 ){
				//�����
				return -1;
			}else if(yqhyStartTime.compareTo(nowTime) == 1){
				//��δ��ʼ
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
     * ��ȡ��Ļ�ֱ���
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = 0;
		int height = 0;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			DisplayMetrics dm = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(dm);
			width  = dm.widthPixels ;
			height = dm.heightPixels;
		}else{
			width = windowManager.getDefaultDisplay().getWidth();// �ֻ���Ļ�Ŀ��
			height = windowManager.getDefaultDisplay().getHeight();// �ֻ���Ļ�ĸ߶�
		}

		int result[] = { width, height };
		return result;
	}
    
    /**
     * �Ƿ�Ҫ��ת����Ʒ�б�ҳ��
     * @param context
     * @param flag
     */
    public static void setMainProductListFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, MAINACTIVITY_PRODUCTLIST, flag);
    }

	/**
	 * �˻����ĸ����Ƿ���ʾ
	 * @param context
	 * @param flag
	 */
	public static void setAccountCenterFloatFlag(Context context,boolean flag){
		DefaultPreferences.setBoolean(context,ACCOUNTCENTER_ISFLOAT,flag);
	}
    
    /**
     * �Ƿ�Ҫ��ת���ҵ��˻�ҳ��
     * @param context
     * @param flag
     */
    public static void setMainAccountFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, MAINACTIVITY_ACCOUNT, flag);
    }
    
    /**
     * ������ ��ȡ��dialog�Ƿ���ʾ
     * @param context
     * @param key �˴�Ϊuserid+"lxfx"
     * @param flag
     */
    public static void setLXFXJXQFlag(Context context,String key,boolean flag){
    	DefaultPreferences.setBoolean(context, key, flag);
    }
    
    /**
     * ��Ա����2�� ��ȡ��dialog�Ƿ���ʾ
     * @param context
     * @param key �˴�Ϊuserid+"hyfl02"
     * @param flag
     */
    public static void setHYFLFlag(Context context,String key,boolean flag){
    	DefaultPreferences.setBoolean(context, key, flag);
    }
    
    /**
     * �Ƿ�Ҫ��ת����ҳ
     * @param context
     * @param flag
     */
    public static void setMainFirstpageFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, MAINACTIVITY_FIRSTPAGE, flag);
    }
    
    /**
     * ����ҳ���Ƿ���ת����Ʒ�б�ҳ��
     * @param context
     * @return
     */
    public static boolean getMainProductListFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, MAINACTIVITY_PRODUCTLIST, false);
    	return flag;
    }

    public static boolean getAccountCenterFloatFlag(Context context){
		boolean flag = DefaultPreferences.getBoolean(context, ACCOUNTCENTER_ISFLOAT, false);
		return flag;
	}
    
    /**
     * ����ҳ���Ƿ���ת���ҵ��˻�ҳ��
     * @param context
     * @return
     */
    public static boolean getMainAccountFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, MAINACTIVITY_ACCOUNT, false);
    	return flag;
    }
    
    /**
     * �û��Ƿ�رո���ʾ
     * @param key �˴�Ϊuserid+"lxfx"
     */
    public static boolean getLXFXJXQFlag(Context context,String key){
    	boolean flag = DefaultPreferences.getBoolean(context, key, true);
    	return flag;
    }
    
    /**
     * �û��Ƿ�رո���ʾ
     * @param key �˴�Ϊuserid+"hyfl02"
     */
    public static boolean getHYFLFlag(Context context,String key){
    	boolean flag = DefaultPreferences.getBoolean(context, key, true);
    	return flag;
    }
    
    /**
     * �Ƿ���ת����ҳ
     * @param context
     * @return
     */
    public static boolean getMainFirstpageFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, MAINACTIVITY_FIRSTPAGE, false);
    	return flag;
    }
    
    /**
     * �Ƿ������Ϣ����
     * @param context
     * @param flag
     */
    public static void setMsgSendFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, GET_MSG_SEND_KAIGUAN, flag);
    }
    
    /**
     * �Ƿ������Ϣ����
     * @param context
     * @return
     */
    public static boolean getMsgSendFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, GET_MSG_SEND_KAIGUAN, true);
    	return flag;
    }
    
    /**
     * �������뿪�أ�
     * @param context
     * @param flag
     */
    public static void setDealPwdFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, DEAL_PWD_KAIGUAN, flag);
    }
    
    /**
     * �������뿪�أ�Ĭ��Ҫ����
     * @param context
     * @return
     */
    public static boolean getDealPwdFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, DEAL_PWD_KAIGUAN, true);
    	return flag;
    }
    
    /**
     * �������뿪�أ�
     * @param context
     * @param flag
     */
    public static void setDownloadApkNum(Context context,long flag){
    	DefaultPreferences.setLong(context, DOWNLOAD_APK_NUM, flag);
    }
    
    /**
     * �������뿪�أ�Ĭ��Ҫ����
     * @param context
     * @return
     */
    public static long getDownloadApkNum(Context context){
    	long flag = DefaultPreferences.getLong(context, DOWNLOAD_APK_NUM,0);
    	return flag;
    }
	
	/**
	 * ��DefaultPreference�ж�ȡ�û��˺ţ���AES���ܺ󷵻ء�
	 * @return �û��˺�
	 */
	public static String getUser(Context context) {
		String user = DefaultPreferences.getString(context,USER_ACCOUNT, "");
		try {
//			if (!user.isEmpty())
//				user = SimpleCrypto.decrypt(masterSeed, user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
	
	/**
	 * banner�Ļ���ʱ�� ������ˢ��ʱ��Ϊ12Сʱ��
	 * @param context
	 * @param cacheTime
	 */
	public static void setBannerCacheTime(Context context,long cacheTime){
		DefaultPreferences.setLong(context, SHARED_BANNER_CACHE_TIME, cacheTime);
	}
	
	/**
	 * banner�Ļ���ʱ�� ������ˢ��ʱ��Ϊ12Сʱ��
	 * @param context
	 * @return
	 */
	public static long getBannerCacheTime(Context context){
		long cacheTime = DefaultPreferences.getLong(context, SHARED_BANNER_CACHE_TIME, 0);
		return cacheTime;
	}
	
	public static void setUserName(Context context,String username){
		if(username!=null){
			DefaultPreferences.setString(context,USER_NAME, username);
		}
	}
	
	public static String getUserName(Context context){
		String username = DefaultPreferences.getString(context,USER_NAME, "");
		return username;
	}
	
	public static void setCompPhone(Context context,String compPhone){
		if(compPhone !=null){
			DefaultPreferences.setString(context,USER_COMP_PHONE, compPhone);
		}
	}
	
	public static String getCompPhone(Context context){
		String compPhone = DefaultPreferences.getString(context,USER_COMP_PHONE, "");
		return compPhone;
	}
	
	public static void setUserType(Context context,String userType){
		if(userType!=null){
			DefaultPreferences.setString(context,USER_TYPE, userType);
		}
	}
	
	public static String getUserType(Context context){
		String usertype = DefaultPreferences.getString(context,USER_TYPE, "");
		return usertype;
	}
	
	public static void clearSharedAllData(Context context){
		DefaultPreferences.clearAll(context);
	}
	
	/**
	 * ���û��˺ž�AES���ܺ󱣴浽DefaultPreference
	 * @param account
	 */
	public static void setUser(Context context,String account) {
			try {
//				String encrypt	= SimpleCrypto.encrypt(masterSeed, account);
//				DefaultPreferences.setString(context,USER_ACCOUNT, encrypt);
				DefaultPreferences.setString(context,USER_ACCOUNT, account);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * @param context
	 * @param userId
	 */
	public static void setUserId(Context context,String userId){
			DefaultPreferences.setString(context,USER_ID, userId);
//			try {
//				String encrypt	= SimpleCrypto.encrypt(masterSeed, userId);
//				DefaultPreferences.setString(context,USER_ID, encrypt);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
	}
	
	public static String getUserId(Context context){
		String userId = DefaultPreferences.getString(context,USER_ID, "");
//		try {
//			if (!userId.isEmpty())
//				userId = SimpleCrypto.decrypt(masterSeed, userId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return userId;
	}
	
	/**
	 * �û�ע��ʱ��
	 * @param context
	 */
	public static void setUserRegTime(Context context,String regTime){
		DefaultPreferences.setString(context,USER_REG_TIME, regTime);
	}
	
	/**
	 * ע��ʱ��
	 * @param context
	 * @return
	 */
	public static String getUserRegTime(Context context){
		String regTime = DefaultPreferences.getString(context,USER_REG_TIME, "");
		return regTime;
	}
	
	public static String getLoginPassword(Context context) {
		String password = DefaultPreferences.getString(context,USER_PASSWORD, "");
//		try {
//			if (!password.isEmpty())
//				password = SimpleCrypto.decrypt(masterSeed, password);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return password;
	}
	
	/**
	 * 
	 * @param context
	 * @param password
	 * @param isEncry  �Ƿ���Ҫ����
	 */
	public static void setLoginPassword(Context context,String password,boolean isEncry) {
//			try {
//				String encrypt	= SimpleCrypto.encrypt(masterSeed, password);
//				DefaultPreferences.setString(context,USER_PASSWORD, encrypt);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		if(isEncry){
			String encrypt	= Util.md5Encryption(password);
			DefaultPreferences.setString(context,USER_PASSWORD, encrypt);
		}else{
			DefaultPreferences.setString(context,USER_PASSWORD, password);
		}
			
	}
	
	/**
	 * �ж��Ƿ�Ϊ�״δ�APP������Ϊ�״δ�
	 * @return
	 */
	public static String getAppFirst(Context context){
		return DefaultPreferences.getString(context,APP_FIRST, "");
	}
	
	public static void setAppFirst(Context context,String firstApp){
		DefaultPreferences.setString(context,APP_FIRST, firstApp);
	}
	
	public static String getXmppAccount(Context context) {
		return DefaultPreferences.getString(context,XMPP_ACCOUNT, "");
	}
	
	public static void setXmppAccount(Context context,String account) {
		DefaultPreferences.setString(context,XMPP_ACCOUNT, account);
	}
	
	public static String getXmppPassword(Context context) {
		return DefaultPreferences.getString(context,XMPP_PASSWORD, "");
	}
	
	public static void setXmppPassword(Context context,String password) {
		DefaultPreferences.setString(context,XMPP_PASSWORD, password);
	}
	
	public static void setLoginDate(Context context) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        DefaultPreferences.setString(context,LOGIN_DATE, format.format(new Date()));
	}
	
	public static void setMyPhoneNumber(Context context,String phone) {
		DefaultPreferences.setString(context,MY_PHONE_NUMBER, phone);
	}
	
	/**
	 * ��һ���Ƕ�����֤��
	 * �ڶ����ɶ�����֤��ƴ�ӳɵĲ���
	 * @return
	 */
	public static String[] getSMSRegisteParams(){
		String authCode = getSMSAuthCode();
		return new String[]{authCode,SMS_REGISTE_VERFIY_TEMP.replace("AUTHCODE", authCode)};
	}
	
	/**
	 * �һ����� ����ģ��
	 * @param userId  
	 * @return
	 */
	public static String[] getSMSForgetPwdParams(String userId){
		String authCode = getSMSAuthCode();
		String params = SMS_FORGET_PWD_VERFIY_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", userId);
		return new String[]{authCode,params};
	}
	
	/**
	 * �һ���������  ���Ÿ�ʽ
	 * @param username
	 * @return
	 */
	public static String[] getSMSGetbackDealPwdParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_GETBACK_DEAL_PWD_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * �һ���������  ���Ÿ�ʽ ��ҵ�û�
	 * @param username
	 * @return
	 */
	public static String[] getSMSGetbackDealPwdCompParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_GETBACK_DEAL_PWD_COMP_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * �һ��������� Ĭ�ϸ�ʽ �û���Ϊ��
	 * @return
	 */
	public static String[] getSMSGetbackDealPwdParamsDefault(){
		String authCode = getSMSAuthCode();
		String params = SMS_GETBACK_DEAL_PWD_TEMP_DEFAULT.replace("AUTHCODE", authCode).replace("USERNAME", "�û�");
		return new String[]{authCode,params};
	}
	
	/**
	 * ������������
	 * @param username
	 * @return
	 */
	public static String[] getSMSSettingDealPwdParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_SETTING_DEAL_PWD_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * ������������   ----  ��ҵ�û�
	 * @param username
	 * @return
	 */
	public static String[] getSMSSettingDealPwdCompParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_SETTING_DEAL_PWD_COMP_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * ������������ Ĭ��ģ��  �û���Ϊ��
	 * @return
	 */
	public static String[] getSMSSettingDealPwdParamsDefault(){
		String authCode = getSMSAuthCode();
		String params = SMS_SETTING_DEAL_PWD_TEMP_DEFAULT.replace("AUTHCODE", authCode).replace("USERNAME", "�û�");
		return new String[]{authCode,params};
	}
	
	/**
	 * ע��ɹ�����ģ��
	 * @param username
	 * @return
	 */
	public static String[] getSMSRegisteSuccessParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_REGISTER_SUCCESS.replace("USERNAME", username);
		return new String[]{authCode,params};	
	}
	
	/**
	 * ��������
	 * @param username
	 * @return
	 */
	public static String[] getSMSWithdrawApplyParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_APPLY_CASH_TEMP.replace("USERNAME", username).replace("NAMELENGTH",String.valueOf(username.length()*3)).replace("AUTHCODE", authCode);
		return new String[]{authCode,params};	
	}
	
	/**
	 * �������� --- ��ҵ�û�
	 * @param username
	 * @return
	 */
	public static String[] getSMSWithdrawApplyCompParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_APPLY_CASH_COMP_TEMP.replace("USERNAME", username).replace("AUTHCODE", authCode);
		return new String[]{authCode,params};	
	}
	
	/**
	 * ��������  �û���Ϊ��
	 * @return
	 */
	public static String[] getSMSWithdrawApplyDefaultParams(){
		String authCode = getSMSAuthCode();
		String params = SMS_APPLY_CASH_TEMP_DEFAULT.replace("USERNAME", "�û�").replace("AUTHCODE", authCode);
		return new String[]{authCode,params};	
	}
	
	private static int dTemp;
	private static String getSMSAuthCode(){
		int d = (int) (Math.random()*9000+1000);
		if(d == dTemp){
			getSMSAuthCode();
		}else{
			dTemp = d;
		}
		return String.valueOf(d);
	}
	
	/**
	 * ��ȡ����״̬��
	 * @param baseInfo
	 * @return
	 */
	public static int getResultCode(BaseInfo baseInfo){
		int resultCode = -1;
		try {
			resultCode = Integer.parseInt(baseInfo.getError_id());
		} catch (Exception e) {
		}
		return resultCode;
	}
	
	/**
	 * ��ȡ���󷵻���Ϣ��������msgΪ���ֵ�ʱ��  
	 * @param baseInfo
	 * @return
	 */
	public static int getResultMsg(BaseInfo baseInfo){
		int resultMsg = -1;
		try {
			resultMsg = Integer.parseInt(baseInfo.getMsg());
		} catch (Exception e) {
		}
		return resultMsg;
	}
	
	/**
	 * �жϵ�ǰ�û��ǲ��Ǹ����û�
	 * @return
	 */
	public static boolean isPersonalUser(Context context){
		String usertype = SettingsManager.getUserType(context);
		if(UserType.USER_NORMAL_PERSONAL.equals(usertype) || UserType.USER_VIP_PERSONAL.equals(usertype)
				|| usertype == null || "".equals(usertype)){
			return true;
		}
		return false;
	}
	
	/**
	 * �жϵ�ǰ�û��ǲ�����ҵ�û�
	 * @param context
	 * @return
	 */
	public static boolean isCompanyUser(Context context){
		String usertype = SettingsManager.getUserType(context);
		if(UserType.USER_COMPANY.equals(usertype)){
			return true;
		}
		return false;
	}
}
