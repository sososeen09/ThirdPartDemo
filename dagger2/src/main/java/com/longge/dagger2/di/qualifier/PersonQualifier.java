package com.longge.dagger2.di.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by suyunlong on 2016/10/26.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface PersonQualifier {
}
