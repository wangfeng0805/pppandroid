package com.ylfcf.ppp.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

/**
 * URL��
 * @author Administrator
 */
public class URLGenerator {
	//��ʽ����
//	private static final String API_DOMAIN_URL = "http://www.ylfcf.com";//API����
//	private static final String WAP_DOMAIN_URL = "http://wap.ylfcf.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://api.ylfcf.com";//

//	//��ʽ����HTTPS
//	private static final String API_DOMAIN_URL = "https://www.ylfcf.com";//API����
//	private static final String WAP_DOMAIN_URL = "https://wap.ylfcf.com";//WAP����
//	private static final String API2_DOMAIN_URL = "https://api.ylfcf.com";//

	//https���Ի���
//	private static final String API_DOMAIN_URL = "https://test1.ylfcf.com";//API����
//	private static final String WAP_DOMAIN_URL = "https://wap.test1.ylfcf.com";//WAP����
//	private static final String API 2_DOMAIN_URL = "https://api.test1.ylfcf.com";//

	//��֤����
//	private static final String API_DOMAIN_URL = "http://www.dev.ylfcf.com";//API����
//	private static final String WAP_DOMAIN_URL = "http://dev.wap.ylfcf.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//���Ի���
	private static final String API_DOMAIN_URL = "http://www.test.ylfcf.com";//API����
	private static final String WAP_DOMAIN_URL = "http://wap.test.ylfcf.com";//WAP����
	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//�����ο�������
//	private static final String API_DOMAIN_URL = "http://www.ylf.com";//API����
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//��������������
//	private static final String API_DOMAIN_URL = "http://www.m_ylf.com";//API����
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://www.api.com";//

	//��������������
//	private static final String API_DOMAIN_URL = "http://www.api.com";//API����
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

//	private static final String API_DOMAIN_URL = "http://www.web";//API����
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//https
//	private static final String API_DOMAIN_URL = "https://test1.ylfcf.com";
//	private static final String WAP_DOMAIN_URL = "https://wap.test1.ylfcf.com";//WAP����
//	private static final String API2_DOMAIN_URL = "https://api.test1.ylfcf.com";//

	//��������������
//	private static final String API_DOMAIN_URL = "http://web.dev.com";
//	private static final String WAP_DOMAIN_URL = "http://wap.test.ylfcf.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//������������
//	private static final String API_DOMAIN_URL = "http://www.m_ylf.com";//API����
//	private static final String WAP_DOMAIN_URL = "http://wap.test.ylfcf.com";//WAP����
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	private static final String BASE_URL = API_DOMAIN_URL + "/API/to_api.php";
	private final String ADD_PHONEINFO_URL = API2_DOMAIN_URL + "/user/phone_info/add";//���ֻ���ϵͳ�汾���ֻ��ͺŵ���Ϣ�����̨

	public static final String REGISTE_AGREEMENT_URL = WAP_DOMAIN_URL + "/home/index/protocol#app";

	public static final String TWOYEARS_TZFX_URL = WAP_DOMAIN_URL + "/home/index/zlq.html";
	public static final String REDBAG_RULE_URL = WAP_DOMAIN_URL + "/home/index/redbag.html";//���ʹ�ù���
	public static final String YUANMONEY_RULE_URL = WAP_DOMAIN_URL + "/home/Index/yuanGoldCoin.html#app";//Ԫ���ʹ�ù���
	public static final String JXQ_RULE_URL = WAP_DOMAIN_URL + "/home/index/increaseInterest";//��Ϣȯʹ�ù���
	public static final String FLOAT_RATE_URL = WAP_DOMAIN_URL + "/home/index/floatrate#app";//�������ʵ�ר��
	public static final String ZQDZP_WAP_URL = WAP_DOMAIN_URL + "/home/index/mdlottery.html";//�����ת�̻��wapҳ��
	public static final String XCFL_WAP_URL = WAP_DOMAIN_URL + "/home/index/qhb.html";//2017���´���������ר��
	public static final String JUNE_ACTIVE_WAP_URL = WAP_DOMAIN_URL + "/home/index/JuneActive#app";//2017��6�·ݻ
	public static final String SIGN_WAP_URL = WAP_DOMAIN_URL + "/home/index/qd";//ǩ���ר��
	public static final String YQHY_WAP_URL = WAP_DOMAIN_URL + "/home/index/fxjh";//���·�������ѷ��ֻ
	public static final String HYFL02_WAP_URL = WAP_DOMAIN_URL + "/home/index/fuli2";//��Ա�����ƻ�2��
	public static final String LXFX_WAP_URL = WAP_DOMAIN_URL + "/home/index/kmh";//2017�������� �����
	public static final String LXJ5_WAP_URL = WAP_DOMAIN_URL + "/home/index/qxj";//���·����ֽ�
	public static final String PROMOTED_BASE_URL = WAP_DOMAIN_URL + "/home/index/friendReg";// ����ע��Ķ�ά��
	public static final String YYY_COMPACT = API_DOMAIN_URL + "/borrow-yyyprotocol-userid-recordid.html";// Ԫ��ӯ���Э��url
	public static final String YYY_PDF_COMPACT = API_DOMAIN_URL + "/borrow/exportYyyProtocolPdf?id=userid&invest=recordid";// Ԫ��ӯ���Э��url PDF
	public static final String VIP_COMPACT = API_DOMAIN_URL + "/home/vip/vipProtocol/id/userid/invest/recordid";// vip��Ʒ�Ľ��Э�飬userid��recordidΪ0ʱΪ��ģ��
	public static final String VIP_BLANK_COMPACT = API_DOMAIN_URL + "/home/vip/vipProtocol/borrow/borrowid";//vip�հ׺�ͬ
	public static final String SRZX_COMPACT = API_DOMAIN_URL + "/appoint/borrowProtocolData/id/recordid/info/userid";//˽�������ͬ
	public static final String SRZX_PDF_COMPACT = API_DOMAIN_URL + "/appoint/exportDataProtocolPdf/id/recordid/info/userid";//˽�������ͬpdf
	public static final String SRZX_BLANK_COMPACT = API_DOMAIN_URL + "/appoint/borrowProtocol/id/borrowid";//˽������պ�ͬ
	public static final String ZXD_XSB_COMPACT = API_DOMAIN_URL + "/home/downfiles/protocol?id=recordid&info=userid";//Ԫ��ӯ�����ֱ������ݵĺ�ͬ
	public static final String ZXD_XSB_PDF_COMPACT = API_DOMAIN_URL + "//member/agreement/exportYzyPdf?id=recordid&info=userid";//Ԫ��ӯ�����ֱ������ݵĺ�ͬ
	public static final String ZXD_XSB_BLANK_COMPACT = API_DOMAIN_URL + "/downfiles/index/id/borrowid";//Ԫ��ӯ�����ֱ�հ׺�ͬ
	public static final String XSMB_BLANK_COMPACT = API_DOMAIN_URL + "/home/seckill/seckillProtocol/id/borrowid";//��ʱ����ͬ��ģ��
	public static final String XSMB_COMPACT = API_DOMAIN_URL + "/home/seckill/protocolData/id/recordid/info/userid";//��ʱ��������ݵĺ�ͬ
	public static final String WDY_BLANK_COMPACT = API_DOMAIN_URL + "/wdy/wdyContract/id/borrowid";//�ȶ�ӯ��ͬ��ģ��
	public static final String WDY_COMPACT = API_DOMAIN_URL + "/wdy/wdyContractData/id/recordid/info/userid";//�ȶ�ӯ�����ݵĺ�ͬ
	public static final String WDY_PDF_COMPACT = API_DOMAIN_URL + "/wdy/exportDataProtocolPdf/id/recordid/info/userid";//�ȶ�ӯ�����ݵĺ�ͬpdf
	public static final String YJY_BLANK_COMPACT = API_DOMAIN_URL + "/ygzx/ygzxProtocol/id/borrowid";//�ȶ�ӯ��ͬ��ģ��
	public static final String YJY_COMPACT = API_DOMAIN_URL + "/ygzx/ygzxProtocol/id/recordid/info/userid";//�ȶ�ӯ�����ݵĺ�ͬ
	public static final String YJY_PDF_COMPACT = API_DOMAIN_URL + "/ygzx/exportDataProtocolPdf/id/recordid/info/userid";//�ȶ�ӯ�����ݵĺ�ͬ

	public static final String VIP_CJWT_URL = WAP_DOMAIN_URL + "/home/vip/vipquestion.html#app";// vip��������
	public static final String YYY_CJWT_URL = WAP_DOMAIN_URL + "/home/yyy/yyyquestion#app";// Ԫ��ӯ�ĳ�������
	public static final String XYJH_CJWT_URL = WAP_DOMAIN_URL + "/home/wdy/wdyQuestion/id/borrowid#app";// �ȶ�ӯ��нӯ�ƻ����ĳ�������
	public static final String YYY_XMJS_URL = WAP_DOMAIN_URL + "/home/yyy/yyyproject#app";// Ԫ��ӯ����Ŀ����
	public static final String XSB_XMJS_URL = WAP_DOMAIN_URL + "/home/index/novice/id/borrowid#app";//���ֱ����
	public static final String PROMOTER_URL = WAP_DOMAIN_URL + "/home/index/promoter.html#app";//�ƹ�Ա��ר��
	public static final String XSMB_XMJS_URL = WAP_DOMAIN_URL + "/home/seckill/seckillProduct#app";//��ʱ�����Ŀ����
	public static final String XYJH_XMJS_URL = WAP_DOMAIN_URL + "/home/wdy/wdyProject/id/borrowid#app";//нӯ�ƻ�(�ȶ�ӯ)��Ŀ����,borrowidΪ��ǰ���id
	public static final String RECHARGE_PROOF_URL = WAP_DOMAIN_URL + "/home/App/rechargeDetail/id/rechargeId#app";//��ֵƾ֤

	//ר�� ҳ��
	public static final String SRZX_TOPIC_URL = WAP_DOMAIN_URL + "/home/Pvip/orderPro.html#app";//˽������ԤԼר��ҳ
	public static final String YJY_TOPIC_URL = WAP_DOMAIN_URL + "/home/Ygzx/yjyOrderPro.html#app";//Ԫ��ӯԤԼר��ҳ

	private final String mOSType = "2";// Android:2 , IOS:10
	private final String API_ROUTER_URL = API_DOMAIN_URL + "/api_router.php";// �汾���½ӿ�

	public static final String YXB_INTRO_URL = WAP_DOMAIN_URL + "/home/index/yxbandroid.html";// Ԫ�ű�����ҳ��
	public static final String BORROW_CAILIAO_BASE_URL = "http://admin.ylfcf.com";// �������֤��ͼƬ��baseurl

