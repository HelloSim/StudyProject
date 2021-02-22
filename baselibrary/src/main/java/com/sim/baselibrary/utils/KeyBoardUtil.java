package com.sim.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.sim.baselibrary.R;

import java.lang.reflect.Method;

/**
 * @author Sim --- 打开或关闭软键盘
 */
public class KeyBoardUtil {

    /**
     * 关闭搜索
     */
    public static void closeSearch(SearchView searchView) {
        try {
            ((SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text)).setText("");
            searchView.setIconified(true);
        } catch (Exception e) {
            LogUtil.e(ZipUtil.class, "SearchView onCloseClicked" + e);
        }
    }

    /**
     * 隐藏系统键盘 Edittext不显示系统键盘；并且要有光标； 4.0以上TYPE_NULL，不显示系统键盘，但是光标也没了；
     */
    public static void hideSoftInputMethod(EditText ed) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
            // 19 setShowSoftInputOnFocus
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            Class<TextView> cls = TextView.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (Exception e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    @Deprecated
    public static void openKeybord(EditText mEditText, Context mContext) {
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
    }

    public static void openKeybord(EditText mEditText) {
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText
     * @param mContext
     */
    @Deprecated
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static void closeKeybord(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static void closeKeybord(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

}
