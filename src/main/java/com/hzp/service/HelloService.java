package com.hzp.service;

import com.hzp.Util.DomainInfo;
import com.hzp.service.impl.HelloServiceImpl;

/**
 * Created by huzhipeng on 2018/4/1.
 */
public interface HelloService {

    public String sayHello();

    public DomainInfo sayHello(String param);
}
