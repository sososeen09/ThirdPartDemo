package com.longge.thirdpartdemo.websocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.longge.thirdpartdemo.R;

public class WebSocket2Activity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private WebSocketHelper.WebSocketListener mWebSocketListener1 = new WebSocketHelper.WebSocketListener<ConnectResBean>() {

        @Override
        public void onTextMessage(Response<ConnectResBean> text) {
            Log.d(TAG, "onTextMessage: " + text.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket2);
        WebSocketHelper.getInstance().addWebSocketListener(mWebSocketListener1);
        WebSocketHelper.getInstance().connect("1", "9f98a7bb-aa81-4315-ad4d-f8fdde89f5b0", "");
    }


}
