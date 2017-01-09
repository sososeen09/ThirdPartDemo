package com.longge.thirdpartdemo.websocket.bean;

/**
 * Created by yunlong.su on 2017/1/9.
 * 直播间用户数变更通知
 */

public class AudienceCounterBean {
    public String roomId;
    public int count;

    @Override
    public String toString() {
        return "AudienceCounterBean{" +
                "roomId='" + roomId + '\'' +
                ", count=" + count +
                '}';
    }
}
