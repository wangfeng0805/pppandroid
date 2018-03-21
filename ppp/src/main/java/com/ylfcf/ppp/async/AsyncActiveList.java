package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseActivePageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * ��б�
 * Created by Administrator on 2017/5/16.
 */

public class AsyncActiveList extends AsyncTaskBase {
    private Context context;

    private String page;
    private String pageSize;
    private String status;
    private String fromWhere;
    private String picShowStatus;

    private Inter.OnCommonInter onCommonInter;
    private BaseInfo baseInfo;

    public AsyncActiveList(Context context, String page,String pageSize,String status,String fromWhere,String picShowStatus,
                                    Inter.OnCommonInter onCommonInter) {
        this.context = context;
        this.page = page;
        this.pageSize = pageSize;
        this.status = status;
        this.fromWhere = fromWhere;
        this.picShowStatus = picShowStatus;
        this.onCommonInter = onCommonInter;//
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getActiveRegionList(page,pageSize,status,fromWhere,picShowStatus);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
                YLFLogger.d("�ר�����ݡ�����������������������������������������������������������������������������������������������������������������������������������"+result);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseActivePageInfo.parseData(result);
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
