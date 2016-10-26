package com.longge.dagger2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.dagger2.di.DaggerPersonComponent;
import com.longge.dagger2.di.DataModule;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModuleActivity extends AppCompatActivity {


    @BindView(R.id.tv_showUser)
    TextView mTvShowUser;
    @BindView(R.id.btn_showPerson)
    Button mBtnShowPerson;

    @Inject
    Person mPerson;

    @Inject
    @Named("male")
    Person mPersonMale;

    @Inject
    @Named("female")
    Person mPersonFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        ButterKnife.bind(this);
        initInject();
    }


    @OnClick({R.id.tv_showUser, R.id.btn_showPerson, R.id.btn_showPerson1, R.id.btn_showPerson2})
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
        }
    }

    private void initInject() {
        DaggerPersonComponent.builder().dataModule(new DataModule()).build().inject(this);
    }
}
