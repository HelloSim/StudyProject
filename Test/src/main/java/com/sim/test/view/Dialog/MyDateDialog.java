package com.sim.test.view.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.sim.test.R;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * 自定义DATe选择Dialog
 */
public class MyDateDialog extends Dialog {
    private Context context;

    public MyDateDialog(Context context) {
        super(context);
        this.context = context;
    }

    private DatePicker data_picker;
    private Button confirm, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_my_date);
        data_picker = findViewById(R.id.data_picker);
        confirm = findViewById(R.id.determine);
        cancel = findViewById(R.id.cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(context, data_picker.getYear() + "年" + (data_picker.getMonth() + 1) + "月" + data_picker.getDayOfMonth() + "日", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                MyDateDialog.this.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(context, "取消", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                MyDateDialog.this.dismiss();
            }
        });
    }

}
