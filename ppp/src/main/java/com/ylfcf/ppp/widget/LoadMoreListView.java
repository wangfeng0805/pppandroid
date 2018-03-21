package com.ylfcf.ppp.widget;

import com.ylfcf.ppp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * ����ˢ��
 * 
 * @author Administrator
 * 
 */
public class LoadMoreListView extends ListView implements OnScrollListener {
	/**
	 * ������������ʱ����������
	 */

	private int mTouchSlop;
	private OnLoadMoreListener mOnLoadListener;
	/**
	 * ListView�ļ�����footer
	 */
	private View mListViewFooter;
	/**
	 * ����ʱ��y����
	 */
	private int mYDown;
	/**
	 * ̧��ʱ��y����, ��mYDownһ�����ڻ������ײ�ʱ�ж���������������
	 */
	private int mLastY;
	/**
	 * �Ƿ��ڼ����� ( �������ظ��� )
	 */
	private boolean isLoading = false;

	public LoadMoreListView(Context context) {
		super(context);
		mListViewFooter = LayoutInflater.from(context).inflate(
				R.layout.listview_footer, null, false);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mListViewFooter = LayoutInflater.from(context).inflate(
				R.layout.listview_footer, null, false);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// ����ʱ������ײ�Ҳ���Լ��ظ���
		if (canLoad() && visibleItemCount < this.getAdapter().getCount()) {
			loadData();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// ����
			mYDown = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			// �ƶ�
			mLastY = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_UP:
			// ̧��
			if (canLoad()) {
				loadData();
			}
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * �Ƿ���Լ��ظ���, �����ǵ�����ײ�, listview���ڼ�����, ��Ϊ��������.
	 * 
	 * @return
	 */
	private boolean canLoad() {
		return isBottom() && !isLoading && isPullUp();
	}

	/**
	 * �ж��Ƿ�����ײ�
	 */
	private boolean isBottom() {
		if (this.getAdapter() != null) {
			return this.getLastVisiblePosition() == (this.getAdapter()
					.getCount() - 1);
		}
		return false;
	}

	/**
	 * �Ƿ�����������
	 * 
	 * @return
	 */
	private boolean isPullUp() {
		return (mYDown - mLastY) >= mTouchSlop;
	}

	/**
	 * ���������ײ�,��������������.��ôִ��onLoad����
	 */
	private void loadData() {
		if (mOnLoadListener != null) {
			// ����״̬
			setLoading(true);
			//
			mOnLoadListener.onLoadMore();
		}
	}

	/**
	 * @param loading
	 */
	public void setLoading(boolean loading) {
		isLoading = loading;
		if (isLoading) {
			this.addFooterView(mListViewFooter);
		} else {
			this.removeFooterView(mListViewFooter);
			mYDown = 0;
			mLastY = 0;
		}
	}

	/**
	 * @param loadListener
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener loadListener) {
		mOnLoadListener = loadListener;
	}

	public interface OnLoadMoreListener {
		void onLoadMore();
	}
}
