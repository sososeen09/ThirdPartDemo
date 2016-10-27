package com.longge.dagger2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.dagger2.di.DaggerPriorityTestComponent;
import com.longge.dagger2.entity.PriorityTestEntity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PriorityTestActivity extends AppCompatActivity {

    @BindView(R.id.tv_showUser)
    TextView mTvShowUser;
    @BindView(R.id.btn_showUser)
    Button mBtnShowUser;

    @Inject
    PriorityTestEntity mPriorityTestEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prority);
        ButterKnife.bind(this);
        initInject();
    }

    private void initInject() {
//        DaggerPriorityTestComponent.builder().build().inject(this);
        DaggerPriorityTestComponent.create().inject(this);
    }

    @OnClick({R.id.tv_showUser, R.id.btn_showUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showUser:
                break;
            case R.id.btn_showUser:
                mTvShowUser.setText(mPriorityTestEntity.getName());
                break;
        }
    }
}
