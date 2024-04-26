package garry.community.event;

import com.google.gson.Gson;
import garry.community.consts.CommunityConst;
import garry.community.enums.MessageStatusEnum;
import garry.community.pojo.Event;
import garry.community.pojo.Message;
import garry.community.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Garry
 * ---------2024/3/26 11:55
 **/
@SuppressWarnings("rawtypes")
@Slf4j
@Component
public class EventConsumer {
    @Resource
    private MessageService messageService;

    private final Gson gson = new Gson();

    /**
     * 项目启动后，自动监听评论，点赞，关注消息队列；
     * 从收到的record中获取value并转回Event对象；
     * 将event转为message并添加到数据库
     *
     * @param record
     */
    @KafkaListener(topics = {CommunityConst.TOPIC_COMMENT, CommunityConst.TOPIC_LIKE, CommunityConst.TOPIC_FOLLOW})
    public void handleSystemMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("【消息的内容为空】");
            throw new RuntimeException("【消息的内容为空】");
        }
        Event event = gson.fromJson((String) record.value(), Event.class);
        if (event == null) {
            log.error("【从消息中获取event失败】");
            throw new RuntimeException("【从消息中获取event失败】");
        }
        Message message = eventToMessage(event);
        messageService.addMessage(message);
    }

    /**
     * 获取message
     *
     * @param event
     * @return
     */
    private Message eventToMessage(Event event) {
        Message message = new Message();
        message.setFromId(CommunityConst.SYSTEM_MESSAGE_FROM_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setContent(gson.toJson(makeContentByEvent(event)));
        message.setStatus(MessageStatusEnum.UNREAD.getCode());
        message.setCreateTime(new Date());
        return message;
    }

    /**
     * 获取message的content
     *
     * @param event
     * @return
     */
    private Map<String, Object> makeContentByEvent(Event event) {
        Map<String, Object> content = new HashMap<>();
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        content.put("userId", event.getUserId());
        //传入data中的所有键值对
        Set<Map.Entry<String, Object>> entrySet = event.getData().entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            content.put(entry.getKey(), ((Double) entry.getValue()).intValue());
        }
        return content;
    }
}
