package com.longge.dagger2.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.dagger2.R;
import com.longge.dagger2.di.component.DaggerPersonComponent;
import com.longge.dagger2.di.module.DataModule;
import com.longge.dagger2.di.qualifier.PersonQualifier;
import com.longge.dagger2.entity.Person;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModuleTestActivity extends AppCompatActivity {


    @BindView(R.id.tv_showUser)
    TextView mTvShowUser;
    @BindView(R.id.btn_showPerson)
    Button mBtnShowPerson;

    @Inject
    Person mPerson;

    //这么多对象，如果需要特定的对象，用@Qualifier标识符注解，@Named是自定义的一个标识符注解
    @Inject
    @Named("male")
    Person mPersonMale;

    @Inject
    @Named("female")
    Person mPersonFemale;

    @Inject
    @PersonQualifier
    Person mPersonQualifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        ButterKnife.bind(this);
        initInject();
    }


    @OnClick({R.id.tv_showUser, R.id.btn_showPerson, R.id.btn_showPerson1, R.id.btn_showPerson2, R.id.btn_showPerson3})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_showUser:
                break;
            case R.id.btn_showPerson:
                mTvShowUser.setText(mPerson.getSex());
                break;
            case R.id.btn_showPerson1:
                mTvShowUser.setText(mPersonMale.getSex());
                break;
            case R.id.btn_showPerson2:
                mTvShowUser.setText(mPersonFemale.getSex());
                break;
            case R.id.btn_showPerson3:
                mTvShowUser.setText(mPersonQualifier.getSex());
                break;
        }
    }

    private void initInject() {
        DaggerPersonComponent.builder().dataModule(new DataModule()).build().inject(this);
    }
}
