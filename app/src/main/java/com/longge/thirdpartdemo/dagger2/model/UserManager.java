package com.longge.thirdpartdemo.dagger2.model;

import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by long on 2016/10/24.
 */

public class UserManager {

    private String name;

    @Inject
    public UserManager() {
        Log.d("TAG", "UserManager: ");
    }

    public UserManager(String name) {
        this.name = name;
        Log.d("TAG", "UserManager: " + name);
    }


    public User getUser() {
        if (TextUtils.isEmpty(name)) {
            return new User("default longge");
        } else {
            return new User(name);
        }
    }
}
