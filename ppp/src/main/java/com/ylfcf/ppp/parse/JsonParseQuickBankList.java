package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BankInfo;
import com.ylfcf.ppp.entity.BankPageInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * ���֧�������б�
 * @author Mr.liu
 *
 */
public class JsonParseQuickBankList {
	private BaseInfo baseInfo;
	private BankPageInfo pageInfo;
	private List<BankInfo> bankList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * �ƹ�������б�
	 * @param data
	 */
	private void parseBankList(String data) {
		bankList = new ArrayList<BankInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				BankInfo info = (BankInfo) MainJson
						.fromJson(BankInfo.class, object);
				bankList.add(info);
			}
			pageInfo.setBankList(bankList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����Msg�ֶ�
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if (object != null) {
			pageInfo = (BankPageInfo) MainJson.fromJson(
					BankPageInfo.class, object);
			parseBankList(pageInfo.getList());
			baseInfo.setBankPageInfo(pageInfo);

		}
	}

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
			parseMsg(baseInfo.getMsg());
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
		JsonParseQuickBankList jsonParse = new JsonParseQuickBankList();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
