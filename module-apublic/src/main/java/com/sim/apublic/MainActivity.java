package com.sim.apublic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.apublic.ui.fragment.PublicArticleFragment;
import com.sim.basicres.base.BaseActivity;

public class MainActivity  extends BaseActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Fragment publicArticleFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.apublic_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (publicArticleFragment == null) {
            publicArticleFragment = new PublicArticleFragment();
            mFragmentTransaction.add(R.id.frameLayout, publicArticleFragment);
        } else {
            mFragmentTransaction.show(publicArticleFragment);
        }
        mFragmentTransaction.commit();
    }

    @Override
    protected void initData() {

    }

}