package com.sim.test.view.SuspensionBall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * 自定义画板VIew(批注View)
 */
public class AnnotationView extends View {

    private float x;
    private float y;
    private Path path;
    private Paint paint;
    private Random random;

    public AnnotationView(Context context) {
        super(context);
    }

    public AnnotationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        random = new Random();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    private int measureSize(int size) {
        int mode = MeasureSpec.getMode(size);
        int s = MeasureSpec.getSize(size);
        if (mode == MeasureSpec.EXACTLY) {
            return s;
        } else if (mode == MeasureSpec.AT_MOST) {
            return Math.min(s, 200);
        } else {
            return 200;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    public void clear() {
        path.reset();
        invalidate();
    }

    public void save() {
        setDrawingCacheEnabled(false);
        setDrawingCacheEnabled(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap drawingCache = getDrawingCache(true);
                long time = new Date().getTime();
                File file = new File(getContext().getCacheDir() + String.valueOf(time) + ".png");
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file);
                    drawingCache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                            try {
                                MediaStore.Images.Media.insertImage(getContext().getContentResolver(), file.getAbsolutePath(), "sad.png", null);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            // 最后通知图库更新
                            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                            Log.e("测试", "保存成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


}
