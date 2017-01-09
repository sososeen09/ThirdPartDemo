package com.longge.thirdpartdemo.websocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longge.thirdpartdemo.R;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebSocketActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.btn_connect)
    Button mBtnConnect;
    @BindView(R.id.et_sendContent)
    TextView mEtSendContent;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.tv_showContent)
    TextView mTvShowContent;
    private WebSocket mSocket;
    private StringBuilder mSB;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        ButterKnife.bind(this);
        mSB = new StringBuilder();
        initSocket();
    }

    private void initSocket() {
        try {
            mSocket = new WebSocketFactory().createSocket("ws://test.wpwebsocket.baidao.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSocket.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                super.onTextMessage(websocket, text);
//                Log.d(TAG, "onTextMessage: " + text + "     Thread: " + Thread.currentThread().getName());
                Log.d(TAG, "onTextMessage: " + text);
                mSB.append(text).append("\n");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvShowContent.setText(mSB.toString());
                    }
                });
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean
                    closedByServer) throws Exception {
                super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
                Log.d(TAG, "onDisconnected: closedByServer " + closedByServer);
//                mSocket = mSocket.recreate().connect();
            }

            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                super.onConnected(websocket, headers);
                Log.d(TAG, "onConnected: ");
            }

            @Override
            public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
                super.onStateChanged(websocket, newState);
            }
        });

    }


    @OnClick({R.id.btn_connect, R.id.btn_disConnect, R.id.btn_send, R.id.btn_enter, R.id.btn_leave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                //建立连接
                connect();
                break;
            case R.id.btn_disConnect:
                //清除会话
                disConnect();
                break;
            case R.id.btn_send:
                sendText(mEtSendContent.getText().toString());
                break;
            case R.id.btn_enter:
                //进入直播间
                enterChat();
                break;

            case R.id.btn_leave:
                //离开直播间
                leaveChat();
                break;
        }
    }

    private void leaveChat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSocket.sendText("{\n" +
                        "  \"type\":\"WCST_LEAVE\",\n" +
                        "  \"payload\":{\n" +
                        "    \"roomId\":\"1\"\n" +
                        "  },\n" +
                        "  \"id\":\"1\"\n" +
                        "}");
            }
        }).start();
    }

    private void enterChat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSocket.sendText("{\n" +
                        "  \"type\":\"WCST_ENTER\",\n" +
                        "  \"payload\":{\n" +
                        "    \"roomId\":\"1\"},\n" +
                        "  \"id\":\"1\"\n" +
                        "}");
            }
        }).start();
    }


    private void disConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSocket.sendText("{\n" +
                        "  \"type\":\"DISCONNECT\",\n" +
                        "  \"payload\":{\n" +
                        "  },\n" +
                        "  \"id\":\"1\"\n" +
                        "}");
                mSocket.disconnect();
            }
        }).start();

    }

    private void sendText(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = "{\n" +
                        "  \"type\":\"WCST_CHAT\",\n" +
                        "  \"payload\":{\n" +
                        "    \"roomId\":\"1\",\n" +
                        "    \"content\":\"" + s + "\"\n" +
                        "  },\n" +
                        "  \"id\":\"3\"\n" +
                        "}";
                mSocket.sendText(message);
            }
        }).start();
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSocket.sendText("{\n" +
                    "  \"type\":\"PING\",\n" +
                    "  \"payload\":{},\n" +
                    "  \"id\":\"1\"\n" +
                    "}");
            Log.d(TAG, "run: sendPing");
            mHandler.postDelayed(this, 30 * 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mRunnable = null;
        mSocket.disconnect();
        mSocket = null;
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket.connect();

                    mSocket.sendText("{\n" +
                            "  \"type\":\"CONNECT\",\n" +
                            "  \"payload\":{\n" +
                            "    \"token\":\"9f98a7bb-aa81-4315-ad4d-f8fdde89f5b0\",\n" +
                            "    \"preSid\":\"\"\n" +
                            "  },\n" +
                            "  \"id\":\"1\"\n" +
                            "}");


                    mHandler.post(mRunnable);

                } catch (WebSocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
