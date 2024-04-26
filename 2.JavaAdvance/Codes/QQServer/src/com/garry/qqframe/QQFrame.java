package com.garry.qqframe;

import com.garry.qqserver.service.QQServer;

import java.io.IOException;

/**
 * 该类创建一个QQServer对象，启动服务器
 */
@SuppressWarnings({"all"})
public class QQFrame {
    public static void main(String[] args) {
        try {
            //启动服务器
            new QQServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
