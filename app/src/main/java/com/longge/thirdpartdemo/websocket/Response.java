package com.longge.thirdpartdemo.websocket;

/**
 * Created by yunlong.su on 2017/1/9.
 */

public class Response<T> {
    public String id; //服务器消息id
    public String type; //请求类型
    public int code;//错误码
    public String msg; //错误描述
    public String reqId; //请求id，如果是对客户端请求的响应，此值对应客户端请求的id
    public T payload; //携带参数

}
