package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParsePopBanner;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * Created by Administrator on 2017/6/26.
 */

public class AsyncPopBanner extends AsyncTaskBase {
    private Context context;

    private Inter.OnCommonInter onCommonInter;
    private BaseInfo baseInfo;

    public AsyncPopBanner(Context context,
                        Inter.OnCommonInter onCommonInter) {
        this.context = context;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getPopBannerURL();
            YLFLogger.d("url:" + url[0] + "\n" + "������" + url[1]);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }
            YLFLogger.d("result:" + result);
            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParsePopBanner.parseData(result);
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
