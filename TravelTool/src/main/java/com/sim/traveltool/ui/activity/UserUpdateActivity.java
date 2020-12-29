package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.bean.EventMessage;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.baselibrary.constant.Constant;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.internet.APIFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import rx.Subscriber;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 显示用户信息的页面
 */
public class UserUpdateActivity extends BaseActivity {

    ImageView back;
    LinearLayout parent;

    RelativeLayout rlUserImage;
    RelativeLayout rlUserNikeName;
    RelativeLayout rlUserAutograph;
    Button btnSignOut;

    ImageView ivUserImage;
    TextView tvUserName;
    TextView tvUserNikeName;
    TextView tvUserAutograph;

    private UserInfo userInfo;//修改前
    private UserInfo newUserInfo;//修改后
    private String userPassword;//密码

    //更新用户名弹窗
    private PopupWindow updateNikeNamePopupWindow;//弹窗
    private View updateNikeNameLayout;//布局
    private EditText et_nike_name;
    private Button btn_nike_name_cancel;
    private Button btn_nike_name_confirm;

    //更新签名弹窗
    private PopupWindow updateAutographNamePopupWindow;//弹窗
    private View updateAutographLayout;//布局
    private EditText et_autograph;
    private Button btn_autograph_cancel;
    private Button btn_autograph_confirm;

    private static final int REQUEST_CODE_GALLERY = 0x10;// 图库选取图片标识请求码
    private static final int CROP_PHOTO = 0x12;// 裁剪图片标识请求码

