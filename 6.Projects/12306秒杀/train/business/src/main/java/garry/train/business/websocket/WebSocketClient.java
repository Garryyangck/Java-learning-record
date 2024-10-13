package garry.train.business.websocket;

import com.alibaba.fastjson.JSONObject;
import garry.train.business.vo.MessageSendVo;
import garry.train.common.consts.CommonConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import jakarta.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

/**
 * @author Garry
 * 2024-10-13 15:12
 */
@Slf4j
@Component
@ClientEndpoint
public class WebSocketClient {

    @Value("${websocket.message.server.uri}")
    private String uri;

    private final String systemId = String.valueOf(CommonConst.SystemId);

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("连接 websocket 服务器 {} 成功", uri + systemId);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("收到 websocket 消息: {}", message);
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        log.info("断开 websocket {} 的连接: {}", uri + systemId, closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("websocket 报错: {}", throwable.getMessage());
    }

    private void connect() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, new URI(uri + systemId));
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.BUSINESS_WEBSOCKET_CONNECT_FAILED);
        }
    }

    // 主动断开连接
    private void disconnect() {
        try {
            if (session != null && session.isOpen()) {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "客户端主动断开连接"));
            }
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.BUSINESS_WEBSOCKET_DISCONNECT_FAILED);
        }
    }

    public void sendMessage(MessageSendVo sendVo) throws Exception {
        connect();
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(JSONObject.toJSONString(sendVo));
            } else {
                throw new BusinessException(ResponseEnum.BUSINESS_WEBSOCKET_MESSAGE_SEND_FAILED);
            }
        } finally {
            disconnect();
        }
    }
}
