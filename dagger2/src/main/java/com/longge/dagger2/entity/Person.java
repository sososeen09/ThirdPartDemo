package com.longge.dagger2.entity;

/**
 * Created by suyunlong on 2016/10/26.
 */

public class Person {
    private String sex;

    public Person(String sex) {
        this.sex = sex;
    }

    public Person() {
        sex = "太监";
    }

    public String getSex() {
        return sex;
    }
}
