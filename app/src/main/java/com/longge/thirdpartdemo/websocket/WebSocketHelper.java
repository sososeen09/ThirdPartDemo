package com.longge.thirdpartdemo.websocket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longge.thirdpartdemo.websocket.bean.AudienceCounterBean;
import com.longge.thirdpartdemo.websocket.bean.ChatBean;
import com.longge.thirdpartdemo.websocket.bean.ConnectReqBean;
import com.longge.thirdpartdemo.websocket.bean.ConnectResBean;
import com.longge.thirdpartdemo.websocket.bean.EmptyBean;
import com.longge.thirdpartdemo.websocket.bean.EnterLeaveReqBean;
import com.longge.thirdpartdemo.websocket.bean.MsgResBean;
import com.longge.thirdpartdemo.websocket.bean.NewAudienceBean;
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
            callbackCall(RequestType.CONNECT, fromJson);
        } else if (text.contains(RequestType.WCST_ENTER.getRequestType())) {
            //进入直播间
            type = new TypeToken<Response<EmptyBean>>() {
            }.getType();

            Response<EmptyBean> fromJson = gson.fromJson(text, type);
            callbackCall(RequestType.WCST_ENTER, fromJson);
        } else if (text.contains(RequestType.WCST_LEAVE.getRequestType())) {
            //离开直播间
            type = new TypeToken<Response<String>>() {
            }.getType();

            Response<String> fromJson = gson.fromJson(text, type);
            callbackCall(RequestType.WCST_LEAVE, fromJson);
        } else if (text.contains(RequestType.WCST_NEW_AUDIENCE.getRequestType())) {
            //TODO 待测试 用户进入直播室通知
            type = new TypeToken<Response<NewAudienceBean>>() {
            }.getType();

            Response<NewAudienceBean> fromJson = gson.fromJson(text, type);
            callbackCall(RequestType.WCST_NEW_AUDIENCE, fromJson);
        } else if (text.contains(RequestType.WCST_CHAT.getRequestType())) {
            //TODO 发送互动消息结果
            type = new TypeToken<Response<EmptyBean>>() {
            }.getType();

            Response<EmptyBean> fromJson = gson.fromJson(text, type);
            callbackCall(RequestType.WCST_CHAT, fromJson);
        } else if (text.contains(RequestType.WCST_MSG.getRequestType())) {
            //TODO 直播室消息通知
            type = new TypeToken<Response<MsgResBean>>() {
            }.getType();

            Response<MsgResBean> fromJson = gson.fromJson(text, type);
            callbackCall(RequestType.WCST_MSG, fromJson);
        } else if (text.contains(RequestType.WCST_AUDIENCES.getRequestType())) {
            //TODO 直播室人数变更消息通知
            type = new TypeToken<Response<AudienceCounterBean>>() {
            }.getType();

            Response<AudienceCounterBean> fromJson = gson.fromJson(text, type);
            callbackCall(RequestType.WCST_AUDIENCES, fromJson);
        }
    }

    private void callbackCall(RequestType requestType, Response fromJson) {
        List<WebSocketListener> webSocketListeners = mHashMap.get(requestType);
        if (webSocketListeners != null) {
            for (WebSocketListener webSocketListener : webSocketListeners) {
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

    public void leave(final String id, final String roomId) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
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
        });

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
                EmptyBean emptyBean = new EmptyBean();
                Request<EmptyBean> stringRequest = new Request<>();
                stringRequest.type = RequestType.PING.getRequestType();
                stringRequest.payload = emptyBean;
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

    public void disConnect(final String id) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                EmptyBean emptyBean = new EmptyBean();
                Request<EmptyBean> stringRequest = new Request<>();
                stringRequest.type = RequestType.DISCONNECT.getRequestType();
                stringRequest.payload = emptyBean;
                stringRequest.id = id;
                String gsonString = GsonTools.createGsonString(stringRequest);
//        mSocket.sendText(gsonString);
                mSocket.sendText("{\n" +
                        "  \"type\":\"DISCONNECT\",\n" +
                        "  \"payload\":{\n" +
                        "  },\n" +
                        "  \"id\":\"1\"\n" +
                        "}");
            }
        });

    }

    public void send(final String id, final String roomId, final String content) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                ChatBean chatBean = new ChatBean();
                chatBean.content = content;
                chatBean.roomId = roomId;
                Request<ChatBean> chatBeanRequest = new Request<>();
                chatBeanRequest.id = id;
                chatBeanRequest.type = RequestType.WCST_CHAT.getRequestType();
                chatBeanRequest.payload = chatBean;

                mSocket.sendText(new Gson().toJson(chatBeanRequest));
            }
        });

    }


    interface WebSocketListener<T> {
        void onResponse(Response<T> t);

        void onFailed(int code, Throwable throwable);
    }
}

