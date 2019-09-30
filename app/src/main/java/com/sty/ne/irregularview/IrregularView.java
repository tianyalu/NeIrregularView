package com.sty.ne.irregularview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 组合图片
 * 根据色值判断点击事件
 * Created by tian on 2019/9/30.
 */

public class IrregularView extends View {
    private int width = -1;
    private int height = -1;
    private Bitmap bitmap;

    public IrregularView(Context context) {
        super(context);
    }

    public IrregularView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IrregularView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }

        float x = event.getX();
        float y = event.getY();
        //获取bitmap
        Drawable background = getBackground();
        Bitmap bitmap = ((BitmapDrawable) background).getBitmap();
        //事件位置调整（判断是否超过bitmap范围）
        if(bitmap == null || x < 0 || y < 0 || x > width || y > height) {
            return false;
        }
        //缩放系数: bitmap得到的是图像本身的大小，当图片较小时，ImageView会将background的画布进行缩放，所以要进行缩放处理
        //得到实际bitmap中的像素点
        int pixel = bitmap.getPixel((int)(x * bitmap.getWidth() / width), (int)(y * bitmap.getHeight() / height));
        //判断颜色值
        if(pixel == Color.TRANSPARENT) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}
