package com.garry.qqcommon;

public interface MessageType {
    //定义常量，不同的常量代表不同的消息类型，接口的属性只能是public static final
    String MESSAGE_LOGIN_SUCCEED = "1";
    String MESSAGE_LOGIN_FAILED = "2";
    String MESSAGE_GET_ONLINE_FRIEND = "3";//显示在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "4";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "5";//客户端请求退出
    String MESSAGE_SEND_SUCCEED = "6";//服务端成功将消息送达
    String MESSAGE_SEND_FAILED = "7";//服务端消息发送失败，附上失败原因
    String MESSAGE_CLIENT_SEND_TO_FRIEND = "8";//客户端用户私聊
    String MESSAGE_CLIENT_SEND_TO_ALL = "9";//客户端用户群发消息
    String MESSAGE_CLIENT_SEND_FILE = "10";//客户端用户向其他用户发送文件
    String MESSAGE_REGISTER_SUCCEED = "11";
    String MESSAGE_REGISTER_FAILED = "12";
    String MESSAGE_SERVER_SEND_TO_ALL = "13";//服务器推送消息
}
