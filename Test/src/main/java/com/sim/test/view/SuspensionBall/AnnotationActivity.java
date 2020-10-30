package com.sim.test.view.SuspensionBall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 批注activity
 */
public class AnnotationActivity extends BaseActivity {

    @BindView(R.id.annotation_cancel)
    LinearLayout annotation_cancel;
    @BindView(R.id.annotation_clear)
    LinearLayout annotation_clear;
    @BindView(R.id.annotation_save)
    LinearLayout annotation_save;
    @BindView(R.id.annotation_view)
    AnnotationView annotation_view;
    @BindView(R.id.annotation_all)
    LinearLayout annotation_all;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        if (annotation_all.getVisibility() == View.GONE) {
            annotation_all.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.annotation_cancel, R.id.annotation_clear, R.id.annotation_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.annotation_cancel:
                finish();
                break;
            case R.id.annotation_clear:
                annotation_view.clear();
                break;
            case R.id.annotation_save:
                annotation_all.setVisibility(View.GONE);
                //截图操作

                finish();
                break;
        }
    }

}
