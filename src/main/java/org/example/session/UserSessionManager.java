package org.example.session;

import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSessionManager {

    private static final UserSessionManager instance = new UserSessionManager();
    private final Map<String, UserSession> sessionsByUserId = new ConcurrentHashMap<>();
    private final Map<Channel, String> userIdsByChannel = new ConcurrentHashMap<>();

    private UserSessionManager() {
    }

    private static class Holder {

        private static final UserSessionManager INSTANCE = new UserSessionManager();
    }

    public static UserSessionManager getInstance() {
        return Holder.INSTANCE;
    }

    public UserSession createSession(String userId, Channel channel) {
        UserSession session = new UserSession(userId, channel);
        sessionsByUserId.put(userId, session);
        userIdsByChannel.put(channel, userId);
        return session;
    }

    public void removeSession(Channel channel) {
        String userId = userIdsByChannel.remove(channel);
        if (userId != null) {
            sessionsByUserId.remove(userId);
        }
    }

    public UserSession getSession(String userId) {
        return sessionsByUserId.get(userId);
    }

    public UserSession getSessionByChannel(Channel channel) {
        String userId = userIdsByChannel.get(channel);
        return userId != null ? sessionsByUserId.get(userId) : null;
    }

    public boolean hasSession(String userId) {
        return sessionsByUserId.containsKey(userId);
    }
}