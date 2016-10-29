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

    /**
     * Exposed to sub-graphs.
     * 其他的依赖想要用这个Context，必须显示的暴露。
     * 因为，其它依赖这个的Component需要Context，然后这个Context会去AppModule中找对应的Context
     * 与方法名无关，只与返回类型有关
     * 举个例子：小弟B依赖大哥A,A有一把杀猪刀。哪天小弟碰上事了，找大哥借一把刀，
     * 如果大哥把刀藏起来不给小弟用，小弟会因为找不到刀用很崩溃的。（程序编译报错），
     * 所以必须是大哥把刀拿出来给小弟用，小弟才能拿出去砍树啊。（代码正常）
     *
     */
    Context context();
}
