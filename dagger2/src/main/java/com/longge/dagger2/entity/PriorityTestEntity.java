package com.longge.dagger2.entity;

import javax.inject.Inject;

/**
 * Created by suyunlong on 2016/10/27.
 */

public class PriorityTestEntity {

    private String name;

    @Inject
    public PriorityTestEntity() {
        name = "我是@Inject注解提供的对象";
    }


    public PriorityTestEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
