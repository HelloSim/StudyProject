package com.sim.baselibrary.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CameraActivity.class中的工具类
 * @author gexinyu
 */
public class ChooseImageTask {

    public static String mCropPath = Environment.getExternalStorageDirectory().getPath();
    public static String mCropImageName = "crop.jpg";//裁剪缓存路径

    //类型和请求码
    public final static int TYPE_GALLERY = 100;//图集
    public final static int TYPE_ALBUM = 101;
    public final static int TYPE_PHOTO = 102;
    public final static int TYPE_CROP = 103;//裁剪

    @IntDef({TYPE_GALLERY, TYPE_ALBUM, TYPE_PHOTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private ChooseImageTask() {
    }

    private static class LazyHolder {
        private static final ChooseImageTask INSTANCE = new ChooseImageTask();
    }

    public static ChooseImageTask getInstance() {
        return LazyHolder.INSTANCE;
    }


    /**
     * 执行选择图片
     *
     * @param builder
     */
    private void performWithType(Builder builder) {

        Context mContext = builder.mContext;
        OnSelectListener mOnSelectListener = builder.mOnSelectListener;
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            int mType = builder.mType;
            //这里可以加一步检测权限是否申请
            PermissionUtil.checkPermission(activity, mType);

            if (mType == TYPE_GALLERY) {
                takeImageFromGallery(activity, builder);
            } else if (mType == TYPE_ALBUM) {
                takeImageFromAlbum(activity, builder);
            } else if (mType == TYPE_PHOTO) {
                takePhoto(activity, builder);
            }
        } else {
            if (mOnSelectListener != null) {
                mOnSelectListener.onError("performNew--->Context is not Activity");
            }
        }
    }

    /**
     * 拍照
     *
     * @param activity
     */
    private void takePhoto(Activity activity, Builder builder) {
        OnSelectListener mOnSelectListener = builder.mOnSelectListener;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //校验activity是否存在
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            //判断是否自定义路径并且是否合法
            Uri fileUri = UriUtil.getUri(activity, new File(builder.mFilePath, builder.mFileName));
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            activity.startActivityForResult(takePictureIntent, builder.mType);
        } else {
            if (mOnSelectListener != null) {
                mOnSelectListener.onError("takePhoto---> Activity is illegal");
            }
        }
    }

    /**
     * 从系统图库里面选择
     *
     * @param activity
     * @param builder
     */
    private void takeImageFromGallery(Activity activity, Builder builder) {
        OnSelectListener mOnSelectListener = builder.mOnSelectListener;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
        if (componentName != null) {
            activity.startActivityForResult(intent, builder.mType);
        } else {
            if (mOnSelectListener != null) {
                mOnSelectListener.onError("takeImageFromGallery---> Activity is illegal");
            }
        }
    }


