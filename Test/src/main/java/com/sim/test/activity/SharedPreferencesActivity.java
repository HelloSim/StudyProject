package com.sim.test.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SharedPreferences储存数据例子
 * SharedPreferences文件储存在data/data/<package name>/shared_prefs目录下
 */

public class SharedPreferencesActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.read)
    Button read;
    @BindView(R.id.write)
    Button write;

    private SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences);
        ButterKnife.bind(this);

        preferences = getSharedPreferences("test_preferences", MODE_PRIVATE);
        editor = preferences.edit();

        read.setOnClickListener(this);
        write.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read:
                String time = preferences.getString("time", null);
                int randNum = preferences.getInt("random", 0);
                String result = time == null ? "暂未写入数据" : "写入时间为：" + time + "\n 上次生成的随机数为" + randNum;
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                break;
            case R.id.write:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日" + "hh:mm:ss");
                editor.putString("time", sdf.format(new Date()));
                editor.putInt("random", (int) (Math.random() * 100));
                editor.commit();
        }
    }
}
