package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YXBCheckRedeemInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * Ԫ�ű���֤ ���
 * 
 * @author Administrator
 * 
 */
public class JsonParseYXBCheckRedeem {
	private BaseInfo baseInfo;
	private YXBCheckRedeemInfo yxbCheckRedeemInfo;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * ����msg�ֶ�
	 * @param msg
	 * @throws Exception
	 */
	private void parseYXBCheckRedeem(String msg) throws Exception {
		JSONObject object = null;
		try {
			object = new JSONObject(msg);
		} catch (Exception e) {
		}

		if (object != null) {
			yxbCheckRedeemInfo = (YXBCheckRedeemInfo) MainJson.fromJson(
					YXBCheckRedeemInfo.class, object);
			baseInfo.setYxbCheckRedeemInfo(yxbCheckRedeemInfo);
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
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if (resultCode == 0) {
				parseYXBCheckRedeem(baseInfo.getMsg());
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
		JsonParseYXBCheckRedeem jsonParse = new JsonParseYXBCheckRedeem();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
