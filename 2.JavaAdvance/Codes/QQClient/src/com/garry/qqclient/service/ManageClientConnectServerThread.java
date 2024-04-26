package com.garry.qqclient.service;

import java.util.HashMap;

/**
 * 管理客户端连接服务器的线程
 */
@SuppressWarnings({"all"})
public class ManageClientConnectServerThread {
    //key就是userId
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    //加入
    public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        hm.put(userId,clientConnectServerThread);
    }

    //查找
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);
    }
}
