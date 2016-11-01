package com.longge.dagger2.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.longge.dagger2.R;
import com.longge.dagger2.di.component.test.SingletonTestComponent;
import com.longge.dagger2.entity.SingletonTestEntity;
import com.longge.dagger2.util.IntentHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingletonTestActivity extends AppCompatActivity {

    @BindView(R.id.tv_showUser)
    TextView mTvShowUser;


    @Inject
    SingletonTestEntity mSingletonTestEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleton_test);
        ButterKnife.bind(this);
        initInject();
        mTvShowUser.setText(mSingletonTestEntity.getDesc() + ": " + mSingletonTestEntity);
    }

    private void initInject() {
//        DaggerSingletonTestComponent.builder().build().inject(this);
        SingletonTestComponent.getInstance().inject(this);
    }

    @OnClick({R.id.tv_showUser, R.id.btn_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showUser:
                break;
            case R.id.btn_new:
                IntentHelper.startAct(this, SingletonTestActivity.class);
                break;
        }
    }
}
