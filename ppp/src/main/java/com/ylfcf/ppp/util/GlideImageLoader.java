package com.ylfcf.ppp.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ylfcf.ppp.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/8/30
 */

public class GlideImageLoader extends ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //���巽�������Լ�ȥѡ�񣬴η�����Ϊ�˼���banner����������������������Խ����Ȩ�޿��Ÿ�ʹ����ȥѡ��
        if(imageView != null){
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        Glide.with(context.getApplicationContext())
                .load(path)
                .placeholder(R.drawable.icon_empty)//���صȴ�������
                .error(R.drawable.icon_error)//����ʧ��
                .into(imageView);
    }
}
