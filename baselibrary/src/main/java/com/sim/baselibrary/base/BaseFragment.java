package com.sim.baselibrary.base;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sim.baselibrary.R;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.baselibrary.callback.OnMultiClickListener;
import com.sim.baselibrary.utils.ScreenUtil;
import com.sim.baselibrary.views.DialogBuilder;

/**
 * @author Sim --- 封装dialog。项目模块BaseFragment继承此类
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private View mRootView;

    protected abstract @LayoutRes
    int getLayoutRes();//指定布局

    protected abstract void bindViews(View view);//绑定控件

    protected abstract void initView(View view);//View的一些初始化

    protected abstract void initData();//Data的一些初始化

    protected boolean isAddScrollView() {
        return false;
    }

    /**
     * 控件绑定监听事件
     *
     * @param vs
     */
    protected void setViewClick(View... vs) {
        for (View v : vs) {
            v.setOnClickListener(this);
        }
    }

    protected void setViewClick(@IdRes int... vs) {
        for (int v : vs) {
            mRootView.findViewById(v).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (OnMultiClickListener.isNoFastClick()) {
            onMultiClick(view);
        }
    }

    /**
     * 防止多次点击
     *
     * @param view
     */
    public void onMultiClick(View view) {
    }


    /**
     * 作用:与宿主Activity关联
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * 作用：系统创建Fragment的时候调用此函数，一般在此函数内实例化一些变量。
     * <p>
     * 这些变量主要是:当你暂停或者停止时你想保持的数据，如果我们要为fragment启动一个后台线程，可以考虑将代码放于此处。
     * 参数是：Bundle savedInstance, 用于保存 Fragment 参数,
     * Fragement 也可以 重写 onSaveInstanceState(BundleoutState) 方法,保存Fragement状态;
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 作用:第一次使用的时候fragment会在这上面画一个layout出来， 为了可以画控件要返回一个 布局的view，也可以返回null。
     * <p>
     * 当系统用到fragment的时候fragment就要返回他的view，越快越好 ，所以尽量在这里不要做耗时操作，比如从数据库加载大量数据显示listview，
     * 当然线程还是可以的。给当前的fragment绘制ui布局，可以使用线程更新UI，说白了就是加载fragment的布局的。 这里一般都先判断是否为null。
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            View inflate = inflater.inflate(getLayoutRes(), container, false);
            if (isAddScrollView()) {
                ScrollView scrollView = new ScrollView(getContext());
                scrollView.addView(inflate);
                mRootView = scrollView;
            } else {
                mRootView = inflate;
            }
            bindViews(inflate);
            initView(inflate);
            initData();
        } else {
//            需要注意的是：如果直接返回会报错（java.lang.IllegalStateException: The specified child already has radio_button_style parent）,就删除
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    /**
     * 作用:与Activity相关的UI交互操作，初始化那些需要父Activity或者Fragment的UI已经被完全初始化才能初始化的元素。
     * <p>
     * 如果在onCreateView里面初始化空间会慢很多，比如listview等
     * 注意:此回调函数是在Activity中的onCreate方法执行完后才进行调用的，
     * 即:当执行onActivityCreated()的时候activity的onCreate才刚刚完成。
     * 所以在onActivityCreated()调用之前 activity的onCreate可能还没有完成，
     * 所以不能在onCreateView()中进行与activity有交互的UI操作，
     * UI交互操作要放在onActivityCreated()里面进行。
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 和activity一致，启动Fragement 启动时回调,，此时Fragement可见。
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 和activity一致  在activity中运行是可见的。激活, Fragement 进入前台, 可获取焦点时激活。
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 和activity一致  其他的activity获得焦点，这个仍然可见第一次调用的时候，指的是 用户 离开这个fragment(并不是被销毁)
     * 通常用于用户的提交(可能用户离开后不会回来了)。
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 和activity一致， fragment不可见的， 可能情况：activity被stopped了或者fragment被移除但被加入到回退栈中，
     * 一个stopped的fragment仍然是活着的如果长时间不用也会被移除。
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * fragemnt销毁相关联的UI布局， 清除所有跟视图相关的资源。在Fragment中的布局被移除时调用
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 销毁fragment对象， 跟activity类似。
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Fragment和Activity解除关联的时候调用。脱离Activity,可见Fragment的销毁。
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 显示popupWindow
     *
     * @param view        布局
     * @param pupDPWidth  宽度
     * @param pupDPHeight 高度
     * @return
     */
    public PopupWindow showPopupWindow(View view, int pupDPWidth, int pupDPHeight) {
        PopupWindow popupWindow = new PopupWindow(getContext());
        popupWindow.setContentView(view);//设置主体布局
        popupWindow.setWidth(ScreenUtil.dip2px(getContext(), pupDPWidth));//宽度
        popupWindow.setHeight(ScreenUtil.dip2px(getContext(), pupDPHeight));//高度
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置空白背景
        popupWindow.setAnimationStyle(R.style.popwindow_anim_style);//动画
        return popupWindow;
    }

    /**
     * dialog显示
     *
     * @param title           标题
     * @param message         提示信息
     * @param sureText        确认按钮
     * @param cancelText      取消按钮
     * @param dialogInterface 点击事件监听
     */
    public void showDialog(String title, String message, String sureText, String cancelText,
                           final DialogInterface dialogInterface) {
        DialogBuilder dialogBuilder;
        dialogBuilder = new DialogBuilder(getContext());
        if (title != null) {
            dialogBuilder.title(title);
        }
        if (message != null) {
            dialogBuilder.message(message);
        }
        if (sureText != null) {
            dialogBuilder.sureText(sureText);
        }
        if (cancelText != null) {
            dialogBuilder.cancelText(cancelText);
        }
        if (dialogInterface != null) {
            dialogBuilder.setSureOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogInterface.sureOnClick();
                }
            });
            dialogBuilder.setCancelOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogInterface.cancelOnClick();
                }
            });
        }
        dialogBuilder.build().show();
    }

}
