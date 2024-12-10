package org.example.handler;

import org.example.broadcast.MessageBroadcaster;
import org.example.message.GameMessage;
import org.example.session.UserSession;

public class LobbyChatHandler implements MessageHandler {

    private final MessageBroadcaster broadcaster;

    public LobbyChatHandler(MessageBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @Override
    public void handle(UserSession session, GameMessage message) {
        broadcaster.broadcastToLobby(message);
    }
}