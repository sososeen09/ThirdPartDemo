package com.longge.thirdpartdemo.websocket.bean;

/**
 * Created by yunlong.su on 2017/1/9.
 * 新用户进入直播间
 */

public class AudienceBean {
    public String roomId;
    public String userId;
    public String avatar;
    public String nickname;

    @Override
    public String toString() {
        return "AudienceBean{" +
                "roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
