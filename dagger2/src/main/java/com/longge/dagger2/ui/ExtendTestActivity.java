package com.longge.dagger2.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.longge.dagger2.R;
import com.longge.dagger2.di.component.DaggerExtendTestComponent;
import com.longge.dagger2.di.module.ActModule;
import com.longge.dagger2.di.module.AppModule;
import com.longge.dagger2.entity.ActEntity;

import javax.inject.Inject;

public class ExtendTestActivity extends AppCompatActivity {
    @Inject
    Context mContext;

    @Inject
    ActEntity mActEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_test);
        DaggerExtendTestComponent.builder().appModule(new AppModule(getApplication())).actModule(new ActModule()).build().inject(this);
        Toast.makeText(mContext, mActEntity.getDesc() + "\n" + mActEntity + "\n" + "Context: " + mContext, Toast.LENGTH_LONG).show();
    }
}