    private File imageFile = null;// 声明File对象
    private Uri imageUri = null;// 裁剪后的图片uri

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_update;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        parent = findViewById(R.id.parent);
        rlUserImage = findViewById(R.id.rl_user_image);
        rlUserNikeName = findViewById(R.id.rl_user_nikeName);
        rlUserAutograph = findViewById(R.id.rl_user_autograph);
        btnSignOut = findViewById(R.id.btn_sign_out);
        ivUserImage = findViewById(R.id.iv_user_image);
        tvUserName = findViewById(R.id.user_name);
        tvUserNikeName = findViewById(R.id.user_nikeName);
        tvUserAutograph = findViewById(R.id.user_autograph);
        setViewClick(back, rlUserImage, rlUserNikeName, rlUserAutograph, btnSignOut);
    }

    @Override
    protected void initView() {
        if (userInfo != null) {
            if (userInfo.getResult().getHeaderImg() != null)
                Glide.with(this).load(userInfo.getResult().getHeaderImg()).into(ivUserImage);
            if (userInfo.getResult().getName() != null)
                tvUserName.setText(userInfo.getResult().getName());
            if (userInfo.getResult().getNikeName() != null)
                tvUserNikeName.setText(userInfo.getResult().getNikeName());
            if (userInfo.getResult().getAutograph() != null)
                tvUserAutograph.setText(userInfo.getResult().getAutograph());
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        updateNikeNameLayout = inflater.inflate(R.layout.view_popup_update_nike_name, null);
        updateAutographLayout = inflater.inflate(R.layout.view_popup_update_autograph, null);

        updateNikeNamePopupWindow = showPopupWindow(updateNikeNameLayout, 300, 180);
        updateAutographNamePopupWindow = showPopupWindow(updateAutographLayout, 300, 180);

        et_nike_name = updateNikeNameLayout.findViewById(R.id.et_nike_name);
        btn_nike_name_cancel = updateNikeNameLayout.findViewById(R.id.btn_nike_name_cancel);
        btn_nike_name_confirm = updateNikeNameLayout.findViewById(R.id.btn_nike_name_confirm);
        et_autograph = updateAutographLayout.findViewById(R.id.et_autograph);
        btn_autograph_cancel = updateAutographLayout.findViewById(R.id.btn_autograph_cancel);
        btn_autograph_confirm = updateAutographLayout.findViewById(R.id.btn_autograph_confirm);

        if (userInfo.getResult().getNikeName() != null)
            et_nike_name.setText(userInfo.getResult().getNikeName());
        if (userInfo.getResult().getAutograph() != null)
            et_autograph.setText(userInfo.getResult().getAutograph());

        setViewClick(btn_nike_name_cancel, btn_nike_name_confirm, btn_autograph_cancel, btn_autograph_confirm);
    }

    @Override
    protected void initData() {
        userPassword = String.valueOf(SPUtil.get(this, AppHelper.userSpName, AppHelper.userSpPasswordKey, ""));
        userInfo = new Gson().fromJson((String) SPUtil.get(this, AppHelper.userSpName, AppHelper.userSpUserInfoKey, ""), UserInfo.class);
    }

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else if (view == rlUserImage) {
            gallery();
        } else if (view == rlUserNikeName) {
            updateNikeNamePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else if (view == rlUserAutograph) {
            updateAutographNamePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else if (view == btnSignOut) {
            showDialog("退出登录", "是否确认退出？", "确认", "取消",
                    new DialogInterface() {
                        @Override
                        public void sureOnClick() {
                            SPUtil.put(UserUpdateActivity.this, AppHelper.userSpName, AppHelper.userSpStateKey, false);
                            SPUtil.remove(UserUpdateActivity.this, AppHelper.userSpName, AppHelper.userSpUserInfoKey);
                            SPUtil.remove(UserUpdateActivity.this, AppHelper.userSpName, AppHelper.userSpPasswordKey);
                            EventBus.getDefault().post(new EventMessage(AppHelper.USER_noLogIn));
                            finish();
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
        } else if (view == btn_nike_name_cancel) {
            updateNikeNamePopupWindow.dismiss();
        } else if (view == btn_nike_name_confirm) {
            if (userInfo != null) {
                userInfo.getResult().setNikeName(et_nike_name.getText().toString());
                updateUserInfo();
            }
        } else if (view == btn_autograph_cancel) {
            updateAutographNamePopupWindow.dismiss();
        } else if (view == btn_autograph_confirm) {
            if (userInfo != null) {
                userInfo.getResult().setAutograph(et_autograph.getText().toString());
                updateUserInfo();
            }
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 更新用户信息的网络请求
     */
    private void updateUserInfo() {
        APIFactory.getInstance().updateUserInfo(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                if (userInfo.getCode() == 200) {
                    updateNikeNamePopupWindow.dismiss();
                    updateAutographNamePopupWindow.dismiss();
                    SPUtil.put(UserUpdateActivity.this, AppHelper.userSpName, AppHelper.userSpUserInfoKey, new Gson().toJson(userInfo));
                    userInfo = new Gson().fromJson((String) SPUtil.get(UserUpdateActivity.this, AppHelper.userSpName, AppHelper.userSpUserInfoKey, ""), UserInfo.class);
                    if (userInfo != null) {
                        if (userInfo.getResult().getHeaderImg() != null)
                            Glide.with(UserUpdateActivity.this).load(userInfo.getResult().getHeaderImg()).into(ivUserImage);
                        if (userInfo.getResult().getName() != null)
                            tvUserName.setText(userInfo.getResult().getName());
                        if (userInfo.getResult().getNikeName() != null)
                            tvUserNikeName.setText(userInfo.getResult().getNikeName());
                        if (userInfo.getResult().getAutograph() != null)
                            tvUserAutograph.setText(userInfo.getResult().getAutograph());
                        if (userInfo.getResult().getNikeName() != null)
                            et_nike_name.setText(userInfo.getResult().getNikeName());
                        if (userInfo.getResult().getAutograph() != null)
                            et_autograph.setText(userInfo.getResult().getAutograph());
                        EventBus.getDefault().post(new EventMessage(AppHelper.USER_UpDate));
                    }
                } else {
                    ToastUtil.T_Error(UserUpdateActivity.this,"更新用户信息出错！");
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.T_Error(UserUpdateActivity.this,"更新用户信息出错！");
                LogUtil.d(UserUpdateActivity.class, "更新用户信息出错: " + e);
            }

            @Override
            public void onNext(UserInfo newUserInfo) {
                userInfo = newUserInfo;
            }
        }, Constant.API_KEY, userInfo.getResult().getName(), userPassword, userInfo.getResult().getHeaderImg(), userInfo.getResult().getNikeName(), userInfo.getResult().getAutograph(), userInfo.getResult().getPhone(), userInfo.getResult().getEmail(), userInfo.getResult().getRemarks(), userInfo.getResult().getVipGrade());
    }

    /**
     * 图库选择图片
     */
    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    /**
     * 接收#startActivityForResult(Intent, int)调用的结果
     *
     * @param requestCode 请求码 识别这个结果来自谁
     * @param resultCode  结果码
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {// 操作成功了
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:// 图库选择图片
                    Uri uri = data.getData();// 获取图片的uri
                    Intent intent_gallery_crop = new Intent("com.android.camera.action.CROP");
                    intent_gallery_crop.setDataAndType(uri, "image/*");
                    // 设置裁剪
                    intent_gallery_crop.putExtra("crop", "true");
                    intent_gallery_crop.putExtra("scale", true);
                    // aspectX aspectY 是宽高的比例
                    intent_gallery_crop.putExtra("aspectX", 1);
                    intent_gallery_crop.putExtra("aspectY", 1);
                    // outputX outputY 是裁剪图片宽高
                    intent_gallery_crop.putExtra("outputX", 400);
                    intent_gallery_crop.putExtra("outputY", 400);
                    intent_gallery_crop.putExtra("return-data", false);
                    // 创建文件保存裁剪的图片
                    createImageFile();
                    imageUri = Uri.fromFile(imageFile);
                    if (imageUri != null) {
                        intent_gallery_crop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent_gallery_crop.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    }
                    startActivityForResult(intent_gallery_crop, CROP_PHOTO);
                    break;
                case CROP_PHOTO:// 裁剪图片
                    try {
                        if (imageUri != null) {
                            displayImage(imageUri);
                            userInfo.getResult().setHeaderImg(imageUri.toString());
                            updateUserInfo();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }

    /**
     * 创建File保存图片
     */
    private void createImageFile() {
        try {
            if (imageFile != null && imageFile.exists()) {
                imageFile.delete();
            }
            // 新建文件
            imageFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "galleryDemo.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示图片
     *
     * @param imageUri 图片的uri
     */
    private void displayImage(Uri imageUri) {
        try {
            // glide根据图片的uri加载图片
            Glide.with(this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher_round)// 占位图设置：加载过程中显示的图片
                    .error(R.mipmap.ic_launcher_round)// 异常占位图
                    .transform(new CenterCrop())
                    .into(ivUserImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
