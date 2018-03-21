package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.TLOrderInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONObject;

/**
 * TL֧������
 * Created by Administrator on 2018/1/18.
 */

public class JsonParseTLOrderInfo {
    private BaseInfo baseInfo;
    private TLOrderInfo tlOrderInfo;

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
            tlOrderInfo = (TLOrderInfo) MainJson.fromJson(TLOrderInfo.class, object);
            baseInfo.setTlOrderInfo(tlOrderInfo);
        }
    }

    /**
     * ��ʼ����...
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
            baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
            int resultCode = SettingsManager.getResultCode(baseInfo);
            if(resultCode == 0){
                parseMsg(baseInfo.getMsg());
            }
        }
    }

    /**
     * �������ýӿ�
     * @param result
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static BaseInfo parseData(String result) throws Exception {
        JsonParseTLOrderInfo jsonParse = new JsonParseTLOrderInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
