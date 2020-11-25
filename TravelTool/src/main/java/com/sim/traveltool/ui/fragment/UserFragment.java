package com.sim.traveltool.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.ui.activity.NewsCollectActivity;
import com.sim.traveltool.ui.activity.UserLogInActivity;
import com.sim.traveltool.ui.activity.UserUpdateActivity;
import com.sim.baselibrary.utils.SPUtil;
import com.google.gson.Gson;

import static android.app.Activity.RESULT_OK;

/**
 * @Auther Sim
 * @Time 2020/4/27 1:05
 * @Description “我的”Fragment
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout userLogIn;
    private RelativeLayout userDetail;
    private RelativeLayout userCollect;
    private RelativeLayout userSetting;

    private ImageView userImage;
    private TextView userNikeName;
    private TextView userAutograph;

    private String spName = "userState";
    private String spStateKey = "isLogIn";
    private String spUserInfoKey = "userInfo";
    private boolean isLogIn = false;

    private UserInfo userInfo;

    private final int LogInNum = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        if (isLogIn) {
            userLogIn.setVisibility(View.GONE);
            userDetail.setVisibility(View.VISIBLE);
            if (SPUtil.contains(getContext(), spName, spUserInfoKey)) {
                userInfo = new Gson().fromJson((String) SPUtil.get(getContext(), spName, spUserInfoKey, ""), UserInfo.class);
                if (userInfo != null) {
                    if (userInfo.getResult().getHeaderImg() != null) {
                        Glide.with(getContext()).load(userInfo.getResult().getHeaderImg()).into(userImage);
                    }
                    if (userInfo.getResult().getName() != null) {
                        userNikeName.setText(userInfo.getResult().getName());
                    }
                    if (userInfo.getResult().getAutograph() != null) {
                        userAutograph.setText(userInfo.getResult().getAutograph());
                    }
                } else {
                    isLogIn = false;
                    userDetail.setVisibility(View.GONE);
                    userLogIn.setVisibility(View.VISIBLE);
                }
            } else {
                isLogIn = false;
                userDetail.setVisibility(View.GONE);
                userLogIn.setVisibility(View.VISIBLE);
            }
        } else {
            userDetail.setVisibility(View.GONE);
            userLogIn.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        if (!SPUtil.contains(getContext(), spName, spStateKey)) {
            SPUtil.put(getContext(), spName, spStateKey, isLogIn);
        } else {
            isLogIn = (boolean) SPUtil.get(getContext(), spName, spStateKey, false);
        }
    }

    private void initView(View view) {
        userLogIn = view.findViewById(R.id.user_log_in);
        userDetail = view.findViewById(R.id.user_detail);
        userCollect = view.findViewById(R.id.user_collect);
        userSetting = view.findViewById(R.id.user_setting);
        userImage = view.findViewById(R.id.user_image);
        userNikeName = view.findViewById(R.id.user_nike_name);
        userAutograph = view.findViewById(R.id.user_autograph);
        if (isLogIn) {
            userLogIn.setVisibility(View.GONE);
            userDetail.setVisibility(View.VISIBLE);
            if (SPUtil.contains(getContext(), spName, spUserInfoKey)) {
                userInfo = new Gson().fromJson((String) SPUtil.get(getContext(), spName, spUserInfoKey, ""), UserInfo.class);
                if (userInfo != null) {
                    if (userInfo.getResult().getHeaderImg() != null) {
                        Glide.with(getContext()).load(userInfo.getResult().getHeaderImg()).into(userImage);
                    }
                    if (userInfo.getResult().getName() != null) {
                        userNikeName.setText(userInfo.getResult().getName());
                    }
                    if (userInfo.getResult().getAutograph() != null) {
                        userAutograph.setText(userInfo.getResult().getAutograph());
                    }
                } else {
                    isLogIn = false;
                    userDetail.setVisibility(View.GONE);
                    userLogIn.setVisibility(View.VISIBLE);
                }
            } else {
                isLogIn = false;
                userDetail.setVisibility(View.GONE);
                userLogIn.setVisibility(View.VISIBLE);
            }
        } else {
            userDetail.setVisibility(View.GONE);
            userLogIn.setVisibility(View.VISIBLE);
        }
        userLogIn.setOnClickListener(this);
        userDetail.setOnClickListener(this);
        userCollect.setOnClickListener(this);
        userSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_log_in:
                startActivityForResult(new Intent(getContext(), UserLogInActivity.class), LogInNum);
                break;
            case R.id.user_detail:
                startActivityForResult(new Intent(getContext(), UserUpdateActivity.class), LogInNum);
                break;
            case R.id.user_collect:
                startActivity(new Intent(getContext(), NewsCollectActivity.class));
                break;
            case R.id.user_setting:

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LogInNum) {
            isLogIn = (boolean) SPUtil.get(getContext(), spName, spStateKey, false);
            if (isLogIn) {
                userLogIn.setVisibility(View.GONE);
                userDetail.setVisibility(View.VISIBLE);
                if (SPUtil.contains(getContext(), spName, spUserInfoKey)) {
                    userInfo = new Gson().fromJson((String) SPUtil.get(getContext(), spName, spUserInfoKey, ""), UserInfo.class);
                    if (userInfo.getResult().getHeaderImg() != null) {
                        Glide.with(getContext()).load(userInfo.getResult().getHeaderImg()).into(userImage);
                    }
                    if (userInfo.getResult().getName() != null) {
                        userNikeName.setText(userInfo.getResult().getName());
                    }
                    if (userInfo.getResult().getAutograph() != null) {
                        userAutograph.setText(userInfo.getResult().getAutograph());
                    }
                } else {
                    isLogIn = false;
                    userDetail.setVisibility(View.GONE);
                    userLogIn.setVisibility(View.VISIBLE);
                }
            } else {
                userDetail.setVisibility(View.GONE);
                userLogIn.setVisibility(View.VISIBLE);
            }
        }
    }
}
