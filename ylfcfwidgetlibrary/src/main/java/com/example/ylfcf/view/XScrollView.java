package com.example.ylfcf.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.example.ylfcf.widget.XRefreshView;

/**
 * Created by Administrator on 2017/6/9.
 */

public class XScrollView extends ScrollView{
    private OnScrollListener onScrollListener, mScrollListener;
    // �Ƿ��ڴ���״̬
    private boolean inTouch = false;
    // �ϴλ��������λ��
    private int lastT = 0;
    private XRefreshView mParent;
    private int mTouchSlop;
    private float lastY;

    public XScrollView(Context context) {
        super(context, null);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener == null) {
            return;
        }
        if (inTouch) {
            if (t != oldt) {
                // ����ָ����������λ���й���
                onScrollListener.onScrollStateChanged(this, OnScrollListener.SCROLL_STATE_TOUCH_SCROLL, isBottom());
                if (mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(this, OnScrollListener.SCROLL_STATE_TOUCH_SCROLL, isBottom());
                }
            }
        } else {
            if (t != oldt) {
                // û����ָ����������λ���й������Ϳ��Լ򵥵���Ϊ����fling
                onScrollListener.onScrollStateChanged(this, OnScrollListener.SCROLL_STATE_FLING, isBottom());
                if (mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(this, OnScrollListener.SCROLL_STATE_FLING, isBottom());
                }
                // ��ס�ϴλ��������λ��
                lastT = t;
                removeCallbacks(mRunnable);
                postDelayed(mRunnable, 20);
            }
        }
        onScrollListener.onScroll(l, t, oldl, oldt);
        if (mScrollListener != null) {
            mScrollListener.onScroll(l, t, oldl, oldt);
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (lastT == getScrollY() && !inTouch) {
                // ����ϴε�λ�ú͵�ǰ��λ����ͬ������Ϊ���ڿ���״̬
                onScrollListener.onScrollStateChanged(XScrollView.this, OnScrollListener.SCROLL_STATE_IDLE, isBottom());
                if (mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(XScrollView.this, OnScrollListener.SCROLL_STATE_IDLE, isBottom());
                }
            }
        }
    };

    private boolean isBottom() {
        return getScrollY() + getHeight() >= computeVerticalScrollRange();
    }

    protected void setOnScrollListener(XRefreshView parent, OnScrollListener scrollListener) {
        mParent = parent;
        this.onScrollListener = scrollListener;
        mParent.addTouchLifeCycle(new XRefreshView.TouchLifeCycle() {
            @Override
            public void onTouch(MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastY = event.getRawY();
                    case MotionEvent.ACTION_MOVE:
                        inTouch = true;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        inTouch = false;
                        lastT = getScrollY();
                        float curY = event.getRawY();
                        if (lastY - curY >= mTouchSlop) {
                            removeCallbacks(mRunnable);
                            postDelayed(mRunnable, 20);
                        }
                        break;
                }
            }
        });
    }

    /**
     * ����XScrollView�Ĺ�������
     *
     * @param scrollListener
     */
    public void setOnScrollListener(OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    /**
     * ���������¼�
     */
    public interface OnScrollListener {
        /**
         * The view is not scrolling. Note navigating the list using the
         * trackball counts as being in the idle state since these transitions
         * are not animated.
         */
        int SCROLL_STATE_IDLE = 0;

        /**
         * The user is scrolling using touch, and their finger is still on the
         * screen
         */
        int SCROLL_STATE_TOUCH_SCROLL = 1;

        /**
         * The user had previously been scrolling using touch and had performed
         * a fling. The animation is now coasting to a stop
         */
        int SCROLL_STATE_FLING = 2;

        /**
         * ����״̬�ص�
         *
         * @param view         ��ǰ��scrollView
         * @param scrollState  ��ǰ��״̬
         * @param arriveBottom �Ƿ񵽴�ײ�
         */
        void onScrollStateChanged(ScrollView view, int scrollState, boolean arriveBottom);

        /**
         * ����λ�ûص�
         *
         * @param l
         * @param t
         * @param oldl
         * @param oldt
         */
        void onScroll(int l, int t, int oldl, int oldt);
    }
}
