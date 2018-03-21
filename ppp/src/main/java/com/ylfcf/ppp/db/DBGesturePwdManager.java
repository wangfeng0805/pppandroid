package com.ylfcf.ppp.db;

import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.util.YLFLogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ���ݿ������
 * @author Mr.liu
 *
 */
public class DBGesturePwdManager {
	private DatabaseHelper helper;
	private SQLiteDatabase db;
	
	public static DBGesturePwdManager gesturePwdManager;
	public static DBGesturePwdManager getInstance(Context context){
		if(gesturePwdManager==null){
			gesturePwdManager=new DBGesturePwdManager(context);
		}
		return gesturePwdManager;
	}
	
	public DBGesturePwdManager(Context context) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
	}
	
	/**
	 * ��������������
	 */
	public void addGesturePwd(GesturePwdEntity entity){
		GesturePwdEntity pwdEntity = getGesturePwdEntity(entity.getUserId());
		if(pwdEntity != null){
			updateGestureEntity(entity);
		}else{
			ContentValues cv = new ContentValues();  
	        cv.put("userId" , entity.getUserId());  
	        cv.put("phone" , entity.getPhone()); 
	        cv.put("status" , entity.getStatus());  
	        cv.put("pwd" , entity.getPwd());
	        db.insert("gesturepwd", null, cv);  
		}
		
//		synchronized (this) {
//			db.beginTransaction();//��ʼ����
//			GesturePwdEntity pwdEntity = getGesturePwdEntity(entity.getUserId());
//			if(pwdEntity != null){
//				updateGestureEntity(entity);
//			}else{
//				try {
//					db.execSQL("INSERT INTO gesturepwd VALUES(NULL,?, ?, ?, ?)", new Object[] {
//							entity.getUserId(), entity.getPhone(),entity.getStatus(),entity.getPwd()});
//					db.setTransactionSuccessful();//��������ɹ����
//				} catch (Exception e) {
//				} finally {
//					db.endTransaction();//��������
//				}
//			}
//		}
	}
	
	/**
	 * �����û�idɾ����������
	 */
	public void deleteGesturePwd(String userId){
		synchronized (this) {
			if(db!=null){
				db.beginTransaction();
				try {
					db.execSQL("DELETE FROM gesturepwd WHERE userId = ?", new Object[] {userId});
					db.setTransactionSuccessful();
				} catch (Exception e) {
				} finally {
					db.endTransaction();
				}
			}
		}
	}
	
	/**
	 * ����userId�������ݿ�
	 * @param gesturePwdEntity
	 */
	public void updateGestureEntity(GesturePwdEntity gesturePwdEntity) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("status", gesturePwdEntity.getStatus());
			cv.put("pwd", gesturePwdEntity.getPwd());
			db.update("gesturepwd", cv, "userId = ?", new String[]{gesturePwdEntity.getUserId()});
		} catch (Exception e) {
		}
	}

	/**
	 * ����userId��ȡ�����������
	 * @param userId
	 * @return
	 */
	public GesturePwdEntity getGesturePwdEntity(String userId){
		Cursor c = db.rawQuery("SELECT * FROM gesturepwd WHERE userId = ?", new String[]{userId});  
		GesturePwdEntity gestureEntity = null;
		while (c.moveToNext()) {
			gestureEntity = new GesturePwdEntity();
			gestureEntity.setUserId(c.getString(c.getColumnIndex("userId")));
			gestureEntity.setPhone(c.getString(c.getColumnIndex("phone")));
			gestureEntity.setStatus(c.getString(c.getColumnIndex("status")));
			gestureEntity.setPwd(c.getString(c.getColumnIndex("pwd")));
			break;
		}
		c.close();
		return gestureEntity;
	}
	
	public void closeDb(){
		helper.close();
		db.close();
		gesturePwdManager = null;
	}
}
