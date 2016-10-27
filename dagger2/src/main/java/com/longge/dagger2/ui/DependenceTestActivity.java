package com.longge.dagger2.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.longge.dagger2.App;
import com.longge.dagger2.R;
import com.longge.dagger2.di.component.DaggerActivityComponent;

import javax.inject.Inject;

public class DependenceTestActivity extends AppCompatActivity {

    @Inject
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependent_test);
        DaggerActivityComponent.builder().appComponent(((App) getApplication()).getAppComponent()).build().inject(this);
        Toast.makeText(mContext, "App Context: " + mContext.toString(), Toast.LENGTH_LONG).show();
    }
}
