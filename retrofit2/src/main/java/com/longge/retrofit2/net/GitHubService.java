package com.longge.retrofit2.net;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by suyunlong on 2016/11/7.
 */

public interface GitHubService {
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<List<Repo>> listReposRx(@Path("user") String user);


    @GET("users/{user}/repos")
    Flowable<List<Repo>> listReposRx2(@Path("user") String user);


}
