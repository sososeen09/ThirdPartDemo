package com.longge.thirdpartdemo.dagger2.di;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by suyunlong on 2016/10/25.
 */

@Qualifier
@Retention(RUNTIME)
public @interface QualifierA {
}
