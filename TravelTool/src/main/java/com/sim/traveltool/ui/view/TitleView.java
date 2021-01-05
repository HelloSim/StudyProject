package com.sim.traveltool.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sim.baselibrary.utils.DensityUtil;
import com.sim.traveltool.R;

/**
 * @Auther Sim
 * @Time 2021/1/5 17:42
 * @Description 自定义标题栏组件
 */
public class TitleView extends RelativeLayout {

    // 定义组件
    private ImageView ivBack, ivRight;
    private TextView titleTextView;

    private String titleText;// 文字
    private int leftImageSrc, rightImageSrc;// 左右两个ImageView的资源

    // 布局属性，用来控制组件元素在ViewGroup中的位置
    private LayoutParams mLeftParams, mTitlepParams, mRightParams;

    // 点击监听接口
    private backClickListener backClickListener;
    private rightClickListener rightClickListener;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable(context, attrs);
        initView(context);
    }

    /**
     * 初始化变量
     *
     * @param context
     * @param attrs
     */
    private void initVariable(Context context, AttributeSet attrs) {
        // 将attrs中的值存储到TypedArray中
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        titleText = ta.getString(R.styleable.TitleBar_title);
        leftImageSrc = ta.getResourceId(R.styleable.TitleBar_leftBackground, Color.TRANSPARENT);
        rightImageSrc = ta.getResourceId(R.styleable.TitleBar_rightBackground, Color.TRANSPARENT);
        ta.recycle();// 注意！此处获取完属性值后要添加recycle()方法，避免重新创建时发生错误
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        setBackgroundColor(Color.BLACK);

        // 创建childView
        ivBack = new ImageView(context);
        ivRight = new ImageView(context);
        titleTextView = new TextView(context);

        // 设置childview属性
        ivBack.setBackgroundResource(leftImageSrc);
        ivBack.setPadding(20, 10, 20, 10);

        ivRight.setBackgroundResource(rightImageSrc);
        ivRight.setPadding(20, 10, 20, 10);

        titleTextView.setText(titleText);
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setTextSize(20);
        titleTextView.setGravity(Gravity.CENTER);

        // 设置布局并添加到ViewGroup中,此处最好是WRAP_CONTENT，不然Measure时控件会过大，会使设置控件高度为wrap_content时失效
        mLeftParams = new LayoutParams(DensityUtil.dp2px(context, 25), DensityUtil.dp2px(context, 25));
        mLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        mLeftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        mLeftParams.setMargins(10, 10, 10, 10);
        // 添加到ViewGroup
        addView(ivBack, mLeftParams);

        mRightParams = new LayoutParams(DensityUtil.dp2px(context, 30), DensityUtil.dp2px(context, 30));
        mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        mRightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        mRightParams.setMargins(10, 10, 10, 10);
        addView(ivRight, mRightParams);

        mTitlepParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTitlepParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(titleTextView, mTitlepParams);

        // 设置监听
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backClickListener != null)
                    backClickListener.onClick();
            }
        });
        ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightClickListener != null)
                    rightClickListener.onClick();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0; // 自己测量的宽度
        int height = 0;// 自己测量的高度

        // 获取子view的个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量子View的宽和高
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            // 得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            // 子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // 子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            width += childWidth;
            if (childHeight > height) {
                height = childHeight;
            }
        }
        setMeasuredDimension(measureDimension(width, widthMeasureSpec), measureDimension(height, heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureDimension(int defaultDimension, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultDimension;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 设置按钮点击监听回调接口
     */
    public interface backClickListener {
        void onClick();
    }

    public interface rightClickListener {
        void onClick();
    }

    /**
     * 添加按钮监听
     */
    public void setBackClickListener(backClickListener backClickListener) {
        this.backClickListener = backClickListener;
    }

    public void setRightClickListener(rightClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    /**
     * 设置按钮是否显示
     */
    public void setButtonVisable(boolean leftBtnVisable, boolean rightBtnVisable) {
        if (leftBtnVisable) {
            ivBack.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.GONE);
        }
        if (rightBtnVisable) {
            ivRight.setVisibility(View.VISIBLE);
        } else {
            ivRight.setVisibility(View.GONE);
        }
    }

}
