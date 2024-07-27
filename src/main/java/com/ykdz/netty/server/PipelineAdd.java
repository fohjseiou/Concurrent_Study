package com.ykdz.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

/**
 * @author dzx
 * @ClassName:
 * @Description: 给NettyServerChannelInitializer初始化类中的commonhandler添加前置处理器
 * @date 2023年06月30日 21:31:24
 */
@Component
public class PipelineAdd {

    public void websocketAdd(ChannelHandlerContext ctx) {
        System.out.println("PipelineAdd");
        ctx.pipeline().addBefore("commonhandler", "http-codec", new HttpServerCodec());
        ctx.pipeline().addBefore("commonhandler", "aggregator", new HttpObjectAggregator(999999999));
        ctx.pipeline().addBefore("commonhandler", "http-chunked", new ChunkedWriteHandler());
//        ctx.pipeline().addBefore("commonhandler","WebSocketServerCompression",new WebSocketServerCompressionHandler());
        ctx.pipeline().addBefore("commonhandler", "ProtocolHandler", new WebSocketServerProtocolHandler("/ws"));

//        ctx.pipeline().addBefore("commonhandler","StringDecoder",new StringDecoder(CharsetUtil.UTF_8)); // 解码器，将字节转换为字符串
//        ctx.pipeline().addBefore("commonhandler","StringEncoder",new StringEncoder(CharsetUtil.UTF_8));
        // HttpServerCodec：将请求和应答消息解码为HTTP消息
//        ctx.pipeline().addBefore("commonhandler","http-codec",new HttpServerCodec());
//
//        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
//        ctx.pipeline().addBefore("commonhandler","aggregator",new HttpObjectAggregator(999999999));
//
//        // ChunkedWriteHandler：向客户端发送HTML5文件,文件过大会将内存撑爆
//        ctx.pipeline().addBefore("commonhandler","http-chunked",new ChunkedWriteHandler());
//
//        ctx.pipeline().addBefore("commonhandler","WebSocketAggregator",new WebSocketFrameAggregator(999999999));
//
//        //用于处理websocket, /ws为访问websocket时的uri
//        ctx.pipeline().addBefore("commonhandler","ProtocolHandler", new WebSocketServerProtocolHandler("/ws"));

    }
}
