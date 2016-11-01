package com.longge.dagger2.di.component.test;

import com.longge.dagger2.di.module.MethodTestModule;
import com.longge.dagger2.ui.MethodInjectTestActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/11/1.
 */

/**
 * 测试方法注入和provide方法带参
 */
@Component(modules = {MethodTestModule.class})
public interface MethodTestComponent {
    void inject(MethodInjectTestActivity methodInjectTestActivity);
}
