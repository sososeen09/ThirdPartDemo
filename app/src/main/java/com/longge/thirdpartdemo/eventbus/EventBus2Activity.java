package com.longge.thirdpartdemo.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.longge.thirdpartdemo.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBus2Activity extends AppCompatActivity {

    @BindView(R.id.btn_sendMessageToFirst)
    Button mBtnSendMessageToFirst;
    @BindView(R.id.activity_event_bus2)
    RelativeLayout mActivityEventBus2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sendMessageToFirst)
    public void onClick() {
        EventBus.getDefault().post("从第二个Activity中返回的数据");
        finish();
    }
}
