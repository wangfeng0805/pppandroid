package com.ylfcf.ppp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * �Զ���scrollview,ʹ���ڲ��Ļ����ؼ����Ի���
 * @author Mr.liu
 *
 */
public class MyScrollView extends ScrollView{
	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
    }
}
