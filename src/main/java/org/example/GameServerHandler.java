package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import org.example.broadcast.MessageBroadcaster;
import org.example.handler.LobbyChatHandler;
import org.example.handler.MessageHandler;
import org.example.handler.RoomChatHandler;
import org.example.message.GameMessage;
import org.example.message.MessageType;
import org.example.room.ChatRoom;
import org.example.room.ChatRoomManager;
import org.example.session.UserSession;
import org.example.session.UserSessionManager;

public class GameServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final UserSessionManager sessionManager;
    private final ChatRoomManager roomManager;
    private final ObjectMapper objectMapper;
    private final Map<MessageType, MessageHandler> messageHandlers;
    private final MessageBroadcaster broadcaster;

    public GameServerHandler() {
        this.sessionManager = UserSessionManager.getInstance();
        this.roomManager = ChatRoomManager.getInstance();
        this.objectMapper = new ObjectMapper();
        this.broadcaster = new MessageBroadcaster(objectMapper, sessionManager, roomManager);

        this.messageHandlers = new EnumMap<>(MessageType.class);
        messageHandlers.put(MessageType.LOBBY_CHAT, new LobbyChatHandler(broadcaster));
        messageHandlers.put(MessageType.ROOM_CHAT, new RoomChatHandler(broadcaster));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        String userId = UUID.randomUUID().toString();
        sessionManager.createSession(userId, incoming);

        ChatRoom lobby = roomManager.getLobby();
        lobby.addParticipant(userId);

        broadcaster.broadcastToLobby(new GameMessage(MessageType.CONNECT, userId,
            "New user connected: " + userId));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        UserSession session = sessionManager.getSessionByChannel(incoming);
        if (session != null) {
            String userId = session.getUserId();
            roomManager.getLobby().removeParticipant(userId);
            broadcaster.broadcastToLobby(new GameMessage(MessageType.DISCONNECT, userId,
                "User disconnected: " + userId));
            sessionManager.removeSession(incoming);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        try {
            GameMessage message = objectMapper.readValue(frame.text(), GameMessage.class);
            UserSession session = sessionManager.getSessionByChannel(ctx.channel());

            if (session == null) {
                return;
            }

            MessageHandler handler = messageHandlers.get(message.getType());
            if (handler != null) {
                handler.handle(session, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        sessionManager.removeSession(ctx.channel());
        ctx.close();
    }
}