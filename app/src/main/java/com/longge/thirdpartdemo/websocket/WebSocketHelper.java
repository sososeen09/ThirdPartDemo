package com.longge.thirdpartdemo.websocket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longge.thirdpartdemo.websocket.bean.ConnectReqBean;
import com.longge.thirdpartdemo.websocket.bean.ConnectResBean;
import com.longge.thirdpartdemo.websocket.bean.EnterLeaveReqBean;
import com.longge.thirdpartdemo.websocket.bean.EnterResBean;
import com.longge.thirdpartdemo.websocket.bean.PingBean;
import com.longge.thirdpartdemo.websocket.bean.Request;
import com.longge.thirdpartdemo.websocket.bean.Response;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
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

    //    private ArrayList<WebSocketListener> mSocketsList = new ArrayList<>();
    private ConcurrentHashMap<RequestType, List<WebSocketListener>> mHashMap = new ConcurrentHashMap<>();
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
                public void onTextMessage(WebSocket websocket, final String text) throws Exception {
                    super.onTextMessage(websocket, text);
                    System.out.println("receive msg: " + text);
//                    Message msg = Message.obtain();
//                    msg.obj = text;
//                    mHandler.sendMessage(msg);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyObservable(text);
                        }
                    });
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean
                        closedByServer) throws Exception {
                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
                    Log.d(TAG, "onDisconnected: closedByServer " + closedByServer);
                    mTimer.cancel();
                    mTimer = null;
                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);
                    Log.d(TAG, "onConnected: ");
                }

                @Override
                public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
                    super.onStateChanged(websocket, newState);
                    Log.d(TAG, "onStateChanged: " + newState.name());
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
        Gson gson = new Gson();
        Type type = null;
        if (text.contains(RequestType.CONNECT.getRequestType())) {
            //连接返回结果
            type = new TypeToken<Response<ConnectResBean>>() {
            }.getType();
            Response<ConnectResBean> fromJson = gson.fromJson(text, type);
            for (WebSocketListener webSocketListener : mHashMap.get(RequestType.CONNECT)) {
                webSocketListener.onResponse(fromJson);
            }
        } else if (text.contains(RequestType.WCST_ENTER.getRequestType())) {
            //进入直播间
            type = new TypeToken<Response<EnterResBean>>() {
            }.getType();

            Response<EnterResBean> fromJson = gson.fromJson(text, type);
            for (WebSocketListener webSocketListener : mHashMap.get(RequestType.WCST_ENTER)) {
                webSocketListener.onResponse(fromJson);
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


    /**
     * 进入直播间
     */
    public void enter(final String id, final String roomId) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                EnterLeaveReqBean enterLeaveReqBean = new EnterLeaveReqBean();
                enterLeaveReqBean.roomId = roomId;
                Request<EnterLeaveReqBean> enterReqBeanRequest = new Request<>();
                enterReqBeanRequest.id = id;
                enterReqBeanRequest.type = RequestType.WCST_ENTER.getRequestType();
                enterReqBeanRequest.payload = enterLeaveReqBean;
                String message = new Gson().toJson(enterReqBeanRequest);
                mSocket.sendText(message);
//                mSocket.sendText("{\n" +
//                        "    \"id\": \"2\",\n" +
//                        "    \"payload\": {\n" +
//                        "        \"roomId\": \"2\"\n" +
//                        "    },\n" +
//                        "    \"type\": \"WCST_ENTER\"\n" +
//                        "}");
            }
        });
    }

    public void leave(String id, String roomId) {
        EnterLeaveReqBean enterLeaveReqBean = new EnterLeaveReqBean();
        enterLeaveReqBean.roomId = roomId;
        Request<EnterLeaveReqBean> enterReqBeanRequest = new Request<>();
        enterReqBeanRequest.id = id;
        enterReqBeanRequest.type = RequestType.WCST_LEAVE.getRequestType();
        enterReqBeanRequest.payload = enterLeaveReqBean;
        String message = new Gson().toJson(enterReqBeanRequest);
        mSocket.sendText(message);
//        mSocket.sendText("{{\n" +
//                "  \"type\":\"WCST_LEAVE\",\n" +
//                "  \"payload\":{\n" +
//                "    \"roomId\":\"1\"\n" +
//                "  },\n" +
//                "  \"id\":\"1\"\n" +
//                "}");
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
                sendPing(10_000L);
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
        connectReqBeanRequest.payload = connectReqBean;
        Gson gson = new Gson();
        return gson.toJson(connectReqBeanRequest);
    }

//    public void addWebSocketListener(WebSocketListener listener) {
//
//        if (!mSocketsList.contains(listener)) {
//            mSocketsList.add(listener);
//        }
//
//        else {
//            throw new IllegalStateException("has added this WebSocketListener: " + listener.toString());
//        }
//    }

    public void addWebSocketListener(RequestType requestType, WebSocketListener listener) {
        List<WebSocketListener> listenerList = mHashMap.get(requestType);
        if (listenerList == null) {
            listenerList = new ArrayList<>();
        }

        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
        mHashMap.put(requestType, listenerList);

    }

//    public void removeWebSocketListener(WebSocketListener listener) {
//        if (mSocketsList.contains(listener)) {
//            mSocketsList.remove(listener);
//        }

//        else {
//            throw new IllegalStateException("has not add this WebSocketListener: " + listener.toString());
//        }
//    }

    public void removeWebSocketListener(RequestType requestType, WebSocketListener listener) {
        List<WebSocketListener> listenerList = mHashMap.get(requestType);


        if (listenerList != null && listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }


    public void sendPing(Long times) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                PingBean pingBean = new PingBean();
                Request<PingBean> stringRequest = new Request<>();
                stringRequest.type = RequestType.PING.getRequestType();
                stringRequest.payload = pingBean;
                stringRequest.id = String.valueOf(System.currentTimeMillis());
                String gsonString = GsonTools.createGsonString(stringRequest);
                mSocket.sendText(gsonString);
//                mSocket.sendText("{\n" +
//                        "  \"type\":\"PING\",\n" +
//                        "  \"payload\":{},\n" +
//                        "  \"id\":\"1\"\n" +
//                        "}");
                Log.d(TAG, "run: sendPing");
            }
        }, times, times);
    }


    interface WebSocketListener<T> {
        void onResponse(Response<T> t);

        void onFailed(int code, Throwable throwable);
    }
}

