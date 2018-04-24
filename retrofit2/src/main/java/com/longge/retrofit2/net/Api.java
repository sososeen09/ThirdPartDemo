package com.longge.retrofit2.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suyunlong on 2016/11/7.
 */

public class Api {

    private static GitHubService mService = null;

    public static GitHubService getDefault() {
        if (mService == null) {
            synchronized (Api.class) {
                if (mService == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.github.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

                    mService = retrofit.create(GitHubService.class);
                }
            }
        }

        return mService;
    }
}
