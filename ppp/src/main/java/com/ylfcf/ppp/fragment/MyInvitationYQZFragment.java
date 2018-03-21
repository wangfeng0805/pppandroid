package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ExtensionAdapter;
import com.ylfcf.ppp.async.AsyncExtensionNewPageInfo;
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionNewInfo;
import com.ylfcf.ppp.entity.ExtensionNewPageInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.MyInvitationActivity;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * ��Ǯ׬
 * Created by Administrator on 2017/11/24.
 */

public class MyInvitationYQZFragment extends BaseFragment implements View.OnClickListener{
    private MyInvitationActivity mainActivity;
    private View rootView;

    private static final int REQUEST_EXTENSION_WHAT = 1200;
    private static final int REQUEST_EXTENSION_SUCCESS_WHAT = 1201;
    private static final int REQUEST_USERINFO_WHAT = 1203;
    private static final int REQUEST_USERINFO_SUC = 1204;

    private static final int REQUEST_LCS_WHAT = 1202;

    private TextView totalMoneyTV;
    private TextView myselfMoneyTV;
    private TextView oneMoneyTV;
    private TextView twoMoneyTV;
    private TextView otherMoneyTV;
    private LinearLayout totalMoneyLayout,myselfMoneyLayout,oneMoneyLayout,twoMoneyLayout,otherMoneyLayout;
    private RelativeLayout btnsLayout;
    private Button qsztcBtn;//����׬���
    private Button catTipsBtn;//�鿴��ʾ
    private PullToRefreshListView mListView;
    private TextView nodataTV;//��������
    private ExtensionNewPageInfo mExtensionPageInfo;
    private ExtensionAdapter adapter;
    private int page = 0;
    private int pageSize = 20;
    private List<ExtensionNewInfo> extensionList = new ArrayList<ExtensionNewInfo>();
    private boolean isLoadMore = false;
    private boolean isLcs = false;
    private UserInfo userInfo = null;
    private LoadingDialog mLoadingDialog;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_EXTENSION_WHAT:
                    requestExtension(SettingsManager.getUserId(mainActivity.getApplicationContext()));
                    break;
                case REQUEST_EXTENSION_SUCCESS_WHAT:
                    mExtensionPageInfo = (ExtensionNewPageInfo) msg.obj;
                    if(mExtensionPageInfo != null){
                        if(isLoadMore){
                            extensionList.addAll(mExtensionPageInfo.getExtensionList());
                        }else{
                            extensionList.clear();
                            extensionList.addAll(mExtensionPageInfo.getExtensionList());
                        }
                        initData();
                        updateAdapter(extensionList);
                    }
                    break;
                case REQUEST_LCS_WHAT:
                    requestLcsName(SettingsManager.getUser(mainActivity.getApplicationContext()));
                    break;
                case REQUEST_USERINFO_WHAT:
                    requestUserInfo(SettingsManager.getUserId(mainActivity.getApplicationContext()),"");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MyInvitationActivity) getActivity();
        mExtensionPageInfo = (ExtensionNewPageInfo) mainActivity.getIntent()
                .getSerializableExtra("ExtensionPageInfo");
        mLoadingDialog = new LoadingDialog(mainActivity,"���ڼ���...",R.anim.loading);
        if(rootView==null){
            rootView = inflater.inflate(R.layout.myinvitation_yqz_fragment_layout, null);
            findViews(rootView,inflater);
        }
        //�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        handler.sendEmptyMessage(REQUEST_LCS_WHAT);
        return rootView;
    }

    private void findViews(View rootView,LayoutInflater inflater){

        qsztcBtn = (Button) rootView.findViewById(R.id.myinvitation_activity_top_btn);
        qsztcBtn.setOnClickListener(this);
        catTipsBtn = (Button) rootView.findViewById(R.id.myinvitation_activity_top_cat_tipsbtn);
        catTipsBtn.setOnClickListener(this);
        btnsLayout = (RelativeLayout) rootView.findViewById(R.id.myinvitation_activity_btns_layout);
        totalMoneyTV = (TextView) rootView.findViewById(R.id.myinvitation_activity_totalmoney);
        myselfMoneyTV = (TextView) rootView.findViewById(R.id.myinvitation_activity_myselfmoney);
        oneMoneyTV = (TextView) rootView.findViewById(R.id.myinvitation_activity_onemoney);
        twoMoneyTV = (TextView) rootView.findViewById(R.id.myinvitation_activity_twomoney);
        otherMoneyTV = (TextView) rootView.findViewById(R.id.myinvitation_activity_othermoney);
        totalMoneyLayout = (LinearLayout) rootView.findViewById(R.id.myinvitation_activity_totalmoney_layout);
        myselfMoneyLayout = (LinearLayout) rootView.findViewById(R.id.myinvitation_activity_myself_layout);
        oneMoneyLayout = (LinearLayout) rootView.findViewById(R.id.myinvitation_activity_onemoney_layout);
        twoMoneyLayout = (LinearLayout) rootView.findViewById(R.id.myinvitation_activity_twomoney_layout);
        otherMoneyLayout = (LinearLayout) rootView.findViewById(R.id.myinvitation_activity_othermoney_layout);
        nodataTV = (TextView) rootView.findViewById(R.id.myinvitation_activity_nodata);
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.myinvitation_listview);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        initListeners();
        initAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myinvitation_activity_top_btn:
                //����׬���
                Intent intentBanner = new Intent(mainActivity,BannerTopicActivity.class);
                BannerInfo bannerInfo = new BannerInfo();
                bannerInfo.setArticle_id(Constants.TopicType.TUIGUANGYUAN);
                bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
                bannerInfo.setFrom_where("��������");
                intentBanner.putExtra("BannerInfo", bannerInfo);
                startActivityForResult(intentBanner, 100);
                break;
            case R.id.myinvitation_activity_top_cat_tipsbtn:
                //�鿴��ʾ
                showTipsDialog();
                break;
            default:
                break;
        }
    }

    /**
     * �˳���¼��Dialog
     */
    private void showTipsDialog(){
        View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.myinvitation_tips_dialog_layout, null);
        final Button okBtn = (Button) contentView.findViewById(R.id.myinvitation_tips_dialog_sure_btn);
        final TextView contentTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_content);
        if(userInfo != null && "΢���".equals(userInfo.getType()) && "0".equals(userInfo.getExtension_user_id())){
            //΢���A���û�
            double otherTotalD = 0d;
            try{
                otherTotalD = Double.parseDouble(mExtensionPageInfo.getOther_total());
            }catch (Exception e){
                e.printStackTrace();
            }
            if(otherTotalD <= 0){
                //���ʦ��ֱ�Ӻ�������û�н���Ϊ���ʦ�����
                contentTV.setText(getResources().getString(R.string.myinvitation_tips_string));
            }else{
                //���ʦ��ֱ�Ӻ��������н���Ϊ���ʦ�����
                contentTV.setText(getResources().getString(R.string.myinvitation_tips_string1));
            }
        }else if(isLcs){
            //���ʦA���û�
            double otherTotalD = 0d;
            try{
                otherTotalD = Double.parseDouble(mExtensionPageInfo.getOther_total());
            }catch (Exception e){
                e.printStackTrace();
            }
            if(otherTotalD <= 0){
                //���ʦ��ֱ�Ӻ�������û�н���Ϊ���ʦ�����
                contentTV.setText(getResources().getString(R.string.myinvitation_tips_string4));
            }else{
                //���ʦ��ֱ�Ӻ��������н���Ϊ���ʦ�����
                contentTV.setText(getResources().getString(R.string.myinvitation_tips_string5));
            }
        }else if(userInfo != null && "΢���".equals(userInfo.getType()) && !"0".equals(userInfo.getExtension_user_id())){
            //΢����B��C���û�
            contentTV.setText(getResources().getString(R.string.myinvitation_tips_string3));
        }else{
            //�����ʦ
            contentTV.setText(getResources().getString(R.string.myinvitation_tips_string2));
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        okBtn.setOnClickListener(new View.OnClickListener() {
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
        lp.width = display.getWidth()*4/5;
        dialog.getWindow().setAttributes(lp);
    }

    private void initListeners() {
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>(){
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // ����ˢ��
                page = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadMore = false;
                        handler.sendEmptyMessage(REQUEST_EXTENSION_WHAT);
                    }
                }, 1000L);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // �������ظ���
                page++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadMore = true;
                        handler.sendEmptyMessage(REQUEST_EXTENSION_WHAT);
                    }
                }, 1000L);
            }
        });
    }

    private void initData(){
        if(isLcs || (userInfo != null && "΢���".equals(userInfo.getType()) && "0".equals(userInfo.getExtension_user_id()))){
            //���ʦ����΢����A���û�
            totalMoneyLayout.setVisibility(View.VISIBLE);
            totalMoneyLayout.setGravity(Gravity.LEFT);
            oneMoneyLayout.setVisibility(View.VISIBLE);
            twoMoneyLayout.setVisibility(View.VISIBLE);
            double otherTotalD = 0d;
            try{
                otherTotalD = Double.parseDouble(mExtensionPageInfo.getOther_total());
            }catch (Exception e){
                e.printStackTrace();
            }
            if(otherTotalD > 0){
                otherMoneyLayout.setVisibility(View.VISIBLE);
            }else{
                otherMoneyLayout.setVisibility(View.GONE);
            }
            if(userInfo != null && "΢���".equals(userInfo.getType())
                    && "0".equals(userInfo.getExtension_user_id())){
                //΢���A���û�����ʾ��ʷ����
                otherMoneyLayout.setVisibility(View.GONE);
            }
            if(mExtensionPageInfo != null && isLcs){
                try{
                    double myselfMoneyD = Double.parseDouble(mExtensionPageInfo.getMyself_total());
                    if(myselfMoneyD > 0){
                        myselfMoneyLayout.setVisibility(View.VISIBLE);
                    }else{
                        myselfMoneyLayout.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    myselfMoneyLayout.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }else{
                myselfMoneyLayout.setVisibility(View.GONE);
            }
        }else{
            totalMoneyLayout.setVisibility(View.VISIBLE);
            totalMoneyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            oneMoneyLayout.setVisibility(View.GONE);
            twoMoneyLayout.setVisibility(View.GONE);
            otherMoneyLayout.setVisibility(View.GONE);
        }
        if(mExtensionPageInfo == null){
            totalMoneyTV.setText("0");
            myselfMoneyTV.setText("0");
            oneMoneyTV.setText("0");
            twoMoneyTV.setText("0");
            otherMoneyTV.setText("0");
            return;
        }
        totalMoneyTV.setText(Util.double2PointDouble(mExtensionPageInfo.getReward_total()));
        myselfMoneyTV.setText(Util.double2PointDouble(mExtensionPageInfo.getMyself_total()));
        oneMoneyTV.setText(Util.double2PointDouble(mExtensionPageInfo.getOne_total()));
        twoMoneyTV.setText(Util.double2PointDouble(mExtensionPageInfo.getSecond_total()));
        otherMoneyTV.setText(Util.double2PointDouble(mExtensionPageInfo.getOther_total()));

        if(mExtensionPageInfo.getExtensionList() == null || mExtensionPageInfo.getExtensionList().size() <= 0 ){
            mListView.setVisibility(View.GONE);
            nodataTV.setVisibility(View.VISIBLE);
            return;
        }
        mListView.setVisibility(View.VISIBLE);
        nodataTV.setVisibility(View.GONE);
    }

    private void initAdapter() {
        adapter = new ExtensionAdapter(mainActivity);
        mListView.setAdapter(adapter);
    }

    private void updateAdapter(List<ExtensionNewInfo> list) {
        if(list == null || list.size() <= 0){
            nodataTV.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            return;
        }
        nodataTV.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        adapter.setItems(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * ������ѵ���Ϣ
     * @param userId
     */
    private void requestExtension(String userId) {
        if(mLoadingDialog != null && !mainActivity.isFinishing()){
            mLoadingDialog.show();
        }
        AsyncExtensionNewPageInfo taks = new AsyncExtensionNewPageInfo(
                mainActivity, userId, String.valueOf(page),
                String.valueOf(pageSize), new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
                mListView.onRefreshComplete();
                if(baseInfo == null){
                    return;
                }
                int resultCode = SettingsManager
                        .getResultCode(baseInfo);
                if (resultCode == 1) {
                    Message msg = handler.obtainMessage(REQUEST_EXTENSION_SUCCESS_WHAT);
                    msg.obj = baseInfo.getExtensionNewPageInfo();
                    handler.sendMessage(msg);
                }else{
//							Util.toastLong(MyInvitationActivity.this, baseInfo.getMsg());
                }
            }
        });
        taks.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * ��ȡ
     * @param phone
     */
    private void requestLcsName(String phone){
        if(mLoadingDialog != null && !mainActivity.isFinishing()){
            mLoadingDialog.show();
        }
        AsyncGetLCSName lcsTask = new AsyncGetLCSName(mainActivity, phone, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        //�����ʦ
                        isLcs = true;
                    }else{
                        //�����ʦ
                        isLcs = false;
                    }
                }else{
                    isLcs = false;
                }
                handler.sendEmptyMessage(REQUEST_USERINFO_WHAT);
            }
        });
        lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * @param userId
     * @param phone
     */
    private void requestUserInfo(final String userId,String phone){
        AsyncUserSelectOne userTask = new AsyncUserSelectOne(mainActivity, userId, phone, "","",
                new Inter.OnGetUserInfoByPhone() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                userInfo = baseInfo.getUserInfo();
                            }
                        }
                        handler.sendEmptyMessage(REQUEST_EXTENSION_WHAT);
                    }
                });
        userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
