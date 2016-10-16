package com.longge.thirdpartdemo.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.longge.thirdpartdemo.R;
import com.longge.thirdpartdemo.eventbus.event.StickyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StickyActivity extends AppCompatActivity {

    @BindView(R.id.btn_sticky1)
    Button mBtnSticky1;
    @BindView(R.id.btn_sticky2)
    Button mBtnSticky2;
    @BindView(R.id.btn_sticky3)
    Button mBtnSticky3;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;
    @BindView(R.id.tv_showMessage)
    TextView mTvShowMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_sticky1, R.id.btn_sticky2, R.id.btn_sticky3, R.id.btn_regist, R.id.tv_showMessage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sticky1:
                EventBus.getDefault().postSticky(new StickyEvent("粘性事件1"));
                Toast.makeText(this, "发送粘性事件1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_sticky2:
                EventBus.getDefault().postSticky(new StickyEvent("粘性事件2"));
                Toast.makeText(this, "发送粘性事件2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_sticky3:
                EventBus.getDefault().postSticky(new StickyEvent("粘性事件3"));
                Toast.makeText(this, "发送粘性事件3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_regist:
                EventBus.getDefault().register(this);
                break;
        }
    }

    @Subscribe(sticky = true)
    public void onStickyEvent(StickyEvent stickyEvent) {
        mTvShowMessage.setText("接收到事件：" + stickyEvent.msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
