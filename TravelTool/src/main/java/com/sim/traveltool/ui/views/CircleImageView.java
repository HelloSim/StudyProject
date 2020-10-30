package com.sim.traveltool.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by Grugsum on 2019/5/1.
 * 圆形ImageView
 */

public class CircleImageView extends AppCompatImageView {

    private int borderColor;
    private int borderWidth;
    private final static int DEFAULT_COLOR = Color.WHITE;
    private final static int DEFAULT_BORDER_WIDTH = 0;

    private Paint mPaint;
    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
//        borderColor = typedArray.getColor(R.styleable.CircleImageView_border_color, DEFAULT_COLOR);
//        borderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
//        borderWidth = dp2px(context,borderWidth);
//        mPaint = new Paint();
//        typedArray.recycle();
    }

    public int dp2px(Context ctx,int dp) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            Bitmap circleBitmap = getCircleBitmap(bitmap);
            Rect srcRect = new Rect(0,0,circleBitmap.getWidth(),circleBitmap.getHeight());
            Rect dstRect = new Rect(0,0,getWidth(),getHeight());
            mPaint.reset();
            canvas.drawBitmap(circleBitmap,srcRect,dstRect,mPaint);

        }else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        mPaint.setAntiAlias(true);
        canvas.drawARGB(0,255,255,255);
        mPaint.setColor(borderColor);
        int radius = bitmap.getWidth() < bitmap.getHeight()? bitmap.getWidth():bitmap.getHeight();
        canvas.drawCircle(radius/2,radius/2,radius/2-borderWidth,mPaint);

        mPaint.setXfermode(new PorterDuffXfermode( PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        canvas.drawBitmap(bitmap,rect,rect,mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawCircle(radius/2,radius/2,radius / 2,mPaint);
        return circleBitmap;
    }


}
