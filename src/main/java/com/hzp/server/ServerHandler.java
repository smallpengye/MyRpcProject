package com.hzp.server;

import com.hzp.Util.Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

/**
 * Created by huzhipeng on 2018/3/31.
 */

@Component
public class ServerHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf in = (ByteBuf)o;
        //
        byte[] bytes = Unpooled.copiedBuffer(in).array();
        System.out.println(Util.byte2hex(bytes));

    }
}
