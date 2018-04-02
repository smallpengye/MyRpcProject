package com.hzp.Util;

import com.hzp.Util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by huzhipeng on 2018/3/31.
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out){
        try{
            if (genericClass.isInstance(in)) {
                byte[] data = SerializationUtil.serialize(in);
                out.writeInt(data.length);
                out.writeBytes(data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}