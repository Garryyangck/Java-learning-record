package com.garry.qqserver.service;

import com.garry.qqcommon.Message;
import com.garry.qqcommon.MessageType;
import com.garry.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class SendNewsToAllThread extends Thread {
    @Override
    public void run() {
        System.out.println("推送新闻/消息线程已启动[exit退出推送服务]");
        System.out.println("使用命令 \"CallToAll: + 推送内容\" 进行推送");
        while (true) {
            String content = Utility.readString(500);
            if (content.equals("exit")) break;
            if (!content.split("\\:")[0].equals("CallToAll")) continue;

            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_SERVER_SEND_TO_ALL);
            message.setContent(content.split("\\:")[1]);
            //获取当前时间
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.now();
            String sendTime = dateTimeFormatter.format(ldt);
            message.setSendTime(sendTime);

            Set<String> keySet = ManageClientThread.getKeySet();
            for (String userId : keySet) {
                ServerConnectClientThread manageClientThread = ManageClientThread.getManageClientThread(userId);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(manageClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("服务器推送了一则消息");
        }
        System.out.println("已退出推送服务...");
    }
}
