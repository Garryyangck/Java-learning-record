package garry.train.business.websocket;

import com.alibaba.fastjson.JSONObject;
import garry.train.business.util.WebsocketSessionHolder;
import garry.train.business.vo.MessageSendVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * @author Garry
 * 2024-10-11 17:16
 */
@Slf4j
@Component
public class MessageWebsocketHandler extends TextWebSocketHandler {

    @Resource
    private WebsocketSessionHolder websocketSessionHolder;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        websocketSessionHolder.setSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获取客户端发送的消息
        String clientMessage = message.getPayload();
        System.out.println("Received message: " + clientMessage);

        // 响应消息
        session.sendMessage(new TextMessage("Hello, " + clientMessage + "!"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        websocketSessionHolder.remove();
    }

    public void sendMessage(MessageSendVo vo) {
        try {
            websocketSessionHolder.getSession().sendMessage(new TextMessage(JSONObject.toJSONString(vo)));
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.BUSINESS_WEBSOCKET_MESSAGE_SEND_FAILED);
        }
    }
}
