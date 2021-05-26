package com.sim.user.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.sim.user.R;
import com.sim.user.ui.view.UserView;

public class MainActivity extends AppCompatActivity {

    LinearLayout left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_main);
        left = findViewById(R.id.left);
        left.addView(new UserView(this));
    }
}