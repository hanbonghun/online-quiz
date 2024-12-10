package org.example.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.Set;
import org.example.message.GameMessage;
import org.example.room.ChatRoom;
import org.example.room.ChatRoomManager;
import org.example.session.UserSession;
import org.example.session.UserSessionManager;

public class MessageBroadcaster {

    private final ObjectMapper objectMapper;
    private final UserSessionManager sessionManager;
    private final ChatRoomManager roomManager;

    public MessageBroadcaster(ObjectMapper objectMapper, UserSessionManager sessionManager,
        ChatRoomManager roomManager) {
        this.objectMapper = objectMapper;
        this.sessionManager = sessionManager;
        this.roomManager = roomManager;
    }

    public void broadcastToLobby(GameMessage message) {
        try {
            String messageStr = objectMapper.writeValueAsString(message);
            ChatRoom lobby = roomManager.getLobby();
            broadcast(lobby.getParticipants(), messageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcastToRoom(String roomId, GameMessage message) {
        ChatRoom room = roomManager.getRoom(roomId);
        if (room != null) {
            try {
                String messageStr = objectMapper.writeValueAsString(message);
                broadcast(room.getParticipants(), messageStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(Set<String> participants, String message) {
        for (String userId : participants) {
            UserSession userSession = sessionManager.getSession(userId);
            if (userSession != null) {
                userSession.getChannel().writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }
}
