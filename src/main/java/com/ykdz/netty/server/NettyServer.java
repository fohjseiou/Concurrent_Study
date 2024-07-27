package com.ykdz.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author dzx
 * @ClassName:
 * @Description: netty服务启动类
 * @date 2023年06月30日 21:27:16
 */
@Slf4j
@Component
public class NettyServer {

    public void start(InetSocketAddress address) {
        //配置服务端的NIO线程组

        /*
         * 在Netty中，事件循环组是一组线程池，用于处理网络事件，例如接收客户端连接、读写数据等操作。
         * 它由两个部分组成：bossGroup和workerGroup。
         * bossGroup 是负责接收客户端连接请求的线程池。
         * workerGroup 是负责处理客户端连接的线程池。
         * */

        EventLoopGroup bossGroup = new NioEventLoopGroup(10);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建ServerBootstrap实例，boss组用于接收客户端连接请求，worker组用于处理客户端连接。
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)  // 绑定线程池
                    .channel(NioServerSocketChannel.class)//通过TCP/IP方式进行传输
                    .childOption(ChannelOption.SO_REUSEADDR, true) //快速复用端口
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                    .localAddress(address)//监听服务器地址
                    .childHandler(new NettyServerChannelInitializer())
//                    .childHandler(new com.ccp.dev.system.netty.NettyServerChannelInitializer())
                    .childOption(ChannelOption.TCP_NODELAY, true)//子处理器处理客户端连接的请求和数据
                    .option(ChannelOption.SO_BACKLOG, 1024)  //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  //保持长连接，2小时无数据激活心跳机制

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(address).sync();
            future.addListener(l -> {
                if (future.isSuccess()) {
                    System.out.println("Netty服务启动成功");
                } else {
                    System.out.println("Netty服务启动失败");
                }
            });
            log.info("Netty服务开始监听端口: " + address.getPort());
            //关闭channel和块，直到它被关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动Netty服务器时出错", e);
        } finally {
            //释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
