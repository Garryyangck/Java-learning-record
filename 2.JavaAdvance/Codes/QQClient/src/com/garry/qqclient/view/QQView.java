package com.garry.qqclient.view;

import com.garry.qqclient.service.ClientService;
import com.garry.qqclient.utils.StreamUtils;
import com.garry.qqclient.utils.Utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 客户端的菜单页面
 */
@SuppressWarnings({"all"})
public class QQView {
    private boolean loop = true;//控制是否显示菜单
    private String key = "";//从键盘获取输入
    private ClientService clientService = new ClientService();

    public static void main(String[] args) {
        new QQView().mainView();
        System.out.println("已退出系统...");
    }

    public void mainView() {
        while (loop) {
            System.out.println("============欢迎登录网络通讯系统============");
            System.out.println("\t\t\t 1: 登录系统");
            System.out.println("\t\t\t 2: 注册用户");
            System.out.println("\t\t\t 9: 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);

            switch (key) {
                case "1":
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密  码：");
                    String password = Utility.readString(50);
                    //需要到服务端去验证用户是否合法
                    //创建一个类 --> UserClientService[用户登录/注册]
                    if (clientService.checkUser(userId, password)) {
                        System.out.println("================欢迎 " + userId + " 用户================\n");
                        while (loop) {
                            System.out.println("=================网络通信系统二级菜单(用户 " + userId + ")==================");
                            System.out.println("\t\t\t 1: 显示在线用户列表");
                            System.out.println("\t\t\t 2: 群发消息");
                            System.out.println("\t\t\t 3: 私聊消息");
                            System.out.println("\t\t\t 4: 发送文件");
                            System.out.println("\t\t\t 5: 查看接收的文件");
                            System.out.println("\t\t\t 6: 下载接收的文件");
                            System.out.println("\t\t\t 9: 退出系统");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);

                            switch (key) {
                                case "1"://显示在线用户列表
                                    clientService.onlineFriendList();
                                    try {
                                        Thread.sleep(50);//主线程等一下子线程，让子线程先执行完毕
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "2"://群发消息
                                    System.out.print("请输入想群发的话：");
                                    String contentFriend = Utility.readString(500);
                                    clientService.sendToALL(contentFriend);
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "3"://私聊消息
                                    String receiverMes = new String();
                                    while (true) {
                                        System.out.print("请输入想聊天的用户：");
                                        receiverMes = Utility.readString(50);
                                        if (receiverMes.equals(userId))
                                            System.out.println("==========私聊对象不能是自己，请重新输入==========");
                                        else
                                            break;
                                    }
                                    System.out.print("请输入想说的话：");
                                    String contentAll = Utility.readString(500);
                                    clientService.sendToFriend(receiverMes, contentAll);
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "4"://发送文件
                                    String recieverFile = new String();
                                    while (true) {
                                        System.out.print("请输入接收文件的用户：");
                                        recieverFile = Utility.readString(50);
                                        if (recieverFile.equals(userId))
                                            System.out.println("==========接收对象不能是自己，请重新输入==========");
                                        else
                                            break;
                                    }
                                    String contentFile = "";
                                    byte[] bytes;
                                    while (true) {
                                        System.out.print("请输入待发送文件的路径：");
                                        contentFile = Utility.readString(500);
                                        //获取文件的流对象
                                        try {
                                            String[] split = contentFile.split("\\\\");
                                            contentFile = split[split.length - 1];
                                            String srcPath = "";
                                            for (String str : split) {
                                                srcPath = srcPath + str + "/";
                                            }
                                            srcPath = srcPath.substring(0, srcPath.length() - 1);

                                            FileInputStream fileInputStream = new FileInputStream(srcPath);
                                            bytes = StreamUtils.streamToByteArray(fileInputStream);
                                            break;

                                        } catch (FileNotFoundException e) {
                                            System.out.println("========文件不存在，请重新输入路径========");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    clientService.sendFile(recieverFile, contentFile, bytes);
                                    try {
                                        Thread.sleep(200);//发文件会慢一点
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "5"://查看接收的文件
                                    clientService.checkReceivedFiles();//该需求不用连接服务器，故没有线程需要等
                                    break;
                                case "6"://下载接收的文件
                                    clientService.downloadReceivedFile();
                                    break;
                                case "9"://退出系统
                                    clientService.logout();
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("===========你的选择无效，请重新选择===========\n");
                            }
                        }
                    } else {
                        System.out.println("===========用户名或密码错误，登录失败===========\n");
                    }
                    break;
                case "2":
                    System.out.println("\n============注册页面============");
                    System.out.print("请输入你的用户名：");
                    String registerUserId = Utility.readString(50);
                    System.out.print("请输入你的密  码：");
                    String registerPassword = Utility.readString(50);
                    if (clientService.registerUser(registerUserId, registerPassword)) {
                        System.out.println("===========注册成功===========\n");
                    } else {
                        System.out.println("========用户名已被使用，注册失败========\n");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
                default:
                    System.out.println("===========你的选择无效，请重新选择===========\n");
            }
        }
    }
}
