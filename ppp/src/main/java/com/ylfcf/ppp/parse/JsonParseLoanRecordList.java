package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestmentListInfo;
import com.ylfcf.ppp.entity.LoanRecordInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * �̻����б�����
 * @author Administrator
 *
 */
public class JsonParseLoanRecordList {
	private BaseInfo baseInfo;
	private LoanRecordInfo pageInfo;
	private List<LoanRecordInfo.ListBean> mLoanRecordInfo;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	private void parseList(String data){
		mLoanRecordInfo = new ArrayList<LoanRecordInfo.ListBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				LoanRecordInfo.ListBean info = (LoanRecordInfo.ListBean) MainJson.fromJson(LoanRecordInfo.ListBean.class, object);
				mLoanRecordInfo.add(info);
			}
			pageInfo.setLoanRecordList(mLoanRecordInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void parsePageInfo(String data){
		JSONObject object = null;
		try {
			object = new JSONObject(data);
		} catch (Exception e) {
		}
		
		if(object != null) {
			try {
				pageInfo = (LoanRecordInfo)MainJson.fromJson(LoanRecordInfo.class, object);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			parseList(pageInfo.getList());
			baseInfo.setLoanRecordInfo(pageInfo);
		}
	}
	
	/**
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
			if(resultCode == 0){
				parsePageInfo(baseInfo.getMsg());
			}
		}
	}
	
	/**
	 * �������ýӿ�
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseLoanRecordList jsonParse = new JsonParseLoanRecordList();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
