package com.sim.baselibrary.callback;

import android.view.View;
import android.widget.EditText;

/**
 * @author Sim --- 长按全选文字
 */
public class SelectAllOnLongClickListener implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View v) {
        EditText et = v instanceof EditText ? ((EditText) v) : null;
        if (et != null) {
            et.selectAll();
            et.requestFocus();
        }
        return true;
    }
}