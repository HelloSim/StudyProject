package com.sim.baselibrary.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * @author Sim --- 自定义TableLayout
 */
public class CustomizeTabLayout extends TabLayout {

    private static final String SCROLLABLE_TAB_MIN_WIDTH = "scrollableTabMinWidth";//28前  mScrollableTabMinWidth

    public CustomizeTabLayout(Context context) {
        super(context);
        initTabMinWidth();
    }

    public CustomizeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTabMinWidth();
    }

    public CustomizeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTabMinWidth();
    }

    private void initTabMinWidth() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int tabMinWidth = getResources().getDisplayMetrics().widthPixels / getTabViewNumber();
        Field field;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 显示的默认tab个数
    protected int getTabViewNumber() {
        return 3;
    }

}
