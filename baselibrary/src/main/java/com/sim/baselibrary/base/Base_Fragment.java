package com.sim.baselibrary.base;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.sim.baselibrary.views.DialogBuilder;

/**
 * @Auther Sim
 * @Time 2020/12/22 11:46
 * @Description 封装dialog。Fragment继承此类
 */
public class Base_Fragment extends Fragment {

    public void showDialog(String title, String message, String sureText, String cancelText,
                           final com.sim.baselibrary.views.DialogInterface dialogInterface) {
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
