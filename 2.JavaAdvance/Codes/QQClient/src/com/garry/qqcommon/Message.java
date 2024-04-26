package com.garry.qqcommon;

import java.io.Serializable;
import java.util.Objects;

/**
 * 表示客户端与服务端通信时的消息对象
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;//增强兼容性

    private String sender;//发送者
    private String receiver;//接收者
    private String content;//内容
    private String sendTime;//发送时间
    private String mesType;//消息类型[在接口中定义消息类型]
    private byte[] fileBuf;//发送的文件

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public byte[] getFileBuf() {
        return fileBuf;
    }

    public void setFileBuf(byte[] fileBuf) {
        this.fileBuf = new byte[fileBuf.length];
        for (int i = 0; i < fileBuf.length; i++) {
            this.fileBuf[i] = fileBuf[i];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sender, message.sender) &&
                Objects.equals(receiver, message.receiver) &&
                Objects.equals(content, message.content) &&
                Objects.equals(sendTime, message.sendTime) &&
                Objects.equals(mesType, message.mesType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, content, sendTime, mesType);
    }
}
