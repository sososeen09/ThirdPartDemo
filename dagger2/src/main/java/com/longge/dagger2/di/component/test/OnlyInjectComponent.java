package com.longge.dagger2.di.component.test;

import com.longge.dagger2.ui.OnlyInjectTestActivity;

import dagger.Component;

/**
 * Created by suyunlong on 2016/10/26.
 */

/**
 * 没有modules和dependencies的情况下，纯粹用@Inject来提供依赖
 */
@Component()
public interface OnlyInjectComponent {
    /**
     * 必须有个目标让Component知道需要往哪个类中注入
     * 这个方法名可以是其它的，但是推荐用inject
     * 目标类OnlyInjectTestActivity必须精确，不能用父类
     * 这是Dagger2的机制决定的
     */
    void inject(OnlyInjectTestActivity onlyInjectTestActivity);
}
