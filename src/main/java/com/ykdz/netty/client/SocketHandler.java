package com.ykdz.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * @author dzx
 * @ClassName:
 * @Description: netty客户端处理器
 * @date 2023年06月30日 21:30:02
 */
@Slf4j
public class SocketHandler extends ChannelInboundHandlerAdapter {

    // 日志打印
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.debug("SocketHandler Active（客户端）");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LOGGER.debug("####接收服务端发送过来的消息####");
        LOGGER.debug("SocketHandler read Message：" + msg);
        //获取服务端连接的远程地址
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        //获取服务端的IP地址
        String clientIp = insocket.getAddress().getHostAddress();
        //获取服务端的端口号
        int clientPort = insocket.getPort();
        log.info("netty服务端[IP:" + clientIp + "--->PORT:" + clientPort + "]");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("####客户端断开连接####");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

