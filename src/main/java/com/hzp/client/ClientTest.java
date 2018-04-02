package com.hzp.client;

import com.hzp.Util.DomainInfo;
import com.hzp.server.RpcRequest;
import com.hzp.server.RpcResponse;
import com.hzp.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huzhipeng on 2018/4/1.
 */
public class ClientTest {




    public static void main(String[] args){

        //获取反射类
        RpcProxy rpcProxy = new RpcProxy();
        HelloService helloService = rpcProxy.create(HelloService.class);
//        if(helloService!=null) {
//            System.out.println("++"+helloService.getClass());
//        }else{
//            System.out.println("nullllllll");
//        }
        String str = helloService.sayHello();
        System.out.println("返回结果："+str);

        DomainInfo o = helloService.sayHello("胡志鹏");
        System.out.println(o.getA());
    }

}
