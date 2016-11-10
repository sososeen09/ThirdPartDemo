package com.longge.okhttp34;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okio.BufferedSink;

public class OfficialRecipesActivity extends AppCompatActivity {
    @BindView(R.id.btn_get_syn)
    Button mBtnGetSyn;
    @BindView(R.id.btn_get_asyn)
    Button mBtnGetAsyn;
    @BindView(R.id.btn_access_header)
    Button mBtnAccessHeader;
    @BindView(R.id.btn_post_string)
    Button mBtnPostString;
    @BindView(R.id.btn_post_stream)
    Button mBtnPostStream;
    @BindView(R.id.btn_post_file)
    Button mBtnPostFile;
    @BindView(R.id.btn_post_form_param)
    Button mBtnPostFormParam;
    @BindView(R.id.btn_post_multpart)
    Button mBtnPostMultpart;
    @BindView(R.id.btn_parse_json)
    Button mBtnParseJson;
    @BindView(R.id.btn_response_cache)
    Button mBtnResponseCache;
    @BindView(R.id.btn_cancel_call)
    Button mBtnCancelCall;
    @BindView(R.id.btn_time_out)
    Button mBtnTimeOut;
    @BindView(R.id.btn_per_call_config)
    Button mBtnPerCallConfig;
    @BindView(R.id.btn_handle_authen)
    Button mBtnHandleAuthen;

    private final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final Gson gson = new Gson();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    @BindView(R.id.tv_showResult)
    TextView mTvShowResult;

    static class Gist {
        Map<String, GistFile> files;
    }

    static class GistFile {
        String content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_recipes);
        ButterKnife.bind(this);
        setTitle(R.string.official_recipes);

    }

    @OnClick({R.id.btn_get_syn, R.id.btn_get_asyn, R.id.btn_access_header, R.id.btn_post_string, R.id.btn_post_stream, R.id.btn_post_file, R.id
            .btn_post_form_param, R.id.btn_post_multpart, R.id.btn_parse_json, R.id.btn_response_cache, R.id.btn_cancel_call, R.id.btn_time_out, R
            .id.btn_per_call_config, R.id.btn_handle_authen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_syn:
                //同步GET
                getSyn();

                break;
            case R.id.btn_get_asyn:
                //异步GET
                getAsyn();
                break;
            case R.id.btn_access_header:
                addHeader();
                break;
            case R.id.btn_post_string:
                postString();
                break;
            case R.id.btn_post_stream:
                postStream();
                break;
            case R.id.btn_post_file:
                postFile();
                break;
            case R.id.btn_post_form_param:
                postFormParams();
                break;
            case R.id.btn_post_multpart:
                postMultpart();
                break;
            case R.id.btn_parse_json:
                parseJson();
                break;
            case R.id.btn_response_cache:
                responseCache();
                break;
            case R.id.btn_cancel_call:
                cancelCall();
                break;
            case R.id.btn_time_out:
                timeOut();
                break;
            case R.id.btn_per_call_config:
                perCallConfig();
                break;
            case R.id.btn_handle_authen:
                handleAuthen();
                break;
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }


    private void handleAuthen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .authenticator(new Authenticator() {
                            @Override
                            public Request authenticate(Route route, Response response) throws IOException {
                                System.out.println("Authenticating for response: " + response);
                                System.out.println("Challenges: " + response.challenges());
                                String credential = Credentials.basic("jesse", "password1");

                                if (credential.equals(response.request().header("Authorization"))) {
                                    return null; // If we already failed with these credentials, don't retry.
                                }

                                if (responseCount(response) >= 3) {
                                    return null; // If we've failed 3 times, give up.
                                }

                                return response.request().newBuilder()
                                               .header("Authorization", credential)
                                               .build();
                            }
                        })
                        .build();
                Request request = new Request.Builder()
                        .url("http://publicobject.com/secrets/hellosecret.txt")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void perCallConfig() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://httpbin.org/delay/1") // This URL is served with a 1 second delay.
                        .build();

                try {
                    // Copy to customize OkHttp for this request.
                    OkHttpClient copy = client.newBuilder()
                                              .readTimeout(500, TimeUnit.MILLISECONDS)
                                              .build();

                    Response response = copy.newCall(request).execute();
                    System.out.println("Response 1 succeeded: " + response);
                } catch (IOException e) {
                    System.out.println("Response 1 failed: " + e);
                }

                try {
                    // Copy to customize OkHttp for this request.
                    OkHttpClient copy = client.newBuilder()
                                              .readTimeout(3000, TimeUnit.MILLISECONDS)
                                              .build();

                    Response response = copy.newCall(request).execute();
                    System.out.println("Response 2 succeeded: " + response);

                } catch (IOException e) {
                    System.out.println("Response 2 failed: " + e);

                }
            }
        }).start();

    }

    private void timeOut() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    System.out.println("Response completed: " + response);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void cancelCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                        .build();

                final long startNanos = System.nanoTime();
                final Call call = client.newCall(request);

                // Schedule a job to cancel the call in 1 second.
                executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f);
                        call.cancel();
                        System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f);
                    }
                }, 1, TimeUnit.SECONDS);

                try {
                    System.out.printf("%.2f Executing call.%n", (System.nanoTime() - startNanos) / 1e9f);
                    Response response = call.execute();
                    System.out.printf("%.2f Call was expected to fail, but completed: %s%n",
                            (System.nanoTime() - startNanos) / 1e9f, response);
                } catch (IOException e) {
                    System.out.printf("%.2f Call failed as expected: %s%n",
                            (System.nanoTime() - startNanos) / 1e9f, e);
                }
            }
        }).start();


    }

    private void responseCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int cacheSize = 10 * 1024 * 1024; // 10 MiB
                File cacheDirectory = new File(getCacheDir(), "OkHttpCache");
                Cache cache = new Cache(cacheDirectory, cacheSize);

                OkHttpClient client = new OkHttpClient.Builder()
                        .cache(cache)
                        .build();

                Request request = new Request.Builder()
                        .url("http://publicobject.com/helloworld.txt")
                        .build();

                Response response1 = null;
                try {
                    response1 = client.newCall(request).execute();
                    if (!response1.isSuccessful()) throw new IOException("Unexpected code " + response1);

                    String response1Body = response1.body().string();
                    System.out.println("Response 1 response:          " + response1);
                    System.out.println("Response 1 cache response:    " + response1.cacheResponse());
                    System.out.println("Response 1 network response:  " + response1.networkResponse());

                    Response response2 = client.newCall(request).execute();
                    if (!response2.isSuccessful()) throw new IOException("Unexpected code " + response2);

                    String response2Body = response2.body().string();
                    System.out.println("Response 2 response:          " + response2);
                    System.out.println("Response 2 cache response:    " + response2.cacheResponse());
                    System.out.println("Response 2 network response:  " + response2.networkResponse());

                    System.out.println("Response 2 equals Response 1? " + response1Body.equals(response2Body));


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private void parseJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("https://api.github.com/gists/c2a7c39532239ff261be")
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Gist gist = gson.fromJson(response.body().charStream(), Gist.class);

                    for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                        System.out.println(entry.getKey());
                        System.out.println(entry.getValue().content);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }

    private void postMultpart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
// Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", "Square Logo")
                        .addFormDataPart("image", "logo-square.png",
                                RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                        .build();

                Request request = new Request.Builder()
                        .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                        .url("https://api.imgur.com/3/image")
                        .post(requestBody)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postFormParams() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new FormBody.Builder()
                        .add("search", "Jurassic Park")
                        .build();
                Request request = new Request.Builder()
                        .url("https://en.wikipedia.org/w/index.php")
                        .post(formBody)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    System.out.println(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void postFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File("README.md");

                Request request = new Request.Builder()
                        .url("https://api.github.com/markdown/raw")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postStream() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MEDIA_TYPE_MARKDOWN;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        sink.writeUtf8("Numbers\n");
                        sink.writeUtf8("-------\n");
                        for (int i = 2; i <= 997; i++) {
                            sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                        }
                    }

                    private String factor(int n) {
                        for (int i = 2; i < n; i++) {
                            int x = n / i;
                            if (x * i == n) return factor(x) + " × " + i;
                        }
                        return Integer.toString(n);
                    }
                };

                Request request = new Request.Builder()
                        .url("https://api.github.com/markdown/raw")
                        .post(requestBody)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postString() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String postBody = ""
                        + "Releases\n"
                        + "--------\n"
                        + "\n"
                        + " * _1.0_ May 6, 2013\n"
                        + " * _1.1_ June 15, 2013\n"
                        + " * _1.2_ August 11, 2013\n";

                Request request = new Request.Builder()
                        .url("https://api.github.com/markdown/raw")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void addHeader() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("https://api.github.com/repos/square/okhttp/issues")
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Accept", "application/vnd.github.v3+json")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();


                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println("Server: " + response.header("Server"));
                    System.out.println("Date: " + response.header("Date"));
                    System.out.println("Vary: " + response.headers("Vary"));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void getAsyn() {
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();

                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        });
    }


    private void getSyn() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Request request = new Request.Builder()
                        .url("http://publicobject.com/helloworld.txt")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
