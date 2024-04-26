package com.garry.qqclient.service;

import com.garry.qqclient.utils.StreamUtils;
import com.garry.qqclient.utils.Utility;
import com.garry.qqcommon.Message;
import com.garry.qqcommon.MessageType;
import com.garry.qqcommon.User;
import com.garry.qqcommon.UserType;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 该类完成用户登录，退出，发送消息等功能
 */
@SuppressWarnings({"all"})
public class ClientService {
    private User user = new User();//做成属性，因为其它地方也可能用到
    private Socket socket = new Socket();//做成属性，因为其它地方也可能用到

    /**
     * 检验用户名和密码是否正确，如果正确，创建线程保持与服务器的连接
     *
     * @param userId
     * @param password
     * @return 登录是否成功
     */
    public boolean checkUser(String userId, String password) {
        boolean res = false;
        //初始化User对象
        user.setUserId(userId);
        user.setPassword(password);
        user.setUserType(UserType.USER_LOGIN);
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            //连接到服务器，发送User对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);//发送user对象
            //此处一定不要socket.shutdownOutput()，否则服务器会读到结束标志，再也不会接收客户端的信息

            //读取服务器回复的Message对象
            ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            //登录成功
            if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                //创建线程，用于和服务器连接以维护这个socket --> ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread =
                        new ClientConnectServerThread(socket, userId);
                //启动客户端的线程
                clientConnectServerThread.start();
                System.out.print("客户端用户 " + userId + " 的线程");
                //方便后续扩展，将线程保存到HashMap中 --> ManageClientConnectServerThread
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
                res = true;
            } else if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_FAILED)) {
                //登陆失败，不会创建线程，关闭连接服务器的socket
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 向服务器发送注册型User对象
     *
     * @param registerUserId
     * @param registerPassword
     * @return 注册是否成功
     */
    public boolean registerUser(String registerUserId, String registerPassword) {
        boolean res = false;
        User registerUser = new User();
        registerUser.setUserId(registerUserId);
        registerUser.setPassword(registerPassword);
        registerUser.setUserType(UserType.USER_REGISTER);

        try {
            Socket registerSocket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(registerSocket.getOutputStream());
            oos.writeObject(registerUser);

            ObjectInputStream ois = new ObjectInputStream(registerSocket.getInputStream());
            Message message = (Message) ois.readObject();

            if (message.getMesType().equals(MessageType.MESSAGE_REGISTER_SUCCEED)) {
                //注册成功
                res = true;

            } else if (message.getMesType().equals(MessageType.MESSAGE_REGISTER_FAILED)) {
                //注册失败
            }

            registerSocket.close();//由于是注册，因此必须关闭socket，不会创建线程与服务器保持联系
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 向服务器发送拉取在线用户列表的请求
     */
    public void onlineFriendList() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());
        //把信息发给服务器
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给服务器发送退出Message，并使用System.exit(0)关闭进程
     */
    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());//指定退出哪个线程
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println("用户 " + user.getUserId() + " 退出系统...");
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 与另一个在线用户私聊
     *
     * @param receiver
     * @param content
     */
    public void sendToFriend(String receiver, String content) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_SEND_TO_FRIEND);
        message.setSender(user.getUserId());
        message.setReceiver(receiver);
        message.setContent(content);
        //获取当前时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String sendTime = dateTimeFormatter.format(ldt);
        message.setSendTime(sendTime);

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);

            //由于客户端的线程在不断接收服务器的回复，因此接收工作应该写到线程中

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户向所有在线用户发信息
     *
     * @param content
     */
    public void sendToALL(String content) {
        Message message = new Message();
        message.setSender(user.getUserId());
        message.setMesType(MessageType.MESSAGE_CLIENT_SEND_TO_ALL);
        message.setContent(content);
        //获取当前时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String sendTime = dateTimeFormatter.format(ldt);
        message.setSendTime(sendTime);

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向其他用户发送文件
     *
     * @param reciever
     * @param content
     * @param bytes
     */
    public void sendFile(String reciever, String content, byte[] bytes) {
        Message message = new Message();
        message.setSender(user.getUserId());
        message.setMesType(MessageType.MESSAGE_CLIENT_SEND_FILE);
        message.setReceiver(reciever);
        message.setContent(content);
        message.setFileBuf(bytes);
        //获取当前时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String sendTime = dateTimeFormatter.format(ldt);
        message.setSendTime(sendTime);

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查看用户接收的文件
     */
    public void checkReceivedFiles() {
        ManageClientRecievedFiles.showReceivedFilesByKey(user.getUserId());
    }

    /**
     * 用户下载已接收的文件
     */
    public void downloadReceivedFile() {
        System.out.print("请输入要下载的文件名：");
        String fileName = Utility.readString(500);
        byte[] buf = null;
        buf = ManageClientRecievedFiles.getReceivedFiles(user.getUserId(), fileName);
        if (buf == null) {
            //没找到文件
            System.out.println("=========未找到该文件=========\n");
            return;
        }

        while (true) {
            System.out.print("请输入要下载的位置(包括文件名)：");
            String desPath = Utility.readString(500);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(desPath);
                fileOutputStream.write(buf);
                break;

            } catch (FileNotFoundException e) {
                System.out.println("========下载路径不存在，请重新输入路径========");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("============下载完成============\n");
        //从已接收文件列表中删除下载的文件
        ManageClientRecievedFiles.removeReceivedFiles(user.getUserId(), fileName);

    }
}
