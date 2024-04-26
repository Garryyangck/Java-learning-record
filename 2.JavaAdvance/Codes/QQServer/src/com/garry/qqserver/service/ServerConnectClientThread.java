package com.garry.qqserver.service;

import com.garry.qqcommon.Message;
import com.garry.qqcommon.MessageType;
import com.garry.qqcommon.User;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * 该类对应的对象和某个客户端保持通讯
 */
@SuppressWarnings({"all"})
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;
    private HashMap<String, User> validUsers;

    public ServerConnectClientThread(Socket socket, String userId, HashMap<String, User> validUsers) {
        this.socket = socket;
        this.userId = userId;
        this.validUsers = validUsers;//这里指向QQServer中validUsers的实例对象，用浅拷贝
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * 判断不在线的接收人是否注册，若注册，就将消息加入ManageOfflineMessage
     *
     * @param receiver
     * @param message
     * @return
     */
    public boolean receiverRegistered(String receiver, Message message) {
        if (!validUsers.containsKey(receiver)) return false;
        ManageOfflineMessage.addOfflineMessage(receiver, message);
        return true;
    }

    /**
     * 与userId对应客户端保持连接的服务器线程
     */
    @Override
    public void run() {
        System.out.println("服务器线程和客户端用户 " + userId + " 保持通讯...");
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();//客户端不把数据发过来的话，线程就会阻塞在这里


                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    //客户请求在线用户列表
                    System.out.println("用户 " + message.getSender() + " 请求拉取在线用户列表");
                    String onlineUsersList = ManageClientThread.getOnlineUsersList();
                    //创建Message对象返回给客户端的socket
                    Message retMessage = new Message();
                    retMessage.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    retMessage.setContent(onlineUsersList);
                    retMessage.setReceiver(message.getSender());
                    //oos传给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(retMessage);

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    //将对应线程从HaspMap中移除
                    ManageClientThread.removeManageClientThread(message.getSender());
                    //关闭连接
                    socket.close();
                    System.out.println("用户 " + message.getSender() + " 退出系统");
                    //退出循环
                    break;

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_SEND_TO_FRIEND)) {
                    Message retMessage = new Message();
                    //尝试通过receiver获取线程
                    ServerConnectClientThread manageClientThread =
                            ManageClientThread.getManageClientThread(message.getReceiver());
                    System.out.print("用户 " + message.getSender() +
                            " 向用户 " + message.getReceiver() + " 发送信息，");

                    if (manageClientThread == null) {//接收人不在线
                        if (receiverRegistered(message.getReceiver(), message)) {//接收人注册了，但不在线
                            retMessage.setMesType(MessageType.MESSAGE_SEND_SUCCEED);
                            retMessage.setContent("，但是对方离线");
                            System.out.println("发送成功");
                        } else {//接收人不存在
                            retMessage.setMesType(MessageType.MESSAGE_SEND_FAILED);
                            retMessage.setContent("，私聊的对象不存在");
                            System.out.println("发送失败");
                        }
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(retMessage);

                    } else {//接收人在线
                        //通过manageClientThread的socket传递message
                        ObjectOutputStream oos =
                                new ObjectOutputStream(manageClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);

                        retMessage.setMesType(MessageType.MESSAGE_SEND_SUCCEED);
                        retMessage.setContent("");
                        ObjectOutputStream retOos = new ObjectOutputStream(socket.getOutputStream());
                        retOos.writeObject(retMessage);
                        System.out.println("发送成功");
                    }

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_SEND_TO_ALL)) {
                    Message retMessage = new Message();
                    Set<String> keySet = validUsers.keySet();
                    int cntOtherUsers = 0;//记录其他在线用户数
                    for (String id : keySet) {
                        if (id.equals(message.getSender()))//不用发给自己
                            continue;
                        //获取线程
                        ServerConnectClientThread manageClientThread = ManageClientThread.getManageClientThread(id);
                        if (manageClientThread == null) {//用户不在线
                            ManageOfflineMessage.addOfflineMessage(id, message);
                        } else {//用户在线
                            ObjectOutputStream oos = new ObjectOutputStream(manageClientThread.getSocket().getOutputStream());
                            oos.writeObject(message);
                            cntOtherUsers++;
                        }
                    }
                    System.out.print("用户 " + message.getSender() + " 群聊所有人，");
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                    if (cntOtherUsers == 0) {//发送成功，但是没有人在线
                        retMessage.setMesType(MessageType.MESSAGE_SEND_SUCCEED);
                        retMessage.setContent("，但没有其他在线用户");
                        oos.writeObject(retMessage);
                        System.out.println("消息发送成功");

                    } else {//发送成功
                        retMessage.setMesType(MessageType.MESSAGE_SEND_SUCCEED);
                        retMessage.setContent("");
                        oos.writeObject(retMessage);
                        System.out.println("消息发送成功");
                    }

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_SEND_FILE)) {
                    Message retMessage = new Message();
                    //尝试根据receiver获取线程
                    ServerConnectClientThread manageClientThread =
                            ManageClientThread.getManageClientThread(message.getReceiver());
                    System.out.print("用户 " + message.getSender() +
                            " 向用户 " + message.getReceiver() + " 发送文件，");

                    if (manageClientThread == null) {//接收者不在线
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        if (receiverRegistered(message.getReceiver(), message)) {//接收者存在
                            retMessage.setMesType(MessageType.MESSAGE_SEND_SUCCEED);
                            retMessage.setContent("，但对方不在线");
                            oos.writeObject(retMessage);
                            System.out.println("发送成功");
                        } else {
                            retMessage.setMesType(MessageType.MESSAGE_SEND_FAILED);
                            retMessage.setContent("，文件接收对象不存在");
                            oos.writeObject(retMessage);
                            System.out.println("发送失败");
                        }
                    } else {//接收者在线
                        ObjectOutputStream oos = new ObjectOutputStream(manageClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);

                        retMessage.setMesType(MessageType.MESSAGE_SEND_SUCCEED);
                        retMessage.setContent("");
                        ObjectOutputStream retOos = new ObjectOutputStream(socket.getOutputStream());
                        retOos.writeObject(retMessage);
                        System.out.println("发送成功");
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
