package com.sty.ne.irregularview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 自绘控件
 * 根据绘制区域判断点击事件
 * Created by tian on 2019/9/30.
 */

public class IrregularMenuView extends View {
    private int cx; //圆心横坐标
    private int cy;  //圆心纵坐标
    private Paint mPaint;

    private int width = -1;
    private int height = -1;
    int innerCr = 80;  //内圆半径
    int outerCr = 200;  //外圆半径
    int division = 10; //间隔
    private List<Region> regions;

    public IrregularMenuView(Context context) {
        this(context, null);
    }

    public IrregularMenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IrregularMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.BLUE);
        regions = new ArrayList<>();
    }

    // 间隔的内弧和外弧是相同的，从而保证同一间隔的宽度相同；内外弧相同则偏转角不同。
    // 参考：show/analyse3.png
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        cx = width / 2;
        cy = height / 2;

        RectF innerRectF = new RectF(cx - innerCr, cy - innerCr, cx + innerCr, cy + innerCr);
        RectF outerRectF = new RectF(cx - outerCr, cy - outerCr, cx + outerCr, cy + outerCr);
        Path path = new Path();
        //内圆
        path.addCircle(cx, cy, innerCr - 2 * division, Path.Direction.CW);
        Region region = new Region();
        // Path path：用来构造的区域的路径
        // Region clip：与前面的path所构成的路径取交集，并将两交集设置为最终的区域
        // 这里有个问题是要指定另一个区域来取共同的交集，当然如果想显示路径构造的区域，Region clip参数可以传一个比Path范围大的多的区域，取完交集之后，当然是Path参数所对应的区域喽
        region.setPath(path, new Region(cx - innerCr, cy - innerCr, cx + innerCr, cy + innerCr)); //裁剪区域
        regions.add(region);
        //外部按钮
        path.reset();
        float disAngleInner = (float) (division / (2 * Math.PI * innerCr) * 360); //内弧偏移角度
        float disAngleOuter = (float) (division / (2 * Math.PI * outerCr) * 360); //外弧偏移角度
        float sweepAngleInner = 90 - 2 * disAngleInner; //内偏转角
        float sweepAngleOuter = 90 - 2 * disAngleOuter; //外偏转角
        // 内弧
        // Math.sin() Math.cos() 的参数为弧度，所以需要将角度转换成弧度： 1° = Math.PI / 180 弧度
        path.addArc(innerRectF, -135 + disAngleInner, sweepAngleInner);
        path.lineTo((float)(cx + outerCr * Math.sin(sweepAngleOuter / 2 * Math.PI / 180)), //点M横坐标（实际上是点M向左上偏移一定距离的横坐标）
                (float) (cy - outerCr * Math.cos(sweepAngleOuter / 2 * Math.PI / 180)));  //点M纵坐标（实际上是点M向左上偏移一定距离的纵坐标）
        path.addArc(outerRectF, -45 - disAngleOuter, -sweepAngleOuter);
        path.lineTo((float)(cx - innerCr * Math.sin(sweepAngleInner / 2 * Math.PI / 180)),  //点E横坐标（实际上是点E向右上偏移一定距离的横坐标）
                (float) (cy - innerCr * Math.cos(sweepAngleInner / 2 * Math.PI / 180)));  //点E纵坐标（实际上是点E向右上偏移一定距离的纵坐标）

        Region region2 = new Region();
        region2.setPath(path, new Region(cx - outerCr, cy - outerCr, cx + outerCr, cy + outerCr));
        regions.add(region2);
        //继续添加其他区域
        for (int i = 0; i < 3; i++) {
            Matrix matrix = new Matrix();
            matrix.setRotate(90, cx, cy);
            path.transform(matrix);
            Region region3 = new Region();
            region3.setPath(path, new Region(cx - outerCr, cy - outerCr, cx + outerCr, cy + outerCr));
            regions.add(region3);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Region region : regions) {
            canvas.drawPath(region.getBoundaryPath(), mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        for (int i = 0; i < regions.size(); i++) {
            //判断当前是否点击到了我的区域
            if(regions.get(i).contains(x, y)) {
                this.setTag(this.getId(), i);
                return super.onTouchEvent(event);
            }
        }

        return false;
    }
}
