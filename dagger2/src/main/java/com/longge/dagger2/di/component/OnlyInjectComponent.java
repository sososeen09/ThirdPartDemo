package com.longge.dagger2.di.component;

import com.longge.dagger2.ui.OnlyInjectTestActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/26.
 */

//没有modules和dependencies的情况下，纯粹用@Inject来提供依赖
@Component()
public interface OnlyInjectComponent {
    //必须有个目标让Component知道需要往哪个类中注入
    void inject(OnlyInjectTestActivity onlyInjectTestActivity);
}