    /**
     * 从图片类型文件中选择图片
     *
     * @param activity
     */
    private void takeImageFromAlbum(Activity activity, Builder builder) {
        OnSelectListener mOnSelectListener = builder.mOnSelectListener;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);//api19之后
        //        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//api19之前
        intent.setType("image/*");
        ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
        if (componentName != null) {
            activity.startActivityForResult(intent, builder.mType);
        } else {
            if (mOnSelectListener != null) {
                mOnSelectListener.onError("takeImageFromAlbum---> Activity is illegal");
            }
        }
    }


    /**
     * 代理Activity的返回值过程然后
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleResult(int requestCode, int resultCode, @Nullable Intent data, Builder builder) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case TYPE_PHOTO:// 拍照
                handlePhoto(builder);
                break;

            case TYPE_ALBUM://
                //跳转到裁剪页面
                handleGallery(data, builder);
                break;
            case TYPE_GALLERY:// 图库选择图片
                //跳转到裁剪页面
                handleGallery(data, builder);
                break;

            case TYPE_CROP:
                handleCropResult(builder);

                break;
        }
    }


    /**
     * 处理拍照
     *
     * @param builder
     */
    private void handlePhoto(Builder builder) {
        if (builder != null) {
            Context mContext = builder.mContext;
            OnSelectListener mOnSelectListener = builder.mOnSelectListener;
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                Uri fileUri = UriUtil.getUri(activity, new File(builder.mFilePath, builder.mFileName));
                handleImage(activity, fileUri, builder);
            } else {
                if (mOnSelectListener != null) {
                    mOnSelectListener.onError("handleAlbum--->Context is not Activity");
                }
            }
        }
    }


    /**
     * 处理图库或者文件
     *
     * @param data
     * @param builder
     */
    private void handleGallery(Intent data, Builder builder) {
        if (builder != null) {
            Context mContext = builder.mContext;
            OnSelectListener mOnSelectListener = builder.mOnSelectListener;
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                Uri uri = data.getData();// 获取图片的uri
                handleImage(activity, uri, builder);
            } else {
                if (mOnSelectListener != null) {
                    mOnSelectListener.onError("handleAlbum--->Context is not Activity");
                }
            }
        }
    }

    /**
     * 处理裁剪后的图片
     *
     * @param builder
     */
    private void handleCropResult(Builder builder) {
        if (builder != null) {
            Context mContext = builder.mContext;
            OnSelectListener mOnSelectListener = builder.mOnSelectListener;
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                Uri uri = Uri.fromFile(FileUtil.makeFilePath(builder.mFilePath, builder.mFileName));
                Bitmap bitmapFormUri = BitmapUtil.getBitmapFromUri(activity, uri);
                //是否去压缩
                handleImageCompress(bitmapFormUri, builder);
            } else {
                if (mOnSelectListener != null) {
                    mOnSelectListener.onError("handleCropResult--->Context is not Activity");
                }
            }
        }
    }


    /**
     * 处理图片
     *
     * @param activity
     * @param photoUri
     * @param builder
     */
    private void handleImage(Activity activity, Uri photoUri, Builder builder) {
        boolean mIsCrop = builder.mIsCrop;
        if (mIsCrop) {
            handleImageWithCrop(activity, photoUri, builder);
        } else {
            //不裁剪的话  直接输出图片压缩
            File file = new File(builder.mFilePath, builder.mFileName);
            int bitmapDegree = BitmapUtil.getBitmapDegree(file.getAbsolutePath());
            Bitmap bitmapFormUri = BitmapUtil.getBitmapFromUri(activity, photoUri);
            Bitmap bitmap1 = BitmapUtil.rotateBitmap(bitmapFormUri, bitmapDegree);
            handleImageCompress(bitmap1, builder);
        }
    }

    /**
     * 处理图片 无裁剪情况
     *
     * @param bitmap
     * @param builder
     */
    private void handleImageCompress(Bitmap bitmap, Builder builder) {
        OnSelectListener mOnSelectListener = builder.mOnSelectListener;
        if (mOnSelectListener != null) {
            if (null == bitmap) {
                mOnSelectListener.onError("bitmap is null after crop handle !");
                return;
            }
            boolean isCompress = builder.isCompress;
            if (isCompress) {
                mOnSelectListener.onSuccess(BitmapUtil.compressImage(bitmap));
            } else {
                mOnSelectListener.onSuccess(bitmap);
            }
        }
    }

    /**
     * 处理裁剪图片
     *
     * @param activity
     * @param uri
     */
    private void handleImageWithCrop(Activity activity, Uri uri, Builder builder) {
        OnSelectListener mOnSelectListener = builder.mOnSelectListener;
        if (null != uri) {
            //需要判断是否裁剪
            File saveFile = new File(builder.mFilePath, builder.mFileName);
            Uri outputUri = Uri.fromFile(saveFile);
            //暂时都是裁剪的
            handleCropImage(activity, uri, outputUri);
        } else {
            if (mOnSelectListener != null) {
                mOnSelectListener.onError("uri is null before crop !");
            }
        }
    }

    /**
     * 图片类型的裁剪
     *
     * @param activity
     * @param uri
     * @param outputUri
     */
    public void handleCropImage(Activity activity, Uri uri, Uri outputUri) {
        //打开系统自带的裁剪图片的intent
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);
        // 人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);
        //输出图片到指定位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        activity.startActivityForResult(intent, ChooseImageTask.TYPE_CROP);
    }

    /**
     * 创建构造
     *
     * @param context
     * @return
     */
    public Builder createBuilder(Context context) {
        Builder builder = new Builder(context);
        return builder;
    }

    //*****************************构造builder***********************

    public final class Builder {

        public Context mContext;
        public int mType;
        public boolean mIsCrop;//是否裁剪
        public boolean isCompress;//是否压缩
        public String mFilePath = mCropPath;// 文件夹名称
        public String mFileName = mCropImageName;// 文件名称
        public OnSelectListener mOnSelectListener;//设置监听

        /**
         * builder的构造
         *
         * @param context
         */
        Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置选择图片的方式
         *
         * @param type
         * @return
         */
        public Builder setType(@Type int type) {
            mType = type;
            return this;
        }

        /**
         * 是否裁剪
         *
         * @param crop
         */
        public Builder setIsCrop(boolean crop) {
            mIsCrop = crop;
            return this;
        }

        /**
         * 是否压缩处理
         *
         * @param compress
         */
        public Builder setIsCompress(boolean compress) {
            isCompress = compress;
            return this;
        }


        /**
         * 设置图片路径
         *
         * @param mFilePath
         */
        public Builder setFilePath(String mFilePath) {
            this.mFilePath = mFilePath;
            return this;
        }

        /**
         * 设置图片名称
         *
         * @param mFileName
         */
        public Builder setFileName(String mFileName) {
            this.mFileName = mFileName;
            return this;
        }

        /**
         * 设置监听器
         *
         * @param onSelectListener
         */
        public Builder setOnSelectListener(OnSelectListener onSelectListener) {
            this.mOnSelectListener = onSelectListener;
            return this;
        }


        /**
         * 最后操作
         *
         * @return
         */
        public Builder perform() {
            performWithType(this);
            return this;
        }
    }


    /**
     * @author gexinyu
     * 监听选择图片
     */
    public interface OnSelectListener {

        void onSuccess(Bitmap bitmap);

        void onError(String message);//可以放一些异常和错误

    }
}