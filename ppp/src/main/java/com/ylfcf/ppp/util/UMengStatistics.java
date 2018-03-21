package com.ylfcf.ppp.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * ����
 * 
 * @author Administrator
 * 
 */
public class UMengStatistics {
	/**
	 * ���˸���Ӧ��
	 * 
	 * @param context
	 */
	// public static void updateApp(final Context context) {
	// UmengUpdateAgent.update(context);
	// UmengUpdateAgent.setUpdateAutoPopup(false);
	// UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
	// @Override
	// public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo)
	// {
	// if (updateStatus == 0) {
	// UmengUpdateAgent.showUpdateDialog(context, updateInfo);
	// }
	// }
	// });
	// }

	/**
	 * �ڵ�������APIͳ��ҳ��֮ǰ����Ҫ�ھ����ڳ�����ڴ��� ����
	 * MobclickAgent.openActivityDurationTrack(false) ��ֹĬ�ϵ�ҳ��ͳ�� ��ʽ������
	 * onResume(Context) �� onPause(Context)
	 * �������������Զ�ͳ��ҳ�档Ӧ�ó���ҳ���ʵ�ֿ����ǻ���ActivityҲ������Fragment�� �Ƽ�����Ӧ�� onResume ��
	 * onPause �����е���
	 */
	public static void statisticsInit() {
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * ͳ���Զ����¼�
	 * 
	 * @param context
	 * @param eventId
	 * @param label
	 */
	public static void statisticsCustomEvent(Context context, String eventId,
			String label) {
		MobclickAgent.onEvent(context, eventId, label);
	}

	/**
	 * ͳ��ʱ��
	 * 
	 * @param context
	 */
	public static void statisticsResume(Context context) {
		MobclickAgent.onResume(context);
	}

	/**
	 * ͳ��ʱ��
	 * 
	 * @param context
	 */
	public static void statisticsPause(Context context) {
		MobclickAgent.onPause(context);
	}

	/**
	 * ͳ��ҳ�濪ʼ
	 * 
	 */
	public static void statisticsOnPageStart(String mClassName) {
		// String contextStr = getRunningActivityName(context);
		MobclickAgent.onPageStart(mClassName);
	}

	/**
	 * ͳ��ҳ�����
	 * 
	 */
	public static void statisticsOnPageEnd(String mClassName) {
		// String contextStr = getRunningActivityName(context);
		MobclickAgent.onPageEnd(mClassName);
	}

	/**
	 * ����Context��ȡ����
	 * 
	 * @param context
	 * @return
	 */
	private static String getRunningActivityName(Context context) {
		// String contextString = context.toString();
		// return contextString.substring(contextString.lastIndexOf(".") + 1,
		// contextString.indexOf("@"));
		return context.getClass().getSimpleName();
	}

	public static void reportError(Context context, String error) {
		MobclickAgent.reportError(context, error);
	}

	// ��
	public static void reportError(Context context, Throwable e) {
		MobclickAgent.reportError(context, e);
	}

	/**
	 * ��Ӧ���ں�̨���г���30�루Ĭ�ϣ��ٻص�ǰ�ˣ�������Ϊ������������session(����)��
	 * �����û��ص�home��������������򣬾���һ��ʱ����ٷ���֮ǰ��Ӧ�á�
	 * �˷���������������������λΪ���룩
	 * @param interval
	 */
	public static void setSessionContinueMillis(long interval){
		MobclickAgent.setSessionContinueMillis(interval);
	}

	/**
	 * ��������ߵ���Process.kill����System.exit֮��ķ���ɱ�����̣�
	 * ������ڴ�֮ǰ���ñ���������ͳ������
	 * @param context
	 */
	public static void onKillProcess(Context context){
		MobclickAgent.onKillProcess(context);
	}
}
