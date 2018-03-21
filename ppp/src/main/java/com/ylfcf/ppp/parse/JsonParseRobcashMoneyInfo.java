package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RobcashMoneyInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/26.
 */

public class JsonParseRobcashMoneyInfo {
    private BaseInfo baseInfo;
    private RobcashMoneyInfo mRobcashMoneyInfo;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /**
     * ����msg�ֶ�
     * @param result
     * @throws Exception
     */
    public void parseMsg(String result) throws Exception {
        JSONObject object = null;
        object = new JSONObject(result);
        if(object != null){
            mRobcashMoneyInfo = (RobcashMoneyInfo) MainJson.fromJson(RobcashMoneyInfo.class, object);
            baseInfo.setmRobcashMoneyInfo(mRobcashMoneyInfo);
        }
    }

    /**
     * ��ʼ����
     * @param result
     * @throws Exception
     */
    public void parseMain(String result) throws Exception{
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (Exception e) {
        }

        if(object != null) {
            baseInfo = (BaseInfo)MainJson.fromJson(BaseInfo.class, object);
            int resultCode = SettingsManager.getResultCode(baseInfo);
            if(resultCode == 0 || resultCode == 504){
                //504�����ֽ� ����Ѿ������ˡ�
                parseMsg(baseInfo.getMsg());
            }
        }
    }

    /**
     * �������ýӿ�
     * @param result
     * @return
     */
    public static BaseInfo parseData(String result) throws Exception {
        JsonParseRobcashMoneyInfo jsonParse = new JsonParseRobcashMoneyInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
