package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ����borrowid��userId��ȡ�������
 * Created by Administrator on 2017/8/3.
 */

public class AsyncYGZXBorrowDetail extends AsyncTaskBase{
    private Context context;

    private String investUserId;
    private String borrowId;
    private OnCommonInter mOnCommonInter;

    private BaseInfo baseInfo;

    public AsyncYGZXBorrowDetail(Context context, String investUserId,
                                String borrowId,OnCommonInter mOnCommonInter) {
        this.context = context;
        this.investUserId = investUserId;
        this.borrowId = borrowId;
        this.mOnCommonInter = mOnCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getYGZXBorrowById(investUserId,borrowId);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseCommon.parseData(result);
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
            mOnCommonInter.back(null);
        } else if (BackType.FAILE.equals(result)) {
            // ��ȡʧ��
            mOnCommonInter.back(null);
        } else {
            // ��ȡ�ɹ�
            mOnCommonInter.back(baseInfo);
        }
    }
}
