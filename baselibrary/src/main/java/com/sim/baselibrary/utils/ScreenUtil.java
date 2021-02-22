package com.sim.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Times --- 截屏工具类
 */
public class ScreenUtil {

    /**
     * adb命令截图（需要root权限）
     * 优点：代码简单，直接获取到图片
     * 缺点：需要系统签名
     */
    public static void screenCap() {
        String cmd[] = new String[]{"su", "-c", "screencap -p sdcard/WhiteBoard/0.png"};
        procCmd(cmd);
    }

    public static int procCmd(String cmd[]) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            try {
                proc.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    /**
     * 使用反射调用截屏
     * 优点：不用弹框授权，不用系统签名
     * 缺点：使用反射机制，如果系统API或者方法发生更改，导致无法调用
     */
    private Bitmap screenShotByReflect() {
        Bitmap mScreenBitmap;
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        float[] dims = {mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels};
        try {
            Class<?> demo = Class.forName("android.view.SurfaceControl");
            Method method = demo.getDeclaredMethod("screenshot", int.class, int.class);
            mScreenBitmap = (Bitmap) method.invoke(null, (int) dims[0], (int) dims[1]);
            return mScreenBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 截取activity
     * 优点：此种方式比较简单只需传入当前要截取屏幕的Activity对象即可，不需要添加任何权限，后续可将截图的bitmap保存到本地即可
     * 缺点：无法截取WebView页面，截屏后是白屏
     *
     * @param activity
     * @return
     */
    public static Bitmap capture(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    /**
     * 使用MediaProjectionManager
     * Android5.0之后，开放截取屏幕的API，
     * 也就是利用MediaProjectionManager创建VirtualDisplay，传入与ImageReader关联的Surface，这样就可以从ImageReader中获取到Image，
     * 然后把Image的像素数组拷贝到Bitmap，如果要保存为图片，就用得到的Bitmap压缩为JPEG格式的图片。
     * 优点：不用系统签名，不依赖系统底层API
     * 缺点：弹出确认框，需要用户授权录屏
     */
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void startScreenShot() {
//        int width;
//        int height;
//        int dpi;
//        MediaProjectionManager mediaProjectionManager;
//
//        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        if (windowManager != null) {
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//            width = displayMetrics.widthPixels;
//            height = displayMetrics.heightPixels;
//            dpi = displayMetrics.densityDpi;
//        }
//        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
//        if (mediaProjectionManager != null) {
//            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 123);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void getBitmap(MediaProjection mediaProjection) {
//        ImageReader imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 3);
//        mediaProjection.createVirtualDisplay("screen_shot", width, height, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null);
//        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
//            @Override
//            public void onImageAvailable(ImageReader reader) {
//                Image image = reader.acquireNextImage();
//                int width = image.getWidth();
//                int height = image.getHeight();
//                final Image.Plane[] planes = image.getPlanes();
//                final ByteBuffer buffer = planes[0].getBuffer();
//                int pixelStride = planes[0].getPixelStride();
//                int rowStride = planes[0].getRowStride();
//                int rowPadding = rowStride - pixelStride * width;
//                Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
//                bitmap.copyPixelsFromBuffer(buffer);
//                String filePath = Environment.getExternalStorageDirectory().getPath() + "/hello.jpg";
//                BitmapFileUtils.saveBitmap2file(bitmap, filePath);
//                image.close();
//            }
//        }, null);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
//        if (mediaProjection != null) {
//            getBitmap(mediaProjection);
//        }
//    }
    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point out = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(out);
        } else {
            int width = display.getWidth();
            int height = display.getHeight();
            out.set(width, height);
        }
        return out;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        int statusBarHeight = sbar;
        return statusBarHeight;
    }

    public static void hideInput(View v) {
        InputMethodManager inputManager = (InputMethodManager) v
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //调用系统输入法
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void showInput(View v) {
        InputMethodManager inputManager = (InputMethodManager) v
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //调用系统输入法
        inputManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public static void toggleInput(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 让popupwindow以外区域阴影显示
     *
     * @param popupWindow
     */
    public static void popOutShadow(PopupWindow popupWindow, final Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.7f;//设置阴影透明度
        activity.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }

}
