package com.ykdz.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dzx
 * @ClassName:
 * @Description: netty客户端
 * @date 2023年06月30日 21:30:02
 */
public class SocketClient {
    // 服务端IP
    static final String HOST = System.getProperty("host", "127.0.0.1");

    // 服务端开放端口
    static final int PORT = Integer.parseInt(System.getProperty("port", "7777"));

    // 日志打印
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketClient.class);

    // 主函数启动
    public static void main(String[] args) throws InterruptedException {
        sendMessage("我是客户端，我发送了一条数据给netty服务端。。");
    }

    /**
     * 核心方法（处理：服务端向客户端发送的数据、客户端向服务端发送的数据）
     */
    public static void sendMessage(String content) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new SocketChannelInitializer());
                        }
                    });

            ChannelFuture future = b.connect(HOST, PORT).sync();
            for (int i = 0; i < 3; i++) {
                future.channel().writeAndFlush(content);
                Thread.sleep(2000);
            }
            // 程序阻塞
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}

