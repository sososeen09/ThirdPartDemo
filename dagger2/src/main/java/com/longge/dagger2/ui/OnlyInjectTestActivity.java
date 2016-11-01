package com.longge.dagger2.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.dagger2.R;
import com.longge.dagger2.di.component.test.DaggerOnlyInjectComponent;
import com.longge.dagger2.entity.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnlyInjectTestActivity extends AppCompatActivity {

    @Inject //在目标类中@Inject标记表示我需要这个类型的依赖
            User mUser;

    @BindView(R.id.tv_showUser)
    TextView mTvShowUser;
    @BindView(R.id.btn_showUser)
    Button mBtnShowUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_inject);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_showUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_showUser:
                initInject();
                mTvShowUser.setText(mUser.getName());
                break;
            case R.id.btn_showUserNoInject:
                mTvShowUser.setText(mUser.getName());
                break;
        }
    }

    private void initInject() {
        DaggerOnlyInjectComponent.builder().build().inject(this);
//        也可以直接用create()方法
//        DaggerOnlyInjectComponent.create().inject(this);
    }
}
