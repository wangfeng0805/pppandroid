package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseTLOrderInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * Created by Administrator on 2018/1/18.
 */

public class AsyncTLOrder extends AsyncTaskBase{
    private Context context;

    private String userId;
    private String amount;
    private String from;

    private Inter.OnCommonInter onCommonInter;
    private BaseInfo baseInfo;

    public AsyncTLOrder(Context context, String userId,String amount,String from,
                               Inter.OnCommonInter onCommonInter) {
        this.context = context;
        this.userId = userId;
        this.amount = amount;
        this.from = from;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getTLOrderNum(userId,amount,from);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }
            YLFLogger.d("ͨ��pos֧��������url:"+url[0]+"\n������"+url[1]+"\nresult:"+result);
            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseTLOrderInfo.parseData(result);
            }
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
