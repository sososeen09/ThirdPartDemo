package com.longge.dagger2.di.component;

import com.longge.dagger2.di.module.DataModule;
import com.longge.dagger2.ui.ModuleActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/26.
 */

@Component(modules = DataModule.class)
public interface PersonComponent {
    void inject(ModuleActivity moduleActivity);
}
