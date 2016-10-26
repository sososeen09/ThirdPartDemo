package com.longge.dagger2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.longge.dagger2.util.IntentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_inject)
    Button mBtnInject;
    @BindView(R.id.btn_module)
    Button mBtnModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_inject, R.id.btn_module})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_inject:
                IntentHelper.startAct(this, OnlyInjectActivity.class);
                break;
            case R.id.btn_module:
                IntentHelper.startAct(this, ModuleActivity.class);
                break;
        }
    }
}
