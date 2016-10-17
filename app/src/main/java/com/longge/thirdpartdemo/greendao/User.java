package com.longge.thirdpartdemo.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by long on 2016/10/17.
 */

@Entity(nameInDb = "user")
public class User {

    @Id
    private int id;
    @Property(nameInDb = "name")
    private String name;
    private String sex;
    @Generated(hash = 1803096763)
    public User(int id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
}
