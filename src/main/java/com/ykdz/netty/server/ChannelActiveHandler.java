package com.ykdz.netty.server;

import com.ykdz.entriy.General;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;



/**
 * @author dzx
 * @ClassName:
 * @Description: 客户端新建连接处理器
 * @date 2023年06月30日 21:27:16
 */

@ChannelHandler.Sharable
@Slf4j
public class ChannelActiveHandler extends ChannelInboundHandlerAdapter {

    /**
     * 有客户端连接服务器会触发此函数
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //获取客户端连接的远程地址
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        //获取客户端的IP地址
        String clientIp = insocket.getAddress().getHostAddress();
        //获取客户端的端口号
        int clientPort = insocket.getPort();
        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();
        //如果map中不包含此连接，就保存连接
        if (General.CHANNEL_MAP.containsKey(channelId)) {
            log.info("Socket------客户端【" + channelId + "】是连接状态，连接通道数量: " + General.CHANNEL_MAP.size());
        } else {
            //保存连接
            General.CHANNEL_MAP.put(channelId, ctx);
            log.info("Socket------客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]");
            log.info("Socket------连接通道数量: " + General.CHANNEL_MAP.size());
        }
    }
}

