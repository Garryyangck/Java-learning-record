package com.garry.qqserver.service;

import com.garry.qqcommon.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;

/**
 * 该类管理离线传输的消息
 */
@SuppressWarnings({"all"})
public class ManageOfflineMessage {
    private static HashMap<String, Vector<Message>> hm = new HashMap<>();

    /**
     * 添加离线用户没有接收到的消息
     *
     * @param receiver
     * @param message
     */
    public static void addOfflineMessage(String receiver, Message message) {
        if (!hm.containsKey(receiver)) {
            Vector<Message> messages = new Vector<>();
            messages.add(message);
            hm.put(receiver, messages);
        } else {
            Vector<Message> messages = hm.get(receiver);
            messages.add(message);
            hm.put(receiver, messages);
        }
    }

    /**
     * 离线用户登录后，将没接收的消息传给他
     *
     * @param receiver
     */
    public static void sendOfflineMessage(String receiver) {
        Vector<Message> messages = hm.get(receiver);
        if (messages == null || messages.isEmpty()) return;//没有离线消息
        //获取连接接收人客户端的线程
        ServerConnectClientThread manageClientThread = ManageClientThread.getManageClientThread(receiver);
        if (manageClientThread == null) return;//线程不存在
        try {
            //将所有消息都传过去
            for (Message message : messages) {
                //一个流对象一次只能传一个对象，因为客户端一次只接收一个
                //同一个流对象不断传对象会导致客户端接收出问题
                //因此每写一个对象，就要创建一次流对象
                ObjectOutputStream oos = new ObjectOutputStream(manageClientThread.getSocket().getOutputStream());
                String content = message.getContent();
                content += " (离线状态下接收)";
                message.setContent(content);
                oos.writeObject(message);
            }
            hm.remove(receiver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
