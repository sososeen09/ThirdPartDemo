package com.longge.dagger2.di.component;

import android.content.Context;

import com.longge.dagger2.di.module.AppModule;
import com.longge.dagger2.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/27.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
//其他的依赖想要用这个Context，必须显示的暴露
    Context context();
}
