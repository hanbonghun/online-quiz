package org.example;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class GameServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // HTTP 요청을 처리하기 위한 코덱
        pipeline.addLast(new HttpServerCodec());

        // HTTP 메시지를 하나로 합치는 어그리게이터
        pipeline.addLast(new HttpObjectAggregator(65536));

        // WebSocket 프로토콜 처리
        pipeline.addLast(new WebSocketServerProtocolHandler("/game", null, true));

        // 게임 로직을 처리할 핸들러
        pipeline.addLast(new GameServerHandler());
    }
}
