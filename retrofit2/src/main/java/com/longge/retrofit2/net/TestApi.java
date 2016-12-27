package com.longge.retrofit2.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suyunlong on 2016/12/27.
 */

public class TestApi {
//    http://api.baidao.com/api/hq/mtdata.do?id=403&quotationType=1
private static TestService mService = null;

    public static TestService getDefault() {
        if (mService == null) {
            synchronized (Api.class) {
                if (mService == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://api.baidao.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    mService = retrofit.create(TestService.class);
                }
            }
        }

        return mService;
    }
}
