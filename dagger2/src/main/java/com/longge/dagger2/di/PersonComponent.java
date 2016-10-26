package com.longge.dagger2.di;

import com.longge.dagger2.ModuleActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/26.
 */

@Component(modules = DataModule.class)
public interface PersonComponent {
    void inject(ModuleActivity moduleActivity);
}
