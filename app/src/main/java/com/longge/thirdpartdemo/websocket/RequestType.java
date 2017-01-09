package com.longge.thirdpartdemo.websocket;

/**
 * Created by yunlong.su on 2017/1/9.
 */

public enum RequestType {

    CONNECT("CONNECT"),//建立会话
    DISCONNECT("DISCONNECT"),//取消会话
    WCST_ENTER("WCST_ENTER"),//进入直播间
    WCST_AUDIENCES("WCST_AUDIENCES"),//直播室观众
    WCST_NEW_AUDIENCE("WCST_NEW_AUDIENCE"),//用户进入直播室广播
    WCST_LEAVE("WCST_LEAVE"),//离开直播间
    WCST_CHAT("WCST_CHAT"),//聊天
    WCST_MSG("WCST_MSG"),//消息
    PING("PING");//PING

    private final String requestType;

    RequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }
}
