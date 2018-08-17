package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseInvestmentDetail;
import com.ylfcf.ppp.parse.JsonParseLoanRecordList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �����¼�б�ҳ��
 *
 * Created by yjx on 2018/8/6.
 */
public class AsyncLoanRecord extends AsyncTaskBase {

    private Context  context;
    private BaseInfo baseInfo;
    private String borrow_id;
    private String page;
    private String pageSize;
    private OnCommonInter onCommonInter;

    public AsyncLoanRecord(Context context, String borrow_id, String page, String pageSize, OnCommonInter onCommonInter) {
        this.context = context;
        this.borrow_id = borrow_id;
        this.page = page;
        this.pageSize = pageSize;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getLoanRecord(borrow_id, page, pageSize);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseLoanRecordList.parseData(result);
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
