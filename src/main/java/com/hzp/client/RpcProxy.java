package com.hzp.client;

import com.hzp.RegisterCenter.RegisterCenter;
import com.hzp.server.RpcRequest;
import com.hzp.server.RpcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by huzhipeng on 2018/4/1.
 */
@Component
public class RpcProxy {

    private String serverAddress;


    private ServerDiscovery serverDiscovery = new ServerDiscovery();
    //
    public <T> T create(final Class<?> interfaceClass) {
        System.out.println("创建代理类对象");
        //通过代理返回实现类
        return (T)Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        System.out.println("--请求的类名--"+request.getClassName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        //从服务中心获取服务地址
//                        System.out.println(interfaceClass.getName());
//                        serverDiscovery.getRegisterServerInfoByServerName(interfaceClass.getName());
//                        if (serverDiscovery.getRegisterServerInfoByServerName(interfaceClass.getName()) != null) {
//                            serverAddress = serverDiscovery.getRegisterServerInfoByServerName(interfaceClass.getName()); // 发现服务
//                        }
                        serverAddress="127.0.0.1:8989";
                        String[] array = serverAddress.split(":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);
                        //通过地址获取所需反射类
                        RpcClient client = new RpcClient(host, port); // 初始化 RPC 客户端
                        RpcResponse response = client.sendInfo(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应

                        System.out.println("返回代理类实体");
                        if (response.getRequestId().equals("null")){
                            System.out.println("返回类为空");
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                });
    }
}
