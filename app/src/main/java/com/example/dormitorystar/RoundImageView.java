package com.example.dormitorystar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

//用户个人界面的头像设置
public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    private final RectF rectF=new RectF();
    private float radius=90;
    private final Paint maskPaint=new Paint();
    private final Paint zonePaint=new Paint();
//  zone矩形
//  mask圆圈


    private void initView(){
//        抗锯齿
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        对位图进行滤波
        maskPaint.setFilterBitmap(true);
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        zonePaint.setFilterBitmap(true);
        float density=getResources().getDisplayMetrics().density;
        radius=radius*density;
    }

    public void setRadius(float radius){
        this.radius=radius;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w=getWidth();
        int h=getHeight();
        rectF.set(0,0,w,h);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.saveLayer(rectF,zonePaint,Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(rectF,radius,radius,zonePaint);
        canvas.saveLayer(rectF, maskPaint, Canvas.ALL_SAVE_FLAG);
//  这个saveLayer我没懂，啥意思啊
        canvas.restore();

    }

    public RoundImageView(Context context) {
        super(context);
        initView();
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }









}
