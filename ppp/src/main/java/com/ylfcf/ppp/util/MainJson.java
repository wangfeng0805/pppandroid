package com.ylfcf.ppp.util;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ͨ��������ƽ���JSON
 * @author Administrator
 *
 */
public class MainJson {
	/**
	 * ��������
	 * @param object
	 * @param c
	 * @param jsonObject
	 * @return
	 */
	private static Object getField(Object object, Class<?> c, JSONObject jsonObject) {
		Field[] fields = c.getDeclaredFields();
		for(int i=0; i<fields.length; i++) {

			Field filed = fields[i];
			filed.setAccessible(true);
			try {

				filed.set(object, jsonObject.getString(fields[i].getName()));

			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		
		return object;
	}
	/**
	 * ���ڵ��õ�ʱ��ǿ��ת���ɴ����ʵ���ࣩ
	 * ��JSONObjectת��ʵ�����
	 * @param c
	 * @param jsonObject
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws JSONException
	 */
	public static Object fromJson(Class<?> c, JSONObject jsonObject) throws InstantiationException, IllegalAccessException  {

		Object object = c.newInstance();
		object = getField(object, c, jsonObject);
		object = getField(object, c.getSuperclass(), jsonObject);

		return object;
	}

	/**
	 * ���ڵ��õ�ʱ��ǿ��ת���ɴ����ʵ���ࣩ
	 * ��Stringת��ʵ�����
	 * @param c
	 * @param jsonStr
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws JSONException
	 */
	public static Object fromJson(Class<?> c, String jsonStr) throws JSONException, InstantiationException, IllegalAccessException {

		JSONObject jsonObject = new JSONObject(jsonStr);
		Object object = fromJson(c, jsonObject);
		return object;
	}
}
