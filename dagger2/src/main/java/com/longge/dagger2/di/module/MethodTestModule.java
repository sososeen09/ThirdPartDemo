package com.longge.dagger2.di.module;

import com.longge.dagger2.entity.TestEntity;
import com.longge.dagger2.entity.TestEntity2;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suyunlong on 2016/11/1.
 */

@Module
public class MethodTestModule {
    @Provides
    TestEntity provideTestEntity() {
        return new TestEntity("实际是TestEntity");
    }

    @Provides
    TestEntity2 provideTestEntity2(TestEntity testEntity) {
        return new TestEntity2(testEntity);
    }
}
