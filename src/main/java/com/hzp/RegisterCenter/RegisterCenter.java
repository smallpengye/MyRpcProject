package com.hzp.RegisterCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huzhipeng on 2018/4/1.
 */
public class RegisterCenter {
    public final static Map<String,Object> registerCenter= new HashMap<String,Object>();
//    public static List<String> registerCenter= new ArrayList<String>();

    public static  void register(String serviceName,String address){
        registerCenter.put(serviceName,address);
        System.out.println(registerCenter.size());

    }

    public static String getAddress(String name){
        System.out.println(registerCenter.size());
        return (String) registerCenter.get(name);
    }


}
