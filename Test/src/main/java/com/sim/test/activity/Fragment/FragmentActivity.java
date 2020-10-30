package com.sim.test.activity.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;

/**
 * 简单的Fragment展示
 */

public class FragmentActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "FragmentActivity";

    //Fragment
    private FragmentLeft fragment_left;
    private FragmentRight fragment_right;
    private FragmentIntermediate fragment_intermediate;

    //底端菜单栏LinearLayout
    private LinearLayout ll_message, ll_home, ll_me;

    //底端菜单栏Imageview
    private ImageView iv_message, iv_home, iv_me;

    //底端菜单栏Textview
    private TextView tv_message, tv_home, tv_me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //初始化控件
        initView();
        //设置监听
        initEvent();
        //将消息页面设置为选中
        iv_message.setImageResource(R.drawable.fragment_message_selected);
        initFragment(1);

    }

    /**
     * 绑定控件
     */
    private void initView() {
        ll_message = findViewById(R.id.ll_message);
        ll_home = findViewById(R.id.ll_home);
        ll_me = findViewById(R.id.ll_me);
        iv_message = findViewById(R.id.iv_message);
        iv_home = findViewById(R.id.iv_home);
        iv_me = findViewById(R.id.iv_me);
        tv_message = findViewById(R.id.tv_message);
        tv_home = findViewById(R.id.tv_home);
        tv_me = findViewById(R.id.tv_me);
    }

    /**
     * 监听事件
     */
    private void initEvent() {
        ll_message.setOnClickListener(this);
        ll_home.setOnClickListener(this);
        ll_me.setOnClickListener(this);
    }

    /**
     * @param i
     */
    private void initFragment(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //先隐藏所有的Fragment
        hideAllFragment(fragmentTransaction);
        switch (i) {
            case 1:
                if (fragment_left == null) {
                    fragment_left = new FragmentLeft();
                    fragmentTransaction.add(R.id.content_fragment, fragment_left);
                    fragmentTransaction.show(fragment_left);
                    Log.d(TAG, "initFragment: 没有fragment_left实例，创建并显示");
                } else {
                    fragmentTransaction.show(fragment_left);
                    Log.d(TAG, "initFragment: 存在fragment_left实例，直接显示");
                }
                break;
            case 2:
                if (fragment_intermediate == null) {
                    fragment_intermediate = new FragmentIntermediate();
                    fragmentTransaction.add(R.id.content_fragment, fragment_intermediate);
                    fragmentTransaction.show(fragment_intermediate);
                    Log.d(TAG, "initFragment: 没有fragment_intermediate实例，创建并显示");
                } else {
                    fragmentTransaction.show(fragment_intermediate);
                    Log.d(TAG, "initFragment: 存在fragment_intermediate实例，直接显示");
                }
                break;
            case 3:
                if (fragment_right == null) {
                    fragment_right = new FragmentRight();
                    fragmentTransaction.add(R.id.content_fragment, fragment_right);
                    fragmentTransaction.show(fragment_right);
                    Log.d(TAG, "initFragment: 没有fragment_right实例，创建并显示");
                } else {
                    fragmentTransaction.show(fragment_right);
                    Log.d(TAG, "initFragment: 存在fragment_right实例，直接显示");
                }
                break;
        }
        //提交事务
        fragmentTransaction.commit();
    }

    /**
     * 隐藏所有的Fragment
     *
     * @param fragmentTransaction
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fragment_left != null) {
            fragmentTransaction.hide(fragment_left);
        }
        if (fragment_intermediate != null) {
            fragmentTransaction.hide(fragment_intermediate);
        }
        if (fragment_right != null) {
            fragmentTransaction.hide(fragment_right);
        }
    }


    @Override
    public void onClick(View v) {
        //首先重置所有ImageView
        restartButton();
        //再根据所选进行Fragment跳转
        switch (v.getId()) {
            case R.id.ll_message:
                iv_message.setImageResource(R.drawable.fragment_message_selected);
                initFragment(1);
                break;
            case R.id.ll_home:
                iv_home.setImageResource(R.drawable.fragment_home_selected);
                initFragment(2);
                break;
            case R.id.ll_me:
                iv_me.setImageResource(R.drawable.fragment_me_selected);
                initFragment(3);
                break;
        }
    }

    /**
     * 重置所有ImageView
     */
    private void restartButton() {
        iv_message.setImageResource(R.drawable.fragment_message);
        iv_home.setImageResource(R.drawable.fragment_home);
        iv_me.setImageResource(R.drawable.fragment_me);
    }

}
