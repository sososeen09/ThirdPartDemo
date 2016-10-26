package com.longge.dagger2;

import javax.inject.Inject;

/**
 * Created by suyunlong on 2016/10/26.
 */

public class User {
    public String name;
    //用这个@Inject表示来表示我可以提供依赖，@Inject只能标记一个构造方法，否则不知道根据哪个构造创建实例。
    @Inject
    public User() {
        name = "sososeen09";
    }


// 用@Inject标记的构造函数如果有参数，那么这个参数也需要其它地方提供依赖。
// 但是@Inject有一个缺陷，就是对于第三方的类无能为力。因为我们不能修改第三方的构造函数，
// 所以对于String还有其他的一些我们不能修改的类，只能用@Module中的@Provides来提供实例了
//    @Inject
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
