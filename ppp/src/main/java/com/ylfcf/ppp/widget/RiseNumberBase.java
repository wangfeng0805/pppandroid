package com.ylfcf.ppp.widget;
/**
 * �Զ����ӵ����ֽӿ�
 * @author Mr.liu
 *
 */
public interface RiseNumberBase {
	public void start();
    public RiseNumberTextView withNumber(float number);
    public RiseNumberTextView withNumber(int number);
    public RiseNumberTextView setDuration(long duration);
    public void setOnEnd(RiseNumberTextView.EndListener callback);
}
