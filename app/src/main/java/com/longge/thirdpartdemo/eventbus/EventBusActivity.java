package com.longge.thirdpartdemo.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longge.thirdpartdemo.R;
import com.longge.thirdpartdemo.eventbus.event.MessageEvent;
import com.longge.thirdpartdemo.eventbus.event.NewActivityEvent;
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
    @BindView(R.id.btn_sendOnMainThread)
    Button mBtnSendOnMainThread;
    @BindView(R.id.btn_sendOnSonThread)
    Button mBtnSendOnSonThread;
    @BindView(R.id.tv_showThreadResult)
    TextView mTvShowThreadResult;
    @BindView(R.id.activity_event_bus)
    LinearLayout mActivityEventBus;
    @BindView(R.id.btn_sendSticky)
    Button mBtnSendSticky;
    private StringBuilder mSB;

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

    @OnClick({R.id.btn_send, R.id.btn_startAct, R.id.btn_sendCustomEvent, R.id.btn_sendOnMainThread, R.id.btn_sendOnSonThread, R.id.btn_sendSticky})
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
            case R.id.btn_sendOnMainThread:
                mSB = new StringBuilder();
                EventBus.getDefault().post("send from MainThread: ");
                break;
            case R.id.btn_sendOnSonThread:
                mSB = new StringBuilder();
                sonThread();
                break;

            case R.id.btn_sendSticky:
                IntentHelper.startAct(this, EventBus2Activity.class);
                EventBus.getDefault().postSticky(new NewActivityEvent("从第一个Activity中发送的Sticky数据"));
                break;
        }
    }

    public void sonThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post("send from SonThread: ");
            }
        }).start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMain(String message) {
        //只有public方法才能收到事件
        mSB.append(message).append("main").append(Thread.currentThread().getName()).append("\n");
//        mTvShowThreadResult.setText(mSB.toString());
        Log.d(message, "onEventMain: " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPosting(String message) {
        mSB.append(message).append("posting").append(Thread.currentThread().getName()).append("\n");
//        mTvShowThreadResult.setText(mSB.toString());
        Log.d(message, "onEventPosting: " + Thread.currentThread().getName());
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackground(String message) {
        mSB.append(message).append("background").append(Thread.currentThread().getName()).append("\n");
//        mTvShowThreadResult.setText(mSB.toString());
        Log.d(message, "onEventBackground: " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(String message) {
        mSB.append(message).append("async").append(Thread.currentThread().getName()).append("\n");
//        mTvShowThreadResult.setText(mSB.toString());
        Log.d(message, "onEventAsync: " + Thread.currentThread().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //活动销毁的时候取消注册
        EventBus.getDefault().unregister(this);
    }

}
