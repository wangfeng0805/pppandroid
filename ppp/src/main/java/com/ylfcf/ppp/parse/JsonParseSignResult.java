package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SignResultInfo;
import com.ylfcf.ppp.util.MainJson;


/**
 * ǩ�����
 * @author Mr.liu
 *
 */
public class JsonParseSignResult {
	private BaseInfo baseInfo;
	private SignResultInfo signResultInfo;
	private List<String> signedDayList;//��ǩ����������
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * �����ʽ���ϸ�б�
	 * @param data
	 * @throws JSONException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void parseSignDate(String data) throws JSONException, InstantiationException, IllegalAccessException {
		JSONObject object = null;
		signedDayList = new ArrayList<String>();
		object = new JSONObject(data);
		JSONArray array = object.getJSONArray("date");
		for(int i=0;i<array.length();i++){
			String str = array.getString(i);
			signedDayList.add(str);
		}
		signResultInfo.setSignDateList(signedDayList);
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
			signResultInfo = (SignResultInfo) MainJson.fromJson(
					SignResultInfo.class, object);
			parseSignDate(signResultInfo.getList());
			baseInfo.setSignResultInfo(signResultInfo);
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
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseSignResult jsonParse = new JsonParseSignResult();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
