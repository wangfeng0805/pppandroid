package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseProductPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * Ա��ר����Ʒ�б�
 * Created by Administrator on 2017/7/25.
 */

public class AsyncYGZXBorrowList extends AsyncTaskBase {
    private Context context;

    private String pageNo;
    private String pageSize;
    private String borrowStatus;
    private String moneyStatus;
    private boolean isFirst = true;
    private Inter.OnCommonInter mOnCommonInter;

    private BaseInfo baseInfo;

    public AsyncYGZXBorrowList(Context context, String borrowStatus,String moneyStatus,String pageNo,
                                  String pageSize,boolean isFirst,Inter.OnCommonInter mOnCommonInter) {
        this.context = context;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.borrowStatus = borrowStatus;
        this.moneyStatus = moneyStatus;
        this.isFirst = isFirst;
        this.mOnCommonInter = mOnCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getYGZXBorrowListURL(borrowStatus,moneyStatus,pageNo,pageSize);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseProductPageInfo.parseData(result);
                if(isFirst)
                    FileUtil.save(context,
                            FileUtil.YLFCF_YGZX_CACHE, result);// ���ػ���
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
            mOnCommonInter.back(baseInfo);
        } else if (BackType.FAILE.equals(result)) {
            // ��ȡʧ��
            mOnCommonInter.back(baseInfo);
        } else {
            // ��ȡ�ɹ�
            mOnCommonInter.back(baseInfo);
        }
    }
}
