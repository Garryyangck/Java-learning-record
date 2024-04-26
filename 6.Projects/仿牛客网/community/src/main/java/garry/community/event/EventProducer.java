package garry.community.event;

import com.google.gson.Gson;
import garry.community.pojo.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/26 11:49
 **/
@SuppressWarnings({"rawtypes", "unchecked"})
@Component
public class EventProducer {
    @Resource
    private KafkaTemplate kafkaTemplate;

    private final Gson gson = new Gson();

    /**
     * 处理事件
     *
     * @param event
     */
    public void fireEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), gson.toJson(event));
    }
}
