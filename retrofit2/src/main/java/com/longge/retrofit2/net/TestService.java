package com.longge.retrofit2.net;

import com.longge.retrofit2.bean.HistoryBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by suyunlong on 2016/12/27.
 */

public interface TestService {
    //    http://api.baidao.com/api/hq/mtdata.do?id=403&quotationType=1
    @GET("api/hq/mtdata.do")
    Call<HistoryBean> getHistory(@Query("id") int id, @Query("quotationType") int quotationType);
}
