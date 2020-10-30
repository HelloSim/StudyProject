package com.sim.test.view.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sim.test.R;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * 自定义User登录Dialog
 */
public class MyUserDialog extends Dialog {
    private Context context;

    public MyUserDialog(Context context) {
        super(context);
        this.context = context;
    }

    private EditText userName, userPassword;
    private Button userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_my_user);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        userLogin = findViewById(R.id.userLogin);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(context, "用户名：" + userName.getText().toString() + ",密码：" + userPassword.getText().toString(), TastyToast.LENGTH_SHORT, TastyToast.INFO);
            }
        });
    }

}
