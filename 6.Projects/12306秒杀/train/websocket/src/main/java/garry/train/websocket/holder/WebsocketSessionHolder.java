package garry.train.websocket.holder;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Garry
 * 2024-10-11 19:01
 */
@Component
public class WebsocketSessionHolder {

    private final ThreadLocal<WebSocketSession> sessions = new ThreadLocal<>();

    public WebSocketSession getSession() {
        return sessions.get();
    }

    public void setSession(WebSocketSession session) {
        sessions.set(session);
    }

    public void remove() {
        sessions.remove();
    }
}
