package com.longge.thirdpartdemo.dagger2.di;

import android.util.Log;

import com.longge.thirdpartdemo.dagger2.model.UserManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/10/24.
 */

@Module
public class InjectModule {
    @Provides
    UserManager provideUserManager() {
        Log.d("TAG", "provideUserManager: ");
        return new UserManager("custom name SylRocky");
    }


    @Provides
    @Named("UserManagerNamed")
    UserManager provideUserManagerNamed() {
        Log.d("TAG", "provideUserManagerNamed: ");
        return new UserManager("QualifierNamed name sososeen09");
    }

    @Provides
    @QualifierA
    UserManager provideUserManagerA() {
        Log.d("TAG", "provideUserManagerNamed: ");
        return new UserManager("QualifierA name sososeen09");
    }


}
