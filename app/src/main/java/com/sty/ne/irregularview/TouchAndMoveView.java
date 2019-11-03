package com.sty.ne.irregularview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class TouchAndMoveView extends View {
    private int cx; //圆心横坐标
    private int cy;  //圆心纵坐标
    private Paint mPaint;
    private Path path;

    private int width = -1;
    private int height = -1;
    int radius = 80;  //圆半径
    private Region region;
    private boolean isDownAndCanMove = false;
    private int mTouchSlop;

    public TouchAndMoveView(Context context) {
        this(context, null);
    }

    public TouchAndMoveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchAndMoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.BLUE);
        region = new Region();
        path = new Path();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        cx = width / 2;
        cy = height / 2;
//        Log.i("sty", "onSizeChanged: width(" + width + ") height(" + height + ")");
        initRegion();
    }

    private void initRegion() {
        path.addCircle(cx, cy, radius, Path.Direction.CW);
        region.setPath(path, new Region(cx - radius, cy - radius, cx + radius,
                cy + radius));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cx, cy, radius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (region.contains(x, y)) {
                    isDownAndCanMove = true;
                    this.setTag(this.getId(), 1);
                }else {
                    this.setTag(this.getId(), -1);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDownAndCanMove) {
                    cx = (int) event.getX();
                    cy = (int) event.getY();
                    initRegion();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDownAndCanMove = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}


