package com.longge.thirdpartdemo.websocket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yunlong.su on 2017/1/9.
 */

public class WebSocketHelper {
    private static final String TAG = WebSocketHelper.class.getSimpleName();
    private static WebSocketHelper sWebSocketHelper = null;
    private WebSocket mSocket;
    private final String WEB_SOCKET_BASE = "ws://test.wpwebsocket.baidao.com";

    private ArrayList<WebSocketListener> mSocketsList = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            notifyObservable((String) msg.obj);
        }
    };
//    private Executor mExecutor;

    ExecutorService mExecutorService = Executors.newFixedThreadPool(5);
    private Timer mTimer;

    private WebSocketHelper() {
        try {
            mSocket = new WebSocketFactory().createSocket(WEB_SOCKET_BASE);
            mSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);

                    Message msg = Message.obtain();
                    msg.obj = text;
                    mHandler.sendMessage(msg);
                }


            });
        } catch (IOException e) {
            e.printStackTrace();
        }

//        mExecutor = new MainThreadExecutor();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    private void notifyObservable(String text) {

        if (text.contains(RequestType.CONNECT.getRequestType())) {
            //连接返回结果
//            Response<ConnectResBean> response = GsonTools.changeGsonToBean(text, Response.class);

            Gson gson = new Gson();
            Type type = new TypeToken<Response<ConnectResBean>>() {
            }.getType();
            Response<ConnectResBean> fromJson = gson.fromJson(text, type);
            for (WebSocketListener webSocketListener : mSocketsList) {
                webSocketListener.onTextMessage(fromJson);
            }
        }
    }

    public static WebSocketHelper getInstance() {
        if (sWebSocketHelper == null) {
            synchronized (WebSocketHelper.class) {
                if (sWebSocketHelper == null) {
                    sWebSocketHelper = new WebSocketHelper();
                }
            }
        }

        return sWebSocketHelper;
    }

    public void connect(final String id, final String token, final String preSid) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket.connect();
                } catch (WebSocketException e) {
                    e.printStackTrace();
                }

                String session = createSession(id, token, preSid);
                mSocket.sendText(session);
                sendPing(30_000L);
            }
        });
    }

    /**
     * 创建会话
     */
    private String createSession(String id, String token, String preSid) {
        ConnectReqBean connectReqBean = new ConnectReqBean();
        connectReqBean.preSid = preSid;
        connectReqBean.token = token;
        Request<ConnectReqBean> connectReqBeanRequest = new Request<>();
        connectReqBeanRequest.id = id;
        connectReqBeanRequest.type = RequestType.CONNECT.getRequestType();
        Gson gson = new Gson();
        return gson.toJson(connectReqBeanRequest);
    }

    public void addWebSocketListener(WebSocketListener listener) {

        if (!mSocketsList.contains(listener)) {
            mSocketsList.add(listener);
        } else {
            throw new IllegalStateException("has added this WebSocketListener: " + listener.toString());
        }
    }

    public void removeWebSocketListener(WebSocketListener listener) {
        if (mSocketsList.contains(listener)) {
            mSocketsList.remove(listener);
        } else {
            throw new IllegalStateException("has not add this WebSocketListener: " + listener.toString());
        }
    }

    public Executor defaultCallbackExecutor() {
        return new MainThreadExecutor();
    }

    public void sendPing(Long times) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Request<String> stringRequest = new Request<>();
                stringRequest.type = RequestType.PING.getRequestType();
                stringRequest.payLoad = "";
                stringRequest.id = String.valueOf(System.currentTimeMillis());
                mSocket.sendText(GsonTools.createGsonString(stringRequest));
                Log.d(TAG, "run: sendPing");
            }
        }, 0, times);
    }

    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r) {
            handler.post(r);
        }
    }

    interface WebSocketListener<T> {
        void onTextMessage(Response<T> t);
    }
}

