package com.longge.dagger2.di;

import com.longge.dagger2.PriorityTestActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/27.
 */

@Component(modules = {DataModule.class})
public interface PriorityTestComponent {
    void inject(PriorityTestActivity priorityTestActivity);
}
