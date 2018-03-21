package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.ActiveInfo;
import com.ylfcf.ppp.entity.ActivePageInfo;
import com.ylfcf.ppp.entity.ActiveStatus;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * ��б�
 * Created by Administrator on 2017/5/16.
 */

public class JsonParseActivePageInfo {
    private BaseInfo baseInfo;
    private ActivePageInfo pageInfo;
    private List<ActiveInfo> activeList;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    private void parseActiveStatus(ActiveInfo activeInfo) throws Exception{
        JSONObject object = null;
        object = new JSONObject(activeInfo.getActive_status());
        ActiveStatus status = null;
        if (object != null) {
            status = (ActiveStatus) MainJson.fromJson(
                    ActiveStatus.class, object);
            activeInfo.setmActiveStatus(status);
            Document doc = Jsoup.parse(activeInfo.getPic());
            Elements ele=doc.getElementsByTag("p");
            for(Element e :ele){
                String imageUrl = e.getElementsByTag("img").attr("src");
                activeInfo.setPic(imageUrl);
            }
            activeList.add(activeInfo);
        }
    }

    /**
     * �����ʽ���ϸ�б�
     * @param data
     */
    private void parseActiveList(String data) {
        activeList = new ArrayList<ActiveInfo>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                ActiveInfo info = (ActiveInfo) MainJson.fromJson(
                        ActiveInfo.class, object);
                parseActiveStatus(info);
            }
            pageInfo.setActiveList(activeList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ����msg�ֶ�
     * @param result
     * @throws Exception
     */
    public void parseMsg(String result) throws Exception {
        JSONObject object = null;
        object = new JSONObject(result);
        if (object != null) {
            pageInfo = (ActivePageInfo) MainJson.fromJson(
                    ActivePageInfo.class, object);
            parseActiveList(pageInfo.getList());
            baseInfo.setmActivePageInfo(pageInfo);
        }
    }

    /**
     * ��ʼ����
     * @param result
     * @throws Exception
     */
    public void parseMain(String result) throws Exception {
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (Exception e) {
        }

        if (object != null) {
            baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
            int resultCode = Integer.parseInt(baseInfo.getError_id());
            if (resultCode == 0) {
                parseMsg(baseInfo.getMsg());
            }
        }
    }

    /**
     * �������ýӿ�
     *
     * @param result
     * @return
     */
    public static BaseInfo parseData(String result) throws Exception {
        JsonParseActivePageInfo jsonParse = new JsonParseActivePageInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
