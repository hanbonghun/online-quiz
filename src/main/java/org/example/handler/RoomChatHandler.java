package org.example.handler;

import org.example.broadcast.MessageBroadcaster;
import org.example.message.GameMessage;
import org.example.session.UserSession;

public class RoomChatHandler implements MessageHandler {

    private final MessageBroadcaster broadcaster;

    public RoomChatHandler(MessageBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @Override
    public void handle(UserSession session, GameMessage message) {
        if (message.getRoomId() != null) {
            broadcaster.broadcastToRoom(message.getRoomId(), message);
        }
    }
}