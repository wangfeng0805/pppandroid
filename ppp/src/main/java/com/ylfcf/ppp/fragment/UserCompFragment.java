package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncHuiFuRMBAccount;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.async.AsyncYJBInterest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.ptr.PtrClassicFrameLayout;
import com.ylfcf.ppp.ptr.PtrDefaultHandler;
import com.ylfcf.ppp.ptr.PtrFrameLayout;
import com.ylfcf.ppp.ptr.PtrHandler;
import com.ylfcf.ppp.ui.AccountSettingActivity;
import com.ylfcf.ppp.ui.AccountSettingCompActivity;
import com.ylfcf.ppp.ui.BindCardActivity;
import com.ylfcf.ppp.ui.FundsDetailsActivity;
import com.ylfcf.ppp.ui.InvitateActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.ModifyLoginPwdActivity;
import com.ylfcf.ppp.ui.RechargeActivity;
import com.ylfcf.ppp.ui.RechargeCompActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.ui.UserVerifyActivity;
import com.ylfcf.ppp.ui.WithdrawActivity;
import com.ylfcf.ppp.ui.WithdrawCompActivity;
import com.ylfcf.ppp.ui.WithdrawPwdGetbackActivity;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.text.DecimalFormat;

/**
 * ��ҵ�û����˻�����
 * Created by Administrator on 2017/9/18.
 */

public class UserCompFragment extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_GET_USERINFO_WHAT = 10039;
    private static final int REQUEST_GET_USERINFO_SUCCESS = 10040;

    private MainFragmentActivity mainActivity;
    private View rootView;
    private LoadingDialog mLoadingDialog;

    private TextView usernameTVComp;//��ҵ�û����û���
    private TextView companyNameTV;//��ҵ��
    private TextView zhyeTotalTVComp;//��ҵ�û����˻��ܶ�

    private TextView zhyeBalanceTV;//�˻����
    private TextView djjeTV;//������
    private TextView dsjeTV;//���ս��
    private Button withdrawBtn;
    private Button rechargeBtn;
    private LinearLayout zjmxLayout;//�ʽ���ϸ
    private LinearLayout tbjlLayout;//Ͷ���¼
    private LinearLayout yqyjLayout;//�����н�
    private LinearLayout zhszLayout;//�˻�����
    private View compMainLayout;
    private View line1,line2,line3,line4,line6;
    private com.ylfcf.ppp.ptr.PtrClassicFrameLayout mainRefreshLayout;

    private UserInfo mUserInfo;
    private UserRMBAccountInfo yilianAccountInfo;//�����˻���Ϣ
    private UserRMBAccountInfo huifuAccountInfo;//�㸶�˻���Ϣ
    private BaseInfo yjbInterestBaseInfo;//Ԫ��Ҳ���������
    private String hfuserId = "";
    private boolean isSetWithdrawPwd = false;//�û��Ƿ��Ѿ����ý�������
    private boolean isShowCompLoginPWDDialog = false;//�Ƿ��Ѿ��������޸���ҵ�û���ʼ��¼�����dialog

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_GET_USERINFO_WHAT:
                    requestUserInfo(SettingsManager.getUserId(getActivity().getApplicationContext()),"");
                    break;
                case REQUEST_GET_USERINFO_SUCCESS:
                    mUserInfo = (UserInfo)msg.obj;
                    initData(mUserInfo);
                    break;
            }
        }
    };

    public static UserCompFragment newInstance(UserInfo userInfo){
        UserCompFragment f = new UserCompFragment();
        Bundle args = new Bundle();
        args.putSerializable("userinfo",userInfo);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainFragmentActivity) getActivity();
        mLoadingDialog = new LoadingDialog(mainActivity,"���ڼ���...",R.anim.loading);
        if(rootView==null){
            rootView=inflater.inflate(R.layout.user_comp_fragment, null);
        }
        findViews(rootView);
        mUserInfo = (UserInfo) getArguments().getSerializable("userinfo");
        if(mUserInfo != null){
            initData(mUserInfo);
        }else{
            handler.sendEmptyMessage(REQUEST_GET_USERINFO_WHAT);
        }
