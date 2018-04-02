package com.hzp.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by huzhipeng on 2018/4/1.
 */
public class ServerMain {
    public static void main(String args[]){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        NettyServer server = ctx.getBean(NettyServer.class);

    }
}
