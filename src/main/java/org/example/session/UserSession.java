package org.example.session;

import io.netty.channel.Channel;

public class UserSession {

    private final String userId;
    private final Channel channel;
    private String nickname;
    private UserStatus status;

    public UserSession(String userId, Channel channel) {
        this.userId = userId;
        this.channel = channel;
        this.status = UserStatus.ONLINE;
    }

    public String getUserId() {
        return userId;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}

