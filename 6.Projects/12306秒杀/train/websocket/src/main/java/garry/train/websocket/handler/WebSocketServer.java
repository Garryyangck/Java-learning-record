package garry.train.websocket.handler;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Garry
 * 2024-10-11 18:56
 */
@Slf4j
@Component
@ServerEndpoint("/ws/message/{memberId}")
public class WebSocketServer {
    /**
     * 当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 用来存放每个客户端对应的 WebSocketServer 对象
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收 memberId
     */
    private String memberId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("memberId") String memberId) {
        this.session = session;
        this.memberId = memberId;
        if (webSocketMap.containsKey(memberId)) {
            webSocketMap.remove(memberId);
            webSocketMap.put(memberId, this);
        } else {
            webSocketMap.put(memberId, this);
            addOnlineCount();
        }
        log.info("用户连接: {}，当前在线人数为: {}", memberId, getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户: {} 网络异常，连接失败", memberId);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(memberId)) {
            webSocketMap.remove(memberId);
            subOnlineCount();
        }
        log.info("用户退出: {}，当前在线人数为: {}", memberId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息: {}，报文: {}", memberId, message);
        if (StrUtil.isNotBlank(message)) {
            try {
                JSONObject jsonObject = JSON.parseObject(message);
                jsonObject.put("fromId", this.memberId);
                String toId = String.valueOf(jsonObject.getObject("toId", Long.class));
                if (ObjUtil.isNotNull(toId) && webSocketMap.containsKey(toId)) {
                    webSocketMap.get(toId).sendMessage(jsonObject.toJSONString());
                } else {
                    log.error("请求的 memberId: {} 不在该服务器上", toId);
                }
            } catch (Exception e) {
                log.error("message {} 参数解析错误", message);
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误: {}，原因: {}", this.memberId, error.getMessage());
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    private static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }
}
