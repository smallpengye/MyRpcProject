package com.hzp.client;

import com.hzp.Util.DomainInfo;
import com.hzp.server.RpcRequest;
import com.hzp.server.RpcResponse;
import com.hzp.service.HelloService;
/**
 * Created by huzhipeng on 2018/4/1.
 */
public class ClientTest {

//    git分支提交测试注释

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