	private final String PRODUCT_LIST_URL = "/borrow/borrow/newSelectList";// ��Ʒ�б�
	private final String PRODUCT_SELECTONE_URL = "/borrow/borrow/selectOne";
	private final String USER_REGISTE_URL = "/user/user/reg";
	private final String USER_LOGIN_URL = "/user/user/login";
	private final String SEND_SMS_AUTH = "/sms/log/create";// ���Ͷ�����֤��
	private final String USER_RMB_HUIFU_ACCOUNT = "/user/account/selectOne";// �㸶�û�������˻�
	private final String USER_RMB_YILIAN_ACCOUNT = "/user/new_account/selectOne";// �����û�������˻�
	private final String USER_YUAN_MONEY_ACCOUNT = "/user/hd_coin/selectOne";// Ԫ����˻�
	private final String INVESTMENT = "/borrow/invest/newTender";// Ͷ��
	private final String CURRENT_USER_INVEST = "/borrow/invest/currentUserInvest";// ��ȡ��ǰ�û��Ƿ�Ͷ�ʹ��ñ��
	private final String BORROW_INVEST_SELECTLIST_ANDTOTAL = "/borrow/invest/selectListAndTotal";// �ܵ�Ͷ�ʼ�¼�б�
	private final String BORROW_INVEST_SELECTLIST = "/borrow/invest/selectList";
	private final String UPDATE_USER_INFO = "/user/user/update";// �����û���Ϣ���޸����������Ϣ�ȵ�
	private final String CHECK_REGISTER = "/user/user/checkRegister";// �ж��û��Ƿ�ע���
	private final String USERINFO_BY_PHONE = "/user/user/selectOneByPhone";// �����ֻ������ѯ�����û���Ϣ
	private final String PROJECT_DETAILS = "/project/project/selectOne";// ����id��ȡ��Ŀ������
	private final String ASSOCIATED_COMPANY_URL = "/project/associated_company/getCompanyInfo";// ��ȡ������˾����Ϣ
	private final String WITHDRAW_MONEY = "/user/cash/toNewCash";// ����
	private final String WITHDRAW_CANCEL_URL = "/user/cash/auditCash";//����ȡ��
	private final String RECHARGE = "/user/recharge/toYLRecharge";// ��ֵ
	private final String RECHARGE_ORDER = "/user/recharge/toOrderRecharge";// ���ɳ�ֵ����
	private final String USER_ISVERIFY = "/user/bank/selectOne";// �ж��û��Ƿ�����֤
	private final String USER_VERIFY = "/user/recharge/toVerify";// �û���֤�ӿ�
	private final String USER_SELECT_ONE = "/user/user/selectOne";// ��ѯ�����û��ĸ�����Ϣ
	private final String YILIAN_SMS = "/user/recharge/toYlSms";// �������Ͷ�����֤�루���·��͵�ʱ����ô˽ӿڣ�
	private final String EXCHANGE_PASSWORD_CHECK = "/user/user/checkDealPwd";// ��֤���������Ƿ���ȷ
	private final String WITHDRAW_LIST_URL = "/user/cash/cashLogList";// �����б�
	private final String HUIFU_FUNDS_DETAILS_LIST = "/user/account_log/selectList";// �㸶�˻��ʽ���ϸ�б�
	private final String YILIAN_FUNDS_DETAILS_LIST = "/user/new_account_log/selectList";// �����˻��ʽ���ϸ�б�
	private final String FUNDS_DETAILS_LIST = "/user/new_account_log/selectCommonList";// �ʽ���ϸ�б��������㸶��������
	private final String YILIAN_STATISTIC = "/user/new_account_log/statistic";// �����˻�ͳ�ƣ�����Ͷ���˶��٣������˶���
	private final String HUIFU_STATISTIC = "/user/account_log/statistic";// �㸶�˻�ͳ�ƣ�����Ͷ���˶��٣������˶���
	private final String CHANGE_ADDRESS_AND_ZIPCODE = "/user/user/updateAddress";// �޸ĵ�ַ�ʱ�
	private final String MYTYJ_URL = "/active/experience/selectList";// �ҵ������
	private final String EXTENSION_INCOME_URL = "/hd/interest/selectList";// �ƹ�����
	private final String EXTENSION_NEWINCOME_URL = "/promoter/promoterLog/getRewardList";//�ƹ������µĽӿ�
	private final String PRIZE_LIST_URL = "/hd/prize/selectListWithActive";// ��Ʒ�б�
	private final String HDPRIZE_LIST_URL = "/hd/prize/selectList";//��Ʒ�б�
	private final String ACTIVE_INFO = "/active/active/selectOne";
	private final String ACTICLE_LIST_URL = "/article/article/selectList";// ���桢���š���Ѷ�б�
	private final String ARTICLE_URL = "/article/article/selectOne";// ����
	private final String BANNERLIST_URL = "/article/article/selectPhoneBannerList";// banner
	private final String ARTICLE_TYJLIST_BYSTATUS = "/active/experience/selectListByStatus";// �����û�id��״̬��ȡ������б�

	// Ԫ�ű��ӿ�
	private final String YXB_PRODUCT = "/yxb/product/selectOne";// ��Ʒ
	private final String YXB_PRODUCT_LOG = "/yxb/product_log/selectOne";// ��Ʒÿ��ͳ��
	private final String YXB_INVEST = "/yxb/invest_detail/invest";// Ԫ�ű����Ϲ��ӿ�
	private final String YXB_USER_CENTER = "/yxb/user_center/selectOne";// Ԫ�ű��û�����
	private final String YXB_REDEEM_RECORD = "/yxb/redeem_detail/selectList";// Ԫ�ű���ؼ�¼
	private final String YXB_INVEST_RECORD = "/yxb/invest_detail/selectList";// Ԫ�ű��Ϲ���¼
	private final String YXB_ACCOUNT_LIST = "/yxb/new_account_log/selectList";// Ԫ�ű��ʽ���ϸ
	private final String YXB_REDEEM_URL = "/yxb/redeem_detail/redeem";// Ԫ�ű�����ؽӿ�
	private final String YXB_CHECK_REDEEM_URL = "/yxb/redeem_detail/checkRedeem";// Ԫ�ű���ص�ʱ���ж��ղ���������

	// ���
	private final String REDBAG_LIST_URL = "/active/red_bag_log/getRedBagList";// �ҵĺ���б�
	private final String REDBAG_CURRENTUSER_LIST = "/active/red_bag_log/getCurrentUserRedBagList";// ��ǰ�û�����ʹ�õĺ���б�
	private final String REDBAG_SELECTONE_URL = "/active/red_bag_log/selectOne";//��ѯĳ�����

	// ����֧���ӿ�
	private final String BF_BINDCARD_URL = "/user/bank/bindCard";// �����󿨽ӿ�
	private final String BF_SENDBINDCARD_MSG_URL = "/user/bank/sendBindCardMsg";// �������Ͱ󿨵���֤��
	private final String BF_RECHARGE_URL = "/user/bank/recharge";// ����֧���ӿ�
	private final String BF_SENDRECHARGE_MSG_URL = "/user/bank/sendRechargeMsg";// �������ͳ�ֵ����֤��
	private final String BF_VERIFY_URL = "/user/bank/verifyName";// ����ʵ����֤�ӿ�

	// ���ֱ�
	private final String XSB_DETAILS_URL = "/borrow/borrow/getNoviceStandard";// ���ֱ�����
	private final String XSB_ISCANBUY_URL = "/borrow/invest/isCanBuyNewHandBorrow";// �ж��ܷ������ֱ�

	// Ԫ��ӯ
	private final String YYY_BORROW_LIST = "/yyy/borrow/selectList";// Ԫ��ӯ�Ĳ�Ʒ�б�ҳ�棬��ʱû��
	private final String YYY_BORROW_SELECTONE = "/yyy/borrow/getOneShowData";// Ԫ��ӯ��Ʒ����(���µ���֧Ԫ��ӯ���)
	private final String YYY_BORROW_SELECTONE_BYID = "/yyy/borrow/selectOne";// ����borrowid��ȡĳ֧Ԫ��ӯ�ı������
	private final String YYY_INVEST_RECORD = "/yyy/borrow_invest/selectList";// ���Ͷ�ʼ�¼
	private final String YYY_USER_INVEST_RECORD = "/yyy/borrow_invest/investMagementNew";// �û�Ͷ�ʼ�¼
	private final String YYY_APPLYORCANCEL_RETURN = "/yyy/return/add";// Ԫ��ӯ�����ȡ����ص�API
	private final String YYY_BORROW_INVEST = "/yyy/borrow_invest/invest";// Ԫ��ӯͶ��
	private final String YYY_CURRENTUSER_INVEST_URL = "/yyy/borrow_invest/currentUserInvest";// �жϵ�ǰ�û��Ƿ�Ͷ�ʹ�Ԫ��ӯ��ĳ����

	// VIP��Ʒ
	private final String VIP_GETLCSNAME_URL = "/user/user/getLxsName";// ��ȡ���ʦ������
	private final String VIP_RECORDLIST_URL = "/vip/borrow_invest/selectList";// VIP��Ʒ��Ͷ�ʼ�¼
	private final String VIP_INVEST_URL = "/vip/borrow_invest/invest";// VIP��Ʒ��Ͷ�ʽӿ�
	private final String VIP_CURRENTUSER_INVEST_URL = "/vip/borrow_invest/currentUserInvest";// �жϵ�ǰ�û��Ƿ�Ͷ�ʹ�VIP��ĳ����

	// �����ת�̻�ӿ�
	private final String DZP_LOTTERY_TIMES = "/hd/zqj_turntable/getLotteryTimes";//��ȡ���г齱����
	private final String DZP_USER_RECORDS = "/hd/zqj_turntable/getLotteryRecord";//�û����н���¼
	private final String DZP_LOTTERY_RECORDS = "/hd/zqj_turntable/getLotteryList";//��ȡ�û���н���¼
	private final String DZP_DRAW_PRIZE = "/hd/zqj_turntable/dzpDrawPrize";//��ȡ����
	private final String DZP_IS_SHARE = "/hd/wechat_prize/isShare";

	//˽������ӿ�
	private final String APPOINT_BORROW_LIST = "/appoint/borrow/selectList";//˽�������Ʒ�б�
	private final String APPOINT_BORROW_DETAILS = "/appoint/borrow/selectOne";//˽�������Ʒ����
	private final String APPOINT_BORROW_INVEST = "/appoint/borrow_invest/invest";//˽�������ƷͶ��ӿ�
	private final String APPOINT_BORROW_INVEST_RECORD = "/appoint/borrow_invest/selectList";//˽������Ͷ�ʼ�¼���ʺ�ĳ֧���Ͷ�ʼ�¼��
	private final String APPOINT_BORROW_INVEST_USER_RECORD = "/appoint/borrow_invest/selectListForPPP";//˽�������û�Ͷ�ʼ�¼
	private final String APPOINT_RECORD = "/appoint/appoint/appointList";//˽�������ƷԤԼ��¼
	private final String APPOINT_BORROW_APPOINT = "/appoint/appoint/add";//˽�������ƷԤԼ�ӿ�

	//��Ϣȯ
	private final String JXQ_LIST_URL = "/addInterest/add_interest_log/optimizedSelectList";//�ҵļ�Ϣȯ�б�
	private final String JXQ_SELECTONE_URL = "/addInterest/add_interest_use_rule/selectOne";
	//
	private final String YJB_INTEREST = "/borrow/invest_repayment/getCoinsAndInterest";//Ԫ��������Լ�����
	private final String QUICK_PAY_BANK = "/pay_way/quick_pay_bank/selectList";//���֧�������б�

	//��ҵ�û�
	private final String COMP_APPLY_REGISTE_URL = "/co_user/co_user/comOrder";//��ҵ�û�����ע��
	private final String COMP_LOGIN_URL = "/co_user/co_user/comLogin";//��ҵ�û���¼�ӿ�
	private final String COMP_APPLY_CASH_URL = "/co_user/co_cash/toNewCash";//��ҵ�û���������
	private final String COMP_USERINFO_URL = "/co_user/co_user/selectOne";//��ȡ��ҵ�û���Ϣ

	//��ʱ���
	private final String XSMB_BORROW_DETAIL = "/mb/borrow/getShowData";//�������
	private final String XSMB_BORROW_SELECTONE = "/mb/borrow/selectOne";//����id��ȡ�������
	private final String XSMB_CURRENT_USER_INVEST = "/mb/borrow_invest/currentUserInvest";//��ǰ�û��Ƿ�Ͷ�ʹ������
	private final String XSMB_INVEST_URL = "/mb/borrow_invest/invest";//��ʱ���Ͷ��ӿ�
	private final String XSMB_INVEST_RECORD_URL = "/mb/borrow_invest/getSeckillInfo";//��ʱ���Ͷ�ʼ�¼

