package com.longge.dagger2.di.component;

import com.longge.dagger2.di.module.ActModule;
import com.longge.dagger2.di.module.AppComponent;
import com.longge.dagger2.di.scope.PerActivity;
import com.longge.dagger2.ui.DependenceTestActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/27.
 */

@PerActivity
//@Singleton
@Component(dependencies = AppComponent.class, modules = ActModule.class)
public interface ActivityComponent {

    void inject(DependenceTestActivity DependenceTestActivity);

    ActSubComponent getActSubComponent();
}
