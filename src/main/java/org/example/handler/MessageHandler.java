package org.example.handler;

import org.example.message.GameMessage;
import org.example.session.UserSession;

public interface MessageHandler {

    void handle(UserSession session, GameMessage message);
}
