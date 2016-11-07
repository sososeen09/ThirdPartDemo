package com.longge.retrofit2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.longge.retrofit2.net.Api;
import com.longge.retrofit2.net.Repo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_getRepo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getRepo:
                getUserRepo("sososeen09");
                break;
        }
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
