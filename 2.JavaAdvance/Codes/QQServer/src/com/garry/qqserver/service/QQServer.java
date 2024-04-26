package com.garry.qqserver.service;

import com.garry.qqcommon.Message;
import com.garry.qqcommon.MessageType;
import com.garry.qqcommon.User;
import com.garry.qqcommon.UserType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是服务器，在9999端口监听，并保持通讯
 */
@SuppressWarnings({"all"})
public class QQServer {
    private ServerSocket ss = null;
    //创建一个集合，存放多个用户，如果是这些用户登录，即认为登录成功
    //这里也可以使用可以处理并发的集合：ConcurrentHashMap，没有线程危险
    private static HashMap<String, User> validUsers = new HashMap<>();

    static {//在静态代码块初始化 validUsers，默认用户
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("张三", new User("张三", "123456"));
        validUsers.put("李四", new User("李四", "123456"));
        validUsers.put("王五", new User("王五", "123456"));
    }

    public QQServer() {
        try {
            ss = new ServerSocket(9999);
            System.out.println("服务器在9999端口监听");
            new Thread(new SendNewsToAllThread()).start();//启动推送服务线程

            while (true) {//需要不断监听多个客户端的连接请求
                ObjectInputStream ois = null;
                ObjectOutputStream oos = null;

                Socket socket = ss.accept();
                //得到socket关联的对象的输入流
                ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();//接收的客户端的用户对象

                if (user.getUserType().equals(UserType.USER_LOGIN)) {//登录请求
                    //验证，用HashMap模拟数据库
                    Message message = new Message();//登录反馈信息
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    if (validUsers.containsKey(user.getUserId()) &&
                            validUsers.get(user.getUserId()).equals(user)) {//登录成功
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                        oos.writeObject(message);
                        //注意此处不能socket.shutdownOutput，否则意味着服务器再也不会给该客户端发送信息了
                        //客户端线程里用while循环接收就直接报错说无法接收！

                        //客户端登录成功，创建一个线程，持有该连接客户端的socket
                        //并且保持与客户端的连接
                        ServerConnectClientThread serverConnectClientThread =
                                new ServerConnectClientThread(socket, user.getUserId(), validUsers);
                        serverConnectClientThread.start();//启动线程
                        //将与该用户保持联系的线程加入HashMap中
                        ManageClientThread.addManageClientThread(user.getUserId(), serverConnectClientThread);

                        ManageOfflineMessage.sendOfflineMessage(user.getUserId());//将用户离线时未接收的消息传给他

                    } else {//登录失败
                        System.out.println("userId=" + user.getUserId() + ", password=" + user.getPassword() + " 的用户登录失败");
                        message.setMesType(MessageType.MESSAGE_LOGIN_FAILED);
                        oos.writeObject(message);
                        socket.close();
                    }

                } else if (user.getUserType().equals(UserType.USER_REGISTER)) {//注册请求
                    Message message = new Message();
                    oos = new ObjectOutputStream(socket.getOutputStream());

                    if (validUsers.containsKey(user.getUserId())) {//用户名已被使用
                        System.out.println("userId=" + user.getUserId() + ", password=" + user.getPassword() + " 的用户注册失败");
                        message.setMesType(MessageType.MESSAGE_REGISTER_FAILED);
                        oos.writeObject(message);
                    } else {//可以注册
                        validUsers.put(user.getUserId(), user);
                        System.out.println("userId=" + user.getUserId() + ", password=" + user.getPassword() + " 的用户注册成功");
                        message.setMesType(MessageType.MESSAGE_REGISTER_SUCCEED);
                        oos.writeObject(message);
                    }
                    socket.close();//注册请求都要关闭socket，该行为是临时的，因此不用一直保持联系，不创建线程

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
