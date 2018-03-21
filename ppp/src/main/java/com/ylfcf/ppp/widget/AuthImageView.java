package com.ylfcf.ppp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ylfcf.ppp.R;

import java.util.Random;
/**
 * ͼƬ��֤��
 * @author Mr.liu
 *
 */
public class AuthImageView extends View{
	private String TAG = "ValidateImageView";
	private Paint paint = new Paint();
	/*
	 * ��֤������
	 */
	private String[] content = null;
	/*
	 * ��֤��ͼƬ
	 */
	private Bitmap bitmap = null;

	public AuthImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AuthImageView(Context context) {
		super(context);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, paint);
		} else {
			paint.setColor(Color.GRAY);
			paint.setTextSize(20);
//			canvas.drawText("�����һ��", 10, 30, paint);
		}
		super.draw(canvas);
	}

	/**
	 * �õ���֤�룻����ͼƬ
	 * @param strContent ��֤����ַ�������
	 * @return
	 */
	public String[] getValidataAndSetImage(String[] strContent) {
		content = strContent;
		//�����������������
		String [] strRes = generageRadom(strContent);
		Log.i(TAG, "generate validate code: " + strRes[0] + strRes[1] + strRes[2] + strRes[3]);
//		String strRes = generageRadomStr(strContent);
		//��������������
		bitmap = generateValidate(content,strRes);
		invalidate();
		
		return strRes;
	}

	private Bitmap generateValidate(String[] strContent,String [] strRes) {
		int width = getResources().getDimensionPixelOffset(R.dimen.common_measure_120dp);
		int	height = getResources().getDimensionPixelOffset(R.dimen.common_measure_60dp);
		
		int isRes = isStrContent(strContent);
		if (isRes == 0) {
			return null;
		}

		Random random = new Random();
		Bitmap sourceBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(sourceBitmap);
		Paint p = new Paint();
		p.setTextSize(height / 2);
		p.setFakeBoldText(true);
		
		p.setColor(getRandColor(200, 230, 170));
		canvas.drawText(strRes[0], 40, height/2+18, p);
		Matrix m1 = new Matrix();
		m1.setRotate(15);
		canvas.setMatrix(m1);

		p.setColor(getRandColor(200, 230, 170));
		canvas.drawText(strRes[1], 40+getResources().getDimensionPixelSize(R.dimen.common_measure_20dp),  height/2, p);
		m1.setRotate(10);
		canvas.setMatrix(m1);

		p.setColor(getRandColor(200, 230, 170));
		canvas.drawText(strRes[2], 40+getResources().getDimensionPixelSize(R.dimen.common_measure_40dp), height/2, p);
		m1.setRotate(20);
		canvas.setMatrix(m1);
		
		p.setColor(getRandColor(200, 230, 170));
		canvas.drawText(strRes[3], 40+getResources().getDimensionPixelSize(R.dimen.common_measure_60dp), height/2-30, p);
		m1.setRotate(5);
		canvas.setMatrix(m1);
		
		//�ϰ�����
//		int startX = 0,startY = 0,stopX = 0,stopY = 0;
//		for (int i = 0; i < 55; i++) {
//			startX = pointRadom(width);
//			startY = pointRadom(height);
//			stopX = pointRadom(15);
//			stopY = pointRadom(15);
//			p.setColor(getRandColor(200, 230, 220));
//			canvas.drawLine(startX, startY - 20, startX + stopX, startY + stopY - 20, p);
//		}
		
		canvas.save();
		return sourceBitmap;
	}
	
	private int isStrContent(String[] strContent) {
		if (strContent == null || strContent.length <= 0) {
			return 0;
		} else {
			return 1;
		}
	}
	
	/**
	 * ��ָ�����������ȡ��4���ַ�(����)
	 * @param strContent
	 * @return
	 */
	private String[] generageRadom(String[] strContent){
		return strContent;
//		String[] str = new String[4];
//		// ������ĸ���
//		int count = strContent.length;
//		// ����4�������
//		Random random = new Random();
//		int randomResFirst = random.nextInt(count);
//		int randomResSecond = random.nextInt(count);
//		int randomResThird = random.nextInt(count);
//		int randomResFourth = random.nextInt(count);
//
//		str[0] = strContent[randomResFirst].toString().trim();
//		str[1] = strContent[randomResSecond].toString().trim();
//		str[2] = strContent[randomResThird].toString().trim();
//		str[3] = strContent[randomResFourth].toString().trim();
//		return str;
	}
	
	/**
	 * ��ָ�����������ȡ��4���ַ�(�ַ���)
	 * @param strContent
	 * @return
	 */
	private String generageRadomStr(String[] strContent){
		StringBuilder str = new StringBuilder();
		// ������ĸ���
		int count = strContent.length;
		// ����4�������
		Random random = new Random();
		int randomResFirst = random.nextInt(count);
		int randomResSecond = random.nextInt(count);
		int randomResThird = random.nextInt(count);
		int randomResFourth = random.nextInt(count);
		
		str.append(strContent[randomResFirst].toString().trim());
		str.append(strContent[randomResSecond].toString().trim());
		str.append(strContent[randomResThird].toString().trim());
		str.append(strContent[randomResFourth].toString().trim());
		return str.toString();
	}
	
	private int pointRadom(int n){
		Random r = new Random();
		return r.nextInt(n);
	}

	/**
	 * ������Χ��������ɫ
	 * 
	 * @param rc
	 *            0-255
	 * @param gc
	 *            0-255
	 * @param bc
	 *            0-255
	 * @return colorValue ��ɫֵ��ʹ��setColor(colorValue)
	 */
	public int getRandColor(int rc, int gc, int bc) {
		Random random = new Random();
		if (rc > 255)
			rc = 255;
		if (gc > 255)
			gc = 255;
		if (bc > 255)
			bc = 255;
		int r = rc + random.nextInt(rc);
		int g = gc + random.nextInt(gc);
		int b = bc + random.nextInt(bc);
		return Color.rgb(r, g, b);
	}

}
