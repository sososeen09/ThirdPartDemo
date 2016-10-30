package com.longge.dagger2.di.component;

import com.longge.dagger2.di.module.DataModule;
import com.longge.dagger2.ui.SingletonTestActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/27.
 */

@Component(modules = {DataModule.class})
@Singleton//这个Component的@Scope要和对应的Module的@Scope一致
public abstract class SingletonTestComponent {
    /**
     * /@Component不仅可以注解接口也可以注解抽象类，为了方便测试单例，把Component改为抽象类，
     * 实际开发中可以在Application中创建单例。
     */
    public abstract void inject(SingletonTestActivity singletonTestActivity);


    /**
     * SingletonTestComponent必须是单例的，否则怎么能保证不同的Component对象提供同一个依赖实例呢？
     */
    private static SingletonTestComponent sComponent;

    public static SingletonTestComponent getInstance() {
        if (sComponent == null) {
            sComponent = DaggerSingletonTestComponent.builder().build();
        }
        return sComponent;
    }

}
