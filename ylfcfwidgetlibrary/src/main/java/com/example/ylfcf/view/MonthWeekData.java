package com.example.ylfcf.view;

import android.graphics.Color;
import android.util.Log;

import com.example.ylfcf.info.DateData;
import com.example.ylfcf.info.DayData;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MonthWeekData {
    private DateData pointDate;
    private Calendar calendar;

    private int realPosition;
    private int weekIndex, preNumber, afterNumber;

    private ArrayList<DayData> monthContent;
    private ArrayList<DayData> weekContent;

    /**
     * ����λ��
     *
     * @param position
     */
    public MonthWeekData(int position) {
        realPosition = position;
        calendar = Calendar.getInstance();
        if (CellConfig.m2wPointDate == null) {
            CellConfig.m2wPointDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        }
        if (CellConfig.w2mPointDate == null) {
            CellConfig.w2mPointDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        }

        if (CellConfig.ifMonth) {
            getPointDate();
            initMonthArray();
        } else {
            initWeekArray();
        }
    }

    private void getPointDate() {
        // �����������Ǹ�point
        calendar.set(CellConfig.w2mPointDate.getYear(), CellConfig.w2mPointDate.getMonth() - 1, 1);
        // ����ܵ���Ի�����ҳ���
        int distance = CellConfig.Week2MonthPos - CellConfig.Month2WeekPos;
        calendar.add(Calendar.DATE, distance * 7);
        // �ж��Ƿ��м�ҳ
        if (realPosition == CellConfig.middlePosition) {
            CellConfig.m2wPointDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            calendar.add(Calendar.MONTH, realPosition - CellConfig.Week2MonthPos);
        }
        calendar.set(Calendar.DATE, 1);
    }

    /**
     * �������ȷ����6�У����Թ���42������
     */
    private void initMonthParams() {
        weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        //�Ҳ� �ܼҵ�11�������⣬������ڲ��ԣ����һ�� TODO
//        if (calendar.get(Calendar.MONTH) == 11)
//            weekIndex--;
        preNumber = weekIndex - 1;
        afterNumber = 42 - calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - preNumber;
//        Log.e("initMonthParams", " weekIndex:" + weekIndex);
//        Log.e("initMonthParams", " preNumber:" + preNumber);
//        Log.e("initMonthParams", " afterNumber:" + afterNumber);
    }

    private void initMonthArray() {
        DayData addDate;
        monthContent = new ArrayList<DayData>();

        initMonthParams();

        // ����ǰ��� �ϸ��µĻ�ɫ������
        calendar.add(Calendar.MONTH, -1);
        int lastMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int preDay = lastMonthDayNumber - preNumber + 1; preDay < lastMonthDayNumber + 1; preDay++) {
            addDate = new DayData(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, preDay));
            addDate.setTextColor(Color.LTGRAY);
            addDate.setEnabled(false);
            monthContent.add(addDate);
        }

        // ���µ� ����
        calendar.add(Calendar.MONTH, 1);
        int thisMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day < thisMonthDayNumber + 1; day++) {
            addDate = new DayData(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day));
            addDate.setTextColor(Color.BLACK);
            addDate.setEnabled(true);
            monthContent.add(addDate);
        }

        // ���µĺ��� �¸��µĻ�ɫ������
        afterNumber = afterNumber + 1;
        calendar.add(Calendar.MONTH, 1);
        for (int afterDay = 1; afterDay < afterNumber; afterDay++) {
            addDate = new DayData(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, afterDay));
            addDate.setTextColor(Color.LTGRAY);
            addDate.setEnabled(false);
            monthContent.add(addDate);
        }
        calendar.add(Calendar.MONTH, -1);
    }

    private void thisMonthArray() {

    }

    private void otherMonthArray() {

    }

    /**
     * Week2MonthPos �� Month2WeekPos �ǹؼ���
     */
    private void initWeekArray() {
        weekContent = new ArrayList<DayData>();

        // �����ǣ�����ϴμ�¼��λ�ã�����ҳ��λ�ƣ��жϸ�ҳ���ê�㣨��ҳ�Ǽ��·ݣ�
        calendar.set(CellConfig.m2wPointDate.getYear(), CellConfig.m2wPointDate.getMonth() - 1, CellConfig.m2wPointDate.getDay());
        if (CellConfig.Week2MonthPos != CellConfig.Month2WeekPos) {
            // �м�ҳ�����������ҳ����
            int distance = CellConfig.Month2WeekPos - CellConfig.Week2MonthPos;
            // ������ǰҳ
            calendar.add(Calendar.MONTH, distance);
        }
        // ����ǽ�����·ݣ���ê�������Ϊ���죻 ������ǽ�����·ݣ���ê�������Ϊ1��
        calendar.set(Calendar.DAY_OF_MONTH, ifThisMonth());
///////////////////////////////////////////////////////////////////////////////////////////
        // �����ǣ���ø�ҳ��ê����ж���ҳ��ʾ�����ݣ��м������ҳ��ʾ��ͬ
        if (realPosition == CellConfig.Month2WeekPos) {
            ;
        } else {
            calendar.add(Calendar.DATE, (realPosition - CellConfig.Month2WeekPos) * 7);
        }

        // ��¼�м�ҳ��pointDate
        if (realPosition == CellConfig.middlePosition) {
            CellConfig.w2mPointDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            Log.v("����Week", " ������ǰҳ��ê�㣺" + CellConfig.w2mPointDate.toString());
        }

        // �������
        DayData addDate;
        weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -weekIndex + 1);
        for (int i = 0; i < 7; i++) {
            addDate = new DayData(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
            weekContent.add(addDate);
            calendar.add(Calendar.DATE, 1);
        }
    }

    private int ifThisMonth() {
        int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
//        Log.e("","================================");
//        Log.e("",calendar.get(Calendar.MONTH)+" "+thisMonth);
//        Log.e("","================================");
        if (calendar.get(Calendar.MONTH) == thisMonth) {
            return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        } else {
            return 1;
        }
    }

    public ArrayList getData() {
        if (CellConfig.ifMonth)
            return monthContent;
        else
            return weekContent;
    }
}
