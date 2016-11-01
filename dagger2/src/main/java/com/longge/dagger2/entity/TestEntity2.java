package com.longge.dagger2.entity;

/**
 * Created by suyunlong on 2016/10/29.
 */

public class TestEntity2 {
    public String desc;

    /**
     * TestEntity2对象依赖TestEntity,TestEntity2把TestEntity包装一下
     * @param testEntity
     */
    public TestEntity2(TestEntity testEntity) {
        this.desc = testEntity.desc;
    }
}
