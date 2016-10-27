package com.longge.dagger2.di.module;

import com.longge.dagger2.entity.ActEntity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suyunlong on 2016/10/27.
 */

@Module
public class ActModule {
    @Provides
    ActEntity getActEntity() {
        return new ActEntity("我是ActEntity");
    }
}
