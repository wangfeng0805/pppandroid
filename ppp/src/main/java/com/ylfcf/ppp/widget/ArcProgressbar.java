package com.ylfcf.ppp.widget;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ���ν�����
 * @author Administrator
 *  progress  �����䷶ΧΪ0 - 260
 */
public class ArcProgressbar extends View{
	private Context context;
	public ArcProgressbar(Context context) {
        super(context);
        this.context = context;
        smallBgColor = context.getResources().getColor(R.color.yxb_arc_ps_bg);
        barColor  = context.getResources().getColor(R.color.yxb_arc_ps_bar);
    }
 
    public ArcProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        smallBgColor = context.getResources().getColor(R.color.yxb_arc_ps_bg);
        barColor  = context.getResources().getColor(R.color.yxb_arc_ps_bar);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
    }
 
    private int arcRadiusTemp = 0;//Բ�İ뾶
    private int cxTemp;//Բ��x����
    private int cyTemp;//Բ��y����
    
    private void init(Canvas canvas) {
    	int[] params = Util.getScreenWidthAndHeight(context);
        // �����εľ�������
    	float left = context.getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
    	float top = context.getResources().getDimensionPixelSize(R.dimen.common_measure_20dp);
    	float right = params[0] - left;
    	float bottom = params[0] - left*2 + top;
    	diameter = (int) (params[0] - left*2);//ֱ�� 
        rectBg = new RectF(left, top, right, bottom);//�����Ǹ�������
 
        // ���㻡�ε�Բ�ĺͰ뾶��
        int arcRadius = (int) (diameter / 2);
        int cx1 = params[0] / 2;
        int cy1 = (int) (arcRadius+top);
        arcRadiusTemp = arcRadius;
        cxTemp = cx1;
        cyTemp = cy1;
        		
        // ProgressBar��β�Ϳ�ʼ��2��Բ��ʵ��ProgressBar��Բ�ǡ�
        mPaintCircleLeft = new Paint();
        mPaintCircleLeft.setAntiAlias(true);
        mPaintCircleLeft.setColor(barColor);
        
        mPaintCircleRight = new Paint();
        mPaintCircleRight.setAntiAlias(true);
        mPaintCircleRight.setColor(smallBgColor);
        
        Paint mPaintCircleCir = new Paint();
        mPaintCircleCir.setAntiAlias(true);
        mPaintCircleCir.setColor(getResources().getColor(R.color.black));
        
        canvas.drawCircle(
                (float) (cx1 + arcRadius * Math.cos(startAngle * 3.1415 / 180)),
                (float) (cy1 + arcRadius * Math.sin(startAngle * 3.1415 / 180)),
                barStrokeWidth / 2 + 1, mPaintCircleLeft);// СԲ
        canvas.drawCircle(
                (float) (cx1 + arcRadius * Math.cos((180 - startAngle) * 3.14 / 180)),
                (float) (cy1 + arcRadius * Math.sin((180 - startAngle) * 3.14 / 180)),
                barStrokeWidth / 2 + 1, mPaintCircleRight);// СԲ
        
        // ���α�����
//        mPaintBg = new Paint();
//        mPaintBg.setAntiAlias(true);
//        mPaintBg.setStyle(Style.STROKE);
//        mPaintBg.setStrokeWidth(bgStrokeWidth);
//        mPaintBg.setColor(bgColor);
//        canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintBg);
 
        // ����С������
        if (showSmallBg) {
            mPaintSmallBg = new Paint();
            mPaintSmallBg.setAntiAlias(true);
            mPaintSmallBg.setStyle(Style.STROKE);
            mPaintSmallBg.setStrokeWidth(barStrokeWidth);
            mPaintSmallBg.setColor(smallBgColor);
            canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintSmallBg);
        }
 
        // ����ProgressBar��
        mPaintBar = new Paint();
        LinearGradient lg=new LinearGradient((float) (cx1 + arcRadius * Math.cos(startAngle * 3.1415 / 180))
        		,(float) (cy1 + arcRadius * Math.sin(startAngle * 3.1415 / 180))
        		,(float) (cx1 + arcRadius * Math.cos((180 - startAngle) * 3.14 / 180))
        		,(float) (cy1 + arcRadius * Math.sin((180 - startAngle) * 3.14 / 180))
        		,getResources().getColor(R.color.yxb_arc_ps_bar),
        		getResources().getColor(R.color.yxb_arc_ps_bg),TileMode.MIRROR);
        mPaintBar.setShader(lg);
        mPaintBar.setAntiAlias(true);
        mPaintBar.setStyle(Style.STROKE);
        mPaintBar.setStrokeWidth(barStrokeWidth);
        mPaintBar.setColor(barColor);
        canvas.drawArc(rectBg, startAngle, progress, false, mPaintBar);
 
        // ��ProgressBar�ƶ���Բ��
        if (showMoveCircle) {
        	//����ɫ��Բ
        	mPaintCircleLeft.setColor(smallBgColor);
        	canvas.drawCircle(
                    (float) (cx1 + arcRadius
                            * Math.cos(angleOfMoveCircle * 3.1415 / 180)),
                    (float) (cy1 + arcRadius
                            * Math.sin(angleOfMoveCircle * 3.1415 / 180)),
                    bgStrokeWidth / 2 + 5, mPaintCircleLeft);// СԲ
        	
        	//����ɫ��Բ
        	mPaintCircleLeft.setColor(barColor);
            canvas.drawCircle(
                    (float) (cx1 + arcRadius
                            * Math.cos(angleOfMoveCircle * 3.1415 / 180)),
                    (float) (cy1 + arcRadius
                            * Math.sin(angleOfMoveCircle * 3.1415 / 180)),
                    bgStrokeWidth / 2, mPaintCircleLeft);// СԲ
        }
        invalidate();
    }
 
    /**
     * 
     * @param progress
     */
    public void addProgress(int _progress) {
    	angleOfMoveCircle = 140;
    	progress = 0;
        progress = +_progress;
        angleOfMoveCircle += _progress;
        System.out.println(progress);
        if (progress > endAngle) {
            progress = 0;
            angleOfMoveCircle = startAngle;
        }
        invalidate();
    }
    
    //��Բ��ĵ���¼����ص�
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent( event);
        }
        int x = (int)event.getX();
        int y = (int)event.getY();
        double distance = Util.getDistance(x, y, cxTemp, cyTemp);
        if(distance > arcRadiusTemp){
        	return false;
        }
        return super.onTouchEvent( event);
    }
 
    /**
     * ���û��α����Ļ��ʿ�ȡ�
     */
    public void setBgStrokeWidth(int bgStrokeWidth) {
        this.bgStrokeWidth = bgStrokeWidth;
    }
 
    /**
     * ���û���ProgressBar�Ļ��ʿ�ȡ�
     */
    public void setBarStrokeWidth(int barStrokeWidth) {
        this.barStrokeWidth = barStrokeWidth;
    }
 
    /**
     * ���û��α�������ɫ��
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
 
    /**
     * ���û���ProgressBar����ɫ��
     */
    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }
 
    /**
     * ���û���С��������ɫ��
     */
    public void setSmallBgColor(int smallBgColor) {
        this.smallBgColor = smallBgColor;
    }
 
    /**
     * ���û��ε�ֱ����
     */
    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }
 
    /**
     * �Ƿ���ʾС������
     */
    public void setShowSmallBg(boolean showSmallBg) {
        this.showSmallBg = showSmallBg;
    }
 
    /**
     * �Ƿ���ʾ�ƶ���СԲ��
     */
    public void setShowMoveCircle(boolean showMoveCircle) {
        this.showMoveCircle = showMoveCircle;
    }
 
    private int bgStrokeWidth = 44;
    private int barStrokeWidth = 15;
    private int bgColor = Color.TRANSPARENT;
    private int barColor = 0;//���ν�������bar����ɫ
    private int smallBgColor = 0;//���α�����ɫ
    private int progress = 0;
    private int angleOfMoveCircle = 140;// �ƶ�С԰����ʼ�Ƕȡ�
    private int startAngle = 140;//��ʼ�Ƕȣ���x��Ϊ��ʼ
    private int endAngle = 260;//�������ʼ���λ�õĽǶ�
    private Paint mPaintBar = null;
    private Paint mPaintSmallBg = null;
    private Paint mPaintBg = null;
    private Paint mPaintCircleLeft = null;
    private Paint mPaintCircleRight = null;
    private RectF rectBg = null;
    /**
     * ֱ����
     */
    private int diameter = 0;
 
    private boolean showSmallBg = true;// �Ƿ���ʾС������
    private boolean showMoveCircle = true;// �Ƿ���ʾ�ƶ���С԰��
 
}
