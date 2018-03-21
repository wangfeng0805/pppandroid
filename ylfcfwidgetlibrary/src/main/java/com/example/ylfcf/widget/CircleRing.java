package com.example.ylfcf.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Administrator on 2017/8/14.
 */

public class CircleRing {
    int ringX;
    int ringY;
    int ringRadius;
    int ringWidth;
    Paint paint;

    //    public CircleRing(int ringX, int ringY, int ringRadius, int ringWidth, int ringColor, int startAngle) {
    public CircleRing(int ringX, int ringY, int ringRadius, int ringWidth, int ringColor) {
        this.ringX = ringX;
        this.ringY =  ringY;
        this.ringRadius =  ringRadius;
        this.ringWidth=  ringWidth;
        paint = new Paint();
        paint.reset();
        paint.setColor(ringColor);
        paint.setAntiAlias(true); //�������
        paint.setStrokeWidth(ringWidth);
        paint.setStyle(Paint.Style.STROKE);  //���ƿ���Բ�� ���ľ���,ֻ��ʾ��Ե���ߣ�����ʾ�ڲ�
    }

    public void drawCircleRing(Canvas canvas, float startAngle,
                               float angle) {
        RectF rect = new RectF(ringX - ringRadius, ringY - ringRadius, ringX + ringRadius, ringY + ringRadius);
        //false ����Բ��
        paint.setAlpha(255);
        canvas.drawArc(rect, startAngle, angle, false, paint);
    }
}
