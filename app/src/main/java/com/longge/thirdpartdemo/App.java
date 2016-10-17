package com.longge.thirdpartdemo;

import android.app.Application;

import com.longge.thirdpartdemo.greendao.DaoMaster;
import com.longge.thirdpartdemo.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by long on 2016/10/17.
 */

public class App extends Application {

    private static DaoSession sDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "changeName-db", null);
        Database database = devOpenHelper.getWritableDb();
        sDaoSession =  new DaoMaster(database).newSession();
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
