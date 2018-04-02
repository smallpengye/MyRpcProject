package com.hzp.client;

import com.hzp.RegisterCenter.RegisterCenter;
import org.springframework.stereotype.Component;

/**
 * Created by huzhipeng on 2018/4/1.
 */
public class ServerDiscovery {

    public String getRegisterServerInfoByServerName(String servername){
        String address = RegisterCenter.getAddress(servername);
//        String address = (String)RegisterCenter.registerCenter.get(servername);
        return address;
    }
}
