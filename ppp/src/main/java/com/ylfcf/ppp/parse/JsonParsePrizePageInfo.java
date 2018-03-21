package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.PrizeInfo;
import com.ylfcf.ppp.entity.PrizePageInfo;
import com.ylfcf.ppp.entity.RewardInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * ��Ʒ
 * 
 * @author Administrator
 * 
 */
public class JsonParsePrizePageInfo {
	private BaseInfo baseInfo;
	private PrizePageInfo mPrizePageInfo;
	private List<PrizeInfo> overTimeList;
	private PrizeInfo overTimeInfo;
	private RewardInfo rewardOverTimeInfo;
	private List<PrizeInfo> unUsedList;
	private PrizeInfo unusedInfo;
	private RewardInfo rewardUnusedInfo;
	private List<PrizeInfo> usedList;
	private PrizeInfo usedInfo;
	private RewardInfo rewardUsedInfo;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * ʧЧ�Ľ�Ʒ
	 * @param data
	 */
	private void parseUnusedAwardInfo(String data) {
		JSONObject object = null;
		try {
			object = new JSONObject(data);
			if (object != null) {
				rewardUnusedInfo = (RewardInfo) MainJson.fromJson(
						RewardInfo.class, object);
				unusedInfo.setRewardInfoEntity(rewardUnusedInfo);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * ʧЧ���б�
	 * @param data
	 */
	private void parseUnusedList(String data) {
		unUsedList = new ArrayList<PrizeInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONArray array1 = jsonArray.getJSONArray(i);
				int size1 = array1.length();
				for(int j=0;j<size1;j++){
					JSONObject object = array1.getJSONObject(j);
					unusedInfo = (PrizeInfo) MainJson.fromJson(PrizeInfo.class,
							object);
					parseUnusedAwardInfo(unusedInfo.getRewardInfo());
					unUsedList.add(unusedInfo);
				}
			}
			mPrizePageInfo.setUnUsedList(unUsedList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ڵĽ�Ʒ
	 * @param data
	 */
	private void parseOverTimeAwardInfo(String data) {
		JSONObject object = null;
		try {
			object = new JSONObject(data);
			if (object != null) {
				rewardOverTimeInfo = (RewardInfo) MainJson.fromJson(
						RewardInfo.class, object);
				overTimeInfo.setRewardInfoEntity(rewardOverTimeInfo);
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * ���õĽ�Ʒ
	 * @param data
	 */
	private void parseUsedAwardInfo(String data) {
		JSONObject object = null;
		try {
			object = new JSONObject(data);
			if (object != null) {
				rewardUsedInfo = (RewardInfo) MainJson.fromJson(
						RewardInfo.class, object);
				usedInfo.setRewardInfoEntity(rewardUsedInfo);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * �������ڵ��б�
	 * @param data
	 */
	private void parseOverTimeList(String data) {
		overTimeList = new ArrayList<PrizeInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				overTimeInfo = (PrizeInfo) MainJson.fromJson(PrizeInfo.class,
						object);
				parseOverTimeAwardInfo(overTimeInfo.getRewardInfo());
				overTimeList.add(overTimeInfo);
			}
			mPrizePageInfo.setOverTimeList(overTimeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �����û����б�
	 * @param data
	 */
	private void parseUsedList(String data){
		usedList = new ArrayList<PrizeInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				usedInfo = (PrizeInfo) MainJson.fromJson(PrizeInfo.class,
						object);
				parseUsedAwardInfo(usedInfo.getRewardInfo());
				usedList.add(usedInfo);
			}
			mPrizePageInfo.setUsedList(usedList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����msg�ֶ�
	 * @param result
	 */
	public void parseMsg(String result) {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if (object != null) {
				mPrizePageInfo = (PrizePageInfo) MainJson.fromJson(
						PrizePageInfo.class, object);
				parseOverTimeList(mPrizePageInfo.getOverTime());
				parseUnusedList(mPrizePageInfo.getUnUsed());
				parseUsedList(mPrizePageInfo.getUsed());
				baseInfo.setmPrizePageInfo(mPrizePageInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ����
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if (object != null) {
				baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
				parseMsg(baseInfo.getMsg());
			}
		} catch (Exception e) {
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
		JsonParsePrizePageInfo jsonParse = new JsonParsePrizePageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