//		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        YLFLogger.d("UserCompFragment onHiddenChanged()");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        YLFLogger.d("UserCompFragment setUserVisibleHint()");
        if(isVisibleToUser && mUserInfo != null && "0".equals(mUserInfo.getInit_pwd())&&!isShowCompLoginPWDDialog){
            //��ҵ�û�û���޸ĳ�ʼ��¼����
            showCompLoginPwdDialog();
        }else{
            //�Ѿ��޸Ĺ���ʼ��¼����
        }
    }

    private void findViews(View view){
        usernameTVComp = (TextView) view.findViewById(R.id.my_account_comp_username);
        companyNameTV = (TextView) view.findViewById(R.id.my_account_comp_companyname);
        zhyeTotalTVComp = (TextView) view.findViewById(R.id.my_account_comp_zhye_total_tv);
        zhyeBalanceTV = (TextView)view.findViewById(R.id.my_account_comp_zhye_balance);
        djjeTV = (TextView)view.findViewById(R.id.my_account_comp_djje_tv);
        dsjeTV = (TextView)view.findViewById(R.id.my_account_comp_dsje_tv);
        withdrawBtn = (Button)view.findViewById(R.id.my_account_comp_withdraw_btn);
        withdrawBtn.setOnClickListener(this);
        rechargeBtn = (Button)view.findViewById(R.id.my_account_comp_recharge_btn);
        rechargeBtn.setOnClickListener(this);
        zjmxLayout = (LinearLayout)view.findViewById(R.id.my_account_comp_zjmx_layout);
        zjmxLayout.setOnClickListener(this);
        tbjlLayout = (LinearLayout)view.findViewById(R.id.my_account_comp_tbjl_layout);
        tbjlLayout.setOnClickListener(this);
        yqyjLayout = (LinearLayout)view.findViewById(R.id.my_account_comp_yqyj_layout);
        yqyjLayout.setOnClickListener(this);
        zhszLayout = (LinearLayout)view.findViewById(R.id.my_account_comp_zhsz_layout);
        zhszLayout.setOnClickListener(this);
        compMainLayout = view.findViewById(R.id.user_comp_fragment_layout);
        initRefreshLayout(view);
    }

    /**
     * ����ˢ�µĲ���
     * @param v
     */
    private void initRefreshLayout(View v){
        mainRefreshLayout = (PtrClassicFrameLayout) v.findViewById(R.id.user_comp_fragment_refresh_layout);
        mainRefreshLayout.setLastUpdateTimeRelateObject(this);
        mainRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.sendEmptyMessage(REQUEST_GET_USERINFO_WHAT);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, compMainLayout, header);
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

    private void initData(UserInfo info){
//        topTitle.setText("�ҵ��˻�");
        if(info.getUser_name() != null && !"".equals(info.getUser_name())){
            usernameTVComp.setText("���ã�" + info.getUser_name());
        }else{
            usernameTVComp.setText("���ã��𾴵Ŀͻ���" );
        }
        companyNameTV.setText(info.getReal_name());
        hfuserId = info.getHf_user_id();
        if(hfuserId != null && !"".equals(hfuserId)){
            requestYilianAccount(info.getId(),true);
        }else{
            requestYilianAccount(info.getId(),false);
        }
        if("0".equals(mUserInfo.getInit_pwd())&&!isShowCompLoginPWDDialog && getParentFragment().getUserVisibleHint()){
            //��ҵ�û�û���޸ĳ�ʼ��¼����
            showCompLoginPwdDialog();
        }else{
            //�Ѿ��޸Ĺ���ʼ��¼����
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_account_comp_withdraw_btn:
                //����
                withdrawBtn.setEnabled(false);
                checkIsVerify("����");
                break;
            case R.id.my_account_comp_recharge_btn:
                //��ֵ
                if(SettingsManager.isPersonalUser(mainActivity)){
                    checkIsVerify("��ֵ");
                }else if(SettingsManager.isCompanyUser(mainActivity)){
                    Intent intentRechargeComp = new Intent(mainActivity,RechargeCompActivity.class);
                    startActivity(intentRechargeComp);
                }
                break;
            case R.id.my_account_comp_zjmx_layout:
                //�ʽ���ϸ
                Intent intentFund = new Intent(mainActivity,FundsDetailsActivity.class);
                intentFund.putExtra("userinfo", mUserInfo);
                startActivity(intentFund);
                break;
            case R.id.my_account_comp_tbjl_layout:
    //			initAllRecLayout();
                Intent intentUserRecord = new Intent(mainActivity,UserInvestRecordActivity.class);
                startActivity(intentUserRecord);
                break;
            case R.id.my_account_comp_yqyj_layout:
                //�����н���
                yqyjLayout.setEnabled(false);
                checkIsVerify("�����н�");
                break;
            case R.id.my_account_comp_zhsz_layout:
                //�˻�����
                if(SettingsManager.isPersonalUser(mainActivity)){
                    zhszLayout.setEnabled(false);
                    checkIsVerify("�˻�����");
                }else if(SettingsManager.isCompanyUser(mainActivity)){
                    Intent intentZHSZComp = new Intent(mainActivity,AccountSettingCompActivity.class);
                    startActivity(intentZHSZComp);
                }
                break;
        }
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
        djjeTV.setText(yilianAccount.getFrozen_money());
        if(dsBalance < 1){
            dsjeTV.setText("0"+df.format(dsBalance));
        }else{
            dsjeTV.setText(df.format(dsBalance));
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
                zhyeTotalTVComp.setText("0"+df.format(accuntTotal));
            }else{
                zhyeTotalTVComp.setText(df.format(accuntTotal));
            }
        } catch (Exception e) {
        }
    }

    /**
     * ��ҵ�û������޸ĳ�ʼ�����dialog
     */
    private void showCompLoginPwdDialog(){
        if(isShowCompLoginPWDDialog)
            return;
        View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.comp_change_pwd_dialog, null);
        final Button sureBtn = (Button) contentView.findViewById(R.id.comp_change_pwd_dialog_sure_btn);//�����޸�
        final Button cancelBtn = (Button) contentView.findViewById(R.id.comp_change_pwd_dialog_cancel_btn);//�Ժ��޸�
        AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mainActivity,ModifyLoginPwdActivity.class);
                intent.putExtra("USERINFO",mUserInfo);
                startActivity(intent);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //��������������ˣ���������ʾ����
        dialog.show();
        WindowManager windowManager = mainActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
        isShowCompLoginPWDDialog = true;
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
                mainRefreshLayout.refreshComplete();
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
                    if(!SettingsManager.isCompanyUser(getActivity().getApplicationContext())){
                        //��ҵ�û���������ʵ�������Բ��ٽ����ж�
                        checkIsBindCard(type);
                    }
                }else{
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
                if(SettingsManager.isCompanyUser(getActivity().getApplicationContext())&&"����".equals(type)){
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
                rechargeBtn.setEnabled(true);
                withdrawBtn.setEnabled(true);
                yqyjLayout.setEnabled(true);
                zhszLayout.setEnabled(true);
                Intent intent = new Intent();
                if(flag){
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
                }else{
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
                }
                startActivity(intent);
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
     * �����û���Ϣ������hf_user_id�ֶ��ж��û��Ƿ��л㸶�˻�
     * @param userId
     * @param phone
     */
    private void requestUserInfo(final String userId,String phone){
        AsyncUserSelectOne userTask = new AsyncUserSelectOne(mainActivity, userId, phone,"", "", new Inter.OnGetUserInfoByPhone() {
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
}
