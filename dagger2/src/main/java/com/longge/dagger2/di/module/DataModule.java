package com.longge.dagger2.di.module;

import com.longge.dagger2.di.qualifier.PersonQualifier;
import com.longge.dagger2.entity.Person;
import com.longge.dagger2.entity.PriorityTestEntity;
import com.longge.dagger2.entity.SingletonTestEntity;
import com.longge.dagger2.entity.User;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suyunlong on 2016/10/26.
 */

@Module
public class DataModule {

    @Provides
    User provideDefaultUser() {
        return new User();
    }

    @Provides
    Person providePerson() {
        return new Person();
    }

    @Provides
    @Named("male")
//采用@Qualifier注解，表示我可以提供这种标识符的Person
    Person providePersonMale() {
        return new Person("汉子");
    }

    @Provides
    @Named("female")
    Person providePersonFemale() {
        return new Person("妹子");
    }

    @Provides
    @PersonQualifier
    Person providePersonByQualifier() {
        return new Person("qualifier sex");
    }

    @Provides
    PriorityTestEntity providePriorityTestEntity() {
        return new PriorityTestEntity("我是module提供的对象");
    }

    @Provides
    @Singleton
    SingletonTestEntity provideSingletonTestEntity() {
        return new SingletonTestEntity("测试单例");
    }

}
