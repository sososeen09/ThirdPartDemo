package com.longge.dagger2.di.component;

import com.longge.dagger2.di.module.DataModule;
import com.longge.dagger2.ui.PriorityTestActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/27.
 */

@Component(modules = {DataModule.class})
public interface PriorityTestComponent {
    void inject(PriorityTestActivity priorityTestActivity);
}
