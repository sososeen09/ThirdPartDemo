package com.longge.dagger2.di.component;

import com.longge.dagger2.di.module.ActModule;
import com.longge.dagger2.di.scope.PerActivity;
import com.longge.dagger2.ui.DependenceTestActivity;
import com.longge.dagger2.ui.SubComponentTestActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/27.
 */

@PerActivity
//@Singleton //不能与依赖的AppComponent的作用域相同，否则会报错
@Component(dependencies = AppComponent.class, modules = ActModule.class)
public interface ActivityComponent {

    void inject(DependenceTestActivity DependenceTestActivity);

    void inject(SubComponentTestActivity subComponentTestActivity);

    //包含SubComponent,这样的话该SubComponent也可以拿到ActivityComponent中能提供的依赖。
    ActSubComponent getActSubComponent();
}
