package com.longge.dagger2.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.longge.dagger2.R;
import com.longge.dagger2.entity.ActEntity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubFragment extends Fragment {
    @Inject
    ActEntity mActEntity;

    @Inject
    Context mContext;

    @BindView(R.id.btn_showToast)
    Button mBtnShowToast;

    public SubFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initInject();
    }

    private void initInject() {
        ((SubComponentTestActivity) getActivity()).getActivityComponent().getActSubComponent().inject(this);
    }

    @OnClick(R.id.btn_showToast)
    public void onClick() {
        Toast.makeText(mContext, mActEntity.getDesc() + "\nContext: " + mContext, Toast.LENGTH_LONG).show();
    }
}
