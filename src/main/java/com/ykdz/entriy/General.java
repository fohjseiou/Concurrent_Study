package com.ykdz.entriy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 500007
 * @ClassName:
 * @Description:
 * @date 2023年07月02日 19:12:42
 */
public class General {

    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    public static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 管理一个全局mao, 报存连接进服务器的各个通道类型
     */
    public static final ConcurrentHashMap<ChannelId, Boolean> CHANNEL_TYPE_MAP = new ConcurrentHashMap<>();

}