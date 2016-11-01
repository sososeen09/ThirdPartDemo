package com.longge.dagger2.di.component.test;

import com.longge.dagger2.di.component.ActivityComponent;
import com.longge.dagger2.di.module.ActModule;
import com.longge.dagger2.di.module.AppModule;
import com.longge.dagger2.ui.ExtendTestActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/28.
 */

/**
 * ExtendTestComponent继承了ActivityComponent，如果ActivityComponent中的modules定义了创建实例的方法，
 * ExtendTestComponent中也必须提供相应的modules。
 */
@Singleton
@Component(modules = {ActModule.class, AppModule.class})
public interface ExtendTestComponent extends ActivityComponent {
    void inject(ExtendTestActivity extendTestActivity);
}
