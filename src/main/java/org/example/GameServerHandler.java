package org.example;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.UUID;
import org.example.session.UserSession;
import org.example.session.UserSessionManager;

public class GameServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final UserSessionManager sessionManager = UserSessionManager.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        String userId = UUID.randomUUID().toString();
        sessionManager.createSession(userId, incoming);
        System.out.println(
            "Client " + incoming.remoteAddress() + " connected with userId: " + userId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        UserSession session = sessionManager.getSessionByChannel(incoming);
        String userId = session != null ? session.getUserId() : "unknown";
        sessionManager.removeSession(incoming);
        System.out.println("Client " + incoming.remoteAddress() + " disconnected with userId: " + userId);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String message = msg.text();
        Channel incoming = ctx.channel();
        UserSession session = sessionManager.getSessionByChannel(incoming);

        if (session != null) {
            System.out.println("Received message from userId" + session.getUserId() + ": " + message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        sessionManager.removeSession(ctx.channel());
        ctx.close();
    }
}