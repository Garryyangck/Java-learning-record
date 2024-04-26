package com.garry.qqserver.service;

import java.util.HashMap;
import java.util.Set;

/**
 * 该类管理服务器连接客户端的线程
 */
@SuppressWarnings({"all"})
public class ManageClientThread {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static ServerConnectClientThread getManageClientThread(String userId) {
        return hm.get(userId);
    }

    public static void addManageClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    public static void removeManageClientThread(String userId) {
        hm.remove(userId);
    }

    public static Set<String> getKeySet() {
        return hm.keySet();
    }

    /**
     * 获取当前在线用户列表
     *
     * @return当前在线用户列表
     */
    public static String getOnlineUsersList() {
        Set<String> keySet = hm.keySet();
        StringBuffer buf = new StringBuffer();
        for (String userId : keySet) {
            buf.append(userId + " ");
        }
        buf.deleteCharAt(buf.length() - 1);//删除末位的空格
        return new String(buf);
    }


}
