package com.garry.qqclient.service;

import com.garry.qqcommon.Message;
import com.garry.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

@SuppressWarnings({"all"})
public class ClientConnectServerThread extends Thread {
    //该线程需要持有socket
    private Socket socket;
    private String userId;

    public ClientConnectServerThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //线程需要在后台和服务器通讯，做成while循环
        System.out.println("已和服务器保持连接...");
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();//服务器不把数据发过来的话，线程就会阻塞在这里

//                long timeMillis1 = System.currentTimeMillis();//测试运行时间，便于调整sleep时间
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //如果读取到的是服务器返回的在线用户列表
                    //取出在线列表的信息，并显示
                    //规定服务器返回的content用空格隔开
                    String[] onlineUsersList = message.getContent().split(" ");
                    System.out.println("=============当前在线用户列表=============");
                    for (int i = 0; i < onlineUsersList.length; i++)
                        System.out.println("用户: " + onlineUsersList[i]);
                    System.out.print("\n");

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_SEND_TO_FRIEND)) {
                    System.out.println("\n\n你收到了用户 " + message.getSender() + " 的一条消息--------------");
                    System.out.println(message.getSender() + ": " + message.getContent());
                    System.out.println("-------------------" + message.getSendTime() + "\n");
                    System.out.print("请输入你的选择：");

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_SEND_TO_ALL)) {
                    System.out.println("\n\n用户 " + message.getSender() + " 群发了一条消息--------------");
                    System.out.println(message.getSender() + " 对所有人说: " + message.getContent());
                    System.out.println("-------------------" + message.getSendTime() + "\n");
                    System.out.print("请输入你的选择：");

                } else if (message.getMesType().equals(MessageType.MESSAGE_SEND_SUCCEED)) {
                    System.out.println("========消息已成功送达" + message.getContent() + "========\n");

                } else if (message.getMesType().equals(MessageType.MESSAGE_SEND_FAILED)) {
                    System.out.println("======消息发送失败" + message.getContent() + "======\n");

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_SEND_FILE)) {
                    ManageClientRecievedFiles.addReceivedFiles(userId, message);

                    System.out.println("\n\n你收到了用户 " + message.getSender() + " 发送的文件--------------");
                    System.out.println("文件名：" + message.getContent());
                    System.out.println("-------------------" + message.getSendTime() + "\n");
                    System.out.print("请输入你的选择：");

                } else if (message.getMesType().equals(MessageType.MESSAGE_SERVER_SEND_TO_ALL)) {
                    //服务器推送消息
                    System.out.println("\n\n服务器推消息给所有人----------------");
                    System.out.println("服务器: " + message.getContent());
                    System.out.println("---------------------" + message.getSendTime() + "\n");
                    System.out.print("请输入你的选择：");
                }

//                long timeMillis2 = System.currentTimeMillis();//测试运行时间，便于调整sleep时间
//                System.out.println(timeMillis2 - timeMillis1);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
