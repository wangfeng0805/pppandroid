package com.example.ylfcf.callback;

import com.example.ylfcf.widget.XRefreshView;

/**
 * Created by Administrator on 2017/6/9.
 */

public interface IFooterCallBack {
    /**
     * �����ǵ���ײ��Զ����ظ����ʱ����Ҫ�Լ�д����¼�
     *
     * @param xRefreshView
     */
    void callWhenNotAutoLoadMore(XRefreshView xRefreshView);

    /**
     * ����״̬��������Ҫ���footerview���ܼ��ظ��࣬��Ҫ�ǵ���ײ����Զ����ظ���ʱ�ᱻ����
     */
    void onStateReady();

    /**
     * ����ˢ��
     */
    void onStateRefreshing();

    /**
     * ��footerview������ʱ���ɿ���ָ���ɼ��ظ���
     */
    void onReleaseToLoadMore();

    /**
     * ˢ�½��� �ڴ˷����в�Ҫ����show()����
     *
     * @param hidefooter footerview�Ƿ�����,hideFooter������XRefreshView.stopLoadMore(boolean)����
     */
    void onStateFinish(boolean hidefooter);

    /**
     * ���޸������� �ڴ˷����в�Ҫ����show()����
     */
    void onStateComplete();


    /**
     * ������ʾ��������footerview ��Ҫ��onStateFinish��onStateComplete�е��ô˷���
     *
     * @param show
     */
    void show(boolean show);

    /**
     * footerview�Ƿ���ʾ��
     *
     * @return
     */
    boolean isShowing();

    /**
     * ���footerview�ĸ߶�
     *
     * @return
     */
    int getFooterHeight();
}

