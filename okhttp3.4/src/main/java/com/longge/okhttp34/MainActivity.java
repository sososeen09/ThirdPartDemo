package com.longge.okhttp34;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.btn_get)
    Button mBtnGet;
    @BindView(R.id.btn_post)
    Button mBtnPost;
    @BindView(R.id.btn_upload)
    Button mBtnUpload;
    @BindView(R.id.btn_download)
    Button mBtnDownload;
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initClient();
    }

    private void initClient() {
        mOkHttpClient = new OkHttpClient.Builder().build();
    }

    @OnClick({R.id.btn_get, R.id.btn_post, R.id.btn_upload, R.id.btn_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                getSomeThing();
                break;
            case R.id.btn_post:
                break;
            case R.id.btn_upload:
                break;
            case R.id.btn_download:
                downloadSome();
                break;
        }
    }

    private void downloadSome() {
        Request request = new Request.Builder().url("http://zhuangbi.idagou.com/i/2016-11-09-8f82d47b6f41da76fa253775fab506e3.jpg").build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long startTime = System.currentTimeMillis();
                saveFile(response);
                Log.d(TAG, "timeUsed: " + (System.currentTimeMillis() - startTime));
            }
        });
    }

    private void saveFile(Response response) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            File file = new File(SDPath, "tracump.jpg");
            fos = new FileOutputStream(file);
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                if (BuildConfig.DEBUG) Log.d(TAG, "progress=" + progress);
            }
            fos.flush();
            Log.d(TAG, "文件下载成功");
        } catch (Exception e) {
            Log.d(TAG, "文件下载失败");
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }

    private void getSomeThing() {

    }
}
