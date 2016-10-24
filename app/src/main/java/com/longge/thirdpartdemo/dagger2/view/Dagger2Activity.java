package com.longge.thirdpartdemo.dagger2.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.thirdpartdemo.R;
import com.longge.thirdpartdemo.dagger2.di.DaggerInjectComponent;
import com.longge.thirdpartdemo.dagger2.di.DaggerModuleComponent;
import com.longge.thirdpartdemo.dagger2.di.InjectModule;
import com.longge.thirdpartdemo.dagger2.present.DaggerPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dagger2Activity extends AppCompatActivity implements IUserView {

    @BindView(R.id.tv_showName)
    TextView mTvShowName;

    @Inject
    DaggerPresenter mDaggerPresenter;
    @BindView(R.id.btn_inject)
    Button mBtnInject;
    @BindView(R.id.btn_module)
    Button mBtnModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2);
        ButterKnife.bind(this);


    }


    @Override
    public void setUserName(String name) {
        mTvShowName.setText(name);
    }

    @OnClick({R.id.btn_inject, R.id.btn_module})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_inject:
                initInject();
                break;
            case R.id.btn_module:
                initModuleInject();
                break;
        }
    }

    private void initModuleInject() {
        DaggerModuleComponent.builder().injectModule(new InjectModule()).build().inject(this);
        mDaggerPresenter.setUserView(this);
        mDaggerPresenter.getUserName();
    }

    private void initInject() {
        DaggerInjectComponent.builder().build().inject(this);
        mDaggerPresenter.setUserView(this);
        mDaggerPresenter.getUserName();
    }
}
