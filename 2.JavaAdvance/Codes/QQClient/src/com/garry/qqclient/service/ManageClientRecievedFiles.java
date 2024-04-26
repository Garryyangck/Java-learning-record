package com.garry.qqclient.service;

import com.garry.qqcommon.Message;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 * 该类管理用户收到的文件
 */
@SuppressWarnings({"all"})
public class ManageClientRecievedFiles {
    //key为userId, value为接收到的文件路径的可变数组
    private static HashMap<String, Vector<Message>> hm = new HashMap<>();

    /**
     * 为指定的用户添加当前接收的文件
     *
     * @param userId
     * @param receivedFile
     */
    public static void addReceivedFiles(String userId, Message message) {
        if (hm.containsKey(userId)) {
            Vector<Message> messages = hm.get(userId);
            messages.add(message);
            hm.put(userId, messages);
        } else {
            Vector<Message> messages = new Vector<>();
            messages.add(message);
            hm.put(userId, messages);
        }
    }

    /**
     * 删除指定用户的指定接收文件，用于用户下载文件后的删除
     *
     * @param userId
     * @param receivedFile
     */
    public static void removeReceivedFiles(String userId, String fileName) {
        if (hm.containsKey(userId)) {
            Vector<Message> messages = hm.get(userId);
            for (Message message : messages) {
                if (message.getContent().equals(fileName)) {
                    messages.remove(message);
                    hm.put(userId, messages);
                    return;
                }
            }
        }
    }

    /**
     * 显示用户当前所有接收的文件，只显示名字
     *
     * @param userId
     */
    public static void showReceivedFilesByKey(String userId) {
        Vector<Message> messages = hm.get(userId);
        System.out.println("=======用户 " + userId + " 当前接收文件=======");
        if (messages == null) {
            System.out.print("\n");
            return;
        }
        for (Message message : messages) {
            System.out.println("文件名: " + message.getContent());
        }
        System.out.print("\n");
    }

    /**
     * 得到指定用户的指定文件的字节码
     *
     * @param userId
     * @param fileName
     * @return 对应文件的byte[]型字节码，没找到返回null
     */
    public static byte[] getReceivedFiles(String userId, String fileName) {
        Vector<Message> messages = hm.get(userId);
        if (messages == null) return null;
        for (Message message : messages) {
            if (message.getContent().equals(fileName)) {
                return message.getFileBuf();
            }
        }
        return null;
    }
}
