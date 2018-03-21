package com.example.ylfcf.callback;
/**
 * �ṩ�Զ���headerview�Ľӿ�
 *
 * @author huxq17@163.com
 */
public interface IHeaderCallBack {
    /**
     * ����״̬
     */
    public void onStateNormal();

    /**
     * ׼��ˢ��
     */
    public void onStateReady();

    /**
     * ����ˢ��
     */
    public void onStateRefreshing();

    /**
     * ˢ�½���
     *
     * @param success �Ƿ�ˢ�³ɹ� success������XRefreshView.stopRefresh(boolean)����
     */
    public void onStateFinish(boolean success);

    /**
     * ��ȡheaderview��ʾ�ĸ߶���headerview�߶ȵı���
     *
     * @param headerMovePercent  �ƶ������headerview�߶ȵı���
     * @param offsetY headerview�ƶ��ľ���
     */
    public void onHeaderMove(double headerMovePercent, int offsetY, int deltaY);

    /**
     * ������ʾ��һ��ˢ�µ�ʱ��
     *
     * @param lastRefreshTime ��һ��ˢ�µ�ʱ��
     */
    public void setRefreshTime(long lastRefreshTime);

    /**
     * ����footerview
     */
    public void hide();

    /**
     * ��ʾfooterview
     */
    public void show();

    /**
     * ���headerview�ĸ߶�,�������headerviewȫ�������أ��Ϳ���ֻ����һ���ֵĸ߶�
     *
     * @return
     */
    public int getHeaderHeight();
}
