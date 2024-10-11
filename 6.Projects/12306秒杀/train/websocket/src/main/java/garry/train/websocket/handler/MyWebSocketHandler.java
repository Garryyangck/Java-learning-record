package garry.train.websocket.handler;

import garry.train.websocket.holder.WebsocketSessionHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * @author Garry
 * 2024-10-11 18:56
 */
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

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

    public void sendMessage(String msg) throws IOException {
        websocketSessionHolder.getSession().sendMessage(new TextMessage(msg));
    }
}
