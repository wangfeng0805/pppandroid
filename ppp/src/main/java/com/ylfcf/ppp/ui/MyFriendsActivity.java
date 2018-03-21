package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ylfcf.view.CustomerFooter;
import com.example.ylfcf.widget.XRefreshView;
import com.example.ylfcf.widget.XRefreshView.SimpleXRefreshListener;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.MyFriendAdapter;
import com.ylfcf.ppp.async.AsyncFriendsPageInfo;
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.FriendInfo;
import com.ylfcf.ppp.entity.FriendsPageInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * �ҵĺ���ҳ��
 * Created by Administrator on 2017/7/4.
 */

public class MyFriendsActivity extends BaseActivity implements View.OnClickListener{
    private static final String className = "MyFriendsActivity";
    private final int REQUEST_FRIENDS_LIST_WHAT = 6721;
    private final int REQUEST_FRIENDS_LIST_SUC = 6722;
    private final int REQUEST_FRIENDS_LIST_FAILT = 6723;
    private static final int REQUEST_USERINFO_WHAT = 1203;

    private final int REQUEST_LCS_NAME_WHAT = 6724;//�Ƿ������ʦ

    private int page = 0;
    private int pageSize = 20;
    private List<FriendInfo> friendInfoTempList = new ArrayList<FriendInfo>();
    private boolean isRefresh = false;//�Ƿ�����ˢ�����ݡ�
    private boolean isLoadMore = false;//�Ϸ����������ظ���

    private LinearLayout topLeftBtn;
    private TextView topTitleTV;
    private LinearLayout topmainLayout;//
    private LayoutInflater mLayoutInflater;
    private XRefreshView mXRefreshView;
    private TextView totalFriendsCountTV;//�ܼƺ���
    private LinearLayout zjjjLayout;//ֱ�Ӽ�Ӻ������ڵĲ���
    private TextView zjhyCountTV;//ֱ�Ӻ���
    private TextView jjhyCountTV;//��Ӻ���
    private ImageView promptImg;//��ʾ
    private ListView mListView;
    private MyFriendAdapter adapter;

