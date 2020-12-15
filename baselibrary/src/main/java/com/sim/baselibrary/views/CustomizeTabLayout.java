package com.sim.baselibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * @Auther Sim
 * @Time 2020/12/15 11:55
 * @Description 自定义TableLayout
 */
public class CustomizeTabLayout extends TableLayout {

    private static final String SCROLLABLE_TAB_MIN_WIDTH = "scrollableTabMinWidth";//28前  mScrollableTabMinWidth
    private int size = 3;//默认3个

    public CustomizeTabLayout(Context context) {
        super(context);
        initTabMinWidth();
    }

    public CustomizeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTabMinWidth();
    }

    private void initTabMinWidth() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int tabMinWidth = screenWidth / getTabViewNumber();
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
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
