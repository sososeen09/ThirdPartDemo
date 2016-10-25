package com.longge.thirdpartdemo.dagger2.di;

import com.longge.thirdpartdemo.dagger2.view.Dagger2Activity;

import dagger.Component;

/**
 * Created by long on 2016/10/24.
 */

@Component(modules = {InjectModule.class})
public interface InjectComponent {
    void inject(Dagger2Activity dagger2Activity);
}
