package com.example.ylfcf.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MarkStyleExp {
    public static final int chooseColor = Color.rgb(63, 81, 200);//���ʱ����ɫ
    public static final int lightGrayColor = Color.rgb(245, 245, 245);//�����ǵ���ɫ

    public static Drawable choose = new Drawable() {
        private Paint paint;

        {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(chooseColor);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(canvas.getWidth() / 2,
                    canvas.getHeight() / 2,
                    (int)(canvas.getHeight() / 2.5),
                    paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    };

    public static Drawable today = new Drawable() {
        private Paint paint;

        {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(lightGrayColor);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(canvas.getWidth() / 2,
                    canvas.getHeight() / 2,
                    (int)(canvas.getHeight() / 2.5),
                    paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    };
}
