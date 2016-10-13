package com.longge.thirdpartdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.longge.thirdpartdemo.util.IntentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_eventBus)
    Button mBtnEventBus;
    @BindView(R.id.btn_greenDAO)
    Button mBtnGreenDAO;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_eventBus, R.id.btn_greenDAO})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_eventBus:
                IntentHelper.startAct(this, EventBusActivity.class);
                break;
            case R.id.btn_greenDAO:
                IntentHelper.startAct(this, GreenDaoActivity.class);
                break;
        }
    }
}
