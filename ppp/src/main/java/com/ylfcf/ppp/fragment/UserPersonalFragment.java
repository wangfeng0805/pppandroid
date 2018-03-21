package com.ylfcf.ppp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncHuiFuRMBAccount;
import com.ylfcf.ppp.async.AsyncPrizeList;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.async.AsyncYJBInterest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.ptr.PtrClassicFrameLayout;
import com.ylfcf.ppp.ptr.PtrDefaultHandler;
import com.ylfcf.ppp.ptr.PtrFrameLayout;
import com.ylfcf.ppp.ptr.PtrHandler;
import com.ylfcf.ppp.ui.AccountCenterActivity;
import com.ylfcf.ppp.ui.AccountSettingActivity;
import com.ylfcf.ppp.ui.AccountSettingCompActivity;
import com.ylfcf.ppp.ui.AwardDetailsActivity;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.BindCardActivity;
import com.ylfcf.ppp.ui.BorrowListYJYActivity;
import com.ylfcf.ppp.ui.FundsDetailsActivity;
import com.ylfcf.ppp.ui.InvitateActivity;
import com.ylfcf.ppp.ui.LXFXTempActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.PrizeRegion2TempActivity;
import com.ylfcf.ppp.ui.RechargeActivity;
import com.ylfcf.ppp.ui.RechargeChooseActivity;
import com.ylfcf.ppp.ui.RechargeCompActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.ui.UserVerifyActivity;
import com.ylfcf.ppp.ui.WithdrawActivity;
import com.ylfcf.ppp.ui.WithdrawCompActivity;
import com.ylfcf.ppp.ui.WithdrawPwdGetbackActivity;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.text.DecimalFormat;

import static com.ylfcf.ppp.R.id.my_account_personal_zscp_invest_btn;

/**
 * Created by Administrator on 2017/9/18.
 */

