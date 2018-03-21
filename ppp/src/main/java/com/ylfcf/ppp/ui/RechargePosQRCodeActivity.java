package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncTLOrder;
import com.ylfcf.ppp.async.AsyncTLOrderStatus;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RechargeTempInfo;
import com.ylfcf.ppp.entity.TLOrderInfo;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.util.Hashtable;

/**
 * POS֧����ά��ɨ��ҳ��
 * Created by Administrator on 2018/1/17.
 */

public class RechargePosQRCodeActivity extends BaseActivity implements View.OnClickListener {
    private static final String className = "RechargePosQRCodeActivity";

    private static final int REQUEST_TLORDERNUM_WHAT = 5130;
    private static final int REQUEST_TLORDERNUM_SUC = 5131;

    private static final int REQUEST_TLORDERSTATUS_WHAT = 5132;
    private static final int REQUEST_TLORDERSTATUS_SUC = 5133;

    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    private ImageView qrcodeImage;
    private Button paycodeBtn;//������ɸ�����
    private TextView ordernumTV;//���׵���
    private TextView qrfailTV;
    private TextView catRechargeRecord,catOperation;
    private TextView promptTV;
    private double amount;
    private int QR_WIDTH = 0;
    private int QR_HEIGHT = 0;
    private TLOrderInfo tlOrderInfo = null;
    private UserInfo userInfo = null;

    private CountDownAsyncTask countDownAsynTask = null;
    private final long intervalTime = 1000L;//ʱ����
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_TLORDERNUM_WHAT:
                    requestTLOrderNum(SettingsManager.getUserId(getApplicationContext()),String.valueOf(amount),"android");
                    break;
                case REQUEST_TLORDERNUM_SUC:
                    break;
                case REQUEST_TLORDERSTATUS_WHAT:
                    //����״̬
                    TLOrderInfo info = (TLOrderInfo)msg.obj;
                    if(info != null){
                        requestTLOrderStatus(info,SettingsManager.getUserId(getApplicationContext()),info.getOrder());
                    }
                    break;
                case REQUEST_TLORDERSTATUS_SUC:
                    break;
                case CountDownAsyncTask.PROGRESS_UPDATE:
                    TaskDate date = (TaskDate) msg.obj;
                    long time = date.getTime();
                    countNPDownView(time);
                    break;
                case CountDownAsyncTask.FINISH:
                    paycodeBtn.setText("�������ɸ�����");
                    paycodeBtn.setEnabled(true);
                    updateQRFailStatus(true);//��ά��ʧЧ
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recharge_pos_qrcode_activity);
        amount = getIntent().getDoubleExtra("amount",0);
        userInfo = (UserInfo)getIntent().getSerializableExtra("userInfo");
        tlOrderInfo = (TLOrderInfo)getIntent().getSerializableExtra("orderInfo");
        findViews();
        QR_WIDTH = getResources().getDimensionPixelSize(R.dimen.common_measure_300dp);
        QR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_measure_300dp);

        initData(tlOrderInfo);
        Message msg = handler.obtainMessage(REQUEST_TLORDERSTATUS_WHAT);
        msg.obj = tlOrderInfo;
        handler.sendMessageDelayed(msg,10000L);//10���ʼ��ѭ����֧��״̬�ӿ�
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("POS��ֵ");

        promptTV = (TextView) findViewById(R.id.recharge_pos_qrcode_activity_prompt);
        promptTV.setText("ȷ����"+userInfo.getReal_name()+"��Ԫ�����˻���ֵ");
        qrfailTV = (TextView) findViewById(R.id.recharge_pos_qrcode_activity_qrfail);
        qrcodeImage = (ImageView) findViewById(R.id.recharge_pos_qrcode_activity_qrcode);
        qrcodeImage.setOnClickListener(this);
        paycodeBtn = (Button) findViewById(R.id.recharge_pos_qrcode_activity_recreate_btn);
        paycodeBtn.setOnClickListener(this);
        ordernumTV = (TextView) findViewById(R.id.recharge_pos_qrcode_activity_ordernum);
        catRechargeRecord = (TextView) findViewById(R.id.recharge_pos_qrcode_activity_catrecord_btn);
        catRechargeRecord.setOnClickListener(this);
        catOperation = (TextView) findViewById(R.id.recharge_pos_qrcode_activity_catoperation_btn);
        catOperation.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownAsynTask = null;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.recharge_pos_qrcode_activity_recreate_btn:
                //�������ɶ�ά��
