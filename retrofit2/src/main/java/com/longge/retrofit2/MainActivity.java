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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_getRepo)
    Button mBtnGetRepo;
    @BindView(R.id.btn_getHistory)
    Button mBtnGetHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_getRepo, R.id.btn_getHistory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getRepo:
                getUserRepo("sososeen09");
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
}
