package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBorrowInfoSafe;
import com.ylfcf.ppp.parse.JsonParseLoanRecordList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �̻��װ�ȫ����
 *
 * Created by yjx on 2018/8/6.
 */
public class AsyncBorrowInfoSafe extends AsyncTaskBase {

    private Context  context;
    private BaseInfo baseInfo;
    private String id;
    private OnCommonInter onCommonInter;

    public AsyncBorrowInfoSafe(Context context, String id, OnCommonInter onCommonInter) {
        this.context = context;
        this.id = id;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getBorrowInfoSafe(id);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseBorrowInfoSafe.parseData(result);
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