	//��Ա�����ƻ�
	private final String FLJH_SELECTPRICE_BYNAME_URL = "/hd/prize/selectPriceByName";//�����û�id�ͽ�Ʒ�����ж��û��Ƿ���ȡ��
	private final String FLJH_PRIZECODE_SELECTONE_URL = "/hd/prize_code/selectOne";//��ȡ������Ʒ��Ϣ
	private final String FLJH_YYYINVEST_BYTIMESPAN = "/yyy/borrow_invest/getUserYyyInvestInfoByTimeSpan";//�ж��û��ڻ�ڼ��Ƿ�Ͷ�ʹ�Ԫ��ӯ
	private final String FLJH_RECEIVE_PRIZE_URL = "/hd/prize/receivePrize";//��ȡ��Ʒ

	//�´�����
	private final String XCFL_LOTTERY_TIMES_URL = "/hd/spring_festival/getLotteryTimes";//ӭ�´�ѹ��Ǯ�齱����
	private final String XCFL_LOTTERY_DRAW_PRIZE_URL = "/hd/spring_festival/springFestivalDrawPrize";//ӭ�´���ȡ�ӿ�
	private final String XCFL_CHECK_ACTIVE_START_URL = "/hd/spring_festival/checkActiveTime";//�鿴��Ƿ�ʼ

	//�ȶ�ӯ
	private final String WDY_BORROW_DETAIL_URL = "/wdy/borrow/getShowData";//�ȶ�Ӯ���µ��Ǹ���Ʒ������
	private final String WDY_BORROW_DETAIL_SELECTONE_URL = "/wdy/borrow/selectOne";//�ȶ�ӯ��Ʒ�������id
	private final String WDY_INVEST_URL = "/wdy/borrow_invest/invest";//�ȶ�ӯͶ�ʽӿ�
	private final String WDY_INVEST_DETAIL_URL = "/wdy/borrow_invest/selectInvestDetail";//�ȶ�ӯ���Ͷ�ʼ�¼
	private final String WDY_INVEST_RECORD_URL = "/wdy/borrow_invest/selectList";//�ȶ�ӯͶ�ʼ�¼
	private final String WDY_CURRENT_USER_INVEST = "/wdy/borrow_invest/currentUserInvest";//��ǰ�û��Ƿ�Ͷ�ʹ��ȶ�ӯ
	private final String WDY_BORROWINVESTLOG_SELECTLIST_URL = "/wdy/borrow_invest_log/selectList";//����Ͷ�ʼ�¼id��ȡ������¼��Ӧ��������Ͷ�������
	private final String WDY_REINVEST_URL = "/wdy/borrow_invest_log/reInvest";//н�ʼƻ���Ʒ��Ͷ

	//������ �����
	private final String LXFX_GET_JXQ_URL = "/addInterest/add_interest/add";//��ȡ��Ϣȯ
	private final String LXFX_GET_JXQID_URL = "/addInterest/add_interest_use_rule/selectOne";//���ݹ����ȡ����id
	private final String LXFX_ISGET_JXQ_URL = "/addInterest/add_interest_log/selectList";//�Ƿ���ȡ����Ϣȯ

	private final String SYSTEM_NOW_TIME_URL = "/mb/borrow/getNowTime";//ϵͳ��ǰʱ��

	private final String RECHARGE_RECORD_LIST_URL = "/user/recharge/selectRechargeList";//��ֵ��¼�б�

	//���·�ǩ���
	private final String HD_SIGN_ISDAYSIGNED_URL = "/hd/sign/isDaySigned";//ĳ���Ƿ��Ѿ�ǩ��
	private final String HD_SIGN_SIGN_URL = "/hd/sign/marchAdd";//ǩ���ӿ�

	//��Ա�����ƻ�2��
	private final String HD_PRIZE_LIST_URL = "/hd/gift/selectList";//��Ʒ�б�
	private final String HD_GIFTCODE_SELECTONE_URL = "/hd/gift_code/selectOne";//������Ʒid��ѯ���û��Ƿ��Ѿ���ȡ����Ʒ

	//΢�Ź��ں������齱�
	private final String HD_LOTTERY_SELECTONE_URL = "/hd/lottery/selectOne";//��ȡ��Ʒ����
	private final String HD_LOTTERYCODE_SELECTONE_URL = "/hd/lottery_code/selectOne";//��ȡ��Ʒȯ��

	//���·� ÿ��һ ���ֽ�
	private final String HD_ROBCASH_GETGIFT_URL = "/hd/rob_cash/getInvestGift";//��ȡ�����̷�
	private final String HD_ROBCASH_CHECK_ISRECEIVE_URL = "/hd/rob_cash/checkUserIsReceiveStatus";//�Ƿ��Ѿ���ȡ�������������̷�
	private final String HD_ROBCASH_ROB_URL = "/hd/rob_cash/rob";//���ֽ�
	private final String HD_ROBCASH_CASH_URL = "/hd/rob_cash/getNowRob";//���ܻ��߱��ܴ������

	//�ר���б�ӿ�
	private final String ACTIVE_REGION_SELECTLIST_URL = "/active/active/selectListWithBanner";

	//��Ϣȯת��
	private final String JXQ_TRANSFER_GETSUBUSER_URL = "/user/user/getSubUser";//��ȡ���ʦ��ֱ�Ӻ���
	private final String JXQ_TRANSFER_TRANS_URL = "/addInterest/add_interest_log/changeAddInterestUser";//ת�ü�Ϣȯ

	//��ҳ�����
	private final String POPBANNER_URL = "/active/banner/selectPopOne";//��ҳ����ͼƬ

	//ת�ü�Ϣȯ
	private final String MYFRIENDS_LIST_URL = "/promoter/promoterLog/myFriends";
	private final String TRANS_COUPONS_URL = "/addInterest/add_interest_log/oneUserGetMoreAddInterestUser";//ת�ö��ż�Ϣȯ
	private final String TRANSFERED_COUPONS_LIST_URL = "/addInterest/add_interest_log/selectAlreadyClassifiedList";//��ת�õļ�Ϣȯ�б�

	//Ա��ר����Ʒ��Ԫ��ӯ��
	private final String YGZX_BORROWLIST_URL = "/ygzx/borrow/selectList";//Ա��ר����Ʒ�б�
	private final String YGZX_BORROW_DETAILS_URL = "/ygzx/borrow/selectOne";//����id���ĳ��������
	private final String YGZX_BORROWINVEST_RECORD_URL = "/ygzx/borrow_invest/selectList";//Ͷ�ʼ�¼
	private final String YGZX_BORROWINVEST_URL = "/ygzx/borrow_invest/invest";//Ա��ר����ƷͶ��
	private final String YGZX_BORROWINVEST_LIST_URL = "/ygzx/borrow_invest/getUserInvestList";//����userid��borrowid��ȡ�û�Ͷ�ʵ�ĳ֧�������
	//�˻�����
	private final String ACCOUNTLOG_REPAYMENTINFO_URL = "/user/account_log/repaymentInfo";//�ؿ�����

	//Ԫ��ӯ
	private final String YQYREWARDLIST_URL = "/promoter/promoterLog/getYqyRewardList";//Ԫ��ӯ����A���Լ����µ��û���Ͷ���б�
	private final String YQYCOMPUSER_FRIENDS_URL = "/promoter/promoterLog/getCompUserFriends";//Ԫ��ӯA+�û��ĺ����б�

	//ͨ��POS֧��
	private final String TL_GETORDERNUM_URL = "/tl/bank/get/order.num";//��ȡ���׵���
	private final String TL_GETORDERSTATUS_URL = "/tl/bank/return/order.success.info";//��ȡ��������״̬

	private static URLGenerator mUrlGenerator;

	private URLGenerator() {
	}

	public static URLGenerator getInstance() {
		if (mUrlGenerator == null) {
			mUrlGenerator = new URLGenerator();
		}
		return mUrlGenerator;
	}

	/**
	 * �汾����
	 *
	 * @param versionCode
	 * @return
	 */
	public String[] getApiRouterUrl(int versionCode) {
		StringBuffer sb = new StringBuffer();
		sb.append("version_code=").append(versionCode).append("&os_type=")
				.append(mOSType);
		return new String[] { API_ROUTER_URL, sb.toString() };
	}

