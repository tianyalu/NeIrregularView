package com.sty.ne.irregularview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自绘控件
 * 根据色值判断点击事件
 * Created by tian on 2019/9/30.
 */

public class IrregularDrawView extends View {
    private Paint paint;
    private int[] colors = {0xFFD21D22, 0xFFFBD109, 0xFF4BB748, 0xFF2F7ABB};
    private int width = -1;
    private int height = -1;
    private Bitmap bitmap;

    private int cx;
    private int cy;
    private Canvas canvasTemp;

    public IrregularDrawView(Context context) {
        this(context, null);
    }

    public IrregularDrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IrregularDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
    }

    // 参考show/analyse.png
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        cx = width / 2;
        cy = height / 2;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 创建新的画布来解决图片的问题（点击事件）
        canvasTemp = new Canvas(bitmap);
    }

    // 参考show/analyse2.png
    @Override
    protected void onDraw(Canvas canvas) {
        int outerCr = 100;  //外圆半径
        int innerCr = 50;  //内圆半径

        RectF innerRectF = new RectF(cx - innerCr, cy - innerCr, cx + innerCr, cy + innerCr);
        RectF outerRectF = new RectF(cx - outerCr, cy - outerCr, cx + outerCr, cy + outerCr);
        Path path = new Path();
        //获取红色区域
        path.addArc(innerRectF, 150, 120); //弧EB
        // BH长度 = AH * sin60° = outerCr * sin60°
        path.lineTo((float) (cx + outerCr * Math.sqrt(3) / 2), cy - innerCr); //线段BH
        path.addArc(outerRectF, -30, -120); //弧HD
        // EA水平投影长度 = EA * sin60° = innerCr * sin60°
        path.lineTo((float) (cx - innerCr * Math.sqrt(3) / 2), cy + innerCr / 2); //线段DE
        paint.setColor(colors[0]);

        canvasTemp.drawPath(path, paint);

        //顺时针旋转红色区域120°
        Matrix matrix = new Matrix();
        matrix.setRotate(120, cx, cy);
        path.transform(matrix);

        //绘制黄色区域
        paint.setColor(colors[1]);
        canvasTemp.drawPath(path, paint);
//        canvas.drawBitmap(bitmap, 0, 0, paint);  //放在这里也可以生效

        //绘制绿色区域
        path.transform(matrix);
        paint.setColor(colors[2]);
        canvasTemp.drawPath(path, paint);
        //绘制蓝色区域
        paint.setColor(Color.WHITE);
        canvasTemp.drawCircle(cx, cy, innerCr, paint);
        paint.setColor(colors[3]);
        canvasTemp.drawCircle(cx, cy, innerCr - 5, paint);

        canvas.drawBitmap(bitmap, 0, 0, paint);

        super.onDraw(canvas);
    }

    // 处理事件传递
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();

        int pixel = bitmap.getPixel((int)x, (int)y);
        // 判断颜色值
        if(pixel == Color.TRANSPARENT) {
            return false;
        }else {
            this.setTag(this.getId(), 3);
            for(int i = 0; i < colors.length; i++) {
                if(colors[i] == pixel) {
                    this.setTag(this.getId(), i);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
