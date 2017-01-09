package com.longge.thirdpartdemo.websocket.bean;

/**
 * Created by yunlong.su on 2017/1/9.
 */

public class MsgResBean {
    public String content;
    public String roomId;
    public String userId;
    public String avatar;
    public String nickname;

    @Override
    public String toString() {
        return "MsgResBean{" +
                "content='" + content + '\'' +
                ", roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
