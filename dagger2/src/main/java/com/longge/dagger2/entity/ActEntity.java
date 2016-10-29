package com.longge.dagger2.entity;

import javax.inject.Inject;

/**
 * Created by suyunlong on 2016/10/27.
 */

public class ActEntity {
    private String desc;

    @Inject
    public ActEntity(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
