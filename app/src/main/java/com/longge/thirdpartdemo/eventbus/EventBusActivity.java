package com.longge.thirdpartdemo.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.thirdpartdemo.R;
import com.longge.thirdpartdemo.eventbus.event.LogEvent;
import com.longge.thirdpartdemo.eventbus.event.MessageEvent;
import com.longge.thirdpartdemo.eventbus.event.NewActivityEvent;
import com.longge.thirdpartdemo.eventbus.event.PriorityEvent;
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

    @BindView(R.id.btn_sendSticky)
    Button mBtnSendSticky;
    @BindView(R.id.btn_sendPriority)
    Button mBtnSendPriority;
    @BindView(R.id.btn_sendPriorityWithCancel)
    Button mBtnSendPriorityWithCancel;
    @BindView(R.id.tv_showPriorityResult)
    TextView mTvShowPriorityResult;
    private StringBuilder mSBThreadResult;
    private StringBuilder mSbPriority;

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
        Log.d(message, "onSendMessageInMain: " + Thread.currentThread().getName() + "  time: " + System.currentTimeMillis());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onCustomMessageEvent(MessageEvent messageEvent) {
        mTvShowCustomEventMsg.setText(messageEvent.msg);
    }

    @OnClick({R.id.btn_send, R.id.btn_startAct, R.id.btn_sendCustomEvent, R.id.btn_sendOnMainThread, R.id.btn_sendOnSonThread, R.id.btn_sendSticky
            , R.id.btn_sendPriority,
            R.id.btn_sendPriorityWithCancel, R.id.btn_stickyMemory
    })
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
                mSBThreadResult = new StringBuilder();
                EventBus.getDefault().post("post in main,");
                break;
            case R.id.btn_sendOnSonThread:
                mSBThreadResult = new StringBuilder();
                sonThread();
                break;

            case R.id.btn_sendSticky:
                IntentHelper.startAct(this, EventBus2Activity.class);
                EventBus.getDefault().postSticky(new NewActivityEvent("从第一个Activity中发送的Sticky数据，\n内存中会一直保存最后一个sticky事件，不需要时，最好删除掉"));
                break;
            case R.id.btn_stickyMemory://
                IntentHelper.startAct(this, StickyActivity.class);
                break;

            case R.id.btn_sendPriority:
                //优先级
                mSbPriority = new StringBuilder();
                EventBus.getDefault().post(new PriorityEvent());
                break;
            case R.id.btn_sendPriorityWithCancel:
                //高优先级打断低优先级消息传送
                mSbPriority = new StringBuilder();
                EventBus.getDefault().post(new PriorityEvent(1));
                break;
        }
    }

    public void sonThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post("post in son,");
            }
        }).start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMain(String message) {
        //只有public方法才能收到事件
        mSBThreadResult.append(message).append("onEventMain: ").append(Thread.currentThread().getName()).append("\n");
        Log.d(message, "onEventMain: " + Thread.currentThread().getName() + "  time: " + System.currentTimeMillis());
        EventBus.getDefault().post(new LogEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogEvent(LogEvent logEvent) {
        mTvShowThreadResult.setText(mSBThreadResult.toString());
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPosting(String message) {
        mSBThreadResult.append(message).append("onEventPosting: ").append(Thread.currentThread().getName()).append("\n");
        Log.d(message, "onEventPosting: " + Thread.currentThread().getName() + "  time: " + System.currentTimeMillis());
        EventBus.getDefault().post(new LogEvent());
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackground(String message) {
        mSBThreadResult.append(message).append("onEventBackground: ").append(Thread.currentThread().getName()).append("\n");
        Log.d(message, "onEventBackground: " + Thread.currentThread().getName() + "  time: " + System.currentTimeMillis());
        EventBus.getDefault().post(new LogEvent());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(String message) {
        mSBThreadResult.append(message).append("onEventAsync: ").append(Thread.currentThread().getName()).append("\n");
        Log.d(message, "onEventAsync: " + Thread.currentThread().getName() + "  time: " + System.currentTimeMillis());
        EventBus.getDefault().post(new LogEvent());
    }


    @Subscribe(priority = 2)
    public void onPriority2(PriorityEvent priorityEvent) {
        mSbPriority.append("收到消息 priority: " + 2 + "\n");
        mTvShowPriorityResult.setText(mSbPriority.toString());
        if (priorityEvent.cancelIndex == 2) {
            EventBus.getDefault().cancelEventDelivery(priorityEvent);
        }
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.POSTING)
    public void onPriority1(PriorityEvent priorityEvent) {
        mSbPriority.append("收到消息 priority: " + 1 + "\n");
        mTvShowPriorityResult.setText(mSbPriority.toString());
        if (priorityEvent.cancelIndex == 1) {
            EventBus.getDefault().cancelEventDelivery(priorityEvent);
        }
    }

    @Subscribe()
    public void onPriorityDefault(PriorityEvent priorityEvent) {
        mSbPriority.append("收到消息 priority: " + 0 + "\n");
        mTvShowPriorityResult.setText(mSbPriority.toString());
        if (priorityEvent.cancelIndex == 0) {
            EventBus.getDefault().cancelEventDelivery(priorityEvent);
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        //活动销毁的时候取消注册
        EventBus.getDefault().unregister(this);
    }

}
