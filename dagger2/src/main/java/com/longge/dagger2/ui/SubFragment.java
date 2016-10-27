package com.longge.dagger2.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longge.dagger2.R;
import com.longge.dagger2.entity.ActEntity;

import javax.inject.Inject;

public class SubFragment extends Fragment {
    @Inject
    ActEntity mActEntity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub, container);
        return view;
    }
}
