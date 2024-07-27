package com.ykdz.netty.server;

import com.ykdz.entriy.General;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.ykdz.utils.SpringContextUtil;

import java.util.List;

@Component
@Slf4j
public class SocketChooseHandler extends ByteToMessageDecoder {
    //默认暗号长度为23
    private static final int MAX_LENGTH = 23;
    //WebSocket握手协议的前缀
    private static final String WEBSOCKET_PREFIX = "GET /";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String protocol = getBufStart(in);
        if (protocol.startsWith(WEBSOCKET_PREFIX)){
            SpringContextUtil.getBean(PipelineAdd.class).websocketAdd(ctx);
            //对于webSocket,不设置超时断开
            ctx.pipeline().remove(IdleStateHandler.class);
            this.putChannelType(ctx.channel().id(),true);
        }else {
            this.putChannelType(ctx.channel().id(),false);
        }
        //刷新读指针
        in.resetReaderIndex();
        ctx.pipeline().remove(this.getClass());
    }

    //根据缓冲流中读指针的位置来读取内容
    public String getBufStart(ByteBuf in){
        int length = in.readableBytes();
        if (length > MAX_LENGTH){
            length = MAX_LENGTH;
        }

        //标记读位置,读取到指定位置将内容进行返回
        in.markReaderIndex();
        byte[] content = new byte[length];
        in.readBytes(content);
        return new String(content);
    }

    /**
     *
     * @param channelId
     * @param type
     */
    public void putChannelType(ChannelId channelId, Boolean type){
        if (General.CHANNEL_TYPE_MAP.containsKey(channelId)){
            log.info("Socket------客户端【" + channelId + "】是否websocket协议："+type);
        }else {
            General.CHANNEL_TYPE_MAP.put(channelId,type);
            log.info("Socket------客户端【" + channelId + "】是否websocket协议："+type);
        }
    }

}
