package com.longge.dagger2.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.longge.dagger2.R;
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

    @OnClick({R.id.btn_inject, R.id.btn_module, R.id.btn_prorier, R.id.btn_singleton, R.id.btn_dependence, R.id.btn_sub, R.id.btn_extend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_inject:
                IntentHelper.startAct(this, OnlyInjectTestActivity.class);
                break;
            case R.id.btn_module:
                IntentHelper.startAct(this, ModuleTestActivity.class);
                break;
            case R.id.btn_prorier:
                IntentHelper.startAct(this, PriorityTestActivity.class);
                break;
            case R.id.btn_singleton:
                IntentHelper.startAct(this, SingletonTestActivity.class);
                break;
            case R.id.btn_dependence:
                IntentHelper.startAct(this, DependenceTestActivity.class);
                break;
            case R.id.btn_sub:
                IntentHelper.startAct(this, SubComponentTestActivity.class);
                break;
            case R.id.btn_extend:
                IntentHelper.startAct(this, ExtendTestActivity.class);
                break;

        }
    }
}
