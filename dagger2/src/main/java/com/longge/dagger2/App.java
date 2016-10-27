package com.longge.dagger2;

import android.app.Application;

import com.longge.dagger2.di.module.AppComponent;
import com.longge.dagger2.di.module.AppModule;
import com.longge.dagger2.di.module.DaggerAppComponent;

/**
 * Created by suyunlong on 2016/10/27.
 */

public class App extends Application {
    private static AppComponent sAppComponent = null;

    @Override
    public void onCreate() {
        super.onCreate();

        if (sAppComponent == null) {
            sAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        }

    }

    public AppComponent getAppComponent() {
        //想外界的依赖提供这个AppComponent
        return sAppComponent;
    }
}
