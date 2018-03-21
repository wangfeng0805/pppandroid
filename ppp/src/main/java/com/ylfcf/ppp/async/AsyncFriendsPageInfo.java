package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseMyFriendsPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �ҵĺ����б�
 * Created by Administrator on 2017/7/5.
 */

public class AsyncFriendsPageInfo extends AsyncTaskBase {
    private Context context;

    private String userId;
    private String pageNo;
    private String pageSize;

    private Inter.OnCommonInter mOnCommonInter;

    private BaseInfo baseInfo;

    public AsyncFriendsPageInfo(Context context, String userId,
                                  String pageNo, String pageSize, Inter.OnCommonInter mOnCommonInter) {
        this.context = context;
        this.userId = userId;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.mOnCommonInter = mOnCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getMyFriendsListURL(userId,pageNo,pageSize);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseMyFriendsPageInfo.parseData(result);
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