public class UserPersonalFragment extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_GET_USERINFO_WHAT = 1003;
    private static final int REQUEST_GET_USERINFO_SUCCESS = 1004;

    private static final int REQUEST_YUANMONEY_WHAT = 1005;
    private static final int REQUEST_YUANMOENY_SUC = 1006;

    private static final int REQUEST_HYFL_WHAT = 1009;

    private static final int REQUEST_LCS_WHAT = 1010;
    private static final int VERIFY_INTENT = 1011;//ʵ���ɹ�
    private static final int BINDCARD_INTENT = 1012;//�󿨳ɹ�

    private MainFragmentActivity mainActivity;
    private View rootView;
    private LoadingDialog mLoadingDialog;

    //�����û�����
    private TextView usernameTV;//�����û����û���
    private TextView zhyeTotalTV;//�˻������
    private TextView zhyeBalanceTV;//�˻����
    private TextView withdrawBtn;
    private TextView rechargeBtn;

    //δʵ������ʾ����
    private LinearLayout unverifyLayout;
    private TextView unverifyPromptTV;
    private ImageView unverifyXImg;

    //Ԫ��ӯģ��
    private LinearLayout yjyLayout;//Ԫ��ӯģ��
    private Button yjyInvestBtn,yjyAppointBtn;//Ԫ��ӯͶ���Լ�ԤԼ��ť

    private LinearLayout yqyjLayout;//�����н�
    private TextView yqyjText;
    private TextView yqyjPrompt;
    private TextView usedYJBTV;//Ԫ��ҿ��ý��
    private LinearLayout jlmxLayout;//������ϸ
    private LinearLayout zjmxLayout;//�ʽ���ϸ
    private LinearLayout tbjlLayout;//Ͷ���¼
    private LinearLayout zhszLayout;//�˻�����
    private View line1,line2,line3,line4,line5,line6;
    private RelativeLayout accouncenterLayout;
    private View personalMainLayout;
    private com.ylfcf.ppp.ptr.PtrClassicFrameLayout mainRefreshLayout;
    private ImageView phoneLogo,idcardLogo,bankcardLogo,headLogo;

    private UserInfo mUserInfo;
    private UserRMBAccountInfo yilianAccountInfo;//�����˻���Ϣ
    private UserRMBAccountInfo huifuAccountInfo;//�㸶�˻���Ϣ
    private BaseInfo yjbInterestBaseInfo;//Ԫ��Ҳ���������
    private String hfuserId = "";
    private boolean isSetWithdrawPwd = false;//�û��Ƿ��Ѿ����ý�������
    private boolean isVerify,isBindcard;
    private boolean isLcs;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_GET_USERINFO_WHAT:
                    requestUserInfo(SettingsManager.getUserId(mainActivity.getApplicationContext()),
                            SettingsManager.getUser(mainActivity.getApplicationContext()));
                    break;
                case REQUEST_GET_USERINFO_SUCCESS:
                    mUserInfo = (UserInfo) msg.obj;
                    hfuserId = mUserInfo.getHf_user_id();
                    handler.sendEmptyMessage(REQUEST_LCS_WHAT);
                    if(hfuserId != null && !"".equals(hfuserId)){
                        requestYilianAccount(mUserInfo.getId(),true);
                        return;
                    }
                    requestYilianAccount(mUserInfo.getId(),false);
                    break;
                case REQUEST_YUANMONEY_WHAT:
                    requestYuanMoney(SettingsManager.getUserId(mainActivity.getApplicationContext()));
                    break;
                case REQUEST_HYFL_WHAT:
                    requestActiveTime("HYFL_02");
                    break;
                case REQUEST_LCS_WHAT:
                    requestLcsName(SettingsManager.getUser(mainActivity.getApplicationContext()));
                    break;
            }
        }
    };

    public static UserPersonalFragment newInstance(){
        UserPersonalFragment fragment = new UserPersonalFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainFragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLoadingDialog = new LoadingDialog(mainActivity,"���ڼ���...",R.anim.loading);
        if(rootView == null){
            rootView = inflater.inflate(R.layout.user_personal_fragment, null);
        }
        findViews(rootView);
//		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        YLFLogger.d("UserPersonalFragment onCreateView()");
        handler.sendEmptyMessage(REQUEST_GET_USERINFO_WHAT);
        handler.sendEmptyMessage(REQUEST_YUANMONEY_WHAT);
        //����Ƿ�ʵ����
        checkIsVerify("��ʼ��");
        return rootView;
    }

    private void findViews(View view){
        initRefreshLayout(view);
        usernameTV = (TextView)view.findViewById(R.id.my_account_personal_username);
        zhyeTotalTV = (TextView)view.findViewById(R.id.my_account_personal_totalmoney_tv);
        zhyeBalanceTV = (TextView)view.findViewById(R.id.my_account_personal_balancemoney_tv);
        withdrawBtn = (TextView)view.findViewById(R.id.my_account_personal_withdraw_btn);
        withdrawBtn.setOnClickListener(this);
        rechargeBtn = (TextView)view.findViewById(R.id.my_account_personal_recharge_btn);
        rechargeBtn.setOnClickListener(this);
        unverifyLayout = (LinearLayout) view.findViewById(R.id.my_account_personal_unverify_layout);
        unverifyLayout.setOnClickListener(this);
        unverifyPromptTV = (TextView) view.findViewById(R.id.my_account_personal_unverify_prompt);
        unverifyXImg = (ImageView) view.findViewById(R.id.my_account_personal_x_img);
        unverifyXImg.setOnClickListener(this);

        //Ԫ��ӯ
        yjyLayout = (LinearLayout) view.findViewById(R.id.my_account_personal_zscp_layout);
        yjyInvestBtn = (Button) view.findViewById(my_account_personal_zscp_invest_btn);
        yjyInvestBtn.setOnClickListener(this);
        yjyAppointBtn = (Button) view.findViewById(R.id.my_account_personal_zscp_yy_btn);
        yjyAppointBtn.setOnClickListener(this);

        yqyjLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_yqyj_layout);
        yqyjLayout.setOnClickListener(this);
        yqyjText = (TextView) view.findViewById(R.id.my_account_personal_yqyj_text);
        yqyjPrompt = (TextView) view.findViewById(R.id.my_account_personal_yqyj_prompt);
        jlmxLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_jlmx_layout);
        jlmxLayout.setOnClickListener(this);
        usedYJBTV = (TextView)view.findViewById(R.id.my_account_personal_used_yjb);
        zjmxLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_zjmx_layout);
        zjmxLayout.setOnClickListener(this);
        tbjlLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_tbjl_layout);
        tbjlLayout.setOnClickListener(this);
        zhszLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_zhsz_layout);
        zhszLayout.setOnClickListener(this);
        line1 = view.findViewById(R.id.my_account_personal_line1);
        line2 = view.findViewById(R.id.my_account_personal_line2);
        line3 = view.findViewById(R.id.my_account_personal_line3);
        line4 = view.findViewById(R.id.my_account_personal_line4);
        line5 = view.findViewById(R.id.my_account_personal_line5);
        line6 = view.findViewById(R.id.my_account_personal_line6);
        accouncenterLayout = (RelativeLayout)view.findViewById(R.id.my_account_personal_layout_account_center);
        accouncenterLayout.setOnClickListener(this);
        personalMainLayout = view.findViewById(R.id.user_personal_fragment_layout);
        phoneLogo = (ImageView)view.findViewById(R.id.my_account_personal_phone_img);
        idcardLogo = (ImageView)view.findViewById(R.id.my_account_personal_idcard_img);
        bankcardLogo = (ImageView)view.findViewById(R.id.my_account_personal_bankcard_img);
        headLogo = (ImageView)view.findViewById(R.id.my_account_personal_headimg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 1021:
                Util.toastLong(mainActivity,"ʵ���ɹ�");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_account_personal_withdraw_btn:
                //����
                withdrawBtn.setEnabled(false);
                checkIsVerify("����");
                break;
            case R.id.my_account_personal_recharge_btn:
                //��ֵ
                if(SettingsManager.isPersonalUser(mainActivity)){
                    Intent intent = new Intent(mainActivity,RechargeChooseActivity.class);
                    startActivity(intent);
                }else if(SettingsManager.isCompanyUser(mainActivity)){
                    Intent intentRechargeComp = new Intent(mainActivity,RechargeCompActivity.class);
                    startActivity(intentRechargeComp);
                }
                break;
            case R.id.my_account_personal_jlmx_layout:
                Intent intentAward = new Intent(mainActivity,AwardDetailsActivity.class);
                startActivity(intentAward);
                break;
            case R.id.my_account_personal_zjmx_layout:
                //�ʽ���ϸ
                Intent intentFund = new Intent(mainActivity,FundsDetailsActivity.class);
                intentFund.putExtra("userinfo", mUserInfo);
                startActivity(intentFund);
                break;
            case R.id.my_account_personal_tbjl_layout:
                Intent intentUserRecord = new Intent(mainActivity,UserInvestRecordActivity.class);
                startActivity(intentUserRecord);
                break;
            case R.id.my_account_personal_yqyj_layout:
                //�����н���
                yqyjLayout.setEnabled(false);
                checkIsVerify("�����н�");
                break;
            case R.id.my_account_personal_zhsz_layout:
                //�˻�����
                if(SettingsManager.isPersonalUser(mainActivity)){
                    zhszLayout.setEnabled(false);
                    checkIsVerify("�˻�����");
                }else if(SettingsManager.isCompanyUser(mainActivity)){
                    Intent intentZHSZComp = new Intent(mainActivity,AccountSettingCompActivity.class);
                    startActivity(intentZHSZComp);
                }
                break;
            case my_account_personal_zscp_invest_btn:
                //Ԫ��ӯͶ�ʰ�ť
                Intent yjyInvestIntent = new Intent(mainActivity,BorrowListYJYActivity.class);
                startActivity(yjyInvestIntent);
                break;
            case R.id.my_account_personal_zscp_yy_btn:
                //Ԫ��ӯԤԼ
                Intent intentYJYAppoint = new Intent(mainActivity,BannerTopicActivity.class);
                BannerInfo info = new BannerInfo();
                info.setArticle_id(Constants.TopicType.YJY_APPOINT);
                info.setLink_url(URLGenerator.YJY_TOPIC_URL);
                intentYJYAppoint.putExtra("BannerInfo",info);
                startActivity(intentYJYAppoint);
                break;
            case R.id.my_account_personal_unverify_layout:
                //���ȥʵ��ȥ��֤
                String tag = (String)unverifyLayout.getTag();
                Intent intent = new Intent();
                if(!isVerify){
                    //δʵ��
                    intent.setClass(mainActivity,UserVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type","��ʾ��");
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }else if(!isBindcard){
                    intent.setClass(mainActivity,BindCardActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.my_account_personal_x_img:
                unverifyLayout.setVisibility(View.GONE);
                break;
            case R.id.my_account_personal_layout_account_center:
                //��ת�˻�����
                Intent accCenterIntent = new Intent(mainActivity, AccountCenterActivity.class);
                accCenterIntent.putExtra("ylUserRMBAccountInfo",yilianAccountInfo);
                accCenterIntent.putExtra("hfUserRMBAccountInfo",huifuAccountInfo);
                accCenterIntent.putExtra("yjbBaseInfo",yjbInterestBaseInfo);
                startActivity(accCenterIntent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        YLFLogger.d("UserPersonalFragment onResume()");
        handler.sendEmptyMessage(REQUEST_GET_USERINFO_WHAT);
        handler.sendEmptyMessage(REQUEST_YUANMONEY_WHAT);
        if(!isVerify || !isBindcard){
            checkIsVerify("��ʼ��");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        YLFLogger.d("UserPersonalFragment onHiddenChanged():"+hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        YLFLogger.d("UserPersonalFragment setUserVisibleHint()");
        if(mainActivity == null)
            return;
        if(isVisibleToUser && (!isBindcard || !isVerify)){
            checkIsVerify("��ʼ��");
        }
        if(isVisibleToUser && SettingsManager.getHYFLFlag(mainActivity.getApplicationContext(),
                SettingsManager.getUserId(mainActivity.getApplicationContext())+"hyfl02")){
            //�жϻ�Ա����2���Ƿ��ڽ���
            handler.sendEmptyMessage(REQUEST_HYFL_WHAT);
        }
        //���ʵ���󿨻�δ��ɣ��򷵻ص�ʱ��ˢ����
        if(unverifyLayout.getVisibility() == View.VISIBLE){
            checkIsVerify("��ʼ��");
        }
    }

    private void initUserInfoData(boolean isVerify, boolean isBinding){
        unverifyLayout.setVisibility(View.VISIBLE);
        if(isVerify){
            idcardLogo.setBackgroundResource(R.drawable.my_account_personal_idcard_light);
            if(isBinding){
                unverifyLayout.setVisibility(View.GONE);
                bankcardLogo.setBackgroundResource(R.drawable.my_account_personal_bankcard_light);
            }else{
                bankcardLogo.setBackgroundResource(R.drawable.my_account_personal_bankcard_logo);
                unverifyPromptTV.setText("����δ�����п������ȥ��");
            }
        }else{
            idcardLogo.setBackgroundResource(R.drawable.my_account_personal_idcard_logo);
            bankcardLogo.setBackgroundResource(R.drawable.my_account_personal_bankcard_logo);
            unverifyPromptTV.setText(Html.fromHtml("����δ���ʵ����֤�����ȥʵ��"));
        }
    }

    /**
     * ����ˢ�µĲ���
     * @param v
     */
    private void initRefreshLayout(View v){
        mainRefreshLayout = (PtrClassicFrameLayout) v.findViewById(R.id.user_personal_fragment_refresh_layout);
        mainRefreshLayout.setLastUpdateTimeRelateObject(this);
        mainRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.sendEmptyMessageDelayed(REQUEST_GET_USERINFO_WHAT,200L);
                handler.sendEmptyMessageDelayed(REQUEST_YUANMONEY_WHAT,300L);
                if(!isVerify || !isBindcard){
                    checkIsVerify("��ʼ��");
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, personalMainLayout, header);
            }
        });
        mainRefreshLayout.setResistance(1.7f);
        mainRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mainRefreshLayout.setDurationToClose(200);
        mainRefreshLayout.setDurationToCloseHeader(1000);
        // default is false
        mainRefreshLayout.setPullToRefresh(false);
        // default is true
        mainRefreshLayout.setKeepHeaderWhenRefresh(true);
    }

    private void initAccountData(UserRMBAccountInfo yilianAccount,UserRMBAccountInfo huifuAccount,double yjbInterest){
        double yilianBalance = 0d;
        double huifuBalance = 0d;
        double totalBalance = 0d;
        double dsBalance = 0d;//����
        double djBalance = 0d;//����
        double accuntTotal = 0d;
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            dsBalance = Double.parseDouble(yilianAccount.getCollection_money()) + yjbInterest;
        } catch (Exception e) {
        }
        try {
            djBalance = Double.parseDouble(yilianAccount.getFrozen_money());
        } catch (Exception e) {
        }
        try {
            yilianBalance = Double.parseDouble(yilianAccount.getUse_money());
            if(huifuAccount != null){
                huifuBalance = Double.parseDouble(huifuAccount.getUse_money());
            }
            totalBalance = yilianBalance + huifuBalance;
            if(totalBalance < 1){
                zhyeBalanceTV.setText("0"+df.format(totalBalance));
            }else{
                zhyeBalanceTV.setText(df.format(totalBalance));
            }
            accuntTotal = totalBalance + dsBalance + djBalance;
            if(accuntTotal < 1){
                zhyeTotalTV.setText("0"+df.format(accuntTotal));
            }else{
                zhyeTotalTV.setText(df.format(accuntTotal));
            }
        } catch (Exception e) {
        }
    }

    /**
     * ��ȡ��Ϣȯ��Dialog
     */
    private void showGetJXQDialog(final String type) {
        View contentView = LayoutInflater.from(mainActivity).inflate(
                R.layout.user_fragment_lxfx_jxq, null);
        Button leftBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_leftBtn);
        Button rightBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_rightBtn);
        ImageView delBtn = (ImageView) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_delbtn);
        TextView content = (TextView) contentView.findViewById(R.id.user_fragment_lxfx_content);
        if("JXQ".equals(type)){
            content.setText("����һ�ż�Ϣȯδ��ȡ��");
        }else if("HYFL_02".equals(type)){
            content.setText("�װ����û�������20����Ʒ����ȡӴ~");
        }
        final CheckBox cb = (CheckBox) contentView.findViewById(R.id.user_fragment_lxfx_dialog_cb);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                mainActivity, R.style.Dialog_Transparent); // �ȵõ�������
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        final String keyLXFX = SettingsManager.getUserId(mainActivity)+"lxfx";
        final String keyHYFL = SettingsManager.getUserId(mainActivity)+"hyfl02";
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
                    }
                }else{
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
                    }
                }
                if("JXQ".equals(type)){
                    Intent intent = new Intent(mainActivity,LXFXTempActivity.class);
                    startActivity(intent);
                }else if("HYFL_02".equals(type)){
                    Intent intent = new Intent(mainActivity,PrizeRegion2TempActivity.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
                    }
                }else{
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
                    }
                }
                dialog.dismiss();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
                    }
                }else{
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
                    }
                }
                dialog.dismiss();
            }
        });
        // ��������������ˣ���������ʾ����
        dialog.show();
        WindowManager windowManager = mainActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * �����û���Ϣ������hf_user_id�ֶ��ж��û��Ƿ��л㸶�˻�
     * @param userId
     * @param phone
     */
    private void requestUserInfo(final String userId,String phone){
        AsyncUserSelectOne userTask = new AsyncUserSelectOne(mainActivity, userId, phone,"", "", new OnGetUserInfoByPhone() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        UserInfo userInfo = baseInfo.getUserInfo();
                        Message msg = handler.obtainMessage(REQUEST_GET_USERINFO_SUCCESS);
                        msg.obj = userInfo;
                        handler.sendMessage(msg);
                    }else{
                        requestYilianAccount(userId,false);
                    }
                }else{
                    requestYilianAccount(userId,false);
                }
            }
        });
        userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * �����˻�
     * @param userId
     * @param isRequestHuifu �Ƿ��л㸶���˻�
     */
    private void requestYilianAccount(final String userId,final boolean isRequestHuifu){
        AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(mainActivity, userId, new OnCommonInter(){
            @Override
            public void back(BaseInfo info) {
                if(info != null){
                    int resultCode = SettingsManager.getResultCode(info);
                    if(resultCode == 0){
                        yilianAccountInfo = info.getRmbAccountInfo();
                        if(isRequestHuifu){
                            requestHuifuAccount(userId);
                        }else{
                            requestYJBInterest(userId, "δ����");
                        }
                    }
                }
            }
        });
        yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * Ԫ����˻�
     * @param userId
     */
    private void requestYuanMoney(String userId){
        AsyncUserYUANAccount accountTask = new AsyncUserYUANAccount(mainActivity,
                userId, new OnUserYUANAccountInter() {
            @Override
            public void back(BaseInfo info) {
                mainRefreshLayout.refreshComplete();
                if(info != null){
                    int resultCode = SettingsManager.getResultCode(info);
                    if(resultCode == 0){
                        UserYUANAccountInfo accountInfo = info.getYuanAccountInfo();
                        if(accountInfo != null){
                            double coinD = 0d;
                            try {
                                coinD= Double.parseDouble(accountInfo.getUse_coin());
                            } catch (Exception e) {
                            }
                            if(coinD <= 0){
                                usedYJBTV.setVisibility(View.GONE);
                            }else{
                                usedYJBTV.setVisibility(View.VISIBLE);
                                usedYJBTV.setText(accountInfo.getUse_coin()+"Ԫ��ҿ���");
                            }
                        }
                    }else{
                        usedYJBTV.setVisibility(View.GONE);
                    }
                }else{
                    usedYJBTV.setVisibility(View.GONE);
                }
            }
        });
        accountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * �жϻ�Ƿ��Ѿ���ʼ
     * @param activeTitle
     */
    private void requestActiveTime(String activeTitle){
        if(mainActivity == null)
            return;
        AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(mainActivity, activeTitle,
                new OnCommonInter() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if (resultCode == 0) {
                                //��ѿ�ʼ
                                requestPrizeList(SettingsManager.getUserId(mainActivity.getApplicationContext()));
                            } else if (resultCode == -3) {
                                //�����
                            } else if (resultCode == -2) {
                                //���û��ʼ
                            }
                        }
                    }
                });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * ��֤�û��Ƿ��Ѿ���֤
     * @param type ����ֵ��,�����֡����������н���
     */
    private void checkIsVerify(final String type){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        RequestApis.requestIsVerify(mainActivity, SettingsManager.getUserId(mainActivity), new OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                isVerify = flag;
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if("�����н�".equals(type)){
                    rechargeBtn.setEnabled(true);
                    withdrawBtn.setEnabled(true);
                    yqyjLayout.setEnabled(true);
                    zhszLayout.setEnabled(true);
                    Intent intent = new Intent();
                    intent.setClass(mainActivity,InvitateActivity.class);
                    intent.putExtra("is_verify", flag);
                    startActivity(intent);
                    return;
                }
                if(flag){
                    //�û��Ѿ�ʵ��
                    checkIsBindCard(type);
                }else{
                    if("��ʼ��".equals(type)){
                        initUserInfoData(false,false);
                        return;
                    }
                    rechargeBtn.setEnabled(true);
                    withdrawBtn.setEnabled(true);
                    yqyjLayout.setEnabled(true);
                    zhszLayout.setEnabled(true);
                    if("�˻�����".equals(type)){
                        Intent intentAccountSetting = new Intent(mainActivity,AccountSettingActivity.class);
                        intentAccountSetting.putExtra("is_binding", false);
                        startActivity(intentAccountSetting);
                        return;
                    }
                    //�û�û��ʵ��
                    Intent intent = new Intent(mainActivity,UserVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void isSetWithdrawPwd(boolean flag, Object object) {
                //�û��Ƿ��Ѿ�������������
                isSetWithdrawPwd = flag;
                if(SettingsManager.isCompanyUser(mainActivity.getApplicationContext())&&"����".equals(type)){
                    rechargeBtn.setEnabled(true);
                    withdrawBtn.setEnabled(true);
                    yqyjLayout.setEnabled(true);
                    zhszLayout.setEnabled(true);
                    Intent intent = new Intent();
                    //��ҵ�û�
                    //Ҫ���ж��û��Ƿ��Ѿ������������루�Ѿ����ж��û��Ƿ�ʵ����ʱ���жϹ������ֶ�isSetWithdrawPwd��
                    if(isSetWithdrawPwd){
                        //�û��Ѿ�������������
                        intent.setClass(mainActivity, WithdrawCompActivity.class);
                    }else{
                        intent.setClass(mainActivity, WithdrawPwdGetbackActivity.class);
                        intent.putExtra("type", "����");
                    }
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * �ж��û��Ƿ��Ѿ���
     * @param type "��ֵ","����","�����н�"
     */
    private void checkIsBindCard(final String type){
        RequestApis.requestIsBinding(mainActivity, SettingsManager.getUserId(mainActivity), "����", new OnIsBindingListener() {
            @Override
            public void isBinding(boolean flag, Object object) {
                isBindcard = flag;
                rechargeBtn.setEnabled(true);
                withdrawBtn.setEnabled(true);
                yqyjLayout.setEnabled(true);
                zhszLayout.setEnabled(true);
                Intent intent = new Intent();
                if(flag){
                    if("��ʼ��".equals(type)){
                        initUserInfoData(true,true);
                        return;
                    }
                    //�û��Ѿ���
                    if("��ֵ".equals(type)){
                        //��ôֱ��������ֵҳ��
                        intent.setClass(mainActivity, RechargeActivity.class);
                    }else if("����".equals(type)){
                        //Ҫ���ж��û��Ƿ��Ѿ������������루�Ѿ����ж��û��Ƿ�ʵ����ʱ���жϹ������ֶ�isSetWithdrawPwd��
                        if(isSetWithdrawPwd){
                            //�û��Ѿ�������������
                            intent.setClass(mainActivity, WithdrawActivity.class);
                        }else{
                            intent.setClass(mainActivity, WithdrawPwdGetbackActivity.class);
                            intent.putExtra("type", "����");
                        }
                    }else if("�����н�".equals(type)){
                        intent.setClass(mainActivity, InvitateActivity.class);
                    }else if("�˻�����".equals(type)){
                        intent.setClass(mainActivity, AccountSettingActivity.class);
                        intent.putExtra("is_binding", true);
                    }
                    startActivity(intent);
                }else{
                    if("��ʼ��".equals(type)){
                        initUserInfoData(true,false);
                        return;
                    }
                    //�û���û�а�
                    if("�˻�����".equals(type)){
                        intent.setClass(mainActivity, AccountSettingActivity.class);
                        intent.putExtra("is_binding", false);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("type", type);
                        intent.putExtra("bundle", bundle);
                        intent.setClass(mainActivity, BindCardActivity.class);
                    }
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * �㸶�˻���Ϣ
     * @param userId
     */
    private void requestHuifuAccount(final String userId){
        AsyncHuiFuRMBAccount huifuTask = new AsyncHuiFuRMBAccount(mainActivity, userId, new OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        huifuAccountInfo = baseInfo.getRmbAccountInfo();
                        requestYJBInterest(userId, "δ����");
                    }
                }
            }
        });
        huifuTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * Ԫ��ұ�������
     * @param userId
     * @param repayStatus
     */
    private void requestYJBInterest(String userId,String repayStatus){
        AsyncYJBInterest yjbTask = new AsyncYJBInterest(mainActivity, userId, repayStatus, new OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        yjbInterestBaseInfo = baseInfo;
                        try {
                            initAccountData(yilianAccountInfo,huifuAccountInfo,Double.parseDouble(baseInfo.getMsg()));
                        } catch (Exception e) {
                            initAccountData(yilianAccountInfo,huifuAccountInfo,0);
                        }
                    }else{
                        initAccountData(yilianAccountInfo,huifuAccountInfo,0);
                    }
                }
            }
        });
        yjbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * �ҵ���Ʒ�б�
     * @param userId
     */
    private void requestPrizeList(String userId){
        AsyncPrizeList prizeListTask = new AsyncPrizeList(mainActivity, userId, "0", "5", "","HYFL_02",
                new OnCommonInter(){
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                //
                            }else{
                                //û����ȡ��,�����ƻ�2��
                                if(SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(mainActivity)+"hyfl02")){
                                    showGetJXQDialog("HYFL_02");
                                }
                            }
                        }else{
                        }
                    }
                });
        prizeListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * ��ȡ���ʦ������
     * @param phone
     */
    private void requestLcsName(String phone){
        AsyncGetLCSName lcsTask = new AsyncGetLCSName(mainActivity, phone, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        //�����ʦ
                        isLcs = true;
                        yjyLayout.setVisibility(View.VISIBLE);
                    }else{
                        //�����ʦ
                        isLcs = false;
                        yjyLayout.setVisibility(View.GONE);
                    }
                }else{
                    isLcs = false;
                    yjyLayout.setVisibility(View.GONE);
                }
                if(isLcs){
                    if("".equals(mUserInfo.getReal_name())){
                        usernameTV.setText("���ã�����"+mUserInfo.getUser_name());
                    }else{
                        usernameTV.setText("���ã�����"+mUserInfo.getReal_name());
                    }
                    headLogo.setBackgroundResource(R.drawable.my_account_personal_lcs_headlogo);
                    yqyjPrompt.setText("���Ӱ��ƣ�ȡ���ѵ�");
                    yqyjText.setText("Ԫ�Ƶ�");
                }else{
                    if("".equals(mUserInfo.getReal_name())){
                        usernameTV.setText("���ã��𾴵�"+mUserInfo.getUser_name());
                    }else{
                        usernameTV.setText("���ã��𾴵�"+mUserInfo.getReal_name());
                    }
                    headLogo.setBackgroundResource(R.drawable.my_account_personal_nor_headlogo);
                    yqyjPrompt.setText("");
                    yqyjText.setText("�����н�");
                }
            }
        });
        lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