    private boolean isLcs = false;
    private UserInfo userInfo = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_FRIENDS_LIST_WHAT:
                    requestFriendsList(SettingsManager.getUserId(MyFriendsActivity.this));
                    break;
                case REQUEST_FRIENDS_LIST_SUC:
                    FriendsPageInfo pageInfo = (FriendsPageInfo)msg.obj;
                    if(isRefresh){
                        friendInfoTempList.clear();
                    }
                    friendInfoTempList.addAll(pageInfo.getFriendList());
                    initDatas(pageInfo,friendInfoTempList);
                    isRefresh = false;
                    isLoadMore = false;
                    break;
                case REQUEST_FRIENDS_LIST_FAILT:
                    if(isLoadMore){
                        mXRefreshView.setLoadComplete(true);
                    }
                    initDatas(null,friendInfoTempList);
                    isRefresh = false;
                    isLoadMore = false;
                    break;
                case REQUEST_LCS_NAME_WHAT:
                    requestLcsName(SettingsManager.getUser(getApplicationContext()));
                    break;
                case REQUEST_USERINFO_WHAT:
                    requestUserInfo(SettingsManager.getUserId(getApplicationContext()),"","");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myfriends_activity);
        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        findViews();
        handler.sendEmptyMessage(REQUEST_LCS_NAME_WHAT);
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("�ҵĺ���");

        topmainLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.myfriends_activity_toplayout,null);
        mXRefreshView = (XRefreshView) findViewById(R.id.myfriends_activity_xrefreshview);
        totalFriendsCountTV = (TextView) topmainLayout.findViewById(R.id.myfriends_activity_count);
        zjjjLayout = (LinearLayout) topmainLayout.findViewById(R.id.myfriends_activity_toplayout_bottomlayout);
        zjhyCountTV = (TextView) topmainLayout.findViewById(R.id.myfriends_activity_zjhy_count);
        jjhyCountTV = (TextView) topmainLayout.findViewById(R.id.myfriends_activity_jjhy_count);
        promptImg = (ImageView) topmainLayout.findViewById(R.id.myfriends_activity_prompt_img);
        promptImg.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.myfriends_activity_listview);
        mListView.addHeaderView(topmainLayout);
        initXRefrshView();
        initListeners();
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
        UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
        UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
    }

    private void initXRefrshView(){
        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setPinnedTime(1000);
        mXRefreshView.setAutoLoadMore(false);
//		xRefreshView.setCustomHeaderView(new CustomHeader(this));
//		xRefreshView.setCustomHeaderView(new XRefreshViewHeader(this));
        mXRefreshView.setMoveForHorizontal(true);
        mXRefreshView.setCustomFooterView(new CustomerFooter(this));
//		xRefreshView.setPinnedContent(true);
        //���õ���RecyclerView������������Ժ�Ļص�ʱ��
        mXRefreshView.setScrollBackDuration(300);
    }

    private void initListeners() {
        mXRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                isRefresh = true;
                isLoadMore = false;
                mXRefreshView.setLoadComplete(false);
                page = 0;
                handler.sendEmptyMessage(REQUEST_FRIENDS_LIST_WHAT);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                isRefresh = false;
                isLoadMore = true;
                page ++;
                handler.sendEmptyMessage(REQUEST_FRIENDS_LIST_WHAT);
            }
        });
    }

    private void initAdapter(){
        adapter = new MyFriendAdapter(MyFriendsActivity.this, new MyFriendAdapter.OnFriendItemClickListener() {
            @Override
            public void onItemClick(int position, FriendInfo friendInfo) {
                //��ȡ��Ϣȯ
                Intent intent = new Intent(MyFriendsActivity.this,ChooseJXQActivity.class);
                intent.putExtra("friendInfo",friendInfo);
                startActivity(intent);
            }
        });
        mListView.setAdapter(adapter);
    }

    private void updateAdapter(List<FriendInfo> friendInfoList){
        if(friendInfoList == null)
            return;
        adapter.setItems(friendInfoList,isLcs);
    }

    private void initDatas(FriendsPageInfo pageInfo,List<FriendInfo> list){
        if(isLcs || (userInfo != null && "΢���".equals(userInfo.getType()))&&"0".equals(userInfo.getExtension_user_id())){
            //���ʦ����΢����A���û�
            zjjjLayout.setVisibility(View.VISIBLE);
        }else{
            zjjjLayout.setVisibility(View.GONE);
        }
        if(pageInfo == null)
            return;
        totalFriendsCountTV.setText(pageInfo.getCount_extension_user());
        zjhyCountTV.setText(pageInfo.getOne_level_users_count());
        jjhyCountTV.setText(pageInfo.getSecond_level_users_count());

        updateAdapter(list);
    }

    private void updateRefreshListViewStatus(boolean isRefresh,boolean isLoadMore){
        if(isRefresh){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mXRefreshView.stopRefresh();
                }
            },1000L);
        }else if(isLoadMore){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mXRefreshView.stopLoadMore();
                }
            },1000L);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.myfriends_activity_prompt_img:
                //��ʾ
                showTipsDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * Dialog
     */
    private void showTipsDialog(){
        View contentView = LayoutInflater.from(MyFriendsActivity.this).inflate(R.layout.myinvitation_tips_dialog_layout, null);
        TextView contentTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_content);
        if(isLcs){
            contentTV.setText(getResources().getString(R.string.my_friends_list_activity_toptips));
        }else{
            contentTV.setGravity(Gravity.CENTER);
            contentTV.setText("ͳ����Ч���ڵĺ���������");
        }
        final Button okBtn = (Button) contentView.findViewById(R.id.myinvitation_tips_dialog_sure_btn);
        AlertDialog.Builder builder=new AlertDialog.Builder(MyFriendsActivity.this, R.style.Dialog_Transparent);  //�ȵõ�������
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
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * �ҵĺ����б�
     * @param userId
     */
    private void requestFriendsList(String userId){
        if(mLoadingDialog != null && !isFinishing()){
            mLoadingDialog.show();
        }
        AsyncFriendsPageInfo friendsTask = new AsyncFriendsPageInfo(MyFriendsActivity.this, userId, String.valueOf(page),
                String.valueOf(pageSize), new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                mXRefreshView.setVisibility(View.VISIBLE);
                updateRefreshListViewStatus(isRefresh,isLoadMore);
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        if(baseInfo.getmFriendsPageInfo().getFriendList().size() > 0){
                            Message msg = handler.obtainMessage(REQUEST_FRIENDS_LIST_SUC);
                            msg.obj = baseInfo.getmFriendsPageInfo();
                            handler.sendMessage(msg);
                        }else{
                            Message msg = handler.obtainMessage(REQUEST_FRIENDS_LIST_FAILT);
                            msg.obj = baseInfo.getMsg();
                            handler.sendMessage(msg);
                        }
                    }else{
                        Message msg = handler.obtainMessage(REQUEST_FRIENDS_LIST_FAILT);
                        msg.obj = baseInfo.getMsg();
                        handler.sendMessage(msg);
                    }
                }else{
                    Message msg = handler.obtainMessage(REQUEST_FRIENDS_LIST_FAILT);
                    msg.obj = "�������粻����";
                    handler.sendMessage(msg);
                }
            }
        });
        friendsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * ��ȡ
     * @param phone
     */
    private void requestLcsName(String phone){
        AsyncGetLCSName lcsTask = new AsyncGetLCSName(MyFriendsActivity.this, phone, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
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
     * �����û���Ϣ������hf_user_id�ֶ��ж��û��Ƿ��л㸶�˻�
     * @param userId
     * @param phone
     */
    private void requestUserInfo(final String userId,String phone,String coMobile){
        AsyncUserSelectOne userTask = new AsyncUserSelectOne(MyFriendsActivity.this, userId, phone,coMobile, "",
                new Inter.OnGetUserInfoByPhone() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                userInfo = baseInfo.getUserInfo();
                                if(userInfo != null && "΢���".equals(userInfo.getType())){
                                    promptImg.setVisibility(View.GONE);
                                }else{
                                    promptImg.setVisibility(View.VISIBLE);
                                }
                            }
                            handler.sendEmptyMessage(REQUEST_FRIENDS_LIST_WHAT);
                        }
                    }
                });
        userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
