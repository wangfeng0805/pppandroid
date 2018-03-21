package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseTransferedCouponPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * �ҵĿ�ת�õļ�Ϣȯ
 * Created by Administrator on 2017/7/7.
 */

public class AsyncTransferedCouponList extends AsyncTaskBase{
    private Context context;

    private String userId;
    private String useStatus;
    private String pageNo;
    private String pageSize;
    private String transfer;

    private Inter.OnCommonInter onCommonInter;
    private BaseInfo baseInfo;

    public AsyncTransferedCouponList(Context context, String userId,String useStatus,
                            String pageNo, String pageSize,String transfer, Inter.OnCommonInter onCommonInter) {
        this.context = context;
        this.userId = userId;
        this.useStatus = useStatus;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.transfer = transfer;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getTransferedCouponListURL(userId,useStatus,pageNo,pageSize,transfer);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }
            YLFLogger.d("��ת�ü�Ϣȯ�б�url:"+url[0]+"\n"+url[1]+"result:"+result);

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseTransferedCouponPageInfo.parseData(result);
            }
            YLFLogger.d("��ת�ü�Ϣȯ�б��������:"+baseInfo.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            result = BackType.ERROR;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (isCancelled()) {
            return;
        }
        if (BackType.ERROR.equals(result)) {
            // ���ʴ���
            onCommonInter.back(null);
        } else if (BackType.FAILE.equals(result)) {
            // ��ȡʧ��
            onCommonInter.back(null);
        } else {
            // ��ȡ�ɹ�
            onCommonInter.back(baseInfo);
        }
    }
}
