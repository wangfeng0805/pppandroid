package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SRZXAppointRecordInfo;
import com.ylfcf.ppp.entity.SRZXAppointRecordPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * ˽�������ƷԤԼ
 * @author Mr.liu
 *
 */
public class JsonParseAppointRecord {
	private BaseInfo baseInfo;
//	private SRZXAppointRecordPageInfo pageInfo;
	private List<SRZXAppointRecordInfo> recordInfo;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * �ƹ�������б�
	 * @param data
	 */
	private void parseAppointRecordList(String data) {
		recordInfo = new ArrayList<SRZXAppointRecordInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				SRZXAppointRecordInfo info = (SRZXAppointRecordInfo) MainJson
						.fromJson(SRZXAppointRecordInfo.class, object);
				recordInfo.add(info);
			}
			baseInfo.setSrzxAppointRecordList(recordInfo);
//			pageInfo.setRecordInfo(recordInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����Msg�ֶ�
	 * @param result
	 * @throws Exception
	 */
//	public void parseMsg(String result) throws Exception {
//		JSONObject object = null;
//		object = new JSONObject(result);
//		if (object != null) {
//			pageInfo = (SRZXAppointRecordPageInfo) MainJson.fromJson(
//					SRZXAppointRecordPageInfo.class, object);
//			parseAppointRecordList(pageInfo.getList());
//			baseInfo.setmSRZXAppointRecordPageInfo(pageInfo);
//		}
//	}

	/**
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
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if(resultCode == 0){
				parseAppointRecordList(baseInfo.getMsg());
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
		JsonParseAppointRecord jsonParse = new JsonParseAppointRecord();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