//                handler.removeCallbacksAndMessages(REQUEST_TLORDERSTATUS_WHAT);
                handler.sendEmptyMessage(REQUEST_TLORDERNUM_WHAT);
                break;
            case R.id.recharge_pos_qrcode_activity_catrecord_btn:
                //�鿴��ֵ��¼
                Intent intent = new Intent(RechargePosQRCodeActivity.this,RechargeRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.recharge_pos_qrcode_activity_catoperation_btn:
                //�鿴ʹ��˵��
                Intent intentInstructions = new Intent(RechargePosQRCodeActivity.this,RechargePosInstructionsActivity.class);
                startActivity(intentInstructions);
                break;
            case R.id.recharge_pos_qrcode_activity_qrcode:
//                showBigEWM();
                break;
        }
    }

    /**
     * ȫ����ʾ��ά��
     */
    private void showBigEWM(){
        View contentView = LayoutInflater.from(RechargePosQRCodeActivity.this).inflate(R.layout.yqyj_ewm_dialog, null);
        ImageView img = (ImageView) contentView.findViewById(R.id.yqyj_ewm_img);
        img.setImageBitmap((Bitmap)qrcodeImage.getTag());
        AlertDialog.Builder builder=new AlertDialog.Builder(RechargePosQRCodeActivity.this, R.style.Dialog_Transparent);  //�ȵõ�������
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        //��������������ˣ���������ʾ����
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        lp.height = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * ��ά���Ƿ�ʧЧ
     * @param isFail
     */
    private void updateQRFailStatus(boolean isFail){
        if(isFail){
            qrfailTV.setVisibility(View.VISIBLE);
        }else{
            qrfailTV.setVisibility(View.GONE);
        }
    }

    /**
     * ���ɶ�ά��
     * @param info
     */
    private void initData(TLOrderInfo info){
        ordernumTV.setText("�����ţ�"+info.getOrder());
        createQRCode(info.getOrder());
        startCountDownTask();
        updateQRFailStatus(false);
    }

    /**
     * ����ʱ����ʱ
     * @param time
     */
    private void countNPDownView(long time) {
        time /= intervalTime;
        StringBuffer sb = new StringBuffer();
        sb.append("�������ɸ����루").append(time).append("�룩");
        paycodeBtn.setText(sb.toString());
    }

    /**
     * ����ʱ��ʼ
     */
    private void startCountDownTask(){
        paycodeBtn.setEnabled(false);
        long createTime = System.currentTimeMillis();
        countDownAsynTask = new CountDownAsyncTask(handler, "",
                System.currentTimeMillis(), createTime + 1000 * 60,
                intervalTime);
        SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);
    }

    private void createQRCode(String orderId){
        try {
            // �ж�orderId�Ϸ���
            if (orderId == null || "".equals(orderId) || orderId.length() < 1) {
                qrcodeImage.setImageResource(R.drawable.invitate_qr_default_logo);
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // ͼ������ת����ʹ���˾���ת��
            BitMatrix bitMatrix = new QRCodeWriter().encode(orderId,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
//            bitMatrix = deleteWhite(bitMatrix);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // �������ﰴ�ն�ά����㷨��������ɶ�ά���ͼƬ��
            // ����forѭ����ͼƬ����ɨ��Ľ��
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // ���ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
//            bitmap = zoomBitmap(bitmap,qrcodeImage.getWidth(),qrcodeImage.getHeight());
            // ��ʾ��һ��ImageView����
            qrcodeImage.setImageBitmap(bitmap);
            qrcodeImage.setClickable(true);
            qrcodeImage.setTag(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height ,matrix ,true);
        return newBmp;
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    /**
     *��ȡͨ�����׶�����
     * @param userId
     * @param amount
     * @param from
     */
    private void requestTLOrderNum(String userId,String amount,String from){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncTLOrder orderTask = new AsyncTLOrder(RechargePosQRCodeActivity.this, userId,
                amount, from, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        tlOrderInfo = baseInfo.getTlOrderInfo();
                        initData(tlOrderInfo);
                        Message msg = handler.obtainMessage(REQUEST_TLORDERSTATUS_WHAT);
                        msg.obj = tlOrderInfo;
                        handler.sendMessageDelayed(msg,10000L);//10���ʼ��ѭ����֧��״̬�ӿ�
                    }else{
                        Util.toastLong(RechargePosQRCodeActivity.this,baseInfo.getMsg());
                    }
                }
            }
        });
        orderTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * ��ȡ����״̬
     * @param userId
     * @param orderId
     */
    private void requestTLOrderStatus(final TLOrderInfo orderInfo, String userId, final String orderId){
        AsyncTLOrderStatus statusTask = new AsyncTLOrderStatus(RechargePosQRCodeActivity.this, userId, orderId,
                new Inter.OnCommonInter() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                //��ֵ�ɹ�
                                RechargeTempInfo info = new RechargeTempInfo();
                                info.setOrder_sn(orderInfo.getOrder());
                                info.setRechargeMoney(orderInfo.getAccount());
                                info.setType("pos");
                                Intent intent = new Intent(RechargePosQRCodeActivity.this,RechargeResultActivity.class);
                                intent.putExtra("RechargeTempInfo",info);
                                startActivity(intent);
                                finish();
                            }else{
                                Message msg = handler.obtainMessage(REQUEST_TLORDERSTATUS_WHAT);
                                msg.obj = orderInfo;
                                handler.sendMessageDelayed(msg,5000L);
                            }
                        }
                    }
                });
        statusTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
