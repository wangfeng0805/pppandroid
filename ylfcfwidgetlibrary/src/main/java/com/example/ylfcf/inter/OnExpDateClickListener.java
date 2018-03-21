package com.example.ylfcf.inter;

import android.view.View;

import com.example.ylfcf.info.DateData;
import com.example.ylfcf.view.CurrentCalendar;
import com.example.ylfcf.view.DefaultCellView;
import com.example.ylfcf.view.DefaultMarkView;

/**
 * Created by Bigflower on 2015/12/10.
 * <p>
 * �ֱ�Ҫ���ϴεĺ���εĴ���
 * �����պ�������Ҳ������ �����������ж�
 * 1.���ϴεĵ���ж�
 * 2.����εĵ���ж�
 */

public class OnExpDateClickListener extends OnDateClickListener {
    private View lastClickedView;
    private DateData lastClickedDate = CurrentCalendar.getCurrentDateData();

    @Override
    public void onDateClick(View view, DateData date) {

        if(view instanceof DefaultCellView) {

            // �ж��ϴεĵ��
            if (lastClickedView != null) {
                // ��Լ��
                if (lastClickedView == view)
                    return;
                if(lastClickedView instanceof DefaultCellView){
                    if (lastClickedDate.equals(CurrentCalendar.getCurrentDateData())) {
                        ((DefaultCellView) lastClickedView).setDateToday();
                    } else {
                        ((DefaultCellView) lastClickedView).setDateNormal();
                    }
                }else if(lastClickedView instanceof DefaultMarkView){
                    ((DefaultMarkView) lastClickedView).setDateMark();
                }
            }
            // �ж���εĵ��
            ((DefaultCellView) view).setDateChoose();
            lastClickedView = view;
            lastClickedDate = date;
        }else if(view instanceof DefaultMarkView){
            // �ж��ϴεĵ��
            if (lastClickedView != null) {
                // ��Լ��
                if (lastClickedView == view)
                    return;
                if(lastClickedView instanceof DefaultCellView){
                    if (lastClickedDate.equals(CurrentCalendar.getCurrentDateData())) {
                        ((DefaultCellView) lastClickedView).setDateToday();
                    } else {
                        ((DefaultCellView) lastClickedView).setDateNormal();
                    }
                }else if(lastClickedView instanceof DefaultMarkView){
                    ((DefaultMarkView) lastClickedView).setDateMark();
                }
            }
            // �ж���εĵ��
            ((DefaultMarkView) view).setDateChoose();
            lastClickedView = view;
            lastClickedDate = date;
        }


    }
}
