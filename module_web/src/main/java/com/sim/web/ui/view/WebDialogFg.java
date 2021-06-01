package com.sim.web.ui.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sim.web.R;

public class WebDialogFg extends DialogFragment implements View.OnClickListener {

    private TextView tvOpen;
    private LinearLayout llCancel;
    private RelativeLayout parent;

    private String url;

    public WebDialogFg(String url) {
        this.url = url;
    }

    public static WebDialogFg newInstance(String url) {
        WebDialogFg fragment = new WebDialogFg(url);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.web_fg, container, false);
        tvOpen = rootView.findViewById(R.id.tvOpen);
        llCancel = rootView.findViewById(R.id.llCancel);
        parent = rootView.findViewById(R.id.parent);
        tvOpen.setOnClickListener(this);
        llCancel.setOnClickListener(this);
        parent.setOnClickListener(this);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true); //点击边际可消失
        dialog.getWindow().setWindowAnimations(R.style.common_dialog_animation);
//        dialog.getWindow().setLayout(100, ViewGroup.LayoutParams.WRAP_CONTENT);
        //width & height
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 1), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvOpen) {
            if (!TextUtils.isEmpty(url) && (url.startsWith("http") || url.startsWith("https"))) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            dismiss();
        } else if (view.getId() == R.id.llCancel || view.getId() == R.id.parent) {
            dismiss();
        }
    }

}
