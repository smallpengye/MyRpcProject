package com.hzp.server;

import com.hzp.RegisterCenter.RegisterCenter;
import com.hzp.Util.RpcDecoder;
import com.hzp.Util.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huzhipeng on 2018/3/31.
 */

@Component
public class NettyServer implements ApplicationContextAware, InitializingBean {



    @Autowired
    public ServerHandler serverHandler;

    private Map<String, Object> handlerMap = new HashMap<>(); // 存放接口名与服务对象之间的映射关系

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        // 获取所有带有 RpcService 注解的 Spring Bean
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
                //获取bean的父接口名称，以及服务所在的地址,将信息注入注册中心
                RegisterCenter.register(interfaceName,"127.0.0.1:8989");

            }
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcDecoder(RpcRequest.class)); // 将 RPC 请求进行解码（为了处理请求）
                            p.addLast(new RpcEncoder(RpcResponse.class)); // 将 RPC 响应进行编码（为了返回响应
                            p.addLast(new RpcServerHandler(handlerMap)); // 处理 RPC 请求
                        }
                    });
            ChannelFuture f = serverBootstrap.bind(8989).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        }finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


}
