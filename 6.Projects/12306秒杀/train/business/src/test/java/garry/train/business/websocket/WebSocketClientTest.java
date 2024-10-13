package garry.train.business.websocket;

import garry.train.business.config.BusinessApplicationTest;
import garry.train.business.enums.MessageStatusEnum;
import garry.train.business.enums.MessageTypeEnum;
import garry.train.business.vo.MessageSendVo;
import jakarta.annotation.Resource;
import org.junit.Test;

/**
 * @author Garry
 * 2024-10-13 15:19
 */
public class WebSocketClientTest extends BusinessApplicationTest {

    @Resource
    private WebSocketClient webSocketClient;

    @Test
    public void sendMessage() {
        MessageSendVo sendVo = new MessageSendVo();
        sendVo.setFromId(0L);
        sendVo.setToId(1833041335083470848L);
        sendVo.setType(MessageTypeEnum.SYSTEM_MESSAGE.getCode());
        sendVo.setContent("订票成功");
        sendVo.setStatus(MessageStatusEnum.UNREAD.getCode());
        sendVo.setUnreadNum(1);
        try {
            webSocketClient.sendMessage(sendVo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}