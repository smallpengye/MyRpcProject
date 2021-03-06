package com.hzp.client;

import com.hzp.Util.RpcDecoder;
import com.hzp.Util.RpcEncoder;
import com.hzp.server.RpcRequest;
import com.hzp.server.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * Created by huzhipeng on 2018/3/31.
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{

    private final Object obj = new Object();


    private String host;
    private int port;

    private RpcResponse response;

    public RpcClient(String host, int port){
        this.host = host;
        this.port = port;
    }


    //用户调用客户端发送请求信息,并将结果返回给调用者
    public RpcResponse sendInfo(RpcRequest request) throws InterruptedException {

        System.out.println("Client__sendInfo");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                    .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                    .addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            //启动客户端,异步链接服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            //发送request数据
            future.channel().writeAndFlush(request).sync();

            synchronized (obj) {
                obj.wait(); // 未收到响应，使线程等待
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }



    //接受服务端返回信息
    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        this.response = response;
        System.out.println("收到信息");
        synchronized (obj) {
            obj.notifyAll(); // 收到服务端响应，唤醒线程，关闭通道
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client caught exception");
        ctx.close();
    }


}
