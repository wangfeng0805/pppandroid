package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.PopBannerInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * ��ҳ����
 * Created by Administrator on 2017/6/26.
 */

public class JsonParsePopBanner {
    private BaseInfo baseInfo;
    private PopBannerInfo mPopBannerInfo;
    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /**
     * ����msg�ֶ�
     * @param result
     * @throws Exception
     */
    public void parseMsg(String result) throws Exception{
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (Exception e) {
        }

        if(object != null) {
            mPopBannerInfo = (PopBannerInfo) MainJson.fromJson(PopBannerInfo.class, object);
            Document doc = Jsoup.parse(mPopBannerInfo.getPic());
            Elements ele=doc.getElementsByTag("p");
            if(ele == null || ele.size() <= 0){
                //û��P��ǩ
                String imgURL = doc.getElementsByTag("img").attr("src");
                mPopBannerInfo.setPic(imgURL);
            }else{
                //��P��ǩ
                for(Element e :ele){
                    String imageUrl = e.getElementsByTag("img").attr("src");
                    mPopBannerInfo.setPic(imageUrl);
                }
            }
            baseInfo.setmPopBannerInfo(mPopBannerInfo);
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
            if(resultCode == 0)
            parseMsg(baseInfo.getMsg());
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
        JsonParsePopBanner jsonParse = new JsonParsePopBanner();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
