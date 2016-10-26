package com.longge.dagger2.di;

import com.longge.dagger2.Person;
import com.longge.dagger2.User;

import javax.inject.Named;

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
    Person providePersonMale() {
        return new Person("汉子");
    }

    @Provides
    @Named("female")
    Person providePersonFemale() {
        return new Person("妹子");
    }
}
