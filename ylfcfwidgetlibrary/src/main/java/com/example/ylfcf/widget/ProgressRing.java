package com.example.ylfcf.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/14.
 */

public class ProgressRing {
    int ringX;
    int ringY;
    int ringRadius;
    int ringWidth;
    int headBallX;
    int headBallY;
    Paint paint;
    Shader sweepGradient;
    //private static final int RED = 230, GREEN = 85, BLUE = 35; //������ɫ�������ǳȺ�ɫ
    private static final int RED = 230, GREEN = 0, BLUE = 0; //������ɫ�������ǳȺ�ɫ
    private static final int MIN_ALPHA = 30; //��С��͸����
    private static final int MAX_ALPHA = 255; //���͸����
    /*
    //Բ����ɫ
    //����˳��ͬ����ʾ�ķ���ͬ
    private static int[] changeColors = new int[]{
            Color.argb(MAX_ALPHA, RED, GREEN, BLUE),
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
//            Color.TRANSPARENT,
    };
    */
    private int[] changeColors = new int[]{
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MAX_ALPHA, RED, GREEN, BLUE),
//            Color.TRANSPARENT,
    };

    public ProgressRing(int ringX, int ringY, int ringRadius, int ringWidth, int ringColor) {
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

    public void drawProgressRing(Canvas canvas, int rotateDegree) {
        paint.reset();
        RectF rect = new RectF(ringX - ringRadius, ringY - ringRadius, ringX + ringRadius, ringY + ringRadius);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(ringWidth);
        paint.setStyle(Paint.Style.STROKE);
        //���ý���
        sweepGradient = new SweepGradient(ringX, ringY, changeColors, null);
        //����Բ����ת
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateDegree, ringX, ringY);
        sweepGradient.setLocalMatrix(matrix);
        paint.setShader(sweepGradient);
        canvas.drawArc(rect, 0, 360, false, paint);
        //���ƽ��Ȼ���ͷ��СԲ��
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(MAX_ALPHA, RED, GREEN, BLUE));
        //ʹ�����Ǻ���ʱ����Ҫ�ѽǶ�תΪ����
        headBallX = ringX + (int)(ringRadius * Math.cos((double)rotateDegree/180 * Math.PI));
        Log.e("degree", "degree: " + rotateDegree + "cos: " + Math.cos((double)rotateDegree/180 * Math.PI));
        headBallY = ringY + (int)(ringRadius * Math.sin((double)rotateDegree/180 * Math.PI));
        canvas.drawCircle(headBallX, headBallY, ringWidth/ 2, paint);
    }
}
