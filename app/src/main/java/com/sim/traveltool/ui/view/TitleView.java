package com.sim.traveltool.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sim.traveltool.R;

/**
 * @author Sim --- 自定义标题栏组件
 */
public class TitleView extends RelativeLayout {

    // 定义组件
    private ImageView ivLeft, ivRight;
    private TextView titleTextView;

    private boolean showLeft, showRight;
    private String titleText;// 文字
    private int leftImageSrc, rightImageSrc;// 左右两个ImageView的资源

    // 布局属性，用来控制组件元素在ViewGroup中的位置
    private LayoutParams mLeftParams, mTitlepParams, mRightParams;

    // 点击监听接口
    private LeftClickListener leftClickListener;
    private RightClickListener rightClickListener;
    private ClickListener clickListener;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable(context, attrs);
        initView(context, showLeft, showRight);
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
        showLeft = ta.getBoolean(R.styleable.TitleBar_showLeft, false);
        showRight = ta.getBoolean(R.styleable.TitleBar_showRight, false);
        titleText = ta.getString(R.styleable.TitleBar_title);
        leftImageSrc = ta.getResourceId(R.styleable.TitleBar_leftImageSrc, Color.TRANSPARENT);
        rightImageSrc = ta.getResourceId(R.styleable.TitleBar_rightImageSrc, Color.TRANSPARENT);
        ta.recycle();// 注意！此处获取完属性值后要添加recycle()方法，避免重新创建时发生错误
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context, boolean showBack, boolean showIcon) {
        setBackgroundColor(Color.BLACK);

        titleTextView = new TextView(context);
        titleTextView.setText(titleText);
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setTextSize(20);
        titleTextView.setPadding(0, 30, 0, 30);
        TextPaint tp = titleTextView.getPaint();
        tp.setFakeBoldText(true);
        titleTextView.setGravity(Gravity.CENTER);
        mTitlepParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTitlepParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(titleTextView, mTitlepParams);

        if (showBack) {
            ivLeft = new ImageView(context);
            ivLeft.setBackgroundResource(leftImageSrc);
            ivLeft.setPadding(40, 30, 40, 30);
            mLeftParams = new LayoutParams(60, 60);
            mLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
            mLeftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mLeftParams.setMargins(30, 10, 10, 10);
            addView(ivLeft, mLeftParams);
            ivLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.left(ivLeft);
                    } else {
                        if (leftClickListener != null) {
                            leftClickListener.onClick(ivLeft);
                        }
                    }
                }
            });
        }

        if (showIcon) {
            ivRight = new ImageView(context);
            ivRight.setBackgroundResource(rightImageSrc);
            ivRight.setPadding(40, 30, 40, 30);
            mRightParams = new LayoutParams(80, 80);
            mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
            mRightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mRightParams.setMargins(10, 10, 30, 10);
            addView(ivRight, mRightParams);
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.right(ivRight);
                    } else {
                        if (rightClickListener != null) {
                            rightClickListener.onClick(ivRight);
                        }
                    }
                }
            });
        }
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
    public interface LeftClickListener {
        void onClick(View leftView);
    }

    public interface RightClickListener {
        void onClick(View rightView);
    }

    public interface ClickListener {
        void left(View leftView);

        void right(View right);
    }

    /**
     * 添加按钮监听
     */
    public void setLeftClickListener(LeftClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    public void setRightClickListener(RightClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setTitleTextView(String titleText) {
        this.titleText = titleText;
        titleTextView.setText(titleText);
    }

    public void setLeftImage(int leftImageSrc) {
        this.leftImageSrc = leftImageSrc;
        ivLeft.setBackgroundResource(leftImageSrc);
    }

    public void setRightImage(int rightImageSrc) {
        this.rightImageSrc = rightImageSrc;
        ivRight.setBackgroundResource(rightImageSrc);
    }

}
