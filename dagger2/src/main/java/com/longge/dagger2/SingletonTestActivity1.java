package com.longge.dagger2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.dagger2.di.DaggerSingletonTestComponent;
import com.longge.dagger2.entity.SingletonTestEntity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingletonTestActivity1 extends AppCompatActivity {
    @BindView(R.id.tv_showUser)
    TextView mTvShowUser;
    @BindView(R.id.btn_showUser)
    Button mBtnShowUser;

    @Inject
    SingletonTestEntity mSingletonTestEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleton_test);
        ButterKnife.bind(this);
        initInject();
    }

    private void initInject() {
        DaggerSingletonTestComponent.builder().build().inject(this);
    }

    @OnClick({R.id.tv_showUser, R.id.btn_showUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showUser:
                break;
            case R.id.btn_showUser:
                mTvShowUser.setText(mSingletonTestEntity.getDesc() + ": " + mSingletonTestEntity);
                break;
        }
    }
}
