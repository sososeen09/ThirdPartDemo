package com.longge.dagger2.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.longge.dagger2.App;
import com.longge.dagger2.R;
import com.longge.dagger2.di.component.ActivityComponent;
import com.longge.dagger2.di.component.DaggerActivityComponent;
import com.longge.dagger2.di.module.ActModule;

public class SubComponentTestActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        mActivityComponent = DaggerActivityComponent.builder()
                                                    .appComponent(((App) getApplication()).getAppComponent())
                                                    .actModule(new ActModule())
                                                    .build();

        SubFragment subFragment = new SubFragment();
        FragmentManager fg = getSupportFragmentManager();
        FragmentTransaction ft = fg.beginTransaction();
        ft.replace(R.id.fl_container, subFragment);
        ft.commit();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
