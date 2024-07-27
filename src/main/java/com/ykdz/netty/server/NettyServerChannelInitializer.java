package com.ykdz.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;


/**
 * @author dzx
 * @ClassName:
 * @Description: 服务端初始化，客户端与服务器端连接一旦创建，这个类中方法就会被回调，设置出站编码器和入站解码器以及各项处理器
 * @date 2023年06月30日 21:27:16
 */
@Component
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

//    private FullHttpResponse createCorsResponseHeaders() {
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
//
//        // 设置允许跨域访问的响应头
//        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
//        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");
//        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, "3600");
//
//        return response;
//    }

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("active", new ChannelActiveHandler());
        //Socket 连接心跳检测
        pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 0, 0));
        pipeline.addLast("socketChoose", new SocketChooseHandler());
        pipeline.addLast("commonhandler",new NettyServerHandler());
    }
}

