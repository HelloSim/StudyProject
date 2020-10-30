package com.sim.test.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;
import com.sim.test.utils.ChooseImageTask;
import com.sim.test.utils.PermissionHelper;

/**
 * 一个图片选择器（打开相机拍照）
 * 待完善
 */
public class CameraActivity extends BaseActivity implements ChooseImageTask.OnSelectListener {

    private ImageView imageView;
    private ChooseImageTask.Builder perform;

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.image);
        PermissionHelper.checkMultiPermission(this, permissions);
    }

    public void getImage(View view) {
        perform = ChooseImageTask.getInstance()
                .createBuilder(this)
                .setIsCrop(false)
                .setIsCompress(true)
                .setOnSelectListener(this)
                .setType(ChooseImageTask.TYPE_GALLERY)
                .perform();
    }

    public void getImage2(View view) {
        perform = ChooseImageTask.getInstance()
                .createBuilder(this)
                .setIsCrop(false)
                .setOnSelectListener(this)
                .setType(ChooseImageTask.TYPE_ALBUM)
                .perform();
    }

    public void getPhoto(View view) {
        perform = ChooseImageTask.getInstance()
                .createBuilder(this)
                .setIsCrop(false)
                .setOnSelectListener(this)
                .setType(ChooseImageTask.TYPE_PHOTO)
                .perform();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ChooseImageTask.getInstance().handleResult(requestCode, resultCode, data, perform);
    }

    @Override
    public void onSuccess(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onError(String message) {
        Log.d("TAG", message);
    }

}
