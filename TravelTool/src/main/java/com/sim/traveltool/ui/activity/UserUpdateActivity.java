package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.baselibrary.constant.Constant;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ScreenUtil;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.internet.APIFactory;

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

    private String spName = "userState";
    private String spStateKey = "isLogIn";
    private String spUserInfoKey = "userInfo";

    private UserInfo userInfo;

    private String userImage;//头像
    private String userName;//用户名
    private String userNikeName;//昵称
    private String userAutograph;//签名

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
            if (userImage != null) {
                Glide.with(this).load(userImage).into(ivUserImage);
            }
            if (userName != null) {
                tvUserName.setText(userName);
            }
            if (userNikeName != null) {
                tvUserNikeName.setText(userNikeName);
            }
            if (userAutograph != null) {
                tvUserAutograph.setText(userAutograph);
            }
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        updateNikeNameLayout = inflater.inflate(R.layout.view_popup_update_nike_name, null);
        updateNikeNamePopupWindow = showPopupWindow(updateNikeNameLayout,300,180);
        et_nike_name = updateNikeNameLayout.findViewById(R.id.et_nike_name);
        btn_nike_name_cancel = updateNikeNameLayout.findViewById(R.id.btn_nike_name_cancel);
        btn_nike_name_confirm = updateNikeNameLayout.findViewById(R.id.btn_nike_name_confirm);
        et_nike_name.setText(userNikeName);
        btn_nike_name_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimeUtil.isFastClick()) {
                    et_nike_name.setText(userNikeName);
                    updateNikeNamePopupWindow.dismiss();
                }
            }
        });
        btn_nike_name_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimeUtil.isFastClick()) {
                    if (et_nike_name.getText().toString().length() > 0) {
                        updateUserInfo(userInfo.getResult().getName(), (String) SPUtil.get(UserUpdateActivity.this, spName, "password", ""), userInfo.getResult().getHeaderImg(),
                                et_nike_name.getText().toString(), userInfo.getResult().getAutograph(), userInfo.getResult().getPhone(), userInfo.getResult().getEmail(),
                                userInfo.getResult().getRemarks(), userInfo.getResult().getVipGrade());
                    }
                }
            }
        });

        updateAutographLayout = inflater.inflate(R.layout.view_popup_update_autograph, null);
        updateAutographNamePopupWindow = showPopupWindow(updateAutographLayout, 300, 180);
        btn_autograph_cancel = updateAutographLayout.findViewById(R.id.btn_autograph_cancel);
        btn_autograph_confirm = updateAutographLayout.findViewById(R.id.btn_autograph_confirm);
        et_autograph.setText(userAutograph);
        btn_autograph_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_autograph.setText(userAutograph);
                updateAutographNamePopupWindow.dismiss();
            }
        });
        btn_autograph_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_autograph.getText() != null) {
                    updateUserInfo(userInfo.getResult().getName(), (String) SPUtil.get(UserUpdateActivity.this, spName, "password", ""), userInfo.getResult().getHeaderImg(),
                            userInfo.getResult().getNikeName(), et_autograph.getText().toString(), userInfo.getResult().getPhone(), userInfo.getResult().getEmail(),
                            userInfo.getResult().getRemarks(), userInfo.getResult().getVipGrade());
                }
            }
        });
    }

    @Override
    protected void initData() {
        userInfo = new Gson().fromJson((String) SPUtil.get(this, spName, spUserInfoKey, ""), UserInfo.class);
        if (userInfo != null) {
            userImage = userInfo.getResult().getHeaderImg();
            userName = userInfo.getResult().getName();
            userNikeName = userInfo.getResult().getNikeName();
            userAutograph = userInfo.getResult().getAutograph();
        }
    }

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else if (view ==rlUserImage) {
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
                            SPUtil.put(UserUpdateActivity.this, spName, spStateKey, false);
                            SPUtil.remove(UserUpdateActivity.this, spName, spUserInfoKey);
                            SPUtil.remove(UserUpdateActivity.this, spName, "password");
                            finish();
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 更新用户信息的网络请求
     *
     * @param name
     * @param passwd
     * @param headerImg
     * @param nikeName
     * @param autograph
     * @param phone
     * @param email
     * @param remarks
     * @param vipGrade
     */
    private void updateUserInfo(String name, String passwd, String headerImg, String nikeName, String autograph, String phone, String email, String remarks, String vipGrade) {
        APIFactory.getInstance().updateUserInfo(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                if (userInfo.getCode() == 200) {
                    updateNikeNamePopupWindow.dismiss();
                    updateAutographNamePopupWindow.dismiss();
                    SPUtil.put(UserUpdateActivity.this, spName, spUserInfoKey, new Gson().toJson(userInfo));
                    userInfo = new Gson().fromJson((String) SPUtil.get(UserUpdateActivity.this, spName, spUserInfoKey, ""), UserInfo.class);
                    if (userInfo != null) {
                        userImage = userInfo.getResult().getHeaderImg();
                        userName = userInfo.getResult().getName();
                        userNikeName = userInfo.getResult().getNikeName();
                        userAutograph = userInfo.getResult().getAutograph();
                        if (userImage != null) {
                            Glide.with(UserUpdateActivity.this).load(userImage).into(ivUserImage);
                        }
                        if (userName != null) {
                            tvUserName.setText(userName);
                        }
                        if (userNikeName != null) {
                            tvUserNikeName.setText(userNikeName);
                        }
                        if (userAutograph != null) {
                            tvUserAutograph.setText(userAutograph);
                        }
                        et_nike_name.setText(userNikeName);
                        et_autograph.setText(userAutograph);
                    }
                } else {
                    Toast.makeText(UserUpdateActivity.this, userInfo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d(UserUpdateActivity.class, "更新用户信息出错: " + e);
            }

            @Override
            public void onNext(UserInfo newUserInfo) {
                userInfo = newUserInfo;
            }
        }, Constant.API_KEY, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
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
                            updateUserInfo(userInfo.getResult().getName(), (String) SPUtil.get(UserUpdateActivity.this, spName, "password", ""), imageUri.toString(),
                                    userInfo.getResult().getNikeName(), et_autograph.getText().toString(), userInfo.getResult().getPhone(), userInfo.getResult().getEmail(),
                                    userInfo.getResult().getRemarks(), userInfo.getResult().getVipGrade());
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
