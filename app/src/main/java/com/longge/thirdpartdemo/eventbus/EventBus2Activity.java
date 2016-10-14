package com.longge.thirdpartdemo.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.longge.thirdpartdemo.R;
import com.longge.thirdpartdemo.eventbus.event.NewActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBus2Activity extends AppCompatActivity {

    @BindView(R.id.btn_sendMessageToFirst)
    Button mBtnSendMessageToFirst;

    @BindView(R.id.tv_showMessage)
    TextView mTvShowMessage;

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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStickyEvent(NewActivityEvent newActivityEvent) {
        mTvShowMessage.setText(newActivityEvent.msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
