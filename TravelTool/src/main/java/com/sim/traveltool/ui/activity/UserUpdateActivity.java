package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.sim.baselibrary.constant.Constant;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ScreenUtil;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 显示用户信息的页面
 */
public class UserUpdateActivity extends BaseActivity {
    private static final String TAG = "Sim_UserUpdateActivity";

    private Context context;

    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.rl_user_image)
    RelativeLayout rlUserImage;
    @BindView(R.id.rl_user_name)
    RelativeLayout rlUserName;
    @BindView(R.id.rl_user_nikeName)
    RelativeLayout rlUserNikeName;
    @BindView(R.id.rl_user_autograph)
    RelativeLayout rlUserAutograph;

    @BindView(R.id.iv_user_image)
    ImageView ivUserImage;
    @BindView(R.id.user_name)
    TextView tvUserName;
    @BindView(R.id.user_nikeName)
    TextView tvUserNikeName;
    @BindView(R.id.user_autograph)
    TextView tvUserAutograph;

    @BindView(R.id.btn_sign_out)
    Button btnSignOut;

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
    private int updateNikeNamePupDPWidth = 300;//宽度，单位DP
    private int updateNikeNamePupDPHeight = 180;//高度，单位DP
    private EditText et_nike_name;
    private Button btn_nike_name_cancel;
    private Button btn_nike_name_confirm;

    //更新签名弹窗
    private PopupWindow updateAutographNamePopupWindow;//弹窗
    private View updateAutographLayout;//布局
    private int updateAutographPupDPWidth = 300;//宽度，单位DP
    private int updateAutographPupDPHeight = 180;//高度，单位DP
    private EditText et_autograph;
    private Button btn_autograph_cancel;
    private Button btn_autograph_confirm;

    private static final int REQUEST_CODE_GALLERY = 0x10;// 图库选取图片标识请求码
    private static final int CROP_PHOTO = 0x12;// 裁剪图片标识请求码
    private static final int STORAGE_PERMISSION = 0x20;// 动态申请存储权限标识

    private File imageFile = null;// 声明File对象
    private Uri imageUri = null;// 裁剪后的图片uri
    private String path = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        userInfo = new Gson().fromJson((String) SPUtil.get(context, spName, spUserInfoKey, ""), UserInfo.class);
        if (userInfo != null) {
            userImage = userInfo.getResult().getHeaderImg();
            userName = userInfo.getResult().getName();
            userNikeName = userInfo.getResult().getNikeName();
            userAutograph = userInfo.getResult().getAutograph();
        }
    }

    private void initView() {
        if (userInfo != null) {
            if (userImage != null) {
                Glide.with(context).load(userImage).into(ivUserImage);
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        updateNikeNameLayout = inflater.inflate(R.layout.view_popup_update_nike_name, null);
        updateNikeNamePopupWindow = new PopupWindow(context);
        updateNikeNamePopupWindow.setContentView(updateNikeNameLayout);//设置主体布局
        updateNikeNamePopupWindow.setWidth(ScreenUtil.dip2px(context, updateNikeNamePupDPWidth));//宽度
        updateNikeNamePopupWindow.setHeight(ScreenUtil.dip2px(context, updateNikeNamePupDPHeight));//高度
        updateNikeNamePopupWindow.setFocusable(true);
        updateNikeNamePopupWindow.setBackgroundDrawable(new BitmapDrawable());//设置空白背景
        updateNikeNamePopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);//动画
        et_nike_name = updateNikeNameLayout.findViewById(R.id.et_nike_name);
        btn_nike_name_cancel = updateNikeNameLayout.findViewById(R.id.btn_nike_name_cancel);
        btn_nike_name_confirm = updateNikeNameLayout.findViewById(R.id.btn_nike_name_confirm);
        et_nike_name.setText(userNikeName);
        btn_nike_name_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_nike_name.setText(userNikeName);
                updateNikeNamePopupWindow.dismiss();

            }
        });
        btn_nike_name_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_nike_name.getText().toString().length() > 0) {
                    updateUserInfo(userInfo.getResult().getName(), (String) SPUtil.get(context, spName, "password", ""), userInfo.getResult().getHeaderImg(),
                            et_nike_name.getText().toString(), userInfo.getResult().getAutograph(), userInfo.getResult().getPhone(), userInfo.getResult().getEmail(),
                            userInfo.getResult().getRemarks(), userInfo.getResult().getVipGrade());
                }
            }
        });

        updateAutographLayout = inflater.inflate(R.layout.view_popup_update_autograph, null);
        updateAutographNamePopupWindow = new PopupWindow(context);
        updateAutographNamePopupWindow.setContentView(updateAutographLayout);//设置主体布局
        updateAutographNamePopupWindow.setWidth(ScreenUtil.dip2px(context, updateAutographPupDPWidth));//宽度
        updateAutographNamePopupWindow.setHeight(ScreenUtil.dip2px(context, updateAutographPupDPHeight));//高度
        updateAutographNamePopupWindow.setFocusable(true);
        updateAutographNamePopupWindow.setBackgroundDrawable(new BitmapDrawable());//设置空白背景
        updateAutographNamePopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);//动画
        et_autograph = updateAutographLayout.findViewById(R.id.et_autograph);
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
                    updateUserInfo(userInfo.getResult().getName(), (String) SPUtil.get(context, spName, "password", ""), userInfo.getResult().getHeaderImg(),
                            userInfo.getResult().getNikeName(), et_autograph.getText().toString(), userInfo.getResult().getPhone(), userInfo.getResult().getEmail(),
                            userInfo.getResult().getRemarks(), userInfo.getResult().getVipGrade());
                }
            }
        });
    }

    /**
     * 更新用户信息的网络请求
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
        retrofitUtil.updateUserInfo(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                if (userInfo.getCode() == 200) {
                    updateNikeNamePopupWindow.dismiss();
                    updateAutographNamePopupWindow.dismiss();
                    SPUtil.put(context, spName, spUserInfoKey, new Gson().toJson(userInfo));
                    userInfo = new Gson().fromJson((String) SPUtil.get(context, spName, spUserInfoKey, ""), UserInfo.class);
                    if (userInfo != null) {
                        userImage = userInfo.getResult().getHeaderImg();
                        userName = userInfo.getResult().getName();
                        userNikeName = userInfo.getResult().getNikeName();
                        userAutograph = userInfo.getResult().getAutograph();
                        if (userImage != null) {
                            Glide.with(context).load(userImage).into(ivUserImage);
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
                    Toast.makeText(context, userInfo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d(TAG, "updateUserInfoOnError: " + e);
            }

            @Override
            public void onNext(UserInfo newUserInfo) {
                userInfo = newUserInfo;
            }
        }, Constant.API_KEY, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
    }

    @OnClick({R.id.back, R.id.rl_user_image, R.id.rl_user_nikeName, R.id.rl_user_autograph, R.id.btn_sign_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_user_image:
                gallery();
                break;
            case R.id.rl_user_nikeName:
                updateNikeNamePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                break;
            case R.id.rl_user_autograph:
                updateAutographNamePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                break;
            case R.id.btn_sign_out:
                new AlertDialog.Builder(context)
                        .setTitle("退出登录")
                        .setMessage("是否确认退出？")
                        .setCancelable(true)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtil.put(context, spName, spStateKey, false);
                                SPUtil.remove(context, spName, spUserInfoKey);
                                SPUtil.remove(context, spName, "password");
                                finish();
                            }
                        })
                        .show();
                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                            updateUserInfo(userInfo.getResult().getName(), (String) SPUtil.get(context, spName, "password", ""), imageUri.toString(),
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
