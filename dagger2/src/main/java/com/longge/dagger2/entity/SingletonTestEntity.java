package com.longge.dagger2.entity;

import javax.inject.Inject;

/**
 * Created by suyunlong on 2016/10/27.
 */

public class SingletonTestEntity {
    private String desc;

    @Inject
    public SingletonTestEntity(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
