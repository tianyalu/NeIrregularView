## NeIrregularView 不规则控件的点击事件
### 一、本文介绍了3种方式来绘制不规则控件：
1. IrregularView   组合图片-->根据色值判断点击事件  
2. IrregularDrawView  自绘控件-->根据色值判断点击事件  
3. IrregularMenuView  自绘控件-->根据绘制区域判断点击事件  

#### 1.1 示例如下图所示：  
![image](https://github.com/tianyalu/NeIrregularView/blob/master/show/show.gif)  

### 二、自定义判断点击是否在小球内部，如果在的话小球随手指移动而移动
#### 2.1 思路：  
首先在View中间画出一个小球，手指按下时，根据Area类来判断点击事件是否发生在小球内部；当手指按下在圆内部并且
手指移动时，更新圆心坐标并重新绘制小球；当手指抬起时恢复标志位。
#### 2.1 关键代码：
```android 
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        cx = width / 2;
        cy = height / 2;
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
```

#### 2.2 示例如下图所示：  
![image](https://github.com/tianyalu/NeIrregularView/blob/master/show/touch_to_move.gif)  
