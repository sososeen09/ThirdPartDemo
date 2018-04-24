package com.longge.retrofit2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.longge.retrofit2.bean.HistoryBean;
import com.longge.retrofit2.net.Api;
import com.longge.retrofit2.net.Repo;
import com.longge.retrofit2.net.TestApi;

import org.reactivestreams.Subscription;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.FlowableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_getRepo)
    Button mBtnGetRepo;
    @BindView(R.id.btn_getHistory)
    Button mBtnGetHistory;

    @BindView(R.id.btn_getRepo_rx)
    Button mBtnGetRepoRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_getRepo, R.id.btn_getRepo_rx, R.id.btn_getRepo_rx2, R.id.btn_getHistory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getRepo:
                getUserRepo("sososeen09");
                break;

            case R.id.btn_getRepo_rx:
                getUserRepoRx("sososeen09");
                break;

            case R.id.btn_getRepo_rx2:
                getUserRepoRx2("sososeen09");
                break;
            case R.id.btn_getHistory:
                getHistory(403, 1);
                break;
        }
    }

    private void getHistory(int id, int quotationType) {
        TestApi.getDefault().getHistory(id, quotationType).enqueue(new Callback<HistoryBean>() {
            @Override
            public void onResponse(Call<HistoryBean> call, Response<HistoryBean> response) {
                if (BuildConfig.DEBUG) Log.d("TAG", "onResponse: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<HistoryBean> call, Throwable t) {
                if (BuildConfig.DEBUG) Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getUserRepo(String name) {
        Api.getDefault().listRepos(name).enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (BuildConfig.DEBUG) Log.d("TAG", "thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                if (BuildConfig.DEBUG) Log.d("TAG", "thread: " + Thread.currentThread().getName());
            }
        });
    }

    private void getUserRepoRx(String name) {
        Api.getDefault().listReposRx(name).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Repo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        if (BuildConfig.DEBUG) Log.d("TAG", repos.toString());
                    }
                });
    }
    private void getUserRepoRx2(String name) {
        Api.getDefault().listReposRx2(name).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<List<Repo>>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        if (BuildConfig.DEBUG) Log.d("TAG", repos.toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
