package com.hzp.service.impl;

import com.hzp.Util.DomainInfo;
import com.hzp.server.RpcService;
import com.hzp.service.HelloService;

/**
 * Created by huzhipeng on 2018/4/1.
 */

@RpcService(HelloService.class) // 指定远程接口
public class HelloServiceImpl implements HelloService {

    @Override
    public String  sayHello() {
        System.out.println("hello,hu" );
        return "sayhello";
    }


    @Override
    public DomainInfo sayHello(String param) {
        System.out.println("hello:"+param);
        DomainInfo domainInfo = new  DomainInfo();
        domainInfo.setA(param);
        return domainInfo;
    }


}
