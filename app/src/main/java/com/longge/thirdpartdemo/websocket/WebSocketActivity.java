package com.longge.thirdpartdemo.websocket;

import android.os.Bundle;
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
    @BindView(R.id.tv_sendContent)
    TextView mTvSendContent;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.tv_showContent)
    TextView mTvShowContent;
    private WebSocket mSocket;
    private StringBuilder mSB;

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
                Log.d(TAG, "onTextMessage: " + text + "     Thread: " + Thread.currentThread().getName());
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
                mSocket = mSocket.recreate().connect();
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


    @OnClick({R.id.btn_connect, R.id.btn_disConnect, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                connect();
                break;
            case R.id.btn_disConnect:
                disConnect();
                break;
            case R.id.btn_send:
                sendText(mTvSendContent.getText().toString());
                break;
        }
    }

    private void disConnect() {
        mSocket.disconnect();
    }

    private void sendText(String s) {

    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket.connect();
                    mSocket.sendText("{\n" +
                            "  \"type\":\"WCST_ENTER\",\n" +
                            "  \"payload\":{\n" +
                            "    \"roomId\":\"1\",\n" +
                            "    \"token\":\"9f98a7bb-aa81-4315-ad4d-f8fdde89f5b0\"\n" +
                            "  }\n" +
                            "}");
                } catch (WebSocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
