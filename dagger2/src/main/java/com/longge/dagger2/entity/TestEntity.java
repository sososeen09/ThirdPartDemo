package com.longge.dagger2.entity;

import javax.inject.Inject;

/**
 * Created by suyunlong on 2016/10/29.
 */

public class TestEntity {
    public String desc;

    public TestEntity() {
    }
    /**
     * 只要被@Inject标记编译后就会生成一个类{@link TestEntity_Factory},并且如果有带参的构造的话就会生成一个方法
     * public static Factory<TestEntity> create(Provider<String> descProvider)
     * 表示需要其它的地方提供这个参数。
     */
    @Inject
    public TestEntity(String desc) {
        this.desc = desc;
    }
}
