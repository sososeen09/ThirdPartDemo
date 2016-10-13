package com.longge.thirdpartdemo.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.thirdpartdemo.R;
import com.longge.thirdpartdemo.util.IntentHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBusActivity extends AppCompatActivity {

    @BindView(R.id.tv_showMessage)
    TextView mTvShowMessage;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.btn_startAct)
    Button mBtnStartAct;
    @BindView(R.id.tv_showCustomEventMsg)
    TextView mTvShowCustomEventMsg;
    @BindView(R.id.btn_sendCustomEvent)
    Button mBtnSendCustomEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        ButterKnife.bind(this);
        //在onCreate的时候注册
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendMessage(String message) {
        mTvShowMessage.setText(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onCustomMessageEvent(MessageEvent messageEvent) {
        mTvShowCustomEventMsg.setText(messageEvent.msg);
    }

    @OnClick({R.id.btn_send, R.id.btn_startAct, R.id.btn_sendCustomEvent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                EventBus.getDefault().post("本页中发送的消息");
                break;
            case R.id.btn_startAct:
                IntentHelper.startAct(this, EventBus2Activity.class);
                break;
            case R.id.btn_sendCustomEvent:
                EventBus.getDefault().post(new MessageEvent("本页中发出的自定义事件的消息"));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //活动销毁的时候取消注册
        EventBus.getDefault().unregister(this);
    }

}