	/**
	 * ��ȡ��Ʒ�б��URL
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param borrowType
	 *            ��ӯ ��ӯ ��ӯ Ϊ�յĻ�����ȫ��
	 * @param moneyStatus
	 *            δ���� ������ �ѷſ� �ѻ���
	 * @param plan
	 *            Ϊ1ʱ��ʾ��Ԫ�ƻ��Ĳ�Ʒ�б��������ʾ���Ŵ�
	 * @param enableShow
	 *            �Ƿ����ֻ�app����ʾ 1������ʾ��0����ʾ
	 * @param isNewHand
	 *            �Ƿ������ֱ� 1���� 2����
	 * @param isVip
	 *            �Ƿ���ʾvip��Ʒ �� 0 �� 1 �� 2
	 * @return
	 */
	public String[] getProduceListUrl(String pageNo, String pageSize,
									  String borrowType, String borrowStatus,String moneyStatus, String isShow,
									  String isWap, String plan, String enableShow, String isNewHand,
									  String isVip) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(PRODUCT_LIST_URL).append("&page=")
					.append(pageNo).append("&page_size=").append(pageSize);
			if (borrowType != null && !"".equals(borrowType)) {
				sb.append("&borrow_type=").append(borrowType);
			}
			if (borrowStatus != null && !"".equals(borrowStatus)) {
				sb.append("&borrow_status=").append(borrowStatus);
			}
			if (moneyStatus != null && !"".equals(moneyStatus)) {
				sb.append("&money_status=").append(moneyStatus);
			}
			if (isShow != null && !"".equals(isShow)) {
				sb.append("&is_show=").append(isShow);
			}
			if (isWap != null && !"".equals(isWap)) {
				sb.append("&is_wap=").append(isWap);
			}
			if (plan != null && !"".equals(plan)) {
				sb.append("&plan=").append(plan);
			}
			if (enableShow != null && !"".equals(enableShow)) {
				sb.append("&enableShow=").append(enableShow);
			}
			if (isNewHand != null && !"".equals(isNewHand)) {
				sb.append("&is_newHand=").append(isNewHand);
			}
			if (isVip != null && !"".equals(isVip)) {
				sb.append("&is_vip=").append(isVip);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����id��ȡĳ֧�����Ϣ
	 *
	 * @param id
	 * @param borrowStatus
	 * @param plan
	 *            1��ʾԪ�ƻ�
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getBorrowOnewURL(String id, String borrowStatus, String plan)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(PRODUCT_SELECTONE_URL).append("&id=")
				.append(id);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ע��URL
	 *
	 * @param phone
	 * @param password
	 * @param open_id
	 * @param user_from
	 * @param user_from_sub
	 * @param user_form_host
	 * @param extension_code
	 * @param type
	 *            �û����� ��ͨ�û� VIP�û�
	 * @param sales_phone
	 *            ���ʦ�ֻ���
	 * @return
	 */
	public String[] getUserRegisteURL(String phone, String password,
									  String open_id, String user_from, String user_from_sub,
									  String user_form_host, String extension_code, String type,
									  String sales_phone) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_REGISTE_URL).append("&phone=")
				.append(phone).append("&password=")
				.append(Util.md5Encryption(password));
		if (open_id != null && !open_id.isEmpty()) {
			sb.append("&open_id=").append(open_id);
		}
		if (user_from!=null && !user_from.isEmpty()) {
			sb.append("&user_from=").append(user_from);
		}
		if (user_from_sub != null && !user_from_sub.isEmpty()) {
			sb.append("&user_from_sub=").append(user_from_sub);
		}
		if (user_form_host != null && !user_form_host.isEmpty()) {
			sb.append("&user_form_host=").append(user_form_host);
		}
		if (extension_code != null && !extension_code.isEmpty()) {
			sb.append("&extension_code=").append(extension_code);
		}
		if (type != null && !type.isEmpty()) {
			sb.append("&type=").append(type);
		}
		if (sales_phone != null && !sales_phone.isEmpty()) {
			sb.append("&sales_phone=").append(sales_phone);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��¼URL
	 * @param phone
	 * @param password
	 * @return
	 */
	public String[] getUserLoginURL(String phone, String password) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_LOGIN_URL).append("&user_name=")
				.append(phone).append("&password=")
				.append(Util.md5Encryption(password));
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����ID��ȡ�㸶������˻�
	 *
	 * @param userId
	 *            num
	 * @return
	 */
	public String[] getHuiFuRMBAccountURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_RMB_HUIFU_ACCOUNT).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ����������˻�
	 *
	 * @param userId
	 * @return
	 */
	public String[] getYiLianRMBAccountURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_RMB_YILIAN_ACCOUNT).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����ID��ȡԪ����˻�
	 *
	 * @param userId
	 *            num
	 * @return
	 */
	public String[] getUserYUANMoneyAccountURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_YUAN_MONEY_ACCOUNT).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����Ͷ��
	 *
	 * @param borrowId
	 *            num ��Ʒid
	 * @param investUserId
	 *            num Ͷ���û�id
	 * @param money
	 *            Ͷ�ʽ�����Ԫ��ҽ�
	 * @param bonusMoney
	 *            Ԫ��ҽ��
	 * @param investFrom
	 *            Ͷ����Դ
	 * @param investFromSub
	 *            ������Դ
	 * @param experienceCode
	 *            �������
	 * @param investFromHost
	 *            Ͷ����Դ��ַ
	 * @param merPriv
	 *            �̻�˽����
	 * @param redBagLogId
	 *            ���id
	 * @return
	 */
	public String[] getBorrowInvestURL(String borrowId, String investUserId,
									   String money, int bonusMoney, String investFrom,
									   String investFromSub, String experienceCode, String investFromHost,
									   String merPriv, String redBagLogId,String couponLogId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(INVESTMENT).append("&borrow_id=")
				.append(borrowId).append("&invest_user_id=")
				.append(investUserId).append("&money=").append(money)
				.append("&bonus_money=").append(bonusMoney);
		if (investFrom != null && !"".equals(investFrom)) {
			sb.append("&invest_from=").append(investFrom);
		}
		if (experienceCode != null && !"".equals(experienceCode)) {
			sb.append("&experience_code=").append(experienceCode);
		}
		if (investFromHost != null && !"".equals(investFromHost)) {
			sb.append("&invest_from_host=").append(investFromHost);
		}
		if (merPriv != null && !"".equals(merPriv)) {
			sb.append("&mer_priv=").append(merPriv);
		}
		if (investFromSub != null && !"".equals(investFromSub)) {
			sb.append("&invest_from_sub=").append(investFromSub);
		}
		if (redBagLogId != null && !"".equals(redBagLogId)
				&& !"null".equals(redBagLogId) && !"NULL".equals(redBagLogId)) {
			sb.append("&red_bag_log_id=").append(redBagLogId);
		}
		if (couponLogId != null && !"".equals(couponLogId)
				&& !"null".equals(couponLogId) && !"NULL".equals(couponLogId)) {
			sb.append("&coupon_log_id=").append(couponLogId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ��ñ��
	 *
	 * @param investUserId
	 * @param borrowId
	 * @return
	 */
	public String[] getCurrentUserInvestURL(String investUserId, String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(CURRENT_USER_INVEST)
				.append("&invest_user_id=").append(investUserId)
				.append("&borrow_id=").append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ͷ�ʼ�¼
	 *
	 * @param investUserId
	 *            �û���Ԫ������id �Ǳ���
	 * @param borrowId
	 *            ��Ʒ��id �Ǳ���
	 * @param status
	 *            Ͷ��״̬ �Ǳ���
	 * @param page
	 *            ҳ�� ����
	 * @param pageSize
	 *            ����
	 * @return
	 */
	public String[] getBorrowInvestSelectListAndTotalURL(String investUserId,
														 String borrowId, String status, int page, int pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BORROW_INVEST_SELECTLIST_ANDTOTAL)
				.append("&page=").append(page).append("&page_size=")
				.append(pageSize);
		if (!"".equals(investUserId) && investUserId != null) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		if (!"".equals(borrowId) && borrowId != null) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (!"".equals(status) && status != null) {
			try {
				sb.append("&status=").append(status);
			} catch (Exception e) {
			}
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ͷ�ʼ�¼
	 *
	 * @param investUserId
	 *            Ͷ���û���id
	 * @param borrowId
	 *            ���id
	 * @param status
	 * @param isAddCoin
	 *            �Ƿ����Ԫ��� 0û��1�Ѽ�
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getBorrowInvestSelectListURL(String investUserId,
												 String borrowId, String status, String isAddCoin, int page,
												 int pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BORROW_INVEST_SELECTLIST);
		if (investUserId != null && !"".equals(investUserId)) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (isAddCoin != null && !"".equals(isAddCoin)) {
			sb.append("&is_add_coin=").append(isAddCoin);
		}

		sb.append("&borrow_id=").append(borrowId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����û���Ϣ�ӿڣ��������䣬���룬�ֻ��ŵȵ�
	 *
	 * @param id
	 * @param password
	 * @param phone
	 * @param email
	 * @param openId
	 * @param dealEnabled
	 *            ���������Ƿ��� ����/�ر�
	 * @param dealPwd
	 *            ��������
	 * @param initPwd �����޸ĵĴ���
	 * @return
	 */
	public String[] getUpdateUserInfoURL(String id, String password,
										 String phone, String email, String openId, String dealEnabled,
										 String dealPwd, String tmpData,String initPwd) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(UPDATE_USER_INFO).append("&id=").append(id);
		if (password != null && !"".equals(password)) {
			sb.append("&password=").append(Util.md5Encryption(password));
		}

		if (phone != null && !"".equals(phone)) {
			sb.append("&phone=").append(phone);
		}
		if (email != null && !"".equals(email)) {
			sb.append("&email=").append(email);
		}
		if (openId != null && !"".equals(openId)) {
			sb.append("&open_id=").append(openId);
		}
		if (dealEnabled != null && !"".equals(dealEnabled)) {
			sb.append("&deal_enabled=").append(dealEnabled);
		}

		if (dealPwd != null && !"".equals(dealPwd)) {
			sb.append("&deal_pwd=").append(Util.md5Encryption(dealPwd));
		}
		if (tmpData != null && !"".equals(tmpData)) {
			sb.append("&tmp_data=").append(tmpData);
		}
		if(initPwd != null && !"".equals(initPwd)){
			sb.append("&init_pwd=").append(initPwd);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ���Ͷ�����֤��
	 *
	 * @param phone
	 * @param template
	 *            register_code��ʾ��ע��ҳ��Ķ�����֤��ģ��,forgot_pwd��ʾ���һ�����ʱ�Ķ�����֤��ģ��
	 * @param params
	 * @param verfiy
	 *            �˲������ڶ�����֤����ʱ��Ч
	 * @return
	 */
	public String[] getSMSAuthNumURL(String phone, String template,
									 String params, String verfiy) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(SEND_SMS_AUTH).append("&phone=")
				.append(phone).append("&template=").append(template)
				.append("&params=").append(params).append("&verfiy=")
				.append(verfiy);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����û��ú����Ƿ�ע��(�˽ӿڷ����û���Ϣ̫�٣����鲻��) ע���ʱ���жϴ˺����Ƿ��Ѿ���ע��
	 *
	 * @param phone
	 *            һ��Ϊ�ֻ���
	 * @return
	 */
	public String[] getCheckRegisterURL(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(CHECK_REGISTER).append("&user_name=")
				.append(phone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����ֻ������ȡ�û���Ϣ ���һ������ʱ���õ��˽ӿ�
	 *
	 * @param phone
	 * @return
	 */
	public String[] getUserInfoByPhone(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USERINFO_BY_PHONE).append("&phone=")
				.append(phone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����Id��ȡ������Ŀ������
	 *
	 * @param id
	 * @return
	 */
	public String[] getProjectDetailsById(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(PROJECT_DETAILS).append("&id=").append(id);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ������˾����Ϣ
	 *
	 * @param loanId
	 * @param recommendId
	 * @param guaranteeId
	 * @return
	 */
	public String[] getAssociatedCompanyURL(String loanId, String recommendId,
											String guaranteeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ASSOCIATED_COMPANY_URL).append("&loan_id=")
				.append(loanId).append("&recommend_id=").append(recommendId)
				.append("&guarantee_id=").append(guaranteeId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ֵ
	 *
	 * @param userId
	 *            �û�id
	 * @param account
	 *            ��ֵ���
	 * @param bankCard
	 *            ���п���
	 * @param realName
	 *            ��ʵ����
	 * @param idNumber
	 *            ���֤����
	 * @return
	 */
	public String[] getRechargeURL(String userId, String account,
								   String bankCard, String phone, String realName, String idNumber) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(RECHARGE).append("&user_id=")
					.append(userId).append("&account=").append(account)
					.append("&bank_card=").append(bankCard)
					.append("&bank_phone=").append(phone).append("&real_name=")
					.append(realName)
					.append("&id_number=").append(idNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����
	 *
	 * @param userId
	 *            �û�ID
	 * @param cashAccount
	 *            ���ֽ��
	 * @return
	 */
	public String[] getWithdrawURL(String userId, String cashAccount) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WITHDRAW_MONEY).append("&user_id=")
				.append(userId).append("&cash_account=").append(cashAccount);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ȡ������
	 *
	 * @param id
	 *            ����ID
	 * @param status
	 *            �����״̬
	 * @param auditor
	 *            ������ID
	 * @param auditType
	 *            ����������
	 * @return
	 * @throws Exception
	 */
	public String[] getWithdrawCancelURL(String id, String status,
										 String auditor, String auditType) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WITHDRAW_CANCEL_URL).append("&id=")
				.append(id).append("&status=")
				.append(status).append("&auditor=")
				.append(auditor).append("&audit_type=")
				.append(auditType);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ���ɳ�ֵ����
	 *
	 * @param userId
	 * @param orderId
	 *            ��ֵ�ӿڷ��صĶ�����
	 * @param smsCode
	 *            ��ֵ�ӿڷ��صĶ�����
	 * @return
	 */
	public String[] getRechargeOrderURL(String userId, String orderId,
										String smsCode) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(RECHARGE_ORDER).append("&user_id=")
				.append(userId).append("&order=").append(orderId)
				.append("&sms_code=").append(smsCode);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����û�id��ѯ �Ƿ�����֤
	 *
	 * @param userId
	 * @param type
	 *            ö�����ͣ�����������
	 * @return
	 */
	public String[] getUserBankInfoById(String userId, String type) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(USER_ISVERIFY).append("&user_id=")
					.append(userId).append("&type=")
					.append(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �û���֤
	 *
	 * @param userId
	 * @param bankCard
	 *            ���п���
	 * @param realName
	 * @param idNumber
	 *            ���֤����
	 * @param bankPhone
	 *            ����Ԥ���ֻ���
	 * @return
	 */
	public String[] getUserVerifyURL(String userId, String bankCard,
									 String realName, String idNumber, String bankPhone) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(USER_VERIFY).append("&user_id=")
					.append(userId).append("&bank_card=").append(bankCard)
					.append("&real_name=")
					.append(realName)
					.append("&id_number=").append(idNumber);
			if (bankPhone != null && !"".equals(bankPhone)) {
				sb.append("&bank_phone=").append(bankPhone);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡһ���û��ĸ�����Ϣ
	 *
	 * @param id
	 * @param phone
	 * @param opneId
	 * @param coMobile ��ҵ�û��ֻ���
	 * @return
	 */
	public String[] getUserInfo(String id, String phone,String coMobile, String opneId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_SELECT_ONE);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (phone != null && !"".equals(phone)) {
			sb.append("&phone=").append(phone);
		}
		if (coMobile != null && !"".equals(coMobile)) {
			sb.append("&co_mobile=").append(coMobile);
		}
		if (opneId != null && !"".equals(opneId)) {
			sb.append("&open_id=").append(opneId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �������·��Ͷ�����֤��
	 *
	 * @param userId
	 * @param order
	 * @return
	 */
	public String[] getYilianSMSURL(String userId, String order) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YILIAN_SMS).append("&user_id=")
				.append(userId).append("&order=").append(order);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��֤���������Ƿ�������ȷ
	 *
	 * @param userId
	 * @param dealPwd
	 *            �������� ������
	 * @return
	 */
	public String[] getExchangePwdCheck(String userId, String dealPwd) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(EXCHANGE_PASSWORD_CHECK).append("&id=")
				.append(userId).append("&deal_pwd=")
				.append(Util.md5Encryption(dealPwd));
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����б�
	 *
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @param status
	 *            �ɹ��������
	 * @param startTime
	 *            ��ʽ2015-12-01 11:20:31
	 * @param endTime
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWithdrawListURL(String userId, String page,
									   String pageSize, String status, String startTime, String endTime)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WITHDRAW_LIST_URL).append("&user_id=")
				.append(userId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �㸶�˻��ʽ��б�
	 *
	 * @param userId
	 * @param type
	 * @param page
	 * @param pageSize
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getHuifuFundsDetaislListURL(String userId, String type,
												String page, String pageSize, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HUIFU_FUNDS_DETAILS_LIST)
				.append("&user_id=").append(userId).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����˻��ʽ��б�
	 *
	 * @param userId
	 * @param type
	 *            �ʽ�������� ��Repayment
	 * @param page
	 * @param pageSize
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getYilianFundsDetaislListURL(String userId, String type,
												 String page, String pageSize, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YILIAN_FUNDS_DETAILS_LIST)
				.append("&user_id=").append(userId).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �ʽ���ϸ�б�
	 *
	 * @param userId
	 * @param type
	 *            �ʽ�������� Repayment
	 * @param page
	 * @param pageSize
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getFundsDetailsListURL(String userId, String type,
										   String page, String pageSize, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FUNDS_DETAILS_LIST).append("&user_id=")
				.append(userId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����ͳ��
	 *
	 * @param userId
	 * @return
	 */
	public String[] getYilianStatistic(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YILIAN_STATISTIC).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �㸶ͳ��
	 *
	 * @param userId
	 * @return
	 */
	public String[] getHuifuStatistic(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HUIFU_STATISTIC).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �޸ĵ�ַ�ʱ�
	 *
	 * @param userId
	 * @param address
	 * @param postCode
	 *            �ʱ�
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getAddressURL(String userId, String address, String postCode)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(CHANGE_ADDRESS_AND_ZIPCODE).append("&id=")
				.append(userId).append("&address=")
				.append(address).append("&post_code=").append(postCode);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ�����
	 *
	 * @param userId
	 *            �ʱ�
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getTYJURL(String page, String pageSize, String status,
							  String userId, String putStatus, String activeTitle)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(MYTYJ_URL).append("&page=").append(page)
				.append("&page_size=").append(pageSize).append("&user_id=")
				.append(userId);
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (putStatus != null && !"".equals(putStatus)) {
			sb.append("&put_status=").append(putStatus);
		}
		if (activeTitle != null && !"".equals(activeTitle)) {
			sb.append("&active_title=").append(activeTitle);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �ƹ�����
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getExtensionIncomeURL(String userId, String pageNo,
										  String pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(EXTENSION_INCOME_URL)
				.append("&extension_user_id=").append(userId).append("&page=")
				.append(pageNo).append("&page_size=").append(pageSize)
				.append("&is_wap=").append("1");
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �ƹ����� �µĽӿ�
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getNewExtensionIncomeURL(String userId, String pageNo,
											 String pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(EXTENSION_NEWINCOME_URL)
				.append("&extension_user_id=").append(userId).append("&page=")
				.append(pageNo).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��Ʒ�б� --- �ٸ���active_title����ֶ�ȥ��ѯ���Ϣ�����ʲôʱ������������жϳ���Щ�Ѿ����ڣ�Ȼ���ٸ����Ѽ���δ������з���
	 *
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param source
	 * @return
	 */
	public String[] getPrizeListURL(String page, String pageSize,
									String userId, String source,String activeTitle) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(PRIZE_LIST_URL).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (userId != null && !"".equals(userId)) {
			sb.append("&user_id=").append(userId);
		}
		if (source != null && !"".equals(source)) {
			sb.append("&source=").append(source);
		}
		if (activeTitle != null && !"".equals(activeTitle)) {
			sb.append("&active_title=").append(activeTitle);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ��Ʒ���Ϣ
	 *
	 * @param activeTitle
	 * @param id
	 * @return
	 */
	public String[] getPrizeActiveInfo(String activeTitle, String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACTIVE_INFO).append("&active_title=")
				.append(activeTitle);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ����
	 *
	 * @param id
	 * @return
	 */
	public String[] getArticleURL(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ARTICLE_URL).append("&id=").append(id);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ���桢���š���ѯ�б�
	 *
	 * @param status
	 *            �������رա�ɾ��
	 * @param type
	 *            ��ҵ���š���վ���桢������Ѷ��������Ѷ����������
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getArticleListURL(String status, String type, String page,
									  String pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACTICLE_LIST_URL).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡbanner
	 *
	 * @param page
	 * @param pageSize
	 * @param status
	 *            ����
	 * @return
	 * @throws Exception
	 */
	public String[] getBannerURL(String page, String pageSize, String status,
								 String type) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BANNERLIST_URL).append("&page=")
				.append(page).append("&page_size=").append(pageSize)
				.append("&status=").append(status);
		if (!type.isEmpty()) {
			sb.append("&type=").append(type);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����û���id��status��ȡ������б�
	 *
	 * @param status
	 *            δʹ�ã���ʹ�õ�
	 * @param user_id
	 * @param put_status
	 *            �ɷ���״̬
	 * @param active_title
	 *            ���ʶ TYJ_02
	 * @return
	 */
	public String[] getArticleTYJListByStatus(String status, String user_id,
											  String put_status, String active_title) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ARTICLE_TYJLIST_BYSTATUS).append("&status=")
				.append(status).append("&user_id=")
				.append(user_id);
		if (put_status != null && !"".equals(put_status)) {
			sb.append("&put_status=").append(put_status);
		}
		if (active_title != null && !"".equals(active_title)) {
			sb.append("&active_title=").append(active_title);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡԪ�ű���ƷURL
	 *
	 * @param id
	 * @param status
	 *            �ѷ���
	 * @return
	 */
	public String[] getYXBProductURL(String id, String status) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_PRODUCT);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡԪ�ű�ÿ��ͳ��URL
	 *
	 * @param id
	 * @param dateTime
	 *            yyyy-MM-dd 00:00:00(�賿0����)
	 * @return
	 */
	public String[] getYXBProductLogURL(String id, String dateTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_PRODUCT_LOG);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (dateTime != null && !"".equals(dateTime)) {
			sb.append("&date_time=").append(dateTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ�ű� ---- �Ϲ�
	 *
	 * @param productId
	 * @param userId
	 * @param orderMoney
	 * @return
	 */
	public String[] getYXBInvestURL(String productId, String userId,
									String orderMoney) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_INVEST);
		sb.append("&product_id=").append(productId).append("&user_id=")
				.append(userId).append("&order_money=").append(orderMoney);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ�ű��û�����
	 *
	 * @param userId
	 * @return
	 */
	public String[] getYXBUserCenterURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_USER_CENTER);
		sb.append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ�ű���ؼ�¼
	 *
	 * @param id
	 *            ��¼��id�����ǲ�Ʒ��id
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYXBRedeemRecordsURL(String id, String userId,
										   String page, String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_REDEEM_RECORD);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (userId != null && !"".equals(userId)) {
			sb.append("&user_id=").append(userId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ�ű� �Ϲ���¼
	 *
	 * @param id
	 * @param userId
	 * @param interestStatus
	 *            //�Ƿ��Ϣ
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getYXBInvestRecordsURL(String id, String userId,
										   String interestStatus, String page, String pageSize)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_INVEST_RECORD);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize)
				.append("&user_id=").append(userId);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (interestStatus != null && !"".equals(interestStatus)) {
			sb.append("&interest_status=").append(interestStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ�ű��ʽ���ϸ�ӿ�
	 *
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYXBNewAccountLog(String userId, String page,
										String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_ACCOUNT_LIST);
		sb.append("&user_id=").append(userId).append("&page_size=")
				.append(pageSize).append("&page=").append(page);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ�ű���ؽӿ�
	 *
	 * @param productId
	 * @param userId
	 * @param applyMoney
	 * @return
	 */
	public String[] getYXBRedeemURL(String productId, String userId,
									String applyMoney) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_REDEEM_URL);
		sb.append("&product_id=").append(productId).append("&user_id=")
				.append(userId).append("&apply_money=").append(applyMoney);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����֤�ӿ�
	 *
	 * @param productId
	 * @param userId
	 * @param applyMoney
	 * @return
	 */
	public String[] getYXBCheckRedeemURL(String productId, String userId,
										 String applyMoney) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_CHECK_REDEEM_URL);
		sb.append("&product_id=").append(productId).append("&user_id=")
				.append(userId).append("&apply_money=").append(applyMoney);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �ҵĺ���б�
	 *
	 * @param userId
	 * @param flag
	 *            1:δʹ�� 2����ʹ�� 3���ѹ���
	 * @return
	 */
	public String[] getMyRedBagListURL(String userId, String flag,String page,String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(REDBAG_LIST_URL);
		sb.append("&user_id=").append(userId).append("&flag=").append(flag)
				.append("&page=").append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ���ֻ���Ϣ�����̨
	 *
	 * @param userId
	 * @param phone
	 * @param phoneModel
	 *            �ֻ�Ʒ���ͺ�
	 * @param sdkVersion
	 *            sdk�汾��
	 * @param systemVersion
	 *            �ֻ�ϵͳ�汾��
	 * @param phoneType
	 *            �ֻ����� Android | IOS
	 * @param location
	 * @param contact
	 * @return
	 */
	public String[] getAddPhoneInfoURL(String userId, String phone,
									   String phoneModel, String sdkVersion, String systemVersion,
									   String phoneType, String location, String contact) {
		StringBuffer sb = new StringBuffer();
		sb.append("user_id=").append(userId).append("&phone=").append(phone)
				.append("&phone_model=").append(phoneModel)
				.append("&sdk_version=").append(sdkVersion)
				.append("&system_version=").append(systemVersion)
				.append("&phone_type=").append(phoneType).append("&location=")
				.append(location).append("&contact=").append(contact);
		return new String[] { ADD_PHONEINFO_URL, sb.toString() };
	}

	/**
	 * ��ǰ�û�����ʹ�õĺ���б�
	 *
	 * @param userId
	 * @param borrowType
	 *            ��Ӯ ��ӯ ��ӯ
	 * @return
	 */
	public String[] getRedbagCurrentUserListURL(String userId, String borrowType) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(REDBAG_CURRENTUSER_LIST);
		try {
			sb.append("&user_id=").append(userId).append("&borrow_type=")
					.append(borrowType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ�����󿨵�URL
	 *
	 * @param userId
	 *            �û�id
	 * @param idNum
	 *            ���֤����
	 * @param realName
	 *            ��ʵ����
	 * @param bankCard
	 *            ���п�����
	 * @param bankName
	 *            ��������
	 * @param bankCode
	 *            ���еļ��
	 * @param bankPhone
	 *            ����Ԥ���ֻ�����
	 * @param smsCode
	 *            ������֤��
	 * @param order_sn
	 *            ������
	 * @return
	 */
	public String[] getBFBindCardURL(String userId, String idNum,
									 String realName, String bankCard, String bankName, String bankCode,
									 String bankPhone, String smsCode, String order_sn) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_BINDCARD_URL);
		try {
			sb.append("&user_id=").append(userId).append("&id_num=")
					.append(idNum).append("&real_name=").append(realName)
					.append("&bank_card=").append(bankCard)
					.append("&bank_code=").append(bankCode)
					.append("&bank_phone=").append(bankPhone)
					.append("&sms_code=").append(smsCode).append("&order_sn=")
					.append(order_sn);
			if (bankName != null && !"".equals(bankName)) {
				sb.append("&bank_name=").append(bankName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����֧���ӿ�
	 *
	 * @param userId
	 *            �û�id
	 * @param amount
	 *            ��ֵ���
	 * @param smsCode
	 *            ������֤��
	 * @return
	 */
	public String[] getBFRechargeURL(String userId, String amount,
									 String smsCode, String orderId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_RECHARGE_URL);
		sb.append("&user_id=").append(userId).append("&amount=").append(amount)
				.append("&sms_code=").append(smsCode).append("&order_sn=")
				.append(orderId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �������Ͱ󿨵���֤��
	 *
	 * @param userId
	 * @param bankCard
	 * @param bankPhone
	 * @return
	 */
	public String[] getBFSendBindcardURL(String userId, String bankCard,
										 String bankPhone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_SENDBINDCARD_MSG_URL);
		sb.append("&user_id=").append(userId).append("&bank_card=")
				.append(bankCard).append("&bank_phone=").append(bankPhone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �������ͳ�ֵ����֤��
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	public String[] getBFSendRechargeURL(String userId, String amount) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_SENDRECHARGE_MSG_URL);
		sb.append("&user_id=").append(userId).append("&amount=").append(amount);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����ʵ����֤��URL
	 *
	 * @param userId
	 *            �û�ID
	 * @param idNum
	 *            �û����֤����
	 * @param realName
	 *            ��ʵ����
	 * @return
	 * @throws Exception
	 */
	public String[] getBFVerifyURL(String userId, String idNum, String realName)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_VERIFY_URL);
		sb.append("&user_id=").append(userId).append("&id_card=").append(idNum)
				.append("&id_holder=")
				.append(realName);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ���ֱ�����
	 *
	 * @param borrowStatus
	 *            ����
	 * @return
	 */
	public String[] getXSBDetailsURL(String borrowStatus) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSB_DETAILS_URL);
		try {
			sb.append("&borrow_status=").append(borrowStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �ж��ܷ������ֱ�
	 *
	 * @param userId
	 * @param borrowId
	 * @return
	 */
	public String[] getIsCanBuyXSBURL(String userId, String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSB_ISCANBUY_URL);
		sb.append("&borrow_id=").append(borrowId).append("&invest_user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ��ӯ��Ʒ�б�URL
	 *
	 * @param borrowStatus
	 *            �����������...�ȵ�
	 * @param moneyStatus
	 *            δ���ꣻ������ȵ�...
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getYYYBorrowListURL(String borrowStatus,
										String moneyStatus, String page, String pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_LIST);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		if (moneyStatus != null && !"".equals(moneyStatus)) {
			sb.append("&money_status=").append(moneyStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ�����۵�Ԫ��ӯ��Ʒ����
	 *
	 * @param borrowStatus
	 *            ����...�ȵ�
	 *            δ����/������ �ȵ�
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getYYYBorrowDetailsURL(String borrowStatus)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_SELECTONE);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ����borrowid��ȡԪ��ӯĳ֧�������
	 *
	 * @param id
	 *            ���id
	 * @param borrowStatus
	 *            �緢����
	 * @param moneyStatus
	 *            ��δ���ꡢ�������
	 * @return
	 * @throws Exception
	 */
	public String[] getYYYBorrowDetailsByIdURL(String id, String borrowStatus,
											   String moneyStatus) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_SELECTONE_BYID);
		sb.append("&id=").append(id);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		if (moneyStatus != null && !"".equals(moneyStatus)) {
			sb.append("&money_status=").append(moneyStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ��ӯ���Ͷ�ʼ�¼
	 *
	 * @param borrowId
	 *            Ԫ��ӯ��Ʒid
	 * @param investStatus
	 *            Ͷ��״̬����Ͷ����Ͷ
	 * @param investUserId
	 *            Ͷ���û�id
	 * @param returnStatus
	 *            ���״̬ Ͷ���С������
	 * @param type
	 *            Ͷ�����ͣ��û�Ͷ��
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getYYYInvestRecordURL(String borrowId, String investStatus,
										  String investUserId, String returnStatus, String type, String page,
										  String pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_INVEST_RECORD);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowId != null && !"".equals(borrowId)) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (investStatus != null && !"".equals(investStatus)) {
			sb.append("&invest_status=").append(investStatus);
		}
		if (investUserId != null && !"".equals(investUserId)) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		if (returnStatus != null && !"".equals(returnStatus)) {
			sb.append("&return_status=").append(returnStatus);
		}
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ��ӯ�û�Ͷ�ʼ�¼
	 *
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYYYUserInvestRecordURL(String userId, String page,
											  String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_USER_INVEST_RECORD);
		sb.append("&invest_user_id=").append(userId).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ��ӯ�����ȡ����ص�API
	 *
	 * @param investId
	 *            Ԫ��ӯͶ�ʼ�¼id
	 * @param userId
	 * @param type
	 *            1ΪԤԼ���2Ϊȡ��ԤԼ
	 * @return
	 */
	public String[] getYYYApplyOrCancelReturnURL(String investId,
												 String userId, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_APPLYORCANCEL_RETURN);
		sb.append("&invest_id=").append(investId).append("&invest_user_id=")
				.append(userId).append("&type=").append(type);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * Ԫ��ӯͶ��URL
	 *
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom ���ں�̨һ��ʼû������ȫ���˲�����ʾͶ����Դ�������Ŵ�������ͬ���˴���һ����Դ�Ͷ�����Դ�����˺ϲ���������Թ�����app����Ϊ����App��׿�棬������Ե������г�������Ӧ�ñ�����ΪӦ�ñ�
	 * @return
	 */
	public String[] getYYYBorrowInvestURL(String borrowId, String money,
										  String userId,String investFrom,String couponId,String redbagId) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_INVEST);
		sb.append("&borrow_id=").append(borrowId).append("&money=")
				.append(money).append("&invest_user_id=").append(userId);
		if(investFrom != null && !"".equals(investFrom)){
			sb.append("&invest_from=").append(investFrom);
		}
		if(couponId != null && !"".equals(couponId) && !"null".equals(couponId) && !"NULL".equals(couponId)){
			sb.append("&add_interest_log_id=").append(couponId);
		}
		if(redbagId != null && !"".equals(redbagId) && !"null".equals(redbagId) && !"NULL".equals(redbagId)){
			sb.append("&red_bag_log_id=").append(redbagId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ�vip�ñ��
	 *
	 * @param investUserId
	 * @param borrowId
	 * @return
	 */
	public String[] getYYYCurrentUserInvestURL(String investUserId,
											   String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_CURRENTUSER_INVEST_URL)
				.append("&invest_user_id=").append(investUserId)
				.append("&borrow_id=").append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �����ֻ������ȡ���ʦ������
	 *
	 * @param phone
	 * @return
	 */
	public String[] getLCSNameURL(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_GETLCSNAME_URL);
		sb.append("&sales_phone=").append(phone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * VIPͶ�ʼ�¼�б�
	 *
	 * @param borrowId
	 * @param status
	 * @param investUserId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getVIPRecordListURL(String borrowId, String status,
										String investUserId, String page, String pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_RECORDLIST_URL);
		if (!borrowId.isEmpty()) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (!status.isEmpty()) {
			sb.append("&status=").append(status);
		}
		if (!investUserId.isEmpty()) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		sb.append("&type=").append("�û�Ͷ��");
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * VIP��ƷͶ�ʽӿ�
	 *
	 * @param borrowId
	 * @param money
	 * @param investUserId
	 * @return
	 */
	public String[] getVIPInvestURL(String borrowId, String money,
									String investUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_INVEST_URL);
		sb.append("&borrow_id=").append(borrowId).append("&money=")
				.append(money).append("&invest_user_id=").append(investUserId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ�vip�ñ��
	 *
	 * @param investUserId
	 * @param borrowId
	 * @return
	 */
	public String[] getVIPCurrentUserInvestURL(String investUserId,
											   String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_CURRENTUSER_INVEST_URL)
				.append("&invest_user_id=").append(investUserId)
				.append("&borrow_id=").append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ��ת�̳齱����
	 * @param userId
	 * @return
	 */
	public String[] getDZPLotteryTimesURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_LOTTERY_TIMES).append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ�û����н���¼
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getDZPUserRecordsURL(String userId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_USER_RECORDS).append("&user_id=").append(userId)
				.append("&page=").append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ�û���н���¼
	 * @param activeTitle
	 * @return
	 */
	public String[] getDZPLotteryRecordsURL(String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_LOTTERY_RECORDS).append("&active_title=").append(activeTitle);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ�齱���
	 * @param userId
	 * @return
	 */
	public String[] getDZPDrawPrizeURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_DRAW_PRIZE).append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ת�̷���ɹ�������˽ӿڣ��Գ齱��������+1
	 * @param userId
	 * @return
	 */
	public String[] getDZPIsshared(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_IS_SHARE).append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ˽�������Ʒ�б�
	 * @param borrowStatus
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getAppointBorrowList(String borrowStatus,String moneyStatus,String page,String pageSize) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_LIST).append("&borrow_status=")
				.append(borrowStatus)
				.append("&page=").append(page).append("&page_size=").append(pageSize);
		if(moneyStatus != null && !"".equals(moneyStatus)){
			sb.append("&money_status=").append(moneyStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ˽�������Ʒ����
	 * @param borrowId
	 * @return
	 */
	public String[] getAppointBorrowDetails(String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_DETAILS).append("&id=")
				.append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ˽������Ͷ��ӿ�
	 * @param borrowId
	 * @param userId
	 * @param money Ͷ����
	 * @param investFrom Ͷ����Դ ��׿APP
	 * @param redbagId ���id
	 * @return
	 * @throws Exception
	 */
	public String[] getAppointBorrowInvest(String borrowId,String userId,String money,String investFrom,String redbagId,String couponId) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_INVEST).append("&borrow_id=")
				.append(borrowId).append("&user_id=").append(userId).append("&money=").append(money);
		if(investFrom != null && !"".equals(investFrom)){
			sb.append("&invest_from=").append(investFrom);
		}
		if(redbagId != null && !"".equals(redbagId) && !"null".equals(redbagId) && !"NULL".equals(redbagId)){
			sb.append("&red_bag_log_id=").append(redbagId);
		}
		if(couponId != null && !"".equals(couponId) && !"null".equals(couponId) && !"NULL".equals(couponId)){
			sb.append("&add_interest_log_id=").append(couponId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ˽������Ͷ�ʼ�¼(�ʺ�ĳ�����Ͷ�ʼ�¼)
	 * @param userId �Ǳش�
	 * @param borrowId �Ǳش�
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getAppointBorrowInvestRecord(String userId,String borrowId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_INVEST_RECORD).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if(userId != null && !"".equals(userId)){
			sb.append("&user_id=").append(userId);
		}
		if(borrowId != null && !"".equals(borrowId)){
			sb.append("&borrow_id=").append(borrowId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ˽�������û�Ͷ�ʼ�¼
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getAppointBorrowInvestUserRecord(String userId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_INVEST_USER_RECORD).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if(userId != null && !"".equals(userId)){
			sb.append("&user_id=").append(userId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ԤԼ��¼
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getAppointRecordURL(String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_RECORD).append("&borrow_period=").append("0").append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ˽�������ƷԤԼ�ӿ�
	 * @param userId
	 * @param money ��λԪ
	 * @param period ��λ��
	 * @param purchaseTime  �ƻ�����ʱ��  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String[] getAppointBorrowAppointURL(String userId,String money,String period,String purchaseTime){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_APPOINT).append("&user_id=")
				.append(userId).append("&money=").append(money).append("&interest_period=").append(period);
		if(purchaseTime != null && !"".equals(purchaseTime)){
			sb.append("&purchase_time=").append(purchaseTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * �ҵļ�Ϣȯ�б�
	 * @param userId
	 * @param userStatus
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getJXQListURL(String userId,String userStatus,String page,String pageSize) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_LIST_URL).append("&user_id=")
				.append(userId).append("&use_status=").append(userStatus).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡĳ����Ϣȯ������
	 * @param id
	 * @param money
	 * @return
	 */
	public String[] getJXQDetailsURL(String id,String money){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_SELECTONE_URL).append("&id=")
				.append(id).append("&money=").append(money);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡԪ�������
	 * @param userId
	 * @param repayStatus
	 * @return
	 */
	public String[] getYJBInterestURL(String userId,String repayStatus){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(YJB_INTEREST).append("&invest_user_id=")
					.append(userId).append("&repay_status=").append(repayStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ���֧�������б�
	 * @param status ���ã�����
	 * @param payWayName ֧��ͨ������ ����֧��
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getQuickPaybankList(String status,String payWayName,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(QUICK_PAY_BANK).append("&status=").append(status)
					.append("&pay_way_name=").append(payWayName).append("&page=").append(page).append("&page_size=").append(pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ҵ�û�����ע��
	 * @param phone
	 * @param userFrom �û���Դ
	 * @param extensionCode �ƹ���
	 * @return
	 */
	public String[] getCompApplyRegisteURL(String phone,String userFrom,String extensionCode){
		StringBuffer sb = new StringBuffer();
		try {
			if(extensionCode!= null&& !TextUtils.isEmpty(extensionCode)) {
				sb.append("_URL_=").append(COMP_APPLY_REGISTE_URL).append("&phone=").append(phone).append("&user_from=").
						append(userFrom).append("&extension_code=").append(extensionCode);
			}else {
				sb.append("_URL_=").append(COMP_APPLY_REGISTE_URL).append("&phone=").append(phone).append("&user_from=").
						append(userFrom);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ҵ�û���¼
	 * @param username ��ҵ�û����û����� �����ֻ���
	 * @param password
	 * @return
	 */
	public String[] getCompLoginURL(String username,String password){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(COMP_LOGIN_URL).append("&user_name=")
				.append(username).append("&password=")
				.append(Util.md5Encryption(password));
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ҵ�û���������
	 * @param userId
	 * @param cashAccount ���ֽ��
	 * @return
	 */
	public String[] getCompApplyCashURL(String userId,String cashAccount){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(COMP_APPLY_CASH_URL).append("&user_id=")
				.append(userId).append("&cash_account=")
				.append(cashAccount);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ��ҵ�û���Ϣ
	 * @param userId
	 * @param yyzzCode Ӫҵִ��
	 * @param jgxyCode �������ú�
	 * @param khxkCode ������ɺ�
	 * @return
	 */
	public String[] getCompUserInfo(String userId,String yyzzCode,String jgxyCode,String khxkCode){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(COMP_USERINFO_URL);
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		if(!yyzzCode.isEmpty()){
			sb.append("&yyzz_code=").append(yyzzCode);
		}
		if(!jgxyCode.isEmpty()){
			sb.append("&jgxy_code=").append(jgxyCode);
		}
		if(!khxkCode.isEmpty()){
			sb.append("&khxk_code=").append(khxkCode);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ʱ�������
	 * @return
	 */
	public String[] getXSMBDetailURL(String borrowStatus){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(XSMB_BORROW_DETAIL).append("&borrow_status=").append(borrowStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ����id��ȡ�������
	 * @param borrowId
	 * @param borrowStatus
	 * @return
	 */
	public String[] getXSMBSelectOneURL(String borrowId,String borrowStatus){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(XSMB_BORROW_SELECTONE).append("&id=").append(borrowId).append("&borrow_status=").append(borrowStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ǰ�û��Ƿ���������
	 * @param userId
	 * @param borrowId
	 * @return
	 */
	public String[] getXSMBCurrentUserInvestURL(String userId,String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSMB_CURRENT_USER_INVEST).append("&borrow_id=").append(borrowId).
				append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ʱ���Ͷ��ӿ�
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom
	 * @return
	 */
	public String[] getXSMBInvestURL(String borrowId,String money,String userId,String investFrom){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSMB_INVEST_URL).append("&borrow_id=").append(borrowId).append("&money=").append(money)
				.append("&invest_user_id=").append(userId);
		if(!investFrom.isEmpty()){
			try {
				sb.append("&invest_from=").append(investFrom);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡ����Ͷ�ʼ�¼
	 * @param userId
	 * @param investStatus Ͷ��״̬
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getXSMBInvestRecordURL(String userId,String investStatus,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSMB_INVEST_RECORD_URL).append("&user_id=").append(userId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		if(!investStatus.isEmpty()){
			sb.append("&invest_status=").append(investStatus);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * �����û�id�ͽ�Ʒ�����ж��û��Ƿ���ȡ��
	 * @param userId
	 * @param prizeName
	 * @return
	 */
	public String[] getFLJHSelectPriceByName(String userId,String prizeName){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_SELECTPRICE_BYNAME_URL).append("&user_id=").append(userId);
		if(!prizeName.isEmpty()){
			sb.append("&prize=").append(prizeName);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ѯ������Ʒ��Ϣ
	 * @param id ��Ʒ���
	 * @param prizeCode ��Ʒ����
	 * @param prizeName ��Ʒ����
	 * @param status ��ʹ�� δʹ��
	 * @param openid ΢��Openid
	 * @param userId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getFLJHPrizeCodeSelectOne(String id,String prizeCode,String prizeName,
											  String status,String openid,String userId) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_PRIZECODE_SELECTONE_URL);
		if(!id.isEmpty()){
			sb.append("&id=").append(id);
		}
		if(!prizeCode.isEmpty()){
			sb.append("&prize_code=").append(prizeCode);
		}
		if(!prizeName.isEmpty()){
			sb.append("&prize_name=").append(prizeName);
		}
		if(!status.isEmpty()){
			sb.append("&status=").append(status);
		}
		if(!openid.isEmpty()){
			sb.append("&open_id=").append(openid);
		}
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * �жϻ�ڼ��û��Ƿ�Ͷ�ʹ�Ԫ��ӯ
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getFLJHYYYInvestByTime(String userId,String startTime,String endTime){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_YYYINVEST_BYTIMESPAN).append("&user_id=").append(userId);
		if(!startTime.isEmpty()){
			sb.append("&start_time=").append(startTime);
		}
		if(!endTime.isEmpty()){
			sb.append("&end_time=").append(endTime);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡ��Ʒ
	 * @param userId
	 * @param prize ��Ʒ����
	 * @param activeTitle ���ʶ
	 * @param operatingRemark ��ע
	 * @param remark ��ע
	 * @param status ��Ʒ״̬
	 * @param source
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getFLJHReceivePrizeURL(String userId,String prize,String giftId,String activeTitle,
										   String operatingRemark,String remark,String status,String source) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_RECEIVE_PRIZE_URL).append("&user_id=").append(userId).
				append("&prize=").append(prize).append("&active_title=").append(activeTitle).
				append("&gift_id=").append(giftId);
		if(!operatingRemark.isEmpty()){
			sb.append("&operating_remark=").append(operatingRemark);
		}
		if(!remark.isEmpty()){
			sb.append("&remark=").append(remark);
		}
		if(!status.isEmpty()){
			sb.append("&status=").append(status);
		}
		if(!source.isEmpty()){
			sb.append("&source=").append(source);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡ�´����� ��ȡѹ��Ǯ�Ĵ���
	 * @param userId
	 * @param activeTitle
	 * @return
	 */
	public String[] getXCFLLotteryTimes(String userId,String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XCFL_LOTTERY_TIMES_URL).append("&user_id=").
				append(userId).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * �´����� �콱
	 * @param userId
	 * @return
	 */
	public String[] getXCFLDrawPrize(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XCFL_LOTTERY_DRAW_PRIZE_URL).append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * �жϻ�Ƿ�ʼ
	 * @param activeTitle
	 * @return
	 */
	public String[] getXCFLActiveTime(String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XCFL_CHECK_ACTIVE_START_URL).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡ����һ�ڵ��ȶ�Ӯ��Ʒ����
	 * @param borrowStatus
	 * @param isShow
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWDYBorrowDetailsURL(String borrowStatus,String isShow) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_BORROW_DETAIL_URL).append("&borrow_status=").append(borrowStatus).
				append("&is_show=").append(isShow);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * �ȶ�ӯͶ�ʽӿ�
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom
	 * @param coinMoney
	 * @param couponId
	 * @param redbagId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWDYBorrowInvestURL(String borrowId,String money,String userId,
										  String investFrom,String coinMoney,String couponId,String redbagId) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_INVEST_URL).append("&borrow_id=").append(borrowId).append("&money=").append(money).
				append("&invest_user_id=").append(userId).append("&invest_from=").append(investFrom);
		if(coinMoney != null && !"".equals(coinMoney)){
			sb.append("&coin=").append(coinMoney);
		}
		if(couponId != null && !"".equals(couponId)){
			sb.append("&add_coupon_id=").append(couponId);
		}
		if(redbagId != null && !"".equals(redbagId)){
			sb.append("&red_bag_id=").append(redbagId);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * �ȶ�ӯ���Ͷ�ʼ�¼
	 * @param borrowId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWDYBorrowInvestDetailURL(String borrowId,String page,
												String pageSize) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_INVEST_DETAIL_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowId != null && !"".equals(borrowId)) {
			sb.append("&borrow_id=").append(borrowId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ��ȡ�ȶ�ӯͶ�ʼ�¼
	 * @param borrowId
	 * @param userId
	 * @param type 0
	 * @param status 0
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getWDYBorrowInvestRecordURL(String borrowId,String userId,String type,
												String status,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_INVEST_RECORD_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowId != null && !"".equals(borrowId)) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (userId != null && !"".equals(userId)) {
			sb.append("&invest_user_id=").append(userId);
		}
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * ������ ����� ��ȡ��Ϣȯ
	 * @param userId
	 * @param ticket ��Ϣȯ����id
	 * @param startTime ��Ϣȯ��Ч�ڿ�ʼʱ�� yyyy-MM-dd HH:mm:ss
	 * @param endTime ��Ϣȯ��Ч�ڽ���ʱ��  yyyy-MM-dd HH:mm:ss
	 * @param remark ��ע ���ź� ������
	 * @param type ���ͷ�ʽ
	 * @param isBatch ����������ָ���û�����
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getLxfxJXQURL(String userId,String ticket,String startTime,String endTime,String remark,String type,String isBatch) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(LXFX_GET_JXQ_URL).append("&user_id=").append(userId).append("&ticket=").append(ticket).
				append("&start_time=").append(startTime).append("&end_time=").append(endTime).append("&remark=").append(remark).
				append("&type=").append(type).append("&is_batch=").append(isBatch);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ���ݹ����ȡ��Ӧ�ļ�Ϣȯ�Ĺ���id
	 * @param money ��Ϣȯ�Ľ��
	 * @param needInvestMoney
	 * @param borrowType ������ͣ���������,�ָ�
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getJXQIDURL(String money,String needInvestMoney,String borrowType) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(LXFX_GET_JXQID_URL).append("&money=").append(money).append("&need_invest_money=").append(needInvestMoney).
				append("&borrow_type=").append(borrowType);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * �������Ƿ���ȡ�˼�Ϣȯ
	 * @param userId
	 * @param couponFrom
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getIsGetJXQURL(String userId,String couponFrom,
								   String useStatus,String page,String pageSize,
								   String transfer) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(LXFX_ISGET_JXQ_URL).append("&user_id=").append(userId);
		if(couponFrom != null && !"".equals(couponFrom)){
			sb.append("&coupon_from=").append(couponFrom);
		}
		if(useStatus != null && !"".equals(useStatus)){
			sb.append("&use_status=").append(useStatus);
		}
		if(page != null && !"".equals(page)){
			sb.append("&page=").append(page);
		}
		if(pageSize != null && !"".equals(pageSize)){
			sb.append("&page_size=").append(pageSize);
		}
		if(transfer != null && !"".equals(transfer)){
			sb.append("&transfer=").append(transfer);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ǰ�û��Ƿ�Ͷ�ʹ��ȶ�ӯ
	 * @param borrowId
	 * @param userId
	 * @return
	 */
	public String[] getWDYCurrentUserInvestURL(String borrowId,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_CURRENT_USER_INVEST).append("&borrow_id=").append(borrowId).
				append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ����Ͷ�ʼ�¼��id��ȡ�˼�¼��Ӧ������Ͷ��״����
	 * @param investRecordId
	 * @return
	 */
	public String[] getWDYBorrowInvestLogSelectListURL(String investRecordId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_BORROWINVESTLOG_SELECTLIST_URL).append("&invest_id=").append(investRecordId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ����id��ȡ�ȶ�ӯ������
	 * @param borrowId
	 * @return
	 */
	public String[] getWDYBorrowDetailById(String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_BORROW_DETAIL_SELECTONE_URL).append("&id=").append(borrowId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * �ȶ�ӯ��Ͷ
	 * @param wdyLogid
	 * @param userId
	 * @param money
	 * @return
	 */
	public String[] getWDYReinvestURL(String wdyLogid,String userId,String money){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_REINVEST_URL).append("&wdy_log_id=").append(wdyLogid).append("&user_id=").append(userId)
				.append("&money=").append(money);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ֵ��¼�б�
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public String[] getRechargeRecordListURL(String page,String pageSize,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(RECHARGE_RECORD_LIST_URL).append("&page=").append(page).append("&page_size=").append(pageSize)
				.append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ϵͳ��ǰʱ��
	 * @param type
	 * @return
	 */
	public String[] getSystemNowTimeURL(String type){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(SYSTEM_NOW_TIME_URL).append("&type=").append(type);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * �ж�ĳ���Ƿ��Ѿ�ǩ��
	 * @param userId
	 * @param day yyyy-MM-dd
	 * @return
	 */
	public String[] getHDSignIsDaySigned(String userId,String day){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_SIGN_ISDAYSIGNED_URL).append("&user_id=").
				append(userId).append("&day=").append(day);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 3�·�ǩ�����ǩ���ӿ�
	 * @param userId
	 * @return
	 */
	public String[] getHDSignURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_SIGN_SIGN_URL).append("&user_id=").
				append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��Ʒ�б�
	 * @param type
	 * @param isShow
	 * @return
	 */
	public String[] getHDGiftListURL(String type,String isShow,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(HD_PRIZE_LIST_URL).append("&type=").
					append(type).append("&is_show=").append(isShow).append("&is_app=").
					append("��").append("&page=").
					append(page).append("&page_size=").append(pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ������Ʒid��ѯ���û��Ƿ��Ѿ���ȡ������Ʒ
	 * @param id ȯid
	 * @param giftId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String[] getHDGiftById(String id,String giftId,String userId,String status) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_GIFTCODE_SELECTONE_URL);
		if(!id.isEmpty()){
			sb.append("&id=").append(id);
		}
		if(!giftId.isEmpty()){
			sb.append("&gift_id=").append(giftId);
		}
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		if(!status.isEmpty()){
			sb.append("&status=").append(status);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 *
	 * @param userId
	 * @param activeTitle
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getHDPrizeListURL(String userId,String activeTitle,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HDPRIZE_LIST_URL);
		sb.append("&user_id=").append(userId).append("&active_title=").append(activeTitle).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ȡ��Ʒ����
	 * @param id
	 * @param name
	 * @param type
	 * @return
	 */
	public String[] getHDLotterySelectoneURL(String id,String name,String type) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_LOTTERY_SELECTONE_URL);
		if(!id.isEmpty()){
			sb.append("&id=").append(id);
		}
		if(!name.isEmpty()){
			sb.append("&name=").append(name);
		}
		if(!type.isEmpty()){
			sb.append("&type=").append(type);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ȡ��Ʒ��ȯ��
	 * @param lotteryId
	 * @param userId
	 * @return
	 */
	public String[] getHDLotteryCodeURL(String lotteryId,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_LOTTERYCODE_SELECTONE_URL);
		if(!lotteryId.isEmpty()){
			sb.append("&lottery_id=").append(lotteryId);
		}
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ȡ�����̷�
	 * @param userId
	 * @return
	 */
	public String[] getHDRobCashGetGiftURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_GETGIFT_URL);
		sb.append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * �Ƿ��Ѿ���ȡ�����������̷�
	 * @param userId
	 * @param activeTitle
	 * @return
	 */
	public String[] getHDRobCashCheckIsReceiveURL(String userId,String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_CHECK_ISRECEIVE_URL);
		sb.append("&user_id=").append(userId).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ���·ݻ���ֽ�
	 * @param userId
	 * @return
	 */
	public String[] getHDRobCashRobURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_ROB_URL);
		sb.append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ���ܴ������
	 * @return
	 */
	public String[] getHDRobCashCashURL(String nowTime){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_CASH_URL).append("&now_time=").append(nowTime);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * �û��Ƿ��Ѿ���ȡ�����̷�
	 * @param userId
	 * @param activeTitle
	 * @return
	 */
	public String[] getHDRobCashIsReceiveGift(String userId,String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_CHECK_ISRECEIVE_URL);
		sb.append("&user_id=").append(userId).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * �ר���б�
	 * @param page
	 * @param pageSize
	 * @param status
	 * @param fromWhere
	 * @param picShowStatus
	 * @return
	 */
	public String[] getActiveRegionList(String page,String pageSize,String status,String fromWhere,String picShowStatus) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACTIVE_REGION_SELECTLIST_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize).append("&status=").append(status)
				.append("&banner_where=").append(fromWhere).append("&banner_pic_show_status=").append(picShowStatus);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ȡ���ʦ��ֱ�Ӻ���
	 * @param phone
	 * @param userId
	 * @return
	 */
	public String[] getSubUserURL(String phone,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_TRANSFER_GETSUBUSER_URL);
		sb.append("&phone=").append(phone).append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ת�ü�Ϣȯ
	 * @param userId �����˵�id
	 * @param addInterestId ��Ϣȯ��id
	 * @return
	 */
	public String[] getTransferURL(String userId,String addInterestId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_TRANSFER_TRANS_URL);
		sb.append("&user_id=").append(userId).append("&add_interest_id=").append(addInterestId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * ��ҳ����banner
	 * @return
	 */
	public String[] getPopBannerURL(){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(POPBANNER_URL);
		sb.append("&banner_where=").append("app_pop");
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡ�����б�
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getMyFriendsListURL(String userId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(MYFRIENDS_LIST_URL);
		sb.append("&user_id=").append(userId).append("&page=").
				append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡ��Ϣȯ�б�
	 * @param userId
	 * @param useStatus δʹ��
	 * @param page
	 * @param pageSize
	 * @param transfer 1��ʾδת�� 2��ʾ��ת�� 0��ʾ����ת��
	 * @return
	 */
	public String[] getTransferedCouponListURL(String userId,String useStatus,
											   String page,String pageSize,String transfer){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(TRANSFERED_COUPONS_LIST_URL);
		sb.append("&user_id=").append(userId).append("&page=").
				append(page).append("&page_size=").append(pageSize).append("&use_status=").append(useStatus).
				append("&transfer=").append(transfer);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ת�ö��ż�Ϣȯ
	 * @param userId ���ռ�Ϣȯ���˵�id
	 * @param couponIds ���ż�Ϣȯ��id���ö��ŷָ�
	 * @return
	 */
	public String[] getTransferCouponURL(String userId,String couponIds){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(TRANS_COUPONS_URL);
		sb.append("&user_id=").append(userId).append("&add_interest_id=").append(couponIds);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * Ա��ר���Ʒ��Ԫ��ӯ��
	 * @param borrowStatus
	 * @param moneyStatus
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYGZXBorrowListURL(String borrowStatus,String moneyStatus,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWLIST_URL);
		sb.append("&borrow_status=").append(borrowStatus);
		if(moneyStatus != null && !"".equals(moneyStatus)){
			sb.append("&money_status=").append(moneyStatus);
		}
		sb.append("&page=")
			.append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * Ա��ר���Ʒ����
	 * @param borrowId
	 * @return
	 */
	public String[] getYGZXBorrowDetailsURL(String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROW_DETAILS_URL);
		sb.append("&id=").append(borrowId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * Ա��ר��Ͷ�ʼ�¼
	 * @param userId
	 * @param borrowId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYGZXBorrowInvestRecordURL(String userId,String borrowId, String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWINVEST_RECORD_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if(userId != null && !"".equals(userId)){
			sb.append("&user_id=").append(userId);
		}
		if(borrowId != null && !"".equals(borrowId)){
			sb.append("&borrow_id=").append(borrowId);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * Ա��ר����ƷͶ��
	 * @param userId
	 * @param borrowId
	 * @param money
	 * @param from
	 * @return
	 */
	public String[] getYGZXBorrowInvestURL(String userId,String borrowId,String money,String from){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWINVEST_URL);
		sb.append("&user_id=").append(userId).append("&borrow_id=").append(borrowId).append("&money=").append(money).append("&invest_from=").append(from);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡһ�������Ϣ
	 * @param borrowId
	 * @param investId Ͷ��Id
	 * @return
	 */
	public String[] getRedbagSelectoneURL(String borrowId,String investId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(REDBAG_SELECTONE_URL);
		sb.append("&borrow_id=").append(borrowId).append("&borrow_invest_id=").append(investId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡԱ��ר��ĳ���������
	 * @param userId
	 * @param borrowId
	 * @return
	 */
	public String[] getYGZXBorrowById(String userId,String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWINVEST_LIST_URL);
		sb.append("&borrow_id=").append(borrowId).append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * �˻����Ļؿ���Ϣ
	 * @param userId
	 * @return
	 */
	public String[] getAccountLogRepaymentURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACCOUNTLOG_REPAYMENTINFO_URL);
		sb.append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * Ԫ��ӯA+�û�����ĺ����б�
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYqyCompUserFriendsURL(String userId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YQYCOMPUSER_FRIENDS_URL);
		sb.append("&user_id=").append(userId).append("&page=").append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL, sb.toString()};
	}

	public String[] getYqyRewardListURL(String extensionUserId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YQYREWARDLIST_URL);
		sb.append("&extension_user_id=").append(extensionUserId).append("&page=").
				append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡͨ��pos֧�����׵���
	 * @param userId
	 * @param amount ֧�����
	 * @param from  ��Դ:android ios pc
	 * @return
	 */
	public String[] getTLOrderNum(String userId,String amount,String from){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(TL_GETORDERNUM_URL);
		sb.append("&user_id=").append(userId).append("&amount=").
				append(amount).append("&from=").append(from);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * ��ȡͨ��pos֧������״̬
	 * @param userId
	 * @param orderId ����id
	 * @return
	 */
	public String[] getTLOrderStatus(String userId,String orderId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(TL_GETORDERSTATUS_URL);
		sb.append("&user_id=").append(userId).append("&order=").
				append(orderId);
		return new String[]{BASE_URL, sb.toString()};
	}
}